package com.ccrawler.ctrip.crawler;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.JMException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;





import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ccrawler.ctrip.CrawlerStart;
import com.ccrawler.ctrip.vo.CtripArriveVO;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.monitor.SpiderMonitor;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.scheduler.component.HashSetDuplicateRemover;

public class CtripPageProcessor implements PageProcessor {

	private static Logger logger = LoggerFactory.getLogger(CtripPageProcessor.class);
	//private static Logger logger = Logger.getLogger(CtripPageProcessor.class );

	private Site site = Site
			.me()
			.setCycleRetryTimes(5)
			.setRetryTimes(5)
			.setSleepTime(500)
			.setTimeOut(3 * 60 * 1000)
			.setUserAgent(
					"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0")
			.addHeader("Accept",
					"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
			.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
			.setCharset("gb2312");

	@Override
	public Site getSite() {
		site.me().setDomain("http://flights.ctrip.com");
		return site;
	}

	// \w代表英文、数字和“_”
	@Override
	public void process(Page page) {
		List<String> pages = page.getHtml().css(".c_page .c_page_list a")
				.links().all();//提取下一页链接
		page.addTargetRequests(pages);//把下一页链接加入抓取队列
		org.jsoup.nodes.Document doc = Jsoup.parse(page.getHtml().toString());
		Elements data = doc.select(".dynamic_data tbody tr");
		//System.out.println("第:"+page.getUrl());
		Date day=new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String crawlerTime = df.format(day);
		List<CtripArriveVO> list = new ArrayList<CtripArriveVO>();
		if(data.size()<20){
			logger.info("长："+data.size()+","+page.getUrl());
			logger.info(data.toString());
		}
		for (int i = 0; i < data.size(); i++) {
			Elements elements = data.get(i).select("td");
			String flyNo = elements.get(0).text();
			String fromAir = elements.get(1).text();
			String arriveAir = elements.get(2).text();
			String planTime = elements.get(3).text();
			System.out.println("planTime"+planTime+"planTime");
			String planFly = "";
			String planArrive = "";
			if (!planTime.equals(" ")) {
				if(planTime.split(" ").length==1){
					planArrive = planTime.split(" ")[1];
				}
				if(planTime.split(" ").length==2){
					planFly = planTime.split(" ")[0];
						planArrive = planTime.split(" ")[1];
				}
			}

			String planTimes = elements.get(4).text();
			//System.out.println("planTimes"+planTimes+"planTimes");
			String estimateFly = "";
			String estimateArrive = "";

			if ((!planTimes.equals(" "))&&(!planTimes.equals(""))) {
				if(planTimes.split(" ").length==1){
					estimateArrive = planTimes.split(" ")[1];
				}
				if(planTimes.split(" ").length==2){
					estimateFly = planTimes.split(" ")[0];
						estimateArrive = planTimes.split(" ")[1];
				}
			}
			String actualFly = "";
			String actualArrive = "";
			String actualTime = elements.get(5).text();
			if (!actualTime.equals(" ")) {// 特殊空格形式xx xx
				if(actualTime.split(" ").length==1){
					actualArrive = actualTime.split(" ")[0];
				}
				if(actualTime.split(" ").length==2){
					actualFly = actualTime.split(" ")[0];
						actualArrive = actualTime.split(" ")[1];
				}
			}
			String status = elements.get(6).text();

			/*System.out.println("航班：" + flyNo + ",出发机场:" + fromAir + ",到达机场:"
					+ arriveAir + "，计划时间:" + planTime + ",实际时间:" + actualTime
					+ ",状态：" + status);
			System.out.println("计划起飞:" + planFly + ",计划降落：：" + planArrive
					+ ",实际起飞：" + actualFly + ",实际降落：" + actualArrive);*/
			CtripArriveVO vo = new CtripArriveVO();
			vo.setActualArrive(actualArrive);
			vo.setActualFly(actualFly);
			vo.setArriveAirport(arriveAir);
			vo.setFromAirport(fromAir);
			vo.setFlightNo(flyNo);
			vo.setPlanFly(planFly);
			vo.setPlanArrive(planArrive);
			vo.setStatus(status);
			vo.setEstimateArrive(estimateArrive);
			vo.setEstimateFly(estimateFly);
			vo.setCrawlerTime(crawlerTime);
			list.add(vo);
		}
		page.putField("ctrip", list);

	}

	public static void main(String[] args) {
		//PropertyConfigurator.configure("src/conf/log4j.properties");
		Spider spider = new Spider(new CtripPageProcessor());
		
		//spider.addUrl("http://flights.ctrip.com/actualtime/arrive-sha/")
		//			.addUrl("http://flights.ctrip.com/actualtime/depart-sha/")
		spider.addUrl("http://flights.ctrip.com/actualtime/depart-syx/")
		.addPipeline(new CtripPipeline())
				.setScheduler(
						new QueueScheduler()
								.setDuplicateRemover(new HashSetDuplicateRemover()))
				.thread(15);
		/*Moniter moniter = new Moniter(spider);
		moniter.start();*/
		spider.run();
		try {
			SpiderMonitor.instance().register(spider);
		} catch (JMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
