package kr.hhplus.be.server.infrastructure.order;

import kr.hhplus.be.server.domain.order.OrderDataPlatform;
import kr.hhplus.be.server.domain.order.Orders;
import org.springframework.stereotype.Component;

@Component
public class OrderFakeDataPlatform implements OrderDataPlatform {

    @Override
    public void send(Orders order) {
        //TODO 데이터플랫폼 결정
    }
}
