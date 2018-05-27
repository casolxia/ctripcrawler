package com.ccrawler.ctrip.job;

import java.util.List;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

import com.ccrawler.ctrip.vo.ScheduleJob;


public class JobManager {

	private static JobManager instance;
	
	private SchedulerFactory schedulerfactory;
	
	private Scheduler scheduler;
	
	
	public static JobManager getInstance(){
		if (instance==null) {
			instance = new JobManager();
		}
		return instance;
	}
	
	public JobManager(){
		init();
	}
	
	public void init(){
		try{
			schedulerfactory = new StdSchedulerFactory();
			scheduler = schedulerfactory.getScheduler();
			scheduler.start();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void addAllJob() throws Exception{
		
	}
	
	/**
	 * 增加任务
	 * @param job
	 * @throws Exception
	 */
	public void addJob(ScheduleJob job) throws Exception{
		TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), job.getJobGroup());
		CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        if(null == trigger){
        	Class clazz = Class.forName(job.getJobClass());
        	JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(job.getJobName(), job.getJobGroup()).build();
        	jobDetail.getJobDataMap().put("scheduleJob", job);
        	CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getJobCron());
        	trigger = TriggerBuilder.newTrigger().withIdentity(job.getJobName(), job.getJobGroup()).withSchedule(scheduleBuilder).build();
        	scheduler.scheduleJob(jobDetail, trigger);
        }else{  
        	CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getJobCron());
        	trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
        	scheduler.rescheduleJob(triggerKey, trigger);
        } 
	}
	
	public void updateJob(ScheduleJob scheduleJob) throws SchedulerException {  
        Scheduler scheduler = schedulerfactory.getScheduler();  
        TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());  
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);  
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getJobCron());  
        trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();  
        scheduler.rescheduleJob(triggerKey, trigger);  
    }
}
