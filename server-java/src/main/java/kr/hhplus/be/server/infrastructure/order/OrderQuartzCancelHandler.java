package kr.hhplus.be.server.infrastructure.order;

import jakarta.annotation.PostConstruct;
import kr.hhplus.be.server.application.order.OrderFacade;
import kr.hhplus.be.server.domain.order.OrderCancelHandler;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.core.ApplicationContext;
import org.quartz.*;
import org.quartz.core.QuartzScheduler;
import org.quartz.core.jmx.QuartzSchedulerMBean;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

@Component
public class OrderQuartzCancelHandler implements OrderCancelHandler {

    private final WebApplicationContext appContext;
    private final Scheduler scheduler;

    @Autowired
    public OrderQuartzCancelHandler(WebApplicationContext context) throws SchedulerException {
        this.appContext = context;
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        scheduler = schedulerFactory.getScheduler();
        scheduler.start();
    }

    @Override
    public void register(String orderId)  {
        JobDetail jobDetail = JobBuilder.newJob(CancelOrderJob.class)
                .withIdentity(orderId, "cancelOrderJob")
                .usingJobData("orderId", orderId)
                .usingJobData(new JobDataMap(Map.of("webApplicationContext", appContext)))
                .build();

        Trigger trigger = newTrigger()
                .withIdentity(orderId, "cancelOrderTrigger")
                .startAt(Date.from(Instant.now().plus(5, ChronoUnit.MINUTES)))
                .build();

        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            //TODO logging?
            throw new RuntimeException("Failed to schedule job", e);
        }
    }

    @Override
    public void delete(String orderId) {
        try {
            scheduler.deleteJob(JobKey.jobKey(orderId, "cancelOrderJob"));
        } catch (SchedulerException e) {
            //TODO logging?
        }
    }
}
