package com.yan.cloud.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 密码加密器
     * BCrypt 不可逆的加密算法
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 创建两个用户放到内存
     * 用户名：user     密码：123      角色：ROLE_USER
     * 用户名：admin    密码：admin    角色：ROLE_ADMIN
     */
    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        manager.createUser(
//                User.withUsername("user")
//                        .password(passwordEncoder().encode("123"))
//                        .authorities("ROLE_USER")
//                        .build());
//        manager.createUser(
//                User.withUsername("admin")
//                        .password(passwordEncoder().encode("admin"))
//                        .authorities("ROLE_ADMIN")
//                        .build());
//        return manager;
        return new UserDetailsServiceImpl();
    }

    /**
     * 认证管理
     *
     * @return 认证管理对象
     * @throws Exception 认证异常信息
     */
    @Override
    @Bean  // 重点是这行，父类并没有将它注册为一个 Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //配置认证方式等
        auth.userDetailsService(userDetailsService());
    }

    @Autowired
    private CustomizeAuthenticationEntryPoint authenticationEntryPoint;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //http相关的配置，包括登入登出、异常处理、会话管理等
        http // 配置登陆页/login并允许访问
            .formLogin().permitAll()
            //异常处理(权限拒绝、登录失效等)  匿名用户访问无权限资源时的异常处理
            .and().exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
            // 登出页
            .and().logout().logoutUrl("/logout").logoutSuccessUrl("/")
            // 其余所有请求全部需要鉴权认证
            .and().authorizeRequests().anyRequest().authenticated()
            // 由于使用的是JWT，我们这里不需要csrf
            .and().csrf().disable();
    }
}
