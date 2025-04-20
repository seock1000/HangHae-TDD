## DB 성능 개선점 분석 보고서

### 1. 개요
현재 시스템 및 DB 환경에서 예상되는 병목구간, 성능저하를 분석하고 이를 개선하기 위한 솔루션을 제안합니다.

### 2. 적용 솔루션
- **DB 인덱스 최적화**
  1. point table :
     - query
        ```
       select p1_0.id,p1_0.balance,p1_0.created_at,p1_0.updated_at,p1_0.user_id from point p1_0 where p1_0.user_id=?;
        ```
     - 인덱스 추가 전 실행계획
       ```
       -> Filter: (p1_0.user_id = 1)  (cost=997 rows=973) (actual time=1.66..7.36 rows=1 loops=1)
       -> Table scan on p1_0  (cost=997 rows=9731) (actual time=1.64..6.67 rows=10000 loops=1)
       ```
     - `idx_point_user_id` : user_id에 대한 인덱스 추가
     - user_id는 높은 카디널리티, 조회 시 유일하게 사용되는 컬럼이므로 커버링 인덱스로 사용 가능
     - 인덱스 추가 후 실행계획
       ```
       -> Index lookup on p1_0 using point_user_id (user_id=1)  (cost=0.35 rows=1) (actual time=0.0505..0.0525 rows=1 loops=1)
       ```
       - 인덱스 추가 후 성능 개선 효과
       - 기존 : 1.66ms ~ 7.36ms
       - 개선 : 0.05ms ~ 0.05ms
       - 성능 개선율 : 약 99%
  2. user_coupon table :
     - query
         ```
         select uc1_0.id,uc1_0.coupon_id,uc1_0.created_at,uc1_0.is_used,uc1_0.issued_at,uc1_0.updated_at,uc1_0.user_id from user_coupon uc1_0 where uc1_0.user_id=?;
         ```
     - 인덱스 추가 전 실행계획
       ```
       -> Filter: (uc1_0.user_id = 1)  (cost=1.1 rows=1) (actual time=15.1..15.1 rows=0 loops=1)
       -> Table scan on uc1_0  (cost=1.1 rows=1) (actual time=15.1..15.1 rows=0 loops=1)
       ```
     - `idx_user_coupon_user_id` : user_id에 대한 인덱스 추가
     - user_coupon table에서 user_id는 카디널리티는 비교적 낮으나, 조회 시 유일하게 사용되는 컬럼이므로 커버링 인덱스로 사용 가능하여 인덱스 추가
     - 인덱스 추가 후 실행계획
       ```
       -> Index lookup on uc1_0 using user_coupon_user_id (user_id=1)  (cost=0.35 rows=1) (actual time=0.0273..0.0273 rows=0 loops=1)
       ```
     - 인덱스 추가 후 성능 개선 효과
       - 기존 : 15.1ms ~ 15.1ms
       - 개선 : 0.02ms ~ 0.03ms
       - 성능 개선율 : 약 99%
  3. best_seller table :
     - query
       ```
       SELECT b.product_id, p.title, p.description, p.stock, b.sales_amount FROM best_seller b JOIN product p ON b.product_id = p.id WHERE b.date = ? ORDER BY b.sales_amount DESC LIMIT ?;
       ```
     - 인덱스 추가 전 실행 계획
       ```
       -> Limit: 5 row(s)  (cost=2.2 rows=1) (actual time=9.76..9.76 rows=0 loops=1)
       -> Nested loop inner join  (cost=2.2 rows=1) (actual time=9.74..9.74 rows=0 loops=1)
       -> Sort: b.sales_amount DESC  (cost=1.1 rows=1) (actual time=9.74..9.74 rows=0 loops=1)
       -> Filter: ((b.`date` = <cache>(cast(now() as date))) and (b.product_id is not null))  (cost=1.1 rows=1) (actual time=9.73..9.73 rows=0 loops=1)
       -> Table scan on b  (cost=1.1 rows=1) (actual time=9.73..9.73 rows=0 loops=1)
       -> Single-row index lookup on p using PRIMARY (id=b.product_id)  (cost=1.1 rows=1) (never executed)
       ```
     - `idx_bestseller_sales_amount` : sales_amount에 대한 인덱스 추가
     - best_seller table에서 date, sales_amount는 인기 상품 조회 시 커버링 인덱스로 사용 가능하여 인덱스 추가
     - 인덱스 추가 후 실행 계획
       ```
       -> Limit: 5 row(s)  (cost=1.45 rows=1) (actual time=0.0165..0.0165 rows=0 loops=1)
       -> Nested loop inner join  (cost=1.45 rows=1) (actual time=0.0161..0.0161 rows=0 loops=1)
       -> Filter: (b.product_id is not null)  (cost=0.35 rows=1) (actual time=0.0152..0.0152 rows=0 loops=1)
       -> Index lookup on b using best_seller_date_sales_amount (date=cast(now() as date)) (reverse)  (cost=0.35 rows=1) (actual time=0.0148..0.0148 rows=0 loops=1)
       -> Single-row index lookup on p using PRIMARY (id=b.product_id)  (cost=1.1 rows=1) (never executed)
       ```
     - 인덱스 추가 후 성능 개선 효과
       - 기존 : 9.76ms ~ 9.76ms
       - 개선 : 0.01ms ~ 0.02ms
       - 성능 개선율 : 약 99%
- **조회 모델 분리**
  1. product table :
     - 재고 관리 테이블과 조회 모델로 분리
     - 조회와 재고 관리 역할을 수행하는 product table을 재고관리 테이블과 조회 모델로 분리, 데이터 변경 시 조회 성능 저하를 방지하여 조회 성능 개선
- **쿼리 리팩토링**
  1. Jpa saveAll() ==> JdbcTemplate batchUpdate()
    - 인기상품 통계를 위한 배치 처리에서 Jpa saveAll()은 내부적으로 save() 메서드를 반복 실행, JdbcTemplate batchUpdate()를 사용하여 대량의 데이터를 한번에 처리하여 쓰기 성능 개선