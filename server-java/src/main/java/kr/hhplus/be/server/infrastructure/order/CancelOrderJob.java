package kr.hhplus.be.server.infrastructure.order;

import kr.hhplus.be.server.application.order.CancelOrderCommand;
import kr.hhplus.be.server.application.order.OrderFacade;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.context.WebApplicationContext;

public class CancelOrderJob extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        var appContext = (WebApplicationContext) context.getJobDetail().getJobDataMap().get("applicationContext");
        var orderId = (String) context.getJobDetail().getJobDataMap().get("orderId");
        OrderFacade orderFacade = (OrderFacade) appContext.getBean("orderFacade");

        orderFacade.cancelOrder(new CancelOrderCommand(orderId));
    }
}
