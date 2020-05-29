//package com.yan.cloud.security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
///**
//  1. @EnableWebSecurity
//     这里启用了 Web 安全
//  2. @EnableGlobalMethodSecurity
//     Spring Security默认是禁用注解的，要想开启注解，
//     需要在继承WebSecurityConfigurerAdapter的类上加@EnableGlobalMethodSecurity注解，
//     来判断用户对某个控制层的方法是否具有访问权限 
// */
//@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
//public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//    @Bean
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }
//
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(new UserDetail());
//    }
//
//    @Override
//    public void configure(WebSecurity web) throws Exception {}
//
//    /**
//     * 安全拦截机制
//     * @param http
//     * @throws Exception
//     */
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().and()
//                .authorizeRequests()
//                .antMatchers("/r/**").hasAnyAuthority("p1")
//                .antMatchers("/login*").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin();
//
//
//    }
//}
