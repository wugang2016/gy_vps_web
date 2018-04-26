package com.bj.util;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationHome;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

@Component
public class ErrorMessage {
	private static final Logger LOGGER = LoggerFactory.getLogger(ErrorMessage.class);
	private static Properties props;

	public ErrorMessage() {
		String fileName = "error-message.properties";
		try {
			ApplicationHome home = new ApplicationHome(getClass());
		    File jarFile = home.getSource();
		    String filePath = jarFile.getParentFile().getPath() + File.separator + fileName;
		    File file = new File(filePath);
		    if(file.exists()) {
				//读取外部配置文件
				Resource resource = new PathResource(filePath);
				props = PropertiesLoaderUtils.loadProperties(new EncodedResource(resource, "UTF-8"));
				LOGGER.info("Loading outside error config：{}", file.getCanonicalPath());
		    }else {
				//读取内部配置文件
				Resource resource = new ClassPathResource(fileName);
				props = PropertiesLoaderUtils.loadProperties(new EncodedResource(resource, "UTF-8"));
				LOGGER.info("Loading inside error config：{}", resource.getURI());
		    }
		} catch (IOException e) { 
			e.printStackTrace();
		}
	}

	/** * 获取属性 * ErrorMessage.getProperty("key");
	 * @param key * 
	 * @return 
	 */ 
	public static String getProperty(String key) {
		return props == null ? null : props.getProperty(key);
	}

	/** 
	 * 获取属性
	 * @param key 属性key
	 * @param defaultValue 属性value 
	 * @return */
	public static String getProperty(String key, String defaultValue) {
		return props == null ? null : props.getProperty(key, defaultValue);
	}
}
