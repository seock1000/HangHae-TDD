## DB 성능 개선점 분석 보고서

### 1. 개요
현재 시스템 및 DB 환경에서 예상되는 병목구간, 성능저하를 분석하고 이를 개선하기 위한 솔루션을 제안합니다.

### 2. 적용 솔루션
- **DB 인덱스 최적화**
  1. point table : 
     - `idx_point_user_id` : user_id에 대한 인덱스 추가
  2. user_coupon table :
     - `idx_user_coupon_user_id` : user_id에 대한 인덱스 추가
  3. best_seller table :
     - `idx_bestseller_sales_amount` : sales_amount에 대한 인덱스 추가
- **조회 모델 분리**
  1. product table :
     - 재고 관리 테이블과 조회 모델로 분리
- **쿼리 리팩토링**
  1. Jpa saveAll() ==> JdbcTemplate batchUpdate()

### 3. 예상 개선 효과
- **DB 인덱스 최적화**
    1. point table : 
       - point table에서 user_id는 높은 카디널리티, 조회 시 유일하게 사용되는 컬럼이므로 커버링 인덱스로 사용 가능하여 인덱스 추가
    2. user_coupon table :
       - user_coupon table에서 user_id는 카디널리티는 비교적 낮으나, 조회 시 유일하게 사용되는 컬럼이므로 커버링 인덱스로 사용 가능하여 인덱스 추가
    3. best_seller table :
         - best_seller table에서 sales_amount는 인기 상품 조회 시 커버링 인덱스로 사용 가능하여 인덱스 추가
- **조회 모델 분리**
    - 조회와 재고 관리 역할을 수행하는 product table을 재고관리 테이블과 조회 모델로 분리, 데이터 변경 시 조회 성능 저하를 방지하여 조회 성능 개선
- **쿼리 리팩토링**
    - 인기상품 통계를 위한 배치 처리에서 Jpa saveAll()은 내부적으로 save() 메서드를 반복 실행, JdbcTemplate batchUpdate()를 사용하여 대량의 데이터를 한번에 처리하여 쓰기 성능 개선