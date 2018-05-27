package com.ccrawler.ctrip.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.ccrawler.ctrip.crawler.CtripPageProcessor;
import com.ccrawler.ctrip.crawler.CtripPipeline;
import com.ccrawler.ctrip.crawler.Moniter;

import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.scheduler.component.HashSetDuplicateRemover;

public class CtripCrawlerJob implements Job { 



@Override
public void execute(JobExecutionContext arg0) throws JobExecutionException {
	System.out.println("********新************");
	System.out.println("********新************");
	System.out.println("********新************");
	Spider spider = new Spider(new CtripPageProcessor());
	spider.addUrl("http://flights.ctrip.com/actualtime/arrive-sha/")
				.addUrl("http://flights.ctrip.com/actualtime/depart-sha/")
			.addPipeline(new CtripPipeline())
			.setScheduler(
					new QueueScheduler()
							.setDuplicateRemover(new HashSetDuplicateRemover()))
			.thread(15);
	Moniter moniter = new Moniter(spider);
	moniter.start();
	spider.run();
	}
}
