package kr.hhplus.be.server.infrastructure.order;

import kr.hhplus.be.server.domain.order.OrderCancelHandler;
import org.quartz.SimpleTrigger;
import org.springframework.stereotype.Component;

import java.util.Date;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

@Component
public class OrderQuartzCancelHandler implements OrderCancelHandler {
    @Override
    public void register(String orderId) {
        //TODO redis에 등록
    }

    @Override
    public void delete(String orderId) {
        //TODO redis에 삭제
    }
}
