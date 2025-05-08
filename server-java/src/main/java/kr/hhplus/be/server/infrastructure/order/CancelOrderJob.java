package kr.hhplus.be.server.infrastructure.order;

import kr.hhplus.be.server.application.order.CancelOrderCommand;
import kr.hhplus.be.server.application.order.OrderLockFacade;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.context.WebApplicationContext;

public class CancelOrderJob extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        var appContext = (WebApplicationContext) context.getJobDetail().getJobDataMap().get("webApplicationContext");
        var orderId = (String) context.getJobDetail().getJobDataMap().get("orderId");
        OrderLockFacade orderFacade = (OrderLockFacade) appContext.getBean("orderLockFacade");

        orderFacade.cancelOrder(new CancelOrderCommand(orderId));
    }
}
