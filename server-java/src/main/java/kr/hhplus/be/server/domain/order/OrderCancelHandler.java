package kr.hhplus.be.server.domain.order;

import org.quartz.SchedulerException;

public interface OrderCancelHandler {
    void register(String orderId);
    void delete(String orderId);
}
