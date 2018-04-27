package com.bj;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.catalina.core.AprLifecycleListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableAsync
@EnableTransactionManagement
public class WebApplication {

    AprLifecycleListener arpLifecycle = null;
    @Value("${com.tomcat.apr:false}")
    private boolean enableApr;

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

    @Bean(name = "statusExecutor")
    public ExecutorService statusExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        // Do any additional configuration here
        return builder.build();
    }

    @Bean
    public TomcatEmbeddedServletContainerFactory tomcatEmbeddedServletContainerFactory() {
        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
        if (enableApr) {
            arpLifecycle = new AprLifecycleListener();
            tomcat.setProtocol("org.apache.coyote.http11.Http11AprProtocol");

            System.out.println("************************************************ APR ********************************************");
        }
        return tomcat;
    }
}
