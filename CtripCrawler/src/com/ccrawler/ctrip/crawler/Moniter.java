package com.ccrawler.ctrip.crawler;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ccrawler.util.DateUtil;
import com.ccrawler.util.FileUtil;
import com.ccrawler.util.PropertyUtil;

import us.codecraft.webmagic.Spider;

public class Moniter extends Thread {

	private static Logger logger = LoggerFactory.getLogger(Moniter.class);
	
	private Spider spider;
	
	public Moniter(Spider spider){
		this.spider = spider;
	}
	//Init(0), Running(1), Stopped(2);
	public void run(){
		while(true){
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(spider.getStatus().toString()=="Stopped"){
				//System.out.println("spider结束："+spider.getStatus());
				logger.info("spider："+spider.getStatus());
				String path = PropertyUtil.getInstants().getProp("TableFilePath");
				String bakpath = PropertyUtil.getInstants().getProp("TableFileBackUpPath");
				File file = new File(path);
				if(file.exists()){
						 String[] files = file.list(); 
						 //备份至以日期命名的文件夹下
						 String folder = DateUtil.getCurrentDate("yyyyMMdd");
						 String	expPath = bakpath + "\\" + folder + "\\";
						 if(files.length>0){
							 String newname = "ctrip"+System.currentTimeMillis()+".csv";
							 String oldname = files[0];
								if(!oldname.equals(newname)){//新的文件名和以前文件名不同时,才有必要进行重命名 
						            File oldfile=new File(path+"/"+oldname); 
						            File newfile=new File(path+"/"+newname); 
						            if(!oldfile.exists()){
						                return;//重命名文件不存在
						            }
						            if(newfile.exists())//若在该目录下已经有一个文件和新文件名相同，则不允许重命名 
						            	logger.info(newname+"已经存在！"); 
						            else{ 
						                oldfile.renameTo(newfile); //重命名文件，然后再转移文件
						                logger.info("重命名:"+newname);
										File expDir = new File(expPath);
										if(!expDir.exists()){
											expDir.mkdirs();
										}
						            } 
						        }else{
						        	 logger.info("文件名相同，无需重命名");
						        }
								logger.info("备份文件");
								FileUtil.copyFolder(path, expPath);//复制备份文件
								FileUtil.delAllFile(path);//删除文件
						 }
				        }
				break;
			}
		}
	}
}
