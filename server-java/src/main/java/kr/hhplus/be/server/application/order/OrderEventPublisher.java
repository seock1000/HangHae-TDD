package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.order.OrderEvent;

public interface OrderEventPublisher {

    public void publishConfirmEvent(OrderEvent.Confirmed event);
}
