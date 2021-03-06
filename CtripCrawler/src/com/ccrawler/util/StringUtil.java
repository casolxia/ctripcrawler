package com.ccrawler.util;

import java.io.IOException;
import java.io.Reader;
import java.net.URLDecoder;
import java.sql.Clob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;



 
public class StringUtil {
	
	public static final String DELIMITER = "~`~";
	public static final byte[] DES_KEY_PUBLIC = {1, 3, 5, 7, 2, 4, 6, 8};
	
	/**
	 * 将字符串内容中的script脚本代码删除
	 * @param content 字符串内容
	 * @return
	 */
	public static String filterScript(String content) {
		if (hasText(content)) {
			String reg = "<script[^>]*>[\\s\\S]*?</script>";
			String replace = "";
			Pattern pat = Pattern.compile(reg);
			Matcher mat = pat.matcher(content); 
			return mat.replaceAll(replace);
		}else {
			return content;
		}
	    
	}
	
	
	public static String pageValue(String value) {
		return pageValue(value, true);
	}

	public static boolean hasText(String str){
		return !StringUtil.isEmpty(str) && !"null".equalsIgnoreCase(str);
	}
	/**
	 * 如果通过连接传递中文，可以通过该方法处理乱码
	 * 
	 * @param str
	 * @return
	 */
	public static String handleUrlCN(String str) {
		String ret = str;
		try {
			ret = URLDecoder.decode(str, "UTF-8");
		} catch (Exception e) {
		}
		return ret;

	}

	/**
	 * utf8 转 gb2312 ，用于手机显示
	 * 
	 * @param strValue
	 *            String
	 * @return String
	 */
	public static String UTF8ToGB(String strValue) {
		if (strValue == null || strValue.trim().length() == 0) {
			return null;
		}
		StringBuffer strbuf = new StringBuffer();
		String[] strarr = strValue.split(";");
		int il = strarr.length;
		for (int i = 0; i < il; i++) {
			int pos = strarr[i].indexOf("&#");
			if (pos >= 0) {
				if (pos > 0) {
					strbuf.append(strarr[i].substring(0, pos));
				}

				String tmp = strarr[i].substring(pos + 2);
				if (tmp.startsWith("00")) {
					tmp = tmp.substring(2);
				}

				try {
					int l = Integer.valueOf(tmp);
					if ((l > 10000) || (l < 1000)) {
						strbuf.append((char) l);
					} else {
						strbuf.append("&#").append(tmp).append(";");
					}
				} catch (NumberFormatException e) {
					strbuf.append(tmp);
				}
			} else {
				strbuf.append(strarr[i]);
			}
		}
		return strbuf.toString();
	}

	/**
	 * @param value
	 *            String 需要转换的字符串
	 * @param flag
	 *            boolean 是否需要转换为html格式的空格
	 * @return 如果value不为null，返回原值。 否则如果boolean为true，返回&nbsp; 否则为空字符串
	 * */
	public static String pageValue(String value, boolean flag) {
		if (flag && value == null)
			return "&nbsp;";
		if (!flag && value == null)
			return "";
		else
			return value;
	}

	/**
	 * 字符串换成Html
	 * 
	 * @param str
	 * @return
	 */
	public static String strToHtml(String str) {
		if (str == null) {
			return null;
		}
		return str.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(
				">", "&gt;").replaceAll("\"", "&quot")
				.replaceAll(" ", "&nbsp;").replaceAll("\n", "<br>");
	}

	/**
	 * 字符串换成Html，只处理&、<、>
	 * 
	 * @param str
	 * @return
	 */
	public static String strToHtml2(String str) {
		if (str == null) {
			return null;
		}
		return str.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	}



	public static String replace(String s, String s1, String s2) {
		if (s == null)
			return null;
		int i = 0;
		if ((i = s.indexOf(s1, i)) >= 0) {
			char ac[] = s.toCharArray();
			char ac1[] = s2.toCharArray();
			int j = s1.length();
			StringBuffer stringbuffer = new StringBuffer(ac.length);
			stringbuffer.append(ac, 0, i).append(ac1);
			i += j;
			int k;
			for (k = i; (i = s.indexOf(s1, i)) > 0; k = i) {
				stringbuffer.append(ac, k, i - k).append(ac1);
				i += j;
			}

			stringbuffer.append(ac, k, ac.length - k);
			return stringbuffer.toString();
		} else {
			return s;
		}
	}

	/**
	 * 根据全文件名获取文件路径
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getFilePath(String pasDataFileName) {
		return pasDataFileName.substring(0,
				pasDataFileName.lastIndexOf("/") + 1);
	}

	/**
	 * 根据全文件名获取文件名
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getFileName(String pasDataFileName) {
		return pasDataFileName.substring(pasDataFileName.lastIndexOf("/") + 1);
	}

	/**
	 * @seejava版本的escape函数,具体参看javascript的escape函数
	 * */
	public static String escape(String src) {
		int i;
		char j;
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length() * 6);
		for (i = 0; i < src.length(); i++) {
			j = src.charAt(i);
			if (Character.isDigit(j) || Character.isLowerCase(j)
					|| Character.isUpperCase(j))
				tmp.append(j);
			else if (j < 256) {
				tmp.append("%");
				if (j < 16)
					tmp.append("0");
				tmp.append(Integer.toString(j, 16));
			} else {
				tmp.append("%u");
				tmp.append(Integer.toString(j, 16));
			}
		}
		return tmp.toString();
	}

	/**
	 * @seejava版本的unescape函数,具体参看javascript的unescape函数
	 * */
	public static String unescape(String src) {
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length());
		int lastPos = 0, pos = 0;
		char ch;
		while (lastPos < src.length()) {
			pos = src.indexOf("%", lastPos);
			if (pos == lastPos) {
				if (src.charAt(pos + 1) == 'u') {
					ch = (char) Integer.parseInt(src
							.substring(pos + 2, pos + 6), 16);
					tmp.append(ch);
					lastPos = pos + 6;
				} else {
					ch = (char) Integer.parseInt(src
							.substring(pos + 1, pos + 3), 16);
					tmp.append(ch);
					lastPos = pos + 3;
				}
			} else {
				if (pos == -1) {
					tmp.append(src.substring(lastPos));
					lastPos = src.length();
				} else {
					tmp.append(src.substring(lastPos, pos));
					lastPos = pos;
				}
			}
		}
		return tmp.toString();
	}

	/**
	 * 数组转化成字符串：|aa|bb|（分隔符要指定）
	 * 
	 * @param values
	 * @param seperator
	 *            分隔符
	 * @return
	 */
	public static String arrayToString(String[] values, String seperator) {
		StringBuffer valueStr = new StringBuffer();
		if ((values != null) && (values.length > 0)) {
			for (int i = 0; i < values.length; i++) {
				if ((values[i] != null) && !"".equals(values[i].trim())) {
					valueStr.append(seperator).append(values[i]);
				}
			}
			valueStr.append(seperator);
		}
		return valueStr.toString();
	}

	/**
	 * 字符串转化成数组：|aa|bb|（分隔符要指定）
	 * 
	 * @param valueStr
	 * @param seperator
	 *            分隔符
	 * @return
	 */
	public static String[] stringToArray(String valueStr, String seperator) {
		String[] values = null;
		/**
		 * 如果分隔符是"|"，那么拆分时要把"|"改成"\\|"才有效
		 */
		String realSeperator = null;
		if ("|".equals(seperator)) {
			realSeperator = "\\|";
		} else {
			realSeperator = seperator;
		}
		if ((valueStr != null) && !"".equals(valueStr.trim())) {
			values = valueStr.split(realSeperator);
		}
		return values;
	}

	/**
	 * 判断字符串是否为空(null或"")
	 * 
	 * @param value
	 * @return
	 */
	public final static boolean isEmpty(String value) {
		return (value == null || "".equals(value.trim()) || "null".equals(value.trim().toLowerCase()));
	}

	/**
	 * null字符串转换成""，如果不为null则返回原值
	 * 
	 * @param value
	 * @return
	 */
	public final static String nullToEmpty(String value) {
		if (isEmpty(value)) {
			return "";
		}
		return value;
	}
	

	public static final String nullToEmptyOfStr(String s) {
		return nullToEmpty(s);
	}

	public static final String nullToEmptyOfStr(String s, String defValue) {
		if (s != null) {
			return s.trim();
		}
		return defValue;
	}

	public static final String nullToEmptyOfPath(Object o) {
		String separator = System.getProperty("file.separator");
		if (o != null) {
			String temp = o.toString();
			if (separator.equals("\\")) {
				temp = replace(temp, "\\", "\\\\");
			}
			return temp;
		}
		return "";
	}


	public static final String nullToEmptyOfBPELId(Object o) {
		if (o != null) {
			return byte2hex((byte[])o);
		}
		return "";
	}

	private static final String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = Integer.toHexString(b[n] & 0xFF);
			if (stmp.length() == 1)
				hs = 
				String.valueOf(
						String.valueOf(
								new StringBuffer(
										String.valueOf(
												String.valueOf(hs))).append(
														"0").append(
																stmp)));
			else {
				hs = String.valueOf(hs) + String.valueOf(stmp);
			}
		}
		return hs.toUpperCase();
	}

	public static final String nullToEmptyOfObject(Object o, Object defValue) {
		if (o != null) {
			return o.toString();
		}
		return defValue.toString();
	}


	/**
	 * 去掉字符串左右空格,如果为null，返回""
	 * 
	 * @param value
	 * @return
	 */
	public final static String trim(String value) {
		if (isEmpty(value)) {
			return "";
		} else {
			return value.trim();
		}
	}

	/**
	 * 转换str，浏览器界面输出，长度限制在len内，超长的就重开一个新行继续
	 * @param str 原始字符串
	 * @param len 输出长度（英文字符的长度）的限制
	 * @returnStr 经过转换的字符串
	 */
	public static String strCtrl_newline(String str, int len){
		String returnStr = "";
		if(str == null || str.trim().length() == 0){
			return returnStr;
		}
		//str = escapeHTML(str);
		char[]temp1 = new char[1];
		temp1[0] = (char) 10;  //处理回车和换行符
		char[] temp2 = new char[1];
		temp2[0] = (char) 13;
		str = str.replaceAll(new String(temp1)," ");
		str = str.replaceAll(new String(temp2)," ");

		int length = 0;
		for (int i = 0; i < str.length(); i++) {
			if (str.indexOf("&nbsp;", i) == i) { // 空格
				length += 1;
				returnStr += "&nbsp;";
				i += 5;
			}
			else if (str.indexOf("&#39;", i) == i) { // '
				length += 1;
				returnStr += "&#39;";
				i += 4;
			}
			else if (str.indexOf("&amp;", i) == i) { // &
				length += 1;
				returnStr += "&amp;";
				i += 4;
			}
			else if (str.indexOf("&lt;", i) == i) { // <
				length += 1;
				returnStr += "&lt;" ;
				i += 3;
			}
			else if (str.indexOf("&gt;", i) == i) { // >
				length += 1;
				returnStr += "&gt;";
				i += 3;
			}
			else if (str.indexOf("&quot;", i) == i) { // "
				length += 1;
				returnStr += "&quot;";
				i += 5;
			}
			else {
				if (str.charAt(i) > 255)   //汉字
					length += 2;
				else
					length += 1;
				returnStr += str.charAt(i);
			}
			if (length > len) {
				returnStr += "<br>";
				length = 0;
			}
		}

		return returnStr;
	}

	public static String  unescapePlusChar(String str){
		if(!isEmpty(str)){
			str = str.replace("%2B","+");
		}
		return str;
	}
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static String escapeXML(String value){
		if(value!=null){
			return value.replaceAll("&", "＆")
			.replaceAll("<","＜")
			.replaceAll("＞", "")
			.replaceAll("\"", "“");
			//.replaceAll("'", "&apos;");
		}else{
			return value;
		}
//		return value;
	}
	
	/**
	 * 判断指定字符串是否是数字。这里不判断是否合法的数字，如"01"返回true
	 * @param value
	 * @return
	 */
	public static boolean isNumber(String value) {
		if (isEmpty(value)) {
			return false;
		}
		for (int i=0; i<value.length(); i++) {
			if ((value.charAt(i) > '9') || (value.charAt(i) < '0')) {
				return false;
			}
		}
		return true;
	}
	
	
	public static long getHash(String str){
		long h = 0;
		long g = 0;
		char[] charArray = str.toCharArray();
		for(int i=0;i<charArray.length;i++){
			int temp = charArray[i];
			h = (h<<4) + temp ;
			g = h&0Xf0000000L;
			if(g!=0){
				 h ^= g >> 24;
			}
			h &= ~g;
		}
		return h;
	}

	/**
	 * 转换str，截取len字节长度的字符串返回，如超长，则截断并结尾追加”..“
	 * @param oriStr 原始字符串
	 * @param len 截取的字节个数
	 * @returnStr 经过转换的字符串
	 */
	public static String stringToEllipsis(/**String startStr,*/String oriStr,int len){
		if(oriStr == null){
			return "";
		}
		int start = 0;
/**		if(hasText(startStr) && oriStr.indexOf(startStr) != -1){
			int startIndex = oriStr.indexOf(startStr);
			String startIndexStr = oriStr.substring(0,startIndex);
			start = startIndexStr.getBytes().length;
		}*/
		byte[] strByte = oriStr.getBytes();
		int strLen = strByte.length;
		int elideLen = 0;
		if(len >= strLen || len < 1){
			return oriStr;
		}
		if(len - elideLen > 0){
			len = len - elideLen;
		}
		int count = 0;
		for(int i=0;i < len; i++){ //单汉字被截取为字节数组，每个字节单个输入为负数
			int value = (int)strByte[i];
			if(value < 0){
				count ++;
			}
		}
		if(count % 2 != 0 ){
			len = (len == 1) ? len + 1 :len -1;
		}
		return new String(strByte,start,len) + "..";
	}

	/**过滤css文件图片url
	 * @param str css文本内容
	 * @return
	 */
	public static String filterCssImgUrl(String str){
		String filterUrl = "url\\([^\\)]*\\)"; 
		String filterHttpUrl = "[\"\']\\s*http://[^\"\']*\\s*[\"\']";
		Pattern p1 = Pattern.compile(filterUrl, Pattern.CASE_INSENSITIVE);
		Pattern p2 = Pattern.compile(filterHttpUrl, Pattern.CASE_INSENSITIVE);
		if(p1.matcher(str).find()){
			str = p1.matcher(str).replaceAll("");
		}
		if(p2.matcher(str).find()){ 
			str = p2.matcher(str).replaceAll("");
		}
		return str;
	}
	
	/**将int类型数字转为网络字节序字节数数组***/
	public static byte[] intToBytes(int n) {
		byte[] b = new byte[4];
		b[3] = (byte) (n & 0xff);
		b[2] = (byte) (n >> 8 & 0xff);
		b[1] = (byte) (n >> 16 & 0xff);
		b[0] = (byte) (n >> 24 & 0xff);
		return b;
	}
	
	public static String getFormatString(String str , String formatSting){
		if(str!=null && str.length()==1){
			return "0" + str;
		}
		return str;
	}
	
	
	/**判断给定的ip是否属于ipSection ip段**/
	public static boolean ipIsValid(String ipSection, String ip) {
		if (ipSection == null)
			throw new NullPointerException("IP段不能为空！");
		if (ip == null)
			throw new NullPointerException("IP不能为空！");
		ipSection = ipSection.trim();
		ip = ip.trim();
		final String REGX_IP = "((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)";
		final String REGX_IPB = REGX_IP + "\\-" + REGX_IP;
		if (!ipSection.matches(REGX_IPB) || !ip.matches(REGX_IP))
			return false;
		int idx = ipSection.indexOf('-');
		String[] sips = ipSection.substring(0, idx).split("\\.");
		String[] sipe = ipSection.substring(idx + 1).split("\\.");
		String[] sipt = ip.split("\\.");
		long ips = 0L, ipe = 0L, ipt = 0L;
		for (int i = 0; i < 4; ++i) {
			ips = ips << 8 | Integer.parseInt(sips[i]);
			ipe = ipe << 8 | Integer.parseInt(sipe[i]);
			ipt = ipt << 8 | Integer.parseInt(sipt[i]);
		}
		if (ips > ipe) {
			long t = ips;
			ips = ipe;
			ipe = t;
		}
		return ips <= ipt && ipt <= ipe;
	}
	
		/**过滤windows文件名非法字符
	 * @param fileName
	 * @return
	 */
	public static String filterWinFileName(String fileName){
		if(hasText(fileName)){
			fileName = fileName.replaceAll("[<\t>:\\*\\?\\|\"/\\\\]", "");
		}
		return fileName;
	}
	
	/**Json特殊字符过滤
	 * @param str 要过滤的字符串
	 * @return 过滤后的字符串
	 */
	public static String jsonCharfilter(String str){
		String ret = str;
		if(hasText(str)){
			ret = ret.replace("\\", "\\\\");
			ret = ret.replace("\"", "\\\"");
			ret = ret.replace("\b", "\\\b");
			ret = ret.replace("\n", "\\\n");
			ret = ret.replace("\f", "\\\f");
			ret = ret.replace("\r", "\\\r");
			ret = ret.replace("\\", "\\\\");
		}
		return ret;
	}
	

	public static String arrayToDelimiterStr(String delimiter, String[] str) {
		StringBuffer buf = new StringBuffer("");
		int i = 0;
		for (i = 0; i < str.length; i++) {
			buf.append(str[i]).append(delimiter);
		}

		if (i > 0) {
			buf.delete(buf.length() - delimiter.length(), buf.length());
		}
		return buf.toString();
	}

	
	/** 
	用DES方法加密输入的字节 
	bytKey需为8字节长，是加密的密码 
	*/ 
	public static String encryptByDES(String str, byte[] bytKey) throws Exception { 
		byte[] data = encryptByDES(str.getBytes(), bytKey);
		String ret = "";
		for (int i = 0; i < data.length; i++) {
			String tmp=	Integer.toString(data[i]&0xff,16);
			if (tmp.length()==1) tmp="0"+tmp;
			if (tmp.length()==0) tmp="00";
			ret = ret + tmp;
		}
		return ret;
	} 
	/** 
	用DES方法加密输入的字节 
	bytKey需为8字节长，是加密的密码 
	*/ 
	public static byte[] encryptByDES(byte[] bytP,byte[] bytKey) throws Exception { 
	   DESKeySpec desKS = new DESKeySpec(bytKey); 
	   SecretKeyFactory skf = SecretKeyFactory.getInstance("DES"); 
	   SecretKey sk = skf.generateSecret(desKS); 
	   Cipher cip = Cipher.getInstance("DES"); 
	   cip.init(Cipher.ENCRYPT_MODE,sk); 
	   return cip.doFinal(bytP); 
	} 
	/** 
	用DES方法解密输入的字节 
	bytKey需为8字节长，是解密的密码 
	*/ 
	public static byte[] decryptByDES(byte[] bytE,byte[] bytKey) throws Exception { 
	   DESKeySpec desKS = new DESKeySpec(bytKey); 
	   SecretKeyFactory skf = SecretKeyFactory.getInstance("DES"); 
	   SecretKey sk = skf.generateSecret(desKS); 
	   Cipher cip = Cipher.getInstance("DES"); 
	   cip.init(Cipher.DECRYPT_MODE,sk); 
	   return cip.doFinal(bytE); 
	} 
	/** 
	用DES方法解密输入的字节 
	bytKey需为8字节长，是解密的密码 
	*/ 
	public static String decryptByDES(String str, byte[] bytKey) throws Exception { 
		byte[] data=new byte[str.length()/2];
		int i;
		for (i = 0; i * 2 < str.length(); i++) {
			data[i] = (byte)(Integer.parseInt(str.substring(2 * i, 2 * i + 2),16));
		}
	   return new String(decryptByDES(data, bytKey));
	} 
	/**
	 * 将字符串转换成16进制字符串
	 * 注意获取字节是按本地字符集处理
	 * @param str
	 * @return
	 */
	public static String String2HexStr(String str){
		String ret = "";
		byte[] b = str.getBytes();
		for (int i = 0; i < b.length; i++) {			
			String tmp = Integer.toHexString(b[i] & 0xFF);
			if (tmp.length() == 1) {
				tmp = '0' + tmp;
			}
			ret = ret + tmp;
		}
		return ret.toUpperCase();		
	}
	
	/**
	 * 将16进制字符串转换成原始串
	 * @param str
	 * @return
	 */
	public static String HexStr2String(String str){
		String retStr = null;
		int length = str.length()/2;
		byte[] ret = new byte[length];
		byte[] tmp = str.getBytes();		
		for (int i = 0; i < length; i++) {
			byte _b0 = Byte.decode("0x" + new String(new byte[] { tmp[i * 2] })).byteValue();		
			_b0 = (byte) (_b0 << 4);		
			byte _b1 = Byte.decode("0x" + new String(new byte[] { tmp[i * 2 + 1] })).byteValue();		
			ret[i] = (byte) (_b0 ^ _b1);	
		}
		retStr = new String(ret);
		return retStr;
	}	
	
	/**
	 * 将特殊字符转义成html字符
	 * 
	 * @param in
	 * @return
	 */
	public static String toHTMLString(String in) {
		StringBuffer out = new StringBuffer();
		for (int i = 0; in != null && i < in.length(); i++) {
			char c = in.charAt(i);
			if (c == '\'')
				out.append("&acute;");
			else if (c == '\"')
				out.append("&quot;");
			else if (c == '<')
				out.append("&lt;");
			else if (c == '>')
				out.append("&gt;");
			else if (c == '&')
				out.append("&amp;");
			else if (c == ' ')
				out.append("&nbsp;");
			else if (c == '\n')
				out.append("<br>");
			else
				out.append(c);
		}
		return out.toString();
	}
	/**
	 * 将特殊字符转义成html字符
	 * 
	 * @param in
	 * @return
	 */
	public static String toJavaScriptString(String in) {
		StringBuffer out = new StringBuffer();
		for (int i = 0; in != null && i < in.length(); i++) {
			char c = in.charAt(i);
			if (c == '\n')
				out.append("\\n");
			else
				out.append(c);
		}
		return out.toString();
	}

	/**
	 * 将html字符转义成特殊字符
	 * 
	 * @param in
	 * @return
	 */
	public static String fromHTMLString(String in) {
		String str = in.replaceAll("&amp;", "&");
		str = str.replaceAll("&quot;", "\"");
		str = str.replaceAll("<br>", "\r\n");
		str = str.replaceAll("&acute;", " ");
		str = str.replaceAll("&apos;", "\'");
		str = str.replaceAll("&gt;", ">");
		str = str.replaceAll("&lt;", "<");
		return str;
	}
	
	//add by zhengbh 2007-08-08
	//专门处理'无法插入数据库字段的问题。
	public static String formatForSqlStr(String in){
		String str = null; 
		if(in!=null){
			str = in.replaceAll("'", "''");
		}
		return str;
	}
	
	public static String pageValueOfStr(String str){
		if(str==null || str.trim().equals("")){
			str = "&nbsp;";
		}
		
		return str;
	}
	

	
	public static String getSqlListStr(String str[]){
        String sqlListStr = "";
        if(str!=null){
        	for(int i=0;i<str.length;i++){
        		if(sqlListStr.equals("")){
        			sqlListStr = "'" +str[i] + "'";
        		}else{
        			sqlListStr = sqlListStr + "," + "'" +str[i] + "'"; 
        		}
        	}
        }
        return sqlListStr;
	}
	
	public static String formatStrForSql(Vector key,String str){
        String newStr = str;
        if(key!=null){
        	for(int i=0;i<key.size();i++){
        		newStr = newStr.replaceFirst("\\$", (String)key.get(i));
        	}
        }
        return newStr;
	}
	
	/**
	 * 对象转成String
	 * @param obj
	 * @param format
	 * @return
	 */
	public static final String objToString(Object obj,String format){
		String retStr = null;
		if(obj!=null){
			if(obj instanceof Date || obj instanceof Timestamp){
				SimpleDateFormat formatter = new SimpleDateFormat(format);
				retStr = formatter.format(obj);
			}else{
				retStr = obj.toString();
			}
		}else{
			retStr = "";
		}
		return retStr;
	}
	
	public static final String objToString(Object obj){
		return objToString(obj,"yyyy-MM-dd HH:mm:ss");
	}
	
	public static final String nullToEmptyOfObject(Object obj,String format){
		return objToString(obj,format);
    }
	
	public static final String nullToEmptyOfObject(Object obj){
		return objToString(obj,"yyyy-MM-dd HH:mm:ss");
    }
	
	public static String getShortMemo(String memo,int slen){
		if(memo == null) return "";
		if(slen < 4) return "...";
		
		int byteLen = 0;
		int[] strLen = new int[slen];
		for(int i = 0; i < memo.length(); i++){
			if(memo.charAt(i) <= 255){
				byteLen++;
			}else{
				byteLen += 2;
			}	
		}
		if(byteLen <= slen) return memo;
		
		int charLoc = 0;
		for(int i = 0; i < slen; i++){
			
			if(i == 0){
				strLen[i] = 0;
			}else{
				strLen[i] = strLen[i - 1] + 1;
			}
			
			if(memo.charAt(charLoc) > 255 && i != slen -1){
				strLen[i + 1] = strLen[i];
				i++;
			}	
			charLoc++;
		}
		
		return memo.substring(0,strLen[slen - 3]) + "...";
	}
	
	
	public static String convertSeletedStr(String optValue, String value) {
		if(optValue.equals(value)) {
			return "selected";
		}
		return "";
	}
	
	public static int toInteger(String str, int defaultVal)
	{
		int i;
		try
		{
			i = Integer.parseInt(str);
		}
		catch (Exception e)
		{
			i = defaultVal;
		}
		return i;
	}
	
	/**
	 * @return 全部转为大写
	 */
	public static String toUpperCase(String str) {
		return nullToEmpty(str).toUpperCase();
	}
	 
	/**
	 * @return 全部转为小写
	 */
	public static String toLowerCase(String str) {
		return nullToEmpty(str).toLowerCase();
	}
	

	
	/**
	 * 判断是否数字
	 * @param num
	 * @return
	 */
	public static boolean isNum(String num) {
		try {
			Integer.parseInt(num);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	
	public static String getSameElement(String strArray1,String strArray2){
		if(strArray2==null) strArray2="";
		String[] array2 = strArray2.split(",");
		StringBuffer sb = new StringBuffer("");
		for(String resource:array2){
			if(strArray1.indexOf(resource)>=0){
				sb.append(resource).append(",");
			}
		}
		if(sb.length()>0){
			sb.deleteCharAt(sb.length()-1);
		}
		return sb.toString();
	}
	

	public static String getUnionForeElement(String mainStr,String str){
		if(str==null) str="";
		String[] array = str.split(",");
		StringBuffer sb = new StringBuffer(mainStr);
		if(StringUtil.hasText(mainStr)){
			for(String resource:array){
				if(mainStr.indexOf(resource)<0){
					sb.append(",").append(resource);
				}
			}
			return sb.toString();
		}else{
			return str;
		}
	}
	

	public static boolean isSameSources(String sourceTypeInit, String sourceType) {
		String[] arrayInit = (sourceTypeInit!=null) ? sourceTypeInit.split(","):null;
		Set setInit = new HashSet(Arrays.asList(arrayInit));//先转为list，再转为set
		String[] array = (sourceType!=null) ? sourceType.split(","):null;
		Set set = new HashSet(Arrays.asList(array));//先转为list，再转为set
		return setInit.equals(set);
	}


	public static String getDifferenceSet(String sourceTypeInit,
			String unselectedSourceType) {
		String[] arrayInit = (sourceTypeInit!=null) ? sourceTypeInit.split(","):new String[0];
		String[] array = (unselectedSourceType!=null) ? unselectedSourceType.split(","):new String[0];
		StringBuffer sb = new StringBuffer();
		if(arrayInit.length>array.length){
			for(int i = 0;i<arrayInit.length;i++){
				if(unselectedSourceType.indexOf(arrayInit[i])<0){
					sb.append(arrayInit[i]).append(",");
				}
			}
		}
		if(sb.lastIndexOf(",")>0){
			sb.deleteCharAt(sb.lastIndexOf(","));
		}
		return sb.toString();
	}

	public static String  clobToString(Clob clob) throws SQLException, IOException {
		Reader inStream = clob.getCharacterStream();
		char[]c = new char[(int)clob.length()];
		inStream.read(c);
		String res = new String(c);
		inStream.close();
		return res;
		
	}
	
    public static List<String> getUrlsForStr(String str){
    	Pattern parttern = Pattern.compile("http://[\\w\\.\\-/:]+");
		Matcher matcher = parttern.matcher(str);
		List<String> urlList = new ArrayList<String>();
		while(matcher.find()){
			String url = matcher.group();
			urlList.add(url);
		}
		return urlList;
    }
	public static void main(String[] args){
		
	}

}
