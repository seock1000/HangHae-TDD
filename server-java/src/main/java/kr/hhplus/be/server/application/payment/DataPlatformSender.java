package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.domain.order.Orders;

/**
 * 어느 위치로 가야할지 모르겠습니다..,,.
 */
public interface DataPlatformSender {
    void sendOrder(Orders order);
}
