package com.Assignment.Synonym.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
 @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
       .antMatchers("/").permitAll().antMatchers("/getSynonym").hasRole("USER")
       .antMatchers("/addSynonym","/updateSynonym","/deleteSynonym").hasRole("ADMIN").anyRequest()
       
        .authenticated()
        .and()
        .oauth2Login()
            .userInfoEndpoint()
                .userService(userService);
 }
    @Autowired
    private CustomOAuth2UserService  userService;
 }

