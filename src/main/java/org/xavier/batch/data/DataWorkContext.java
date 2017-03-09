/* 
 * All rights Reserved, Designed By 农金圈
 * 2017年3月9日 下午3:25:40
 */
package org.xavier.batch.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xavier.batch.enums.SmsEventEnums;
import org.xavier.batch.model.ScheduleJob;

/** 
 *
 * @author:	XavierZz 
 */
public class DataWorkContext {

    private static Map<String, ScheduleJob> jobs = new HashMap<>();

    static {
        SmsEventEnums[] values = SmsEventEnums.values();
        for (int i = 0; i < 4; i++) {
            ScheduleJob job = new ScheduleJob();
            job.setJobId("10001" + i);
            job.setJobName(values[i].name());
            job.setJobGroup("SMS");
            job.setJobStatus("1");
            job.setCronExpression("0/3 * * * * ?");
            job.setDesc("短信任务");
            addJob(job);
        }
    }

    /**
     * 添加任务
     * 
     * @param scheduleJob
     */
    public static void addJob(ScheduleJob scheduleJob) {
        jobs.put(scheduleJob.getJobGroup() + "_" + scheduleJob.getJobName(), scheduleJob);
    }

    public static List<ScheduleJob> getAllJob() {
        List<ScheduleJob> rest = new ArrayList<>();
        rest.addAll(jobs.values());
        return rest;

    }
}
