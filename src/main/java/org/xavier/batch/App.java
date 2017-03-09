package org.xavier.batch;

import java.util.List;
import java.util.Set;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.xavier.batch.data.DataWorkContext;
import org.xavier.batch.enums.SmsEventEnums;
import org.xavier.batch.job.QueryFactoryJobBean;
import org.xavier.batch.model.ScheduleJob;

/**
 * Hello world!
 *
 */
public class App 
{

    public static void main(String[] args) throws SchedulerException, InterruptedException
    {
        ApplicationContext context = new ClassPathXmlApplicationContext("/batch/job/job-quartz.xml");
        String jobGroup = "SMS";
        String scheduleJobKey = "scheduleJob";
        SchedulerFactoryBean bean = context.getBean(SchedulerFactoryBean.class);

        Scheduler scheduler2 = bean.getScheduler();

        System.out.println("><><><><><>:" + scheduler2);
        List<ScheduleJob> allJob = DataWorkContext.getAllJob();
        int i = 0;
        for (ScheduleJob job : allJob) {
            TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), job.getJobGroup());
            Trigger trigger = scheduler2.getTrigger(triggerKey);
            if (trigger == null) {
                JobDetail jobDetail =
                    JobBuilder.newJob(QueryFactoryJobBean.class).withIdentity(job.getJobName(), job.getJobGroup()).build();

                jobDetail.getJobDataMap().put(scheduleJobKey, job);
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
                trigger = TriggerBuilder.newTrigger()
                    .withIdentity(job.getJobName(), job.getJobGroup())
                    .withSchedule(scheduleBuilder)
                    .build();
                scheduler2.scheduleJob(jobDetail, trigger);
                if (job.getJobName().equals(SmsEventEnums.PAY_PASS.name())) {
                    scheduler2.triggerJob(JobKey.jobKey(SmsEventEnums.PAY_PASS.name(), jobGroup));
                }
            } else {
                // Trigger已存在，那么更新相应的定时设置
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
                CronTrigger trigger1 = (CronTrigger) scheduler2.getTrigger(triggerKey);
                // 按新的cronExpression表达式重新构建trigger
                trigger = trigger1.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
                // 按新的trigger重新设置job执行
                scheduler2.rescheduleJob(triggerKey, trigger);
            }
        }

        System.out.println("任务在运行中.....");

        // ### 移除job3 ###
        Thread.sleep(5000);
        System.out.println("### 移除LAST_REPAYMENT_SEVEN ###");
        scheduler2.deleteJob(JobKey.jobKey(SmsEventEnums.LAST_REPAYMENT_SEVEN.name(), jobGroup));
        // ### 暂停job1和job2 ###
        Thread.sleep(5000);
        System.out.println("### 暂停PAY_PASS 和CREADIT_PASS ###");
        scheduler2.pauseJob(JobKey.jobKey(SmsEventEnums.PAY_PASS.name(), jobGroup));
        scheduler2.pauseJob(JobKey.jobKey(SmsEventEnums.CREADIT_PASS.name(), jobGroup));
        // ### 再次启动job1 ###
        Thread.sleep(5000);
        System.out.println("### 再次启动PAY_PASS ###");
        scheduler2.resumeJob(JobKey.jobKey(SmsEventEnums.PAY_PASS.name(), jobGroup));
        // ### 修改job1的cron ###
        Thread.sleep(5000);
        System.out.println("### 修改PAY_PASS 的cron为每10秒执行一次 ###");
        TriggerKey triggerKey = TriggerKey.triggerKey(SmsEventEnums.PAY_PASS.name(), jobGroup);
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0/10 * * * * ?");
        CronTrigger trigger = (CronTrigger) scheduler2.getTrigger(triggerKey);
        trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
        // 获取PAY_PASS参数并修改描述
        ScheduleJob jobParam =
            (ScheduleJob) scheduler2.getJobDetail(JobKey.jobKey(SmsEventEnums.PAY_PASS.name(), jobGroup))
                .getJobDataMap()
                .get(scheduleJobKey);
        jobParam.setCronExpression("0/3 * * * * ?");
        jobParam.setDesc("嘿嘿,放款成功短信已发送");
        scheduler2.rescheduleJob(triggerKey, trigger);
        // 打印内存中的所有 Job
        Thread.sleep(5000);
        System.out.println("### 打印内存中的所有 Job的状态 ###");
        Set<TriggerKey> triggerKeys = scheduler2.getTriggerKeys(GroupMatcher.anyTriggerGroup());
        for (TriggerKey tKey : triggerKeys) {
            CronTrigger t = (CronTrigger) scheduler2.getTrigger(tKey);
            System.out.println("Trigger details: " + t.getJobKey().getName() + ", " + t.getJobKey().getGroup() + ", "
                + scheduler2.getTriggerState(tKey) + ", " + t.getFinalFireTime() + ", " + t.getCronExpression());
        }

    }
}
