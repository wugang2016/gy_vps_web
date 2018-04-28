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
 * 图片效果： 
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
        properties.setProperty("kaptcha.border", "yes");  //图片边框
        properties.setProperty("kaptcha.border.color", "192,192,192");  //边框颜色
        properties.setProperty("kaptcha.textproducer.font.color", "0,0,255");   //字体颜色
        properties.setProperty("kaptcha.image.width", "120");  //验证码宽度
        properties.setProperty("kaptcha.image.height", "43");  //验证码高度
        properties.setProperty("kaptcha.textproducer.font.size", "30");  //字体大小
        properties.setProperty("kaptcha.session.key", "code");  //session key
        properties.setProperty("kaptcha.background.clear.from", "248,248,248");  //验证码背景颜色渐变，开始颜色
        properties.setProperty("kaptcha.background.clear.to", "248,248,248");  //验证码背景颜色渐变，结束颜色
        properties.setProperty("kaptcha.textproducer.char.space", "2");  //验证码文字间距
        properties.setProperty("kaptcha.textproducer.char.length", "4"); //验证码位数
        properties.setProperty("kaptcha.textproducer.font.names", "new Font(\"Arial\", 1, fontSize), new Font(\"Courier\", 1, fontSize)"); //字体
        properties.setProperty("kaptcha.obscurificator.impl", "com.google.code.kaptcha.impl.WaterRipple"); //验证码的效果
        properties.setProperty("kaptcha.noise.impl", "com.google.code.kaptcha.impl.NoNoise"); //不加噪声
        
        Config config = new Config(properties);  
        defaultKaptcha.setConfig(config);  
        return defaultKaptcha;  
    } 
}
