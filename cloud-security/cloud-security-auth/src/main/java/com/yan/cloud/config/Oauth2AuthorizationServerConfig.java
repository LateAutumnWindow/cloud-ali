package com.yan.cloud.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;
import java.util.Arrays;

/**
 * 授权服务
 * @RequiredArgsConstructor lombok 提供可以不用写 @Autowired
 * 但是必须写 final 或 @NonNull
 */
@Configuration
@RequiredArgsConstructor
@EnableAuthorizationServer
public class Oauth2AuthorizationServerConfig extends
        AuthorizationServerConfigurerAdapter {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final DataSource dataSource;
    private final RedisConnectionFactory redisConnectionFactory;
    private final InfoTokenEnhancer infoTokenEnhancer;
    private final UserDetailsService userDetailsService;

    /**
     * 配置授权服务器的安全信息，比如 ssl 配置、checktoken
     * 是否允许访问，是否允许客户端的表单身份验证等。
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // 开启 /oauth/check_token 接口
        security
                .checkTokenAccess("isAuthenticated()");
    }

    /**
     * 配置客户端的 service，也就是应用怎么获取到客户端的信息，
     * 一般来说是从内存或者数据库中获取，已经提供了他们的默认实现，你也可以自定义。
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetails());
//        clients.inMemory()
//                // 应用客户端ID
//                .withClient("taobao")
//                // 应用客户端密码需要加密
//                .secret(passwordEncoder.encode("taobao123"))
//                // 允许访问的资源ID
//                .resourceIds("res1")
//                // 客户端可以使用的授权类型：密码，令牌，刷新令牌
//                .authorizedGrantTypes("password", "authorization_code", "refresh_token")
//                // 可以授权的角色
//                .authorities("ROLE_ADMIN", "ROLE_USER")
//                // 授权范围
//                .scopes("all")
//                // 设置令牌有效时间，刷新令牌有效时间： 1 小时
//                .accessTokenValiditySeconds(Math.toIntExact(Duration.ofHours(1).getSeconds()))
//                .refreshTokenValiditySeconds(Math.toIntExact(Duration.ofHours(1).getSeconds()))
//                // 回调地址
//                .redirectUris("http://example.com")
//        ;
    }


    /**
     * 因为用的是mysql存储客户端信息所以数据库中表oauth_client_details.client_secret
     * 字段需要用 BCryptPasswordEncoder 加密
     */
    @Bean
    public ClientDetailsService clientDetails() {
        return new JdbcClientDetailsService(dataSource);
    }

    /**
     * 配置授权服务器各个端点的非安全功能，如令牌存储，令牌自定义，用户批准和授权类型。
     * 如果需要密码授权模式，需要提供 AuthenticationManager 的 bean。
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // token 增强
        TokenEnhancerChain tec = new TokenEnhancerChain();
        tec.setTokenEnhancers(Arrays.asList(infoTokenEnhancer, jwtAccessTokenConverter()));

        // authenticationManager 在 WebSecurityConfig 中创建的
        endpoints.authenticationManager(authenticationManager)
            .tokenStore(tokenStore())
            .tokenEnhancer(tec)
            .userDetailsService(userDetailsService);
    }

    /**
     * 2. token Store 实现
     */
    @Bean
    public TokenStore tokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }

    /**
     * 1. 令牌转换器
     * @return
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter jat = new JwtAccessTokenConverter();
        // 对称密钥加密
//        jat.setSigningKey("oauth2");
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(
                new ClassPathResource("oauth2.jks"), "123456".toCharArray()
        );
        jat.setKeyPair(keyStoreKeyFactory.getKeyPair("oauth2"));
        return jat;
    }

}
