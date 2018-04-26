package com.bj;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.ApplicationHome;
import org.springframework.boot.autoconfigure.mustache.MustacheEnvironmentCollector;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.samskivert.mustache.Mustache;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {	
	private static ThreadLocal<SimpleDateFormat> DATE_FORMAT_THREAD_LOCAL = ThreadLocal
			.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));
	private static ThreadLocal<SimpleDateFormat> DATETIME_FORMAT_THREAD_LOCAL = ThreadLocal
			.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

	private final Environment environment;

	public WebMvcConfig(Environment environment) {
		this.environment = environment;
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		// registry.addViewController("/admin/login").setViewName("admin/login");
		// registry.addViewController("/hello").setViewName("hello");
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**");
	}

	@Bean
	public Mustache.Compiler mustacheCompiler(Mustache.TemplateLoader mustacheTemplateLoader) {
		return Mustache.compiler().withLoader(mustacheTemplateLoader).withCollector(collector())
				.withFormatter(new Mustache.Formatter() {
					public String format(Object value) {
						if (value instanceof java.sql.Date) {
							return DATE_FORMAT_THREAD_LOCAL.get().format((java.sql.Date) value);
						} else if (value instanceof Date) {
							return DATETIME_FORMAT_THREAD_LOCAL.get().format((Date) value);
						} else {
							return String.valueOf(value);
						}
					}
				});
	}

	private Mustache.Collector collector() {
		MustacheEnvironmentCollector collector = new MustacheEnvironmentCollector();
		collector.setEnvironment(this.environment);
		return collector;
	}
    
	@Bean
	public MultipartConfigElement multipartConfigElement() {
		ApplicationHome home = new ApplicationHome(getClass());
	    File jarFile = home.getSource();
	    String tmpPath = jarFile.getParentFile().getPath() + File.separator + "tmp";
	    File tmpDir = new File(tmpPath);
	    if(!tmpDir.exists()) {
	    	tmpDir.mkdirs();
	    }
		MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setLocation(tmpPath);
		return factory.createMultipartConfig();
	}
}