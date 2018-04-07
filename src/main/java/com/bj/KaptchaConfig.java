/**
 * 
 */
package com.bj;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

/**
 * 验证码配置文件
 * 图片样式： 
 * 水纹com.google.code.kaptcha.impl.WaterRipple 
 * 鱼眼com.google.code.kaptcha.impl.FishEyeGimpy
 * 阴影com.google.code.kaptcha.impl.ShadowGimpy
 * @author LQK
 */
@Component
public class KaptchaConfig {
    @Bean  
    public DefaultKaptcha getDefaultKaptcha(){  
        com.google.code.kaptcha.impl.DefaultKaptcha defaultKaptcha = new com.google.code.kaptcha.impl.DefaultKaptcha();  
        Properties properties = new Properties();  
        properties.setProperty("kaptcha.border", "yes");  
        properties.setProperty("kaptcha.border.color", "105,179,90");  
        properties.setProperty("kaptcha.textproducer.font.color", "blue");  
        properties.setProperty("kaptcha.image.width", "120");  
        properties.setProperty("kaptcha.image.height", "43");  
        properties.setProperty("kaptcha.textproducer.font.size", "30");  
        properties.setProperty("kaptcha.session.key", "code");  
        properties.setProperty("kaptcha.textproducer.char.length", "4");  
        properties.setProperty("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑");
        properties.setProperty("kaptcha.obscurificator.impl", "com.google.code.kaptcha.impl.ShadowGimpy");
        Config config = new Config(properties);  
        defaultKaptcha.setConfig(config);  
        return defaultKaptcha;  
    } 
}
