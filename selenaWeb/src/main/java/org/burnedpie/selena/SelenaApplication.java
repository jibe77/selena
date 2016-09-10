package org.burnedpie.selena;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
/*
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
*/
import java.util.Arrays;

/**
 * Created by jibe on 22/08/16.
 */
@SpringBootApplication
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SelenaApplication {

    private static final Logger log = LoggerFactory.getLogger(SelenaApplication.class);

    /**
     * Starts web server, for test purpose.
     * @param args
     */
    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(SelenaApplication.class, args);

        System.out.println("Let's inspect the beans provided by Spring Boot:");

        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }
    }
}
