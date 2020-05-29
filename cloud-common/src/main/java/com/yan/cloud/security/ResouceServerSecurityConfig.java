//package com.yan.cloud.security;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
//import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
//import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
//import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
//import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
//
//
//@Configuration
//@EnableResourceServer
//public class ResouceServerSecurityConfig extends
//        ResourceServerConfigurerAdapter {
//
//    public static final String RESOURCE_ID = "res1";
//
//    /**
//     * ResourceServerSecurityConfigurer中主要包括：
//     *  tokenServices：   ResourceServerTokenServices 类的实例，用来实现令牌服务。
//     *  tokenStore：      TokenStore类的实例，指定令牌如何访问，与tokenServices配置可选
//     *  resourceId：      这个资源服务的ID，这个属性是可选的，但是推荐设置并在授权服务中进行验证。
//     *  其他的拓展属性例如 tokenExtractor 令牌提取器用来提取请求中的令牌。
//     *
//     * @param resources
//     * @throws Exception
//     */
//    @Override
//    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
//        resources
//                .resourceId(RESOURCE_ID)
//                .tokenServices(resourceServerTokenServices())
//                .stateless(true);
//    }
//
//    /** 资源服务令牌解析 */
//    public ResourceServerTokenServices resourceServerTokenServices(){
//        // 使用 DefaultTokenServices 在资源服务器本地配置令牌存储、解码、解析方式
//        // 使用 RemoteTokenServices 资源服务器通过 HTTP 请求来解码令牌，每次都请求授权服务器端点
//        // 使用远程服务请求授权服务器校验token,必须指定校验，token的url,client_id,client_secret
//        RemoteTokenServices rts = new RemoteTokenServices();
//        rts.setCheckTokenEndpointUrl("http://localhost:7777/oauth/check_token");
//        rts.setClientId("c1");
//        rts.setClientSecret("secret");
//        return rts;
//    }
//
//
//    /**
//     * HttpSecurity配置这个与Spring Security类似：
//     *  请求匹配器，用来设置需要进行保护的资源路径，默认的情况下是保护资源服务的全部路径。
//     *  通过http.authorizeRequests()来设置受保护资源的访问规则
//     *  其他的自定义权限保护规则通过 HttpSecurity 来进行配置。
//     *
//     * @param http
//     * @throws Exception
//     */
//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests()
//                .antMatchers("/**").access("#oauth2.hasScope('all')")
//                .and().csrf()
//                .and()
//                .formLogin()
//                .and()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//    }
//}
