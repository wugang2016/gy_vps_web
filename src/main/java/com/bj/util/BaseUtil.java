/**
 * 
 */
package com.bj.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author LQK
 *
 */
public class BaseUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(BaseUtil.class);

    /**
     * 保存文件到本地
     */
    public static void doSaveFile(String path, MultipartFile file, String newFileName) throws IOException {
        File subDir = new File(path);
        if (!subDir.exists()) {
            subDir.mkdirs();
        }
        if(newFileName == null || newFileName.trim().length() == 0) {
        	newFileName = file.getOriginalFilename();
        }
        File destFile = new File(subDir, newFileName);
        OutputStream out = null;
        try {
            out = new BufferedOutputStream(new FileOutputStream(destFile));
            IOUtils.copy(file.getInputStream(), out);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

	/**
	 * 生成32位md5码
	 * 
	 * @param password
	 * @return
	 */
	public static String md5(String password) {
		try {
			// 得到一个信息摘要器
			MessageDigest digest = MessageDigest.getInstance("md5");
			byte[] result = digest.digest(password.getBytes());
			StringBuffer buffer = new StringBuffer();
			// 把每一个byte 做一个与运算 0xff;
			for (byte b : result) {
				// 与运算
				int number = b & 0xff;// 加盐
				String str = Integer.toHexString(number);
				if (str.length() == 1) {
					buffer.append("0");
				}
				buffer.append(str);
			}
			// 标准的md5加密后的结果
			return buffer.toString();
		} catch (NoSuchAlgorithmException e) {
        	LOGGER.error(e.getMessage(),e);
			return "";
		}
	}
	
	/**
	 * 生成随机数字和字母,  
	 * @param length
	 * @return
	 */
    public static String getStrRandom(int length) {  
          
        String val = "";  
        Random random = new Random();  
          
        //参数length，表示生成几位随机数  
        for(int i = 0; i < length; i++) {  
              
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";  
            //输出字母还是数字  
            if( "char".equalsIgnoreCase(charOrNum) ) {  
                //输出是大写字母还是小写字母  
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;  
                val += (char)(random.nextInt(26) + temp);  
            } else if( "num".equalsIgnoreCase(charOrNum) ) {  
                val += String.valueOf(random.nextInt(10));  
            }  
        }  
        return val.toUpperCase();  
    }  
    
    /**
     * 判断字符串是否为空
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
    	if(str != null && str.trim().length() > 0) {
    		return false;
    	}else {
    		return true;
    	}
    }
	
    /**
     * 格式化日期
     * @param date
     * @return
     */
    public static String format(Date date) {
    	return format(date, "yyyyMMddHHmmss");
    }
	
    /**
     * 格式化日期
     * @param date
     * @return
     */
    public static String format(Date date, String foramt) {
    	if(date != null) {
    		SimpleDateFormat df = new SimpleDateFormat(foramt);
    		return df.format(date);
    	}
    	return "";
    }
    
    /**
     * 发送消息到设备
     */
    public static String udpSend(String ip, int port, String _message) throws SocketException,IOException{
        LOGGER.info("向{}:{} 发送内容：{}", ip, port, _message);
    	DatagramSocket ds = null;
		try {
			ds = new DatagramSocket();
			//1.发送
			byte[] buf = _message.getBytes();
			DatagramPacket dp = new DatagramPacket(buf, buf.length, InetAddress.getByName(ip), port);
			ds.send(dp);
			//2.接收
            byte[] getBuf = new byte[1024];
            DatagramPacket getPacket = new DatagramPacket(getBuf, getBuf.length);  
            ds.setSoTimeout(2000);
            ds.receive(getPacket);
            String backMes = new String(getBuf, 0, getPacket.getLength());  
            LOGGER.info("接受方返回的消息：{}", backMes);
            return backMes;
		} finally{
			ds.close();
		}
    }
    
	public static void main(String[] args) throws IOException {
	}
}
