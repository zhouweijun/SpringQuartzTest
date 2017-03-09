/* 
 * All rights Reserved, Designed By 农金圈
 * 2017年3月9日 下午4:51:22
 */
package org.xavier.batch.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.xavier.batch.model.ScheduleJob;

/** 
 *
 * @author:	XavierZz 
 */
public class QueryFactoryJobBean extends QuartzJobBean {

    /**
     * <p>
     * TODO
     * </p>
     * 
     * @param context
     * @throws JobExecutionException
     */
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        System.out.println("任务成功运行");
        ScheduleJob scheduleJob = (ScheduleJob) context.getMergedJobDataMap().get("scheduleJob");
        System.out.println("任务名称 = [" + scheduleJob.getJobName() + "]");
        System.out.println("任务描述=[" + scheduleJob.getDesc() + "]");
    }
}
