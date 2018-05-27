

package com.ccrawler.util;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.imageio.ImageIO;


public class FileUtil{
	  private static String pisRootPath="";
	  private static final String FILE_SUBFIX = ",7z,ai,avi,bmp,bt,conf,db,dll,doc,docx,draw,emf,eml,exe," +
	  		"fla,fold,gif,htm,html,ico,iso,jpg,mkv,mp3,pdf,png,ppt,pptx,psd,rar,raw,rm,rmvb,s,swf,tif,ttf," +
	  		"txt,txta,unknow,wangyi,wav,wmf,xls,xlsx,zip,";
	  private static HashMap IMGFIXMAP ;//图片文件的后缀名集合
	  private static HashMap MVFIXMAP ;//视频、语音文件的后缀名集合
	  static{
		    IMGFIXMAP = new HashMap();
		    IMGFIXMAP.put("jpg", "");
			IMGFIXMAP.put("jpeg", "");
			IMGFIXMAP.put("png", "");
			IMGFIXMAP.put("bmp", "");
			IMGFIXMAP.put("gif", "");
			IMGFIXMAP.put("webp", "");
			
		    MVFIXMAP = new HashMap();
		    MVFIXMAP.put("aiff", "");
			MVFIXMAP.put("avi", "");
			MVFIXMAP.put("mov", "");
			MVFIXMAP.put("mpeg", "");
			MVFIXMAP.put("mpg", "");
			MVFIXMAP.put("qt", "");
			MVFIXMAP.put("viv", "");
			MVFIXMAP.put("hzmv", "");
			MVFIXMAP.put("amr", "");
			MVFIXMAP.put("mp3","");
			MVFIXMAP.put("mp4","");

			
			
	  }
	private static String getPisRootPath() {
		//tangql修改,不依赖配置文件配置
		if(pisRootPath==null || "".equals(pisRootPath)){
			pisRootPath=FileUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath();
			if (pisRootPath.indexOf(":")==-1) {
				pisRootPath=pisRootPath.substring(0, pisRootPath.lastIndexOf("/"));
			}else{
				pisRootPath=pisRootPath.substring(1, pisRootPath.lastIndexOf("/"));
			}
			
			if(pisRootPath.indexOf("/bin")!=-1){
				pisRootPath = pisRootPath.replace("/bin", "");
			}
			
		}
		return pisRootPath;
	}
	public static String getWebRoot(){
		return getPisRootPath();
	}  
	/**
	 * 判断当前文件是否是图片文件
	 * @param fileName 文件名
	 * @return Boolean true 是 false 否
	 */
	public static Boolean isImgFile(String fileName){
		if(fileName==null||fileName.length()==0)return false;
		String suffix = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
		if(IMGFIXMAP.containsKey(suffix.toLowerCase()))return true;
		return false;
	}
	/**
	 * 判断当前文件是否是视频文件
	 * @param fileName 文件名
	 * @return Boolean true 是 false 否
	 */
	public static Boolean isMVFile(String fileName){
		if(fileName==null||fileName.length()==0)return false;
		String suffix = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
		if(MVFIXMAP.containsKey(suffix.toLowerCase()))return true;
		return false;
	}
	/**
	 * 重绘图像
	 * @param sourceFile 源图片
	 * @param redrawImage  目标图片路径和文件名
	 * @param width 目标图片宽度
	 * @param height 目标图片高度
	 * @throws Exception
	 */
	/*public static void reDrawImage(File sourceFile, String redrawImage, int width, int height)throws Exception{
		BufferedImage image = ImageIO.read(sourceFile);
		BufferedImage bufImage = new BufferedImage((int)width, (int)height, BufferedImage.TYPE_INT_RGB);
		bufImage.getGraphics().drawImage(image.getScaledInstance(width, height, BufferedImage.SCALE_AREA_AVERAGING), 0, 0, null);
		FileOutputStream out = new FileOutputStream(redrawImage);
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		encoder.encode(bufImage);
		out.close();
	}*/
	/**
	   * 扫描指定文件夹下面的文件，只扫描当前目录，不进行递归扫描
	   * @param path 文件夹路径
	   * @param suffix 需要扫描的文件后缀名
	   * @return
	   * @throws Exception
	   */
	  public static List scanFolder(String path, String suffix)throws Exception{
		  List list = new ArrayList();
		  File file = new File(path);
		  if (!file.exists() || file.isFile()) {
			return list;
		  }
		  String[] files = file.list();
		  for (int i = 0; i < files.length; i++) {
			if (suffix==null || "".equals(suffix)) {
				list.add(files[i]);
			}else {
				if (files[i].toLowerCase().endsWith(suffix.toLowerCase())) {
					list.add(files[i]);
				}
			}
		  }
		  return list;
	  }
	  
  /**
   * 统一取配置文件路径
   * @param fileName 文件名称，需要以不能以/符号开头
   * @return
   */
  public static String getConfAppPath(String fileName){
	  System.out.println(getPisRootPath() + "/WEB-INF/classes/conf/app/" + fileName);
	  return getPisRootPath() + "/WEB-INF/classes/conf/app/" + fileName;
  }
  public static String getConfPath(){
	  return getPisRootPath() + "/WEB-INF/classes/conf";
  }  
  /**
   * 获取消息服务器配置文件的路径
   * @param fileName
   * @return
   */
  public static String getConfMsgserverPath(String fileName){
	  System.out.println(getPisRootPath() + "/WEB-INF/classes/conf/msgserver/" + fileName);
	  return getPisRootPath() + "/WEB-INF/classes/conf/msgserver/" + fileName;
  }
  
  /**
   * 通过文件后缀名获取文件图标
   * @param fileName 文件名称
   * @return
   */
  public static String getFileIcon(String fileName){
	  String unknow = "unknow.gif";
	  if (fileName==null || "".equals(fileName)) {
	    	return unknow;
	    }
	    String subfix = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
		String icon = unknow;
		if (FILE_SUBFIX.indexOf(","+subfix.toLowerCase()+",")!=-1) {
			icon = subfix.toLowerCase() + ".gif";
		}
		return icon;
  }
  
  

  
  public static void newFolder(String folderPath)
  {
    String filePath;
    try
    {
      filePath = folderPath;
      filePath = filePath.toString();
      File myFilePath = new File(filePath);
      if (!(myFilePath.exists()))
        myFilePath.mkdir();
    }
    catch (Exception e)
    {
      System.out.println("新建目录操作出错");
      e.printStackTrace();
    }
  }

  public void newFile(String filePathAndName, String fileContent)
  {
    String filePath;
    FileWriter resultFile = null;
    PrintWriter myFile = null;
    try
    {
      filePath = filePathAndName;
      int i = filePath.lastIndexOf("/");
      i = i<0?filePath.lastIndexOf(File.separator):i;
      filePath = filePath.substring(0,i);
      filePath = filePath.toString();
      File folder = new File(filePath);
      if(!folder.exists()){
    	  folder.mkdir();
      }
      File myFilePath = new File(filePathAndName);
      
      
//      System.out.println(filePath);
      if (!(myFilePath.exists()))
        myFilePath.createNewFile();

      resultFile = new FileWriter(myFilePath);
      myFile = new PrintWriter(resultFile);
      String strContent = fileContent;
      myFile.println(strContent);
    }
    catch (Exception e)
    {
      System.out.println("新建目录操作出错");
      e.printStackTrace();
    }finally{
    	try {
    		myFile.close();
    	    resultFile.close();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
  }

  public static void delFile(String filePathAndName)
  {
    String filePath;
    try
    {
      filePath = filePathAndName;
      filePath = filePath.toString();
      File myDelFile = new File(filePath);
      myDelFile.delete();
    }
    catch (Exception e)
    {
      System.out.println("删除文件操作出错");
      e.printStackTrace();
    }
  }

  public static void delFolder(String folderPath)
  {
    try
    {
      delAllFile(folderPath);
      String filePath = folderPath;
      filePath = filePath.toString();
      File myFilePath = new File(filePath);
      myFilePath.delete();
    }
    catch (Exception e)
    {
      System.out.println("删除文件夹操作出错");
      e.printStackTrace();
    }
  }

  public static void delAllFile(String path)
  {
    File file = new File(path);
    if (!(file.exists()))
      return;

    if (!(file.isDirectory()))
      return;

    String[] tempList = file.list();
    File temp = null;
    for (int i = 0; i < tempList.length; ++i) {
      if (path.endsWith(File.separator)) {
        temp = new File(path + tempList[i]);
      }
      else
        temp = new File(path + File.separator + tempList[i]);

      if (temp.isFile())
        temp.delete();

      if (temp.isDirectory()) {
        delAllFile(path + "/" + tempList[i]);
        delFolder(path + "/" + tempList[i]);
      }
    }
  }

  public void copyFile(String oldPath, String newPath)
  {
    int bytesum;
    InputStream inStream = null;
    //FileOutputStream fs = null;
    try
    {
      bytesum = 0;
      int byteread = 0;
      File oldfile = new File(oldPath);
      if (oldfile.exists()) {
        inStream = new FileInputStream(oldPath);
        copyFile(inStream,newPath);
      }
    }
    catch (Exception e) {
      System.out.println("复制单个文件操作出错");
      e.printStackTrace();
    }finally{
    	try {
    		//fs.close();
    		if(inStream!=null){
    			inStream.close();
    		}
    	} catch (Exception e) {
			e.printStackTrace();
		}
    }
  }

  public void copyFile(File file, String newPath)
  {
    int bytesum;
    InputStream inStream = null;
    FileOutputStream fs = null;
    try
    {
      bytesum = 0;
      int byteread = 0;
      if (file.exists()) {
        inStream = new FileInputStream(file);
        fs = new FileOutputStream(newPath);
        byte[] buffer = new byte[1444];

        while ((byteread = inStream.read(buffer)) != -1) {
          bytesum += byteread;
          System.out.println(bytesum);
          fs.write(buffer, 0, byteread);
        }
      }
    }
    catch (Exception e) {
      System.out.println("复制单个文件操作出错");
      e.printStackTrace();
    }finally{
    	try {
    		fs.close();
            inStream.close();
    	} catch (Exception e) {
			e.printStackTrace();
		}
    }
  }

  public static void copyFile(InputStream inStream, String newPath) {
	    int bytesum;
	    FileOutputStream fs = null;
	    try {
	    	/*************临时增加  add by liubouri 2010-07-28*************************/
		    String filePath = newPath;
		    int i = filePath.lastIndexOf("/");
		    if (i<0) {
				i = filePath.lastIndexOf(File.separator);
			}
		    filePath = filePath.substring(0,i);
		    filePath = filePath.toString();
		    File folder = new File(filePath);
		    if(!folder.exists()){
		  	  folder.mkdirs();
		    }      	
	    	/*************ends**************************/    	
	    	
	      bytesum = 0;
	      int byteread = 0;
	      fs = new FileOutputStream(newPath);
	      byte[] buffer = new byte[1444];
	      while ((byteread = inStream.read(buffer)) != -1) {
	        bytesum += byteread;
//	        System.out.println(bytesum);
	        fs.write(buffer, 0, byteread);
	      }
	    }
	    catch (Exception e) {
	      System.out.println("复制单个文件操作出错");
	      e.printStackTrace();
	    }finally{
	    	try {
	    		if(fs!=null){
	    			fs.close();
	    			fs=null;
	    		}
	            inStream.close();
	    	} catch (Exception e) {
				e.printStackTrace();
			}
	    }
	  }

  public static void copyFolder(String oldPath, String newPath)
  {
      FileInputStream input = null;
      FileOutputStream output = null;
    try
    {
      //new File(newPath).mkdirs();
      File newFolder = new File(newPath);
      if(!newFolder.exists()){
    	  newFolder.mkdirs();
      }
      File a = new File(oldPath);
      String[] file = a.list();
      File temp = null;
      for (int i = 0; i < file.length; ++i) {
        if (oldPath.endsWith(File.separator)) {
          temp = new File(oldPath + file[i]);
        }
        else {
          temp = new File(oldPath + File.separator + file[i]);
        }

        if (temp.isFile()) {
          int len;
          input = new FileInputStream(temp);
          output = new FileOutputStream(newPath + "/" + temp.getName().toString());
          byte[] b = new byte[5120];

          while ((len = input.read(b)) != -1)
            output.write(b, 0, len);

          output.flush();
          input.close();
          output.close();
        }
        if (temp.isDirectory())
          copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
      }
    }
    catch (Exception e)
    {
      System.out.println("复制整个文件夹内容操作出错");
      e.printStackTrace();
    }finally{
    	try {
            output.close();
            input.close();
    	}catch (Exception e){
    		e.printStackTrace();
    	}
    }
  }

  public void moveFile(String oldPath, String newPath)
  {
    copyFile(oldPath, newPath);
    delFile(oldPath);
  }

  public void moveFolder(String oldPath, String newPath)
  {
    copyFolder(oldPath, newPath);
    delFolder(oldPath);
  }
  /**
   * 指定字符集读取文件
   * @param filePath 文件路径
   * @param charset 读取字符集
   * @return
   */
  @SuppressWarnings("finally")
  public static String getFileContentByCharset(String filePath, String charset){
	  StringBuffer buffer = new StringBuffer();
	  try {
			FileInputStream fis = new FileInputStream(filePath);
			InputStreamReader reader = new InputStreamReader(fis, charset);
			BufferedReader bf = new BufferedReader(reader);
			String line = null;
			while ((line = bf.readLine())!=null) {
				buffer.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			return buffer.toString();
		}
  }
  
  public static String getFileContent(String filePath)
  {
    StringBuffer fileContent = new StringBuffer("");
    BufferedReader br = null;
    try {
      File file = new File(filePath);
      if ((file.exists()) && (file.isFile())) {
        String line;
        br = new BufferedReader(new FileReader(filePath));

        while ((line = br.readLine()) != null)
          fileContent.append(line);
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
    	try {
    		br.close();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    return fileContent.toString();
  }

  /**
   * 按格式读取文件内容
   * @param filePath
   * @param format
   * @return
   */
  public static String getFileContent(String filePath, String format){
	  StringBuffer fileContent = new StringBuffer("");
	    BufferedReader br = null;
	    try {
	      File file = new File(filePath);
	      if ((file.exists()) && (file.isFile())) {
	        String line;
	        br = new BufferedReader(new FileReader(filePath));

	        while ((line = br.readLine()) != null){
	        	if (!StringUtil.isEmpty(line)) {
	        		fileContent.append(line);
		        	if ("html".equalsIgnoreCase(format)) {
						fileContent.append("<br>");
					}
				}
	        }
	      }
	    } catch (Exception e) {
	      e.printStackTrace();
	    } finally {
	    	try {
	    		br.close();
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    	}
	    }
	    return fileContent.toString();
  }
  
  public static boolean setFileContent(String filePath, String fileContent)
  {
    File file;
    DataOutputStream dos = null;
    try
    {
      file = new File(filePath);
      if ((file.exists()) && (file.isFile())) {
        dos = new DataOutputStream(new FileOutputStream(filePath));
        dos.writeBytes(fileContent);
        return true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
    	try {
    		dos.close();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    return false;
  }

  public static void writeFile(String filePathAndName, String fileContent) { 
	   OutputStreamWriter write = null; 
	   BufferedWriter writer=null;   
	  try { 
	   File f = new File(filePathAndName); 
	   if (!f.exists()) { 
		   f.createNewFile(); 
	   } 
	   write = new OutputStreamWriter(new FileOutputStream(f),"UTF-8"); 
	   writer=new BufferedWriter(write);   
	   writer.write(fileContent); 
	  } catch (Exception e) { 
	   System.out.println("写文件内容操作出错:" + filePathAndName); 
	   e.printStackTrace(); 
	  } finally {
		  try {
			  writer.close();
			  write.close();
		  } catch (Exception e) {
			  e.printStackTrace();
		  }
	  }
	} 
  
  public static void appendWriteFile(String filePathAndName, String fileContent) { 
	   OutputStreamWriter write = null; 
	   BufferedWriter writer=null;   
	  try { 
	   File f = new File(filePathAndName); 
	   if (!f.exists()) { 
		   f.createNewFile(); 
	   } 
	   write = new OutputStreamWriter(new FileOutputStream(f,true),"UTF-8"); 
	   writer=new BufferedWriter(write);   
	   writer.write(fileContent);
	  } catch (Exception e) { 
	   System.out.println("写文件内容操作出错"); 
	   e.printStackTrace(); 
	  } finally {
		  try {
			  writer.close();
			  write.close();
		  } catch (Exception e) {
			  e.printStackTrace();
		  }
	  }
	} 
  
  public static void SetExcelFolder(String realPath, String sessionId)
  {
    String fileName = realPath + "ExcelFolder/" + sessionId + "/";
    new File(fileName).mkdirs();
  }

  public static boolean openFile(String fileAddress)
  {
    boolean info = false;
    try
    {
      Runtime.getRuntime().exec("cmd.exe /c " + fileAddress);

      info = true;
    } catch (IOException e) {
      e.printStackTrace();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return info;
  }
  

  /**
   * 获取一个网页的内容
   * @param strUrl
   * @return
   */
	public static String getUrlContent(String strUrl,int timeOut)
	{
		java.io.InputStream is=null;
		BufferedReader br=null;
		try{
			URL url=new URL(strUrl);
			HttpURLConnection httpUrl=(HttpURLConnection)url.openConnection();
			httpUrl.setConnectTimeout(timeOut);
			httpUrl.setReadTimeout(timeOut);
			httpUrl.setRequestProperty("Content-Type","text/xml");
			httpUrl.connect();
			is=httpUrl.getInputStream();
			
			br=new BufferedReader(new InputStreamReader(is));
			String s="";
			StringBuffer sb=new StringBuffer("");
			while((s=br.readLine())!=null)
			{
			 sb.append(s); 
			 }
			return sb.toString();
		}
		catch(Exception e){
			e.printStackTrace();
			return "faile";
		} finally {
			try {
				is.close();
				br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 获取当前文件的大小
	 * @param fileUrl 文件路径
	 * @return long 文件大小
	 * @throws AppException
	 * @throws IOException 
	 */
	public static long getFileSize(String fileUrl)throws Exception,IOException{
		if(fileUrl==null||fileUrl.length()==0)throw new Exception("文件路径为空，无法计算文件大小");
		File file = new File(fileUrl);
		FileChannel fc = null;
		
		if(!file.exists()){
			throw new Exception("当前文件不存在");
		}else if(file.isFile()){
			FileInputStream fis = new FileInputStream(file);
			fc = fis.getChannel();
			long fileSize = fc.size();
			fis.close();
			return fileSize;
		}else{
			throw new Exception("当前路径不是文件路径，无法计算文件的大小");
		}
	}
		
	public static void main(String[] args){
		try{
			/*String filePath="D:/lbr/web_project/pis3.0/pasdata/datafile/2012/1105/1705/web/news.qq.com/";
			File myFilePath = new File(filePath);
			myFilePath.mkdirs();*/
			//FileUtil.reDrawImage(new File("c:/DSC03132_.gif"), "c:/DSC03132_1.jpg", 2000, 2500);
			System.out.println(FileUtil.getFileSize("D:/export/admin/1438245900751.zip"));
		}catch (Exception e) {
			e.printStackTrace();
		}
		
 	}
  
}