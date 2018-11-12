package com.summer.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * SpringBoot entry point application
 *
 * @author Fabio Carvalho (facarvalho@paypal.com or fabiocarvalho777@gmail.com)
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.summer"})
@ImportResource(locations= {"classpath:spring/applicationContext.xml"})
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        ConfigurableEnvironment env = SpringApplication
                .run(Application.class, args)
                .getEnvironment();

//        SpringApplication.run(Application.class, args);
    }

}
