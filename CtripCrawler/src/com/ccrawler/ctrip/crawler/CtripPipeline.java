package com.ccrawler.ctrip.crawler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;





import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ccrawler.ctrip.vo.CtripArriveVO;
import com.ccrawler.util.PropertyUtil;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.FilePipeline;

public class CtripPipeline extends FilePipeline {

	private static Logger logger = LoggerFactory.getLogger(CtripPipeline.class);
	//private static Logger logger = Logger.getLogger(CtripPipeline.class );

	@Override
	public void process(ResultItems resultItems, Task arg1) {
		// TODO Auto-generated method stub
		List<CtripArriveVO> list = resultItems.get("ctrip");
		StringBuilder csvStr = new StringBuilder();
		Calendar c = Calendar.getInstance();// 可以对每个时间域单独修改
		 Date day=new Date();
		 SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		 String today = df.format(day);
		 String time = System.currentTimeMillis()+"";
		//logger.info("长："+list.size());

		for (int i = 0; i < list.size(); i++) {
			CtripArriveVO vo = new CtripArriveVO();
			vo = list.get(i);
			String flyNo = vo.getFlightNo();
			String fromAir = vo.getFromAirport();
			String arriveAir = vo.getArriveAirport();
			String planFly = vo.getPlanFly();
			String planArrive = vo.getPlanArrive();

			String estimateFly = vo.getEstimateFly();
			String estimateArrive = vo.getEstimateArrive();
			String actualFly = vo.getActualFly();
			String actualArrive = vo.getActualArrive();
			String status = vo.getStatus();

			if (flyNo != null && fromAir != null) {
				try {
					//String filePath = "C:/ctrip/exportcsv";
					String filePath = PropertyUtil.getInstants().getProp("TableFilePath");
					File filedir = new File(filePath);
					// 如果文件夹不存在则创建
					if (!filedir.exists() && !filedir.isDirectory()) {
						filedir.mkdir();
					}
					//String filename = filePath +"/ctrip"+ today + ".csv";
					String filename = filePath +"/ctrip" + ".csv";

					File file = new File(filename);
					
					csvStr.delete( 0, csvStr.length() );
					// csvStr.append("航班,出发机场,到达机场,计划起飞时间,计划降落时间,预计起飞时间,预计降落时间,实际起飞时间,实际降落时间,状态"+"\n");
					csvStr.append(flyNo + ",").append(fromAir + ",")
							.append(arriveAir + ",").append(planFly + ",")
							.append(planArrive + ",").append(estimateFly + ",")
							.append(estimateArrive + ",")
							.append(actualFly + ",").append(actualArrive + ",")
							.append(status + ",")
							.append(vo.getCrawlerTime()).append("\n");
					if (file.exists()) {
						FileWriter writer;
						writer = new FileWriter(filename, true);
						writer.write(csvStr.toString());
						writer.flush();
						writer.close();
					
					} else {
						FileWriter writer;
						writer = new FileWriter(filename, true);
						writer.write(csvStr.toString());
						writer.flush();
						writer.close();
						
						/*Writer writer = new BufferedWriter(
								new OutputStreamWriter(new FileOutputStream(
										new File(filename)), "utf-8"));
						writer.write(csvStr.toString());
						writer.flush();
						writer.close();*/
					}
					 /*FileWriter writer = null; try {
						 writer = new
					  FileWriter(filename, true);
					  writer.write(csvStr.toString()); writer.flush();
					  writer.close();
					  } catch (IOException e) { 
						  writer.close();
					  e.printStackTrace(); 
					 }*/
					 
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		}

	}

	@Override
	public void checkAndMakeParentDirecotry(String arg0) {
		// TODO Auto-generated method stub
		super.checkAndMakeParentDirecotry(arg0);
	}

	@Override
	public File getFile(String fullName) {
		// TODO Auto-generated method stub
		return super.getFile(fullName);
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return super.getPath();
	}

	@Override
	public void setPath(String path) {
		// TODO Auto-generated method stub
		super.setPath(path);
	}

}
