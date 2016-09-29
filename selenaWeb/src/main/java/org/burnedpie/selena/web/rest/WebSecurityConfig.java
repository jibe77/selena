package org.burnedpie.selena.web.rest;

import org.burnedpie.selena.persistance.dao.ConfigurationRepository;
import org.burnedpie.selena.persistance.domain.ConfigurationKeyEnum;
import org.burnedpie.selena.web.rest.controller.CustomUserDetailsService;
import org.burnedpie.selena.web.rest.controller.RemoteController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

/**
 * Created by jibe on 06/09/16.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers(
                    RemoteController.REST_IS_AIRPLAY_ON,
                    RemoteController.REST_IS_RADIO_ON,
                    RemoteController.REST_PLAY_RADIO_STATION,
                    RemoteController.REST_RESET,
                    RemoteController.REST_START_AIRPLAY,
                    RemoteController.REST_STOP,
                    RemoteController.REST_VOLUME_DOWN,
                    RemoteController.REST_VOLUME_UP,
                    "/",
                    "/player",
                    "/recorder",
                    "/images/*")
                .permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin()
                    //.loginPage("/login")
            .permitAll()
            .and()
            .logout()
            .permitAll();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
