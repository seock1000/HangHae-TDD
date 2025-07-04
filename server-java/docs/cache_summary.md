### Cache

---
#### Cache란?
- 자주 사용하는 데이터나 값을 미리 복사해두는 임시 공간
- 작은 저장공간 & 높은 비용 / 빠른 속도
- 캐시 적용을 고려할만한 기능 
  - 단순한 데이터
  - 동일한 데이터를 반복적으로 제공해야하는 경우 (자주 조회되는 데이터)
  - 데이터의 변경주기가 빈번하지 않고, 단위 처리 시간이 오래걸리는 경우 (데이터 갱신으로 인해 DB와 불일치가 발생할 수 있기 때문)
  - 데이터의 최신화가 반드시 실시간으로 이뤄지지 않아도 서비스 품질에 영향을 거의 주지 않는 데이터
---
#### Local Cache vs Global Cache
- Local Cache
  - Local 장비 내에서만 사용되는 캐시로, Local 장비의 Resource를 이용한다. 
  - Local에서만 작동하기 때문에 속도가 빠르다. 
  - Local에서만 작동하기 때문에 다른 서버와 데이터 공유가 어렵다.
  - 적용하기 좋은 기능
    - 변경이 자주 일어나지 않는 데이터
    - 데이터가 변경되더라도 서버간 동기화가 실시간에 가깝게 이뤄질 필요가 없는 단순 조회용 데이터 -> 최종적 일관성을 만족하면 되는 데이터
- Global Cache
  - 여러 서버에서 공유하는 캐시로, 별도의 서버를 이용한다. 
  - 네트워크를 통해 접근하기 때문에 네트워크 비용이 발생 -> Local Cache 보다 느리다. 
  - 별도 서버에서 작동하기 때문에 다른 서버와 데이터 공유가 쉽다.
  - 적용하기 좋은 기능
    - 변경이 자주 일어나는 데이터
    - 서버간 공유가 필요한 데이터
---
#### Local Cache
  - 스프링 진영에서 ConcurrentMap, ehcache, Caffeine 등으로 구현 가능
    - ConcurrentMapCacheManager
        - ConcurrentHashMap을 이용한 Local Cache -> Thread Safe
        - spring-boot-starter-cache의 default 설정
        - Cache 관리를 위한 기능이 빈약
          - TTL, TTI 등을 이용한 캐시 삭제 기능이 빈약
          - 직접 호출 or 구현해서 사용은 가능
    - ehcache
      - off-heap 지원 : RAM에 데이터를 저장하고 TTL, expiry 등을 통해 만료기한 설정 가능
      - 분산 캐시 기능 지원 : 한 노드의 변경을 다른 노드로 전파 가능(자바의 원격 매커니즘인 RMI를 사용)
    - Caffeine
      - 캐시의 용량 제한이 없고, 완전히 채워지며 항상 일정한 값을 계산한다는 전제 하에 가장 높은 성능
      - evict 정책 : window tinyLFU(참조 횟수가 가장 작은 페이지 교체)
        - 높은 적중률 + 낮은 메모리 설치 공간
        - Size-based : 설정한 크기를 초과하면 사용된지 가장 오래된 캐시 또는 가장 적게 사용된 캐시를 제거
        - Time-based : 설정한 시간 이후에 사용된 캐시를 제거
          - expireAfterAccess : 캐시 생성 이후 특정 기간 접근 또는 대체된 적이 없는 경우 제거
          - expireAfterWrite : 캐시 생성 이후 특정 기간 접근 또는 대체된 적이 없는 경우 제거
          - expireAfter : 캐시 생성 또는 마지막 업데이트 이후 지정된 시간 간격으로 새로고침
        - Reference-based : 캐시가 사용되지 않으면 제거
          - softReference : 값을 저장하고 메모리 수요에 따라 least-recently-used 방식으로 가비지 수집
          - weakReference : 키를 저장하고 키에 대한 강력한 참조가 없으면 가비지 수집
---
#### Caching 전략
- 읽기 전략
  - Lock Aside 패턴
    - 캐시조회 -> 캐시 미스 -> DB 조회
    - 원하는 데이터만 구성하여 캐시에 저장
    - 캐시와 DB가 분리되어 캐시 장애 대응 가능
    - 캐시 서버(redis 등) 장애 시 순간적으로 cache에 붙어있던 커넥션이 DB로 몰려 Cache Stamped 발생 가능
  - Read Through 패턴
    - 데이터를 캐시로부터만 읽어오는 전략
    - 동기화는 라이브러리 또는 캐시 제공자에게 위임
    - 캐시서버(redis) 다운 시 서비스 장애 발생 가능
      - 클러스터 구성 등으로 가용성 확보가 필요
    - 캐시와 DB 간의 동기화가 항상 이루어져 정합성 문제에 강점
- 쓰기 전략
  - Write Back 패턴
    - 캐시에 데이터를 모아놓고, 일정 주기마다 배치작업으로 DB에 반영
    - 장점
      - DB에 대한 부하를 줄일 수 있다.
      - 데이터 정합성을 확보
      - Write가 빈번하고 Read의 비용이 높은 경우 유리
    - 단점
      - 캐시 서버 장애 시 데이터 유실 가능성
      - 자주 사용되지 않는 불필요한 리소스 저장
  - Write Through 패턴
    - 캐시에 데이터를 저장하고, 즉시 해당 데이터를 DB에 저장
    - 장점
      - 캐시 서버의 데이터 정합성, 일관성을 확보
      - 캐시 서버 장애 시 데이터 유실 가능성 없음
    - 단점
      - DB에 대한 부하가 증가한다.
      - 매 요청 두 번의 write가 발생하므로 성능 저하
  - Write Around 패턴
    - 캐시 갱신 없이 DB에 저장, cache miss가 발생하는 경우에만 캐시에도 데이터 저장
    - 장점
      - 빠른 속도
      - 데이터가 쓰여지고 조회가 없거나 거의 없는 경우 유리
    - 단점
      - 캐시 서버의 데이터 정합성, 일관성을 확보할 수 없다.
      - DB의 데이터가 변경, 삭제 될 때마다 cache를 변경, 삭제하거나, expiry를 짧게 조정하는 방식으로 대처
- 캐시 읽기 + 쓰기 전략
  - Look Aside + Write Around
    - 일반적으로 가장 많이 사용
    - 읽기 : 캐시 조회 -> 캐시 미스 -> DB 조회
    - 쓰기 : DB에 저장
  - ReadThrough + Write Around
    - 항상 DB에 쓰고, 캐시에서 읽을때 항상 DB에서 먼저 읽어오므로 데이터 정합성 이슈에 대한 완벽한 안전 장치를 구성할 수 있음
    - 읽기 : 캐시 조회
    - 쓰기 : DB에 저장
  - Read Through + Write Through
    - 최신 캐시 데이터, 데이터 정합성 보장
    - 읽기 : 캐시 조회
    - 쓰기 : 캐시 저장 -> DB 저장