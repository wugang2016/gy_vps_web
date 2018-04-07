/**
 * 
 */
package com.bj.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.io.IOUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author LQK
 *
 */
public class BaseUtil {

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
			e.printStackTrace();
			return "";
		}
	}
	
	public static void main(String[] args) {
		System.out.println(md5("2"));
	}
}
