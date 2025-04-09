package kr.hhplus.be.server.domain.order;

/**
 * OrderIdGenerator
 * 추후 저수준 모듈을 통해 주문번호를 생성하기 위해 interface로 분리
 * 현재는 UUID밖에 생각나지 않지만, 알아본 결과 Redis 등을 사용해서도 생성할 수 있는 것으로 보임
 * pk 생성을 외부에 위임해도 되나...? 근데 DB에서 생성해도 위임하는거 아닌가..? 고민입니다.
 */
public interface OrderIdGenerator {
    String gen();
}
