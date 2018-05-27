package com.ccrawler.ctrip;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ccrawler.ctrip.job.JobManager;
import com.ccrawler.ctrip.vo.ScheduleJob;
import com.ccrawler.util.PropertyUtil;

public class CrawlerStart   {

	private static Logger logger = LoggerFactory.getLogger(CrawlerStart.class);
	
	public static void addCrawlerJob(){		
		try {
			String crawlerTime= PropertyUtil.getInstants().getProp("crawlerTime");
			ScheduleJob job = new ScheduleJob();
			job.setJobName("addCrawlerJTask");
			job.setJobCron(crawlerTime);//定时时间
			job.setJobGroup("CountDayGroup");
			job.setJobClass("com.ccrawler.ctrip.job.CtripCrawlerJob");
			JobManager.getInstance().addJob(job);
			logger.info("增加定时任务：隔3分钟定时抓取一次数据");
		} catch (Exception e) {			
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException {
		//创建定时任务
		addCrawlerJob();
	}
	

}
