package com.bj.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

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
public class InfoMessage {
	private static final Logger LOGGER = LoggerFactory.getLogger(ErrorMessage.class);
	private static Properties props;
	private static Map<String, String> map = new HashMap<>();

	public InfoMessage() {
		String fileName = "error-front-message.properties";
		try {
			//先读取内部配置文件->map
			Resource resource = new ClassPathResource(fileName);
			props = PropertiesLoaderUtils.loadProperties(new EncodedResource(resource, "UTF-8"));
			for (Entry<Object, Object> entry:props.entrySet()) {
				map.put((String) entry.getKey(), (String) entry.getValue());
			}
			LOGGER.info("Loading Info error config：{}({})", resource.getURI(), map.size());

			//读取外部配置文件->map
			ApplicationHome home = new ApplicationHome(getClass());
		    File dirFile = home.getDir();
		    String filePath = dirFile.getPath() + File.separator + fileName;
		    File file = new File(filePath);
		    if(file.exists()) {
				resource = new PathResource(filePath);
				props = PropertiesLoaderUtils.loadProperties(new EncodedResource(resource, "UTF-8"));
				Map<String, String> outMap = new HashMap<>();
				for (Entry<Object, Object> entry:props.entrySet()) {
					outMap.put((String) entry.getKey(), (String) entry.getValue());
				}
				map.putAll(outMap);
				LOGGER.info("Loading outside error config：{}({})", file.getCanonicalPath(), outMap.size());
		    }
		} catch (IOException e) { 
			e.printStackTrace();
		}
	}

	/** * 获取属性 * InfoMessage.getProperty("key");
	 * @param key * 
	 * @return 
	 */ 
	public static String getProperty(String key) {
		return map.get(key);
	}
}
