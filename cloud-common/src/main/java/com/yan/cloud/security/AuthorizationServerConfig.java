//package com.yan.cloud.security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
//import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
//import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
//import org.springframework.security.oauth2.provider.ClientDetailsService;
//import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
//import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
//import org.springframework.security.oauth2.provider.token.*;
//import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
//
//import javax.annotation.Resource;
//import java.util.Arrays;
//
///**
// * 授权服务
// */
//@Configuration
//@EnableAuthorizationServer
//public class AuthorizationServerConfig extends
//        AuthorizationServerConfigurerAdapter {
//
//    /** 放入自己的TokenStore ---> TokenConfig */
//    @Autowired
//    private TokenStore tokenStore;
//    @Autowired
//    private JwtAccessTokenConverter accessTokenConverter;
//    @Autowired
//    private ClientDetailsService clientDetailsService;
//
//
//    /**
//     * 令牌管理
//     * @return
//     */
//    @Bean
//    public AuthorizationServerTokenServices authorizationTokenServices() {
//        DefaultTokenServices services = new DefaultTokenServices();
//        services.setClientDetailsService(clientDetailsService);
//        services.setSupportRefreshToken(true);
//        services.setTokenStore(tokenStore);
//
//        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
//        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(accessTokenConverter));
//        services.setTokenEnhancer(tokenEnhancerChain);
//
//        // 令牌默认有效2小时
//        services.setAccessTokenValiditySeconds(7200);
//        // 刷新令牌默认有效3天
//        services.setRefreshTokenValiditySeconds(259200);
//        return services;
//    }
//
//
//
//    /**
//     * 用来配置令牌端点的安全约束
//     * @param security
//     * @throws Exception
//     */
//    @Override
//    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
//        security
//                .tokenKeyAccess("permitAll()")
//                .checkTokenAccess("permitAll()")
//                .allowFormAuthenticationForClients(); // 允许表单认证
//    }
//
//
//    /**
//     * 用来配置客户端详情服务（ClientDetailsService），客户端详情信息在
//     * 这里进行初始化，你能够把客户端详情信息写死在这里或者是通过数据库来存储调取详情信息。
//     * @param clients
//     * @throws Exception
//     */
//    @Override
//    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        // 使用内存方式配置用户
//        clients.inMemory()
//                .withClient("c1")
//                .secret(new BCryptPasswordEncoder().encode("secret"))
//                .resourceIds("res1")
//                // 允许的授权类型
//                .authorizedGrantTypes("authorization_code", "password",
//                        "client_credentials", "implicit", "refresh_token")
//                // 允许的授权范围
//                .scopes("all")
//                .autoApprove(false)
//                .redirectUris("http://www.baidu.com");
//    }
//
//    @Resource
//    private AuthorizationCodeServices authorizationCodeServices;
//    @Resource
//    private AuthenticationManager authenticationManager;
//
//    /**
//     * 用来配置令牌（token）的访问端点和令牌服务(token services)。
//     * @param endpoints
//     * @throws Exception
//     */
//    @Override
//    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//        endpoints
//                // 认证
//                .authenticationManager(authenticationManager)
//                // 授权
//                .authorizationCodeServices(authorizationCodeServices)
//                .tokenServices(authorizationTokenServices())
//                .allowedTokenEndpointRequestMethods(HttpMethod.POST);
//    }
//
//    /**
//     * 设置授权吗模式的授权码如何存取，暂时采用内存方式
//     */
//    @Bean
//    public AuthorizationCodeServices authorizationCodeServices() {
//        return new InMemoryAuthorizationCodeServices();
//    }
//}
