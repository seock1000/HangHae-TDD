## 동시성 이슈 - DB Lock
- 원인 : Race Condition
    - 여러 작업(프로세스, 스레드, 트랜잭션 등) 이 공유 자원에 동시에 접근하는 상태
        - 공유 자원 : 여러 작업이 공동으로 사용하는 변수, 메모리, 파일, 데이터 등
- 동시성 **문제**
    - 분실 갱신(Lost Update)

        ```java
        TxA : read t -> - -> update t -> -
        TxB : - -> read t -> - -> update t
        
        => TxA에서 update 한 값은 TxB에 의해 덮어 씌워지면서 상실
        ```
        
      - 
    - 커밋되지 않은 의존(Uncommitted Dependency)

        ```java
        TxA : update t -> - -> rollback
        TxB : - -> read t -> -
        
        => TxA에서 rollback 발생 이전에 TxB에서 값을 read하여 commit 되지 않은 값을 사용
        ```

    - 커밋되지 않은 변경(Uncommitted Change)

        ```java
        TxA : update t -> - -> rollback
        TxB : - -> update t -> -
        
        => TxA에서의 rollback으로 TxB에서의 update 내용 상실
        ```

    - 모순 감지(Inconsistent Analysis)

        ```java
        initialize t1 = 40, t2 = 50, t3 = 30
        
        TxA : read t1(sum=40) -> read t2(sum=90) ->  - -> - -> - -> read t3(sum=110, not 120)
        TxB : - -> - -> update t3 = 20 -> update t1 = 50 -> commit -> -
        
        트랜잭션이 실행 중에 다른 트랜잭션이 데이터 수정에 개입해 데이터 일관성이 깨지는 문제
        변경이 발생했지만 총합은 여전히 120인 반면, TxB의 개입으로 총합이 110이 됨
        ```

- DB Lock
    - 동시성 문제 해결을 위해 공유자원 사용 시 해당 자원에 읽기 또는 읽기와 쓰기를 잠그는(Lock) 방법
    - 종류
        - 공유 Lock(Shared Lock, Read Lock, S-Lock)
            - …..query…… for share
            - 데이터 변경이 발생하지 않는 읽기 작업을 위해 자원을 잠그는 것
            - 읽기 작업만 발생하는 경우, 다른 작업에서 해당 데이터를 읽어도 정합성이 깨지지 않기 때문에 읽기 작업에 대해서는 허용(다른 작업에서의 공유 Lock 허용)
            - 쓰기 작업의 경우에는  문제가 발생(Non-repeatable Read 등)할 수 있기 때문에 쓰기 작업에 대해서는 허용 X(다른 작업에서의 배타 Lock 허용 X)
        - 배타 Lock(Exclusive Lock, Write Lock, X-Lock)
            - …..query…… for update
            - 데이터 변경을 위해 작업을 잠그는 것
            - 쓰기 작업이 발생하는 경우 다른 작업에서 해당 데이터를 읽는다면 작업 결과가 달라질 수 잇기 때문에 다른 작업의 접근 허용 X
            - 공유 Lock과 배타 Lock 모두 허용 X
        - Dead Lock
            - 작업간 Cycle이 형성되어 각 작업이 서로에게 블로킹 된 상태
            - ex) 식사하는 철학자들 문제
            - timeout 엔딩
          

- Lock을 통한 동시성 제어
    - 낙관적 락(Optimistic lock)
        - 자원에 락을 걸지 않고, 동시성 문제가 발생하면 그 때 처리
        - 어플리케이션 단에서 처리 → 실패 응답, 내부적으로 재시도 등
        - 낙관적 락은 update에 실패해도 자동으로 예외를 던지지 않고 단순히 0개의 row를 업데이트 하므로, 여러 작업이 묶인 트랜잭션 요청이 실패하는 경우 개발자가 직접 롤백 처리 필요
        - 일반적으로 version(hashcode / timestamp 등 사용) 상태로 충돌 확인
            - 작업 전, 후의 version을 확인하여 충돌 확인
        - JPA에서는 @Version 을 통해 낙관적 락 사용 ⇒ ObjectOptimisticLockingFailureException 발생, 어플리케이션 단에서 해당 예외 처리 필요
    - 비관적 락
        - Repeatable Read 또는 Serializable 정도의 격리수준 제공
        - 트랜잭션이 시작될 때, Shared Lock 또는 Exclusive Lock을 걸고 시작
    - 낙관적 락 vs 비관적 락
        - 낙관적 락 : 트랜잭션 필요 X → 일반적으로 비관적 락보다 좋은 성능, 충돌이 잦은 환경에서는 수동 rollback으로 update 처리 필요 ⇒ 성능 저하
            - 데이터 충돌이 잦지 않은 환경
            - 조회 작업이 많은 데이터 → 동시 접근 성능이 중요한 환경
        - 비관적 락 : 데이터 자체에 락 → 동시성이 떨어져 성능 저하, 데드락 발생 가능성
            - 데이터의 무결성이 중요한 환경
            - 데이터 충돌이 잦은 환경