/* 
 * All rights Reserved, Designed By 农金圈
 * 2017年3月9日 下午4:40:42
 */
package org.xavier.batch.core;

import java.util.List;

import javax.annotation.PostConstruct;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;
import org.xavier.batch.data.DataWorkContext;
import org.xavier.batch.job.QueryFactoryJobBean;
import org.xavier.batch.model.ScheduleJob;

/** 
 *
 * @author:	XavierZz 
 */
@Component
public class InitScheduler {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SchedulerFactoryBean scheduler;

    @Autowired
    private DataWorkContext dataWorkContext;

    public void init() {

        List<ScheduleJob> allJob = dataWorkContext.getAllJob();

        for (ScheduleJob scheduleJob : allJob) {
            try {
                this.addJob(scheduleJob);
            } catch (SchedulerException e) {
                logger.error("Error <><><><><><><>: </br> {}", e);
            }
        }
    }

    private void addJob(ScheduleJob job) throws SchedulerException {
        Scheduler scheduler2 = scheduler.getScheduler();

        logger.debug("><><><><><>:{}", scheduler2);

        TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), job.getJobGroup());
        Trigger trigger = scheduler2.getTrigger(triggerKey);
        if (trigger != null) {
            JobDetail jobDetail =
                JobBuilder.newJob(QueryFactoryJobBean.class).withIdentity(job.getJobName(), job.getJobGroup()).build();

            jobDetail.getJobDataMap().put("scheduleJob", job);
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
            trigger =
                TriggerBuilder.newTrigger().withIdentity(job.getJobName(), job.getJobGroup()).withSchedule(scheduleBuilder).build();
            scheduler2.scheduleJob(jobDetail, trigger);
        } else {
            // Trigger已存在，那么更新相应的定时设置
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
            
            // 按新的cronExpression表达式重新构建trigger
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).build();

            // 按新的trigger重新设置job执行
            scheduler2.rescheduleJob(triggerKey, trigger);
        }
        scheduler2.start();
    }
}
