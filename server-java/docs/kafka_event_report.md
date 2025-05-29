# Kafka 적용 보고서

---
### 주문 확정 부가로직 카프카 적용
- 주문 확정 시 데이터 플랫폼에 주문 정보 전송, 랭킹 서비스(best-seller)에 판매량 전송을 카프카를 통해 처리하였습니다. 
- sequence diagram
    ```mermaid
    sequenceDiagram
        actor Client
        participant PaymentService
        participant ApplicationEventManager
        participant OrderService
        participant Kafka
        participant DataPlatform
        participant RankingService
    
        Client->>PaymentService: 결제 요청
        PaymentService-->>ApplicationEventManager: 결제 완료 이벤트 발행
        PaymentService ->> Client : 결제 완료 응답
        OrderService ->> ApplicationEventManager: 결제 완료 이벤트 수신
        OrderService ->> OrderService: 주문 확정 처리
        OrderService-->>Kafka: 주문 확정 이벤트 발행
        DataPlatform ->> Kafka: 주문 정보 수신
        DataPlatform-->>DataPlatform: 주문 정보 저장
        RankingService ->> Kafka: 판매량 정보 수신
        RankingService-->>RankingService: 판매량 업데이트
    ```
  - Kafka를 통해 주문 확정 이벤트를 발행하고, 데이터 플랫폼과 랭킹 서비스가 해당 이벤트를 수신하여 각각의 처리를 수행합니다.
  - 주문 확정에 따른 데이터 플랫폼의 주문 정보 저장과 랭킹 서비스의 판매량 업데이트는 부가 로직으로 판단하여 실패 시, 별도의 사후 처리 없이 로그로 남겼습니다.

---
### 선착순 쿠폰 발급 카프카 적용(대용량 트래픽 프로세스)
- 선착순 쿠폰 발급 시 대용량 트래픽이 예상되어 이를 처리하기 위해 카프카를 적용하였습니다.
- sequence diagram
    ```mermaid
    sequenceDiagram
        actor Client
        participant CouponService
        participant ApplicationEventManager
        participant Kafka
    
        Client->>CouponService: 쿠폰 발급 요청
        CouponService->>ApplicationEventManager: 쿠폰 발급 요청 이벤트 발행
        CouponService ->> Client : 쿠폰 발급 요청 생성됨 응답
        ApplicationEventManager-->> Kafka: 쿠폰 발급 요청 이벤트 전송(메시지키: coupon-id)
        Kafka ->> CouponService: 쿠폰 발급 요청 이벤트 수신
        CouponService->>CouponService: 쿠폰 발급 처리
        alt : 발급 성공
            CouponService->>CouponService : 커밋 
            CouponService->>ApplicationEventManager: 쿠폰 발급 완료 이벤트 발행
            Kafka->>ApplicationEventManager: 쿠폰 발급 완료 이벤트 수신
            Kafka->>Kafka: 쿠폰 발급 완료 이벤트 발행
        else : 발급 실패
            CouponService->>CouponService : 롤백
            CouponService->>ApplicationEventManager: 쿠폰 발급 실패 이벤트 발행
            Kafka->>ApplicationEventManager: 쿠폰 발급 실패 이벤트 수신
            Kafka->>Kafka: 쿠폰 발급 실패 이벤트 발행
        end
    ```
  - 대량의 쿠폰 발급 요청을 카프카를 통해 비동기적으로 처리하여 시스템 부하를 분산시켰습니다.
  - 쿠폰 발급 이벤트는 카프카를 통해 전달되며, 쿠폰 서비스가 이를 다시 수신하여 처리합니다.
  - 유저는 쿠폰 발급 요청이 생성되었다는 응답을 즉시 받으며, 쿠폰 발급 완료 여부는 별도의 이벤트(쿠폰 발급 성공/실패 알림 등)로 처리됩니다.
  - 쿠폰 발급 요청 이벤트는 메시지 키를 사용하여 쿠폰 ID로 파티셔닝되어, 동일한 쿠폰에 대한 요청이 순차적으로 처리되도록 하였습니다.
    - 이로 인해 쿠폰 발급 요청이 중복되거나 순서가 뒤바뀌는 문제를 방지할 수 있습니다.
    - 여러가지 쿠폰의 요청을 동시에 처리할 수 있도록 해당 토픽(coupon-issue)의 파티션을 5개, 컨슈머의 concurrency 를 5로 설정하였습니다.
    - 병목 방지를 위해 쿠폰 발급 성공/실패 토픽의 파티션도 5개로 설정하였습니다. 