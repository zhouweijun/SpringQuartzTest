/* 
 * All rights Reserved, Designed By 农金圈
 * 2017年3月9日 下午2:54:08
 */
package org.xavier.batch.model;

/** 
 *
 * @author:	XavierZz 
 */
public class ScheduleJob {

    public final static String JOB_PARAM = "SMS";
    /** 任务id */
    private String jobId;

    /** 任务名称 */
    private String jobName;

    /** 任务分组 */
    private String jobGroup;

    /** 任务状态 0禁用 1启用 2删除 */
    private String jobStatus;

    /** 任务运行时间表达式 */
    private String cronExpression;

    /** 任务描述 */
    private String desc;


    /**
     * @return: String <br>
     */
    public String getJobId() {
        return jobId;
    }


    /**
     * @return: String <br>
     */
    public void setJobId(String jobId) {
        this.jobId = jobId;
    }


    /**
     * @return: String <br>
     */
    public String getJobName() {
        return jobName;
    }


    /**
     * @return: String <br>
     */
    public void setJobName(String jobName) {
        this.jobName = jobName;
    }


    /**
     * @return: String <br>
     */
    public String getJobGroup() {
        return jobGroup;
    }


    /**
     * @return: String <br>
     */
    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }


    /**
     * @return: String <br>
     */
    public String getJobStatus() {
        return jobStatus;
    }


    /**
     * @return: String <br>
     */
    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }


    /**
     * @return: String <br>
     */
    public String getCronExpression() {
        return cronExpression;
    }


    /**
     * @return: String <br>
     */
    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }


    /**
     * @return: String <br>
     */
    public String getDesc() {
        return desc;
    }


    /**
     * @return: String <br>
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }


}
