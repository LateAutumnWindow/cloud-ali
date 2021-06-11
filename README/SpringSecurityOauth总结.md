1. **资料地址：https://zyue.wiki/articles/2019/07/14/1563082088646.html**

授权码模式

1. 获取授权码地址
http://localhost:5501/oauth/authorize?response_type=code&client_id=taobao&redirect_uri=http://example.com&scope=all

2. 成功获取授权码后  http://example.com/?code=urxwkZ

3. 获取token
	地址  		http://localhost:5501/oauth/token
	参数：grant_type	authorization_code
	参数：code	urxwkZ
	参数：redirect_uri	http://example.com
	参数：client_id	控制台打印的
	参数：scope	all

---------------------------------------------------------------------------
密码模式

1. 获取token
	地址  		http://localhost:5501/oauth/token
	参数：grant_type	password
	参数：scope	all
	参数：username	user 		// 默认固定额 user 
	参数：password	控制台打印

---------------------------------------------------------------------------
刷新地址		http://localhost:5501/oauth/check_token 

---------------------------------------------------------------------------
客户端：

微信   		微信小程序		用户

当用户打开微信小程序，小程序可能会想获得用户的头像。这个时候就会弹出授权框，用户同意后就会去微信获取用户头像。这时候 微信小程序就是 客户端 

# **0. 问题**

```txt
报错
1. 基于Mysql存储客户端信息是 获取授权吗报错
报错信息：org.springframework.security.access.AccessDeniedException: Access is denied
。。。。
2. 因为要拆分授权和资源服务，但是启动类中并没有删除资源 @EnableResourceServer 的注解删除后就好了
```





# 1. 单体应用使用 Security

## 1.1 POM 依赖

```xml
<dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<!--security依赖-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.security.oauth.boot</groupId>
    <artifactId>spring-security-oauth2-autoconfigure</artifactId>
    <version>2.1.13.RELEASE</version>
</dependency>
```

## 1.2 启动类

```java
@EnableAuthorizationServer	// 开启授权服务
@EnableResourceServer 	    // 开启资源服务
@SpringBootApplication
public class SecurityAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(SecurityAuthApplication.class, args);
    }
}
```

## 1.3 配置类

```java

import org.springframework.context.annotation.Bean;
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
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(
                User.withUsername("user")
                        .password(passwordEncoder().encode("123"))
                        .authorities("ROLE_USER")
                        .build());
        manager.createUser(
                User.withUsername("admin")
                        .password(passwordEncoder().encode("admin"))
                        .authorities("ROLE_ADMIN")
                        .build());
        return manager;
    }
}

```

## 1.4 YAML配置

```java
server:
  port: 5501
spring:
  profiles:
    active: dev
logging:
  level:
    org:
      springframework:
        security: DEBUG  #查看 security DEBUG 信息
security:
  oauth2:
    authorization:
      # 允许使用 /oauth/check_token
      check-token-access: isAuthenticated()
    client:
      # 客户端注册的 回调地址 当客户端或应用 第一步获取授权码的时候会带一个回调地址
      # 因为你传递过来的回调地址授权服务器不知道是否合法，可能会在传输的中途被篡改，
      # 所以在授权服务器里面需要你注册一个回调地址，与你传递过来的进行对比，
      # 如果匹配才会携带授权码进行回调
      registered-redirect-uri: http://example.com
      # 客户端ID 和 密钥
      client-id: yanApp
      client-secret: yanApp123
      # 客户端授权范围
      scope: all
      # token 有效期
      access-token-validity-seconds: 6000
      # 刷新 token 的有效期
      refresh-token-validity-seconds: 6000
      # 允许的授权类型：授权码模式，密码模式
      grant-type: authorization_code, password
      # 可以访问资源的 ID
      resource-ids: res1
    # 集合类型可以多个，如果客户端可以访问的资源服务器ID不再他的范围就无法访问响应资源
    resource:
      id: res1
```

# 2.基于内存的客户端信息与令牌存储

## 2.1 Oauth2AuthorizationServerConfig

```java

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

import java.time.Duration;

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

    /**
     * 配置授权服务器的安全信息，比如 ssl 配置、checktoken
     * 是否允许访问，是否允许客户端的表单身份验证等。
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        super.configure(security);
    }

    /**
     * 配置客户端的 service，也就是应用怎么获取到客户端的信息，
     * 一般来说是从内存或者数据库中获取，已经提供了他们的默认实现，你也可以自定义。
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                // 应用客户端ID
                .withClient("taobao")
                // 应用客户端密码需要加密
                .secret(passwordEncoder.encode("taobao123"))
                // 允许访问的资源ID
                .resourceIds("res1")
                // 客户端可以使用的授权类型：密码，令牌，刷新令牌
                .authorizedGrantTypes("password", "authorization_code", "refresh_token")
                // 可以授权的角色
                .authorities("ROLE_ADMIN", "ROLE_USER")
                // 授权范围
                .scopes("all")
                // 设置令牌有效时间，刷新令牌有效时间： 1 小时
                .accessTokenValiditySeconds(Math.toIntExact(Duration.ofHours(1).getSeconds()))
                .refreshTokenValiditySeconds(Math.toIntExact(Duration.ofHours(1).getSeconds()))
                // 回调地址
                .redirectUris("http://example.com")
        ;

    }

    /**
     * 配置授权服务器各个端点的非安全功能，如令牌存储，令牌自定义，用户批准和授权类型。
     * 如果需要密码授权模式，需要提供 AuthenticationManager 的 bean。
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // authenticationManager 在 WebSecurityConfig 中创建的
        // 令牌存储方式在这个类中 
        // org.springframework.security.oauth2.config.annotation.web.configuration.
        // AuthorizationServerEndpointsConfiguration
        // 的 AuthorizationServerTokenServicesFactoryBean.createInstance()
        // 这个工长方法中配置, 默认是以 内存方式存储
        endpoints.authenticationManager(authenticationManager);
    }
}
```

## 2.2 WebSecurityConfig

```java

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
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
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(
                User.withUsername("user")
                        .password(passwordEncoder().encode("123"))
                        .authorities("ROLE_USER")
                        .build());
        manager.createUser(
                User.withUsername("admin")
                        .password(passwordEncoder().encode("admin"))
                        .authorities("ROLE_ADMIN")
                        .build());
        return manager;
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
}
```

# 3. 基于Mysql存储客户端信息

## 3.1 配置文件

```yaml
server:
  port: 5501
spring:
  profiles:
    active: dev
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://192.168.40.199:3316/auth?useUnicode=true&characterEncoding=UTF-8&useOldAliasMetadataBehavior=true&autoReconnect=true&serverTimezone=UTC
    username: root
    password: 123456
    # 用来初始化数据库的，如果不存在表就自动创建
    initialization-mode: ALWAYS
    schema: classpath:ddl.sql
```



## 3.2 表结构

```sql
CREATE TABLE IF NOT EXISTS `oauth_client_details` (
  `client_id` varchar(128) NOT NULL COMMENT '客户端id',
  `resource_ids` varchar(256) DEFAULT NULL COMMENT '客户端所能访问的资源id集合',
  `client_secret` varchar(256) DEFAULT NULL COMMENT '客户端访问密匙',
  `scope` varchar(256) DEFAULT NULL COMMENT '客户端申请的权限范围',
  `authorized_grant_types` varchar(256) DEFAULT NULL COMMENT '授权类型',
  `web_server_redirect_uri` varchar(256) DEFAULT NULL COMMENT '客户端重定向URI',
  `authorities` varchar(256) DEFAULT NULL COMMENT '客户端权限',
  `access_token_validity` int(11) DEFAULT NULL COMMENT 'access_token的有效时间（单位:秒）',
  `refresh_token_validity` int(11) DEFAULT NULL COMMENT 'refresh_token的有效时间（单位:秒）',
  `additional_information` varchar(4096) DEFAULT NULL COMMENT '预留字段，JSON格式',
  `autoapprove` varchar(256) DEFAULT NULL COMMENT '否自动Approval操作',
  PRIMARY KEY (`client_id`)
) ENGINE=InnoDB COMMENT='客户端详情' DEFAULT CHARSET=utf8mb4;


CREATE TABLE IF NOT EXISTS `oauth_client_token` (
  `authentication_id` varchar(128) NOT NULL COMMENT '身份验证ID',
  `token_id` varchar(128) DEFAULT NULL COMMENT '令牌ID',
  `token` blob COMMENT '令牌',
  `user_name` varchar(256) DEFAULT NULL COMMENT '用户名',
  `client_id` varchar(256) DEFAULT NULL COMMENT '客户端ID',
  PRIMARY KEY (`authentication_id`)
) ENGINE=InnoDB COMMENT='客户端系统中存储从服务端获取的token数据' DEFAULT CHARSET=utf8mb4;


CREATE TABLE IF NOT EXISTS `oauth_access_token` (
  `authentication_id` varchar(128) NOT NULL COMMENT '身份验证ID',
  `token_id` varchar(128) DEFAULT NULL COMMENT '令牌ID',
  `token` blob COMMENT '令牌',
  `user_name` varchar(256) DEFAULT NULL COMMENT '用户名',
  `client_id` varchar(128) DEFAULT NULL COMMENT '客户端ID',
  `authentication` blob COMMENT '认证体',
  `refresh_token` varchar(256) DEFAULT NULL COMMENT '刷新令牌',
  PRIMARY KEY (`authentication_id`),
  KEY `PK_token_id` (`token_id`) USING BTREE,
  KEY `PK_refresh_token` (`refresh_token`) USING BTREE
) ENGINE=InnoDB COMMENT='生成的 token 数据' DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `oauth_refresh_token` (
  `token_id` varchar(128) DEFAULT NULL COMMENT '令牌ID',
  `token` blob COMMENT '令牌',
  `authentication` blob COMMENT '认证体',
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='刷新 token' DEFAULT CHARSET=utf8mb4;


CREATE TABLE IF NOT EXISTS `oauth_code` (
  `code` varchar(256) DEFAULT NULL,
  `authentication` blob,
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='服务端生成的 code 值' DEFAULT CHARSET=utf8mb4;

# timestamp 类型 5.7 Msql需要 这样写 --------
CREATE TABLE IF NOT EXISTS `oauth_approvals` (
  `userId` varchar(128) DEFAULT NULL,
  `clientId` varchar(128) DEFAULT NULL,
  `scope` varchar(256) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  `expiresAt` timestamp  DEFAULT CURRENT_TIMESTAMP,
  `lastModifiedAt` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='授权同意信息' DEFAULT CHARSET=utf8mb4;


CREATE TABLE IF NOT EXISTS client_details (
  appId VARCHAR(256) PRIMARY KEY,
  resourceIds VARCHAR(256),
  appSecret VARCHAR(256),
  scope VARCHAR(256),
  grantTypes VARCHAR(256),
  redirectUrl VARCHAR(256),
  authorities VARCHAR(256),
  access_token_validity INTEGER,
  refresh_token_validity INTEGER,
  additionalInformation VARCHAR(4096),
  autoApproveScopes VARCHAR(256)
)ENGINE=InnoDB COMMENT='客户端信息' DEFAULT CHARSET=utf8mb4;
```

## 3.3 表说明

### 3.3.1 oauth_client_details

**客户端信息**

| 列名                    | 类型          | 描述                                                         |
| ----------------------- | ------------- | ------------------------------------------------------------ |
| lient_id（主键）        | VARCHAR(256)  | 主键,必须唯一,不能为空. 用于唯一标识每一个客户端(client); 在注册时必须填写(也可由服务端自动生成). 对于不同的grant_type,该字段都是必须的. 在实际应用中的另一个名称叫appKey,与client_id是同一个概念. |
| resource_ids            | VARCHAR(256)  | 客户端所能访问的资源id集合,多个资源时用逗号(,)分隔           |
| client_secret           | VARCHAR(256)  | 用于指定客户端(client)的访问密匙; 在注册时必须填写(也可由服务端自动生成). 对于不同的grant_type,该字段都是必须的. 在实际应用中的另一个名称叫appSecret,与client_secret是同一个概念. |
| scope                   | VARCHAR(256)  | 指定客户端申请的权限范围,可选值包括read,write,trust;若有多个权限范围用逗号(,)分隔,如: "read,write''. |
| authorized_grant_types  | VARCHAR(256)  | 指定客户端支持的grant_type,可选值包括authorization_code,password,refresh_token,implicit,client_credentials,若支持多个grant_type用逗号(,)分隔,如: ``authorization_code,password''. 在实际应用中,当注册时,该字段是一般由服务器端指定的,而不是由申请者去选择的, |
| web_server_redirect_uri | VARCHAR(256)  | 客户端的重定向URI,可为空, 当grant_type为authorization_code或implicit时, 在Oauth的流程中会使用并检查与注册时填写的redirect_uri是否一致. |
| authorities             | VARCHAR(256)  | 指定客户端所拥有的Spring Security的权限值,可选, 若有多个权限值,用逗号(,)分隔, 如: ``ROLE_ADMIN'' |
| access_token_validity   | INTEGER       | 设定客户端的access_token的有效时间值(单位:秒),可选, 若不设定值则使用默认的有效时间值(60 * 60 * 12, 12小时). |
| refresh_token_validity  | INTEGER       | 设定客户端的refresh_token的有效时间值(单位:秒),可选, 若不设定值则使用默认的有效时间值(60 * 60 * 12, 12小时). |
| additional_information  | VARCHAR(4096) | 这是一个预留的字段,在Oauth的流程中没有实际的使用,可选,但若设置值,必须是JSON格式的数据,在实际应用中, 可以用该字段来存储关于客户端的一些其他信息 |
| autoapprove             | VARCHAR(256)  | 设置用户是否自动Approval操作, 默认值为 false, 可选值包括 true,false, read,write.该字段只适用于grant_type=“authorization_code“的情况,当用户登录成功后,若该值为’true’或支持的scope值,则会跳过用户Approve的页面,直接授权. |

### 3.3.2 oauth_client_token

**客户端系统中存储从服务端获取的 token 数据**

| 字段名            | 字段类型     | 描述                                                         |
| ----------------- | ------------ | ------------------------------------------------------------ |
| token_id          | VARCHAR(256) | 从服务器端获取到的access_token的值.                          |
| token             | BLOB         | 这是一个二进制的字段, 存储的数据是OAuth2AccessToken.java对象序列化后的二进制数据. |
| authentication_id | VARCHAR(256) | 该字段具有唯一性, 是根据当前的username(如果有),client_id与scope通过MD5加密生成的. 具体实现请参考DefaultClientKeyGenerator.java类. |
| user_name         | VARCHAR(256) | 登录时的用户名                                               |
| client_id         | VARCHAR(256) | 客户端 id                                                    |

### 3.3.3 oauth_access_token

 **生成的 token 数据**

| 字段名            | 字段类型     | 描述                                                         |
| ----------------- | ------------ | ------------------------------------------------------------ |
| token_id          | VARCHAR(256) | 从服务器端获取到的access_token的值.                          |
| token             | BLOB         | 存储将OAuth2AccessToken.java对象序列化后的二进制数据, 是真实的AccessToken的数据值. |
| authentication_id | VARCHAR(256) | 该字段具有唯一性, 其值是根据当前的username(如果有),client_id与scope通过MD5加密生成的. |
| user_name         | VARCHAR(256) | 登录时的用户名, 若客户端没有用户名(如grant_type=``client_credentials''),则该值等于client_id |
| client_id         | VARCHAR(256) | 客户端 id                                                    |
| authentication    | BLOB         | 存储将 OAuth2Authentication 对象序列化后的二进制数据.        |
| refresh_token     | VARCHAR(256) | 该字段的值是将refresh_token的值通过MD5加密后存储的.          |

### 3.3.4 oauth_refresh_token

**刷新 token**

| 字段名         | 字段类型     | 描述                                                     |
| -------------- | ------------ | -------------------------------------------------------- |
| token_id       | VARCHAR(256) | 该字段的值是将refresh_token的值通过MD5加密后存储的.      |
| token          | BLOB         | 存储将OAuth2RefreshToken.java对象序列化后的二进制数据.   |
| authentication | BLOB         | 存储将OAuth2Authentication.java对象序列化后的二进制数据. |

### 3.3.5 oauth_code

**服务端生成的 code 值**

| 字段名 | 字段类型     | 描述                                  |
| ------ | ------------ | ------------------------------------- |
| code   | VARCHAR(256) | 存储服务端系统生成的code的值(未加密). |

### 3.3.6 oauth_approvals

 **授权同意信息**

| 字段名         | 字段类型     | 描述           |
| -------------- | ------------ | -------------- |
| userId         | VARCHAR(256) | 用户 id        |
| clientId       | VARCHAR(256) | 客户端 id      |
| scope          | VARCHAR(256) | 请求的范围     |
| status         | VARCHAR(10)  | 授权的状态     |
| expiresAt      | TIMESTAMP    | 时间           |
| lastModifiedAt | TIMESTAMP    | 最后修改的时间 |

## 3.4 认证授权服务

### 3.4.1 启动类

```java

/**
 * 认证授权服务
 */
@EnableAuthorizationServer
@SpringBootApplication
public class SecurityAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityAuthApplication.class, args);
    }

}
```

### 3.4.2 WebSecurityConfig

```java

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
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
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(
                User.withUsername("user")
                        .password(passwordEncoder().encode("123"))
                        .authorities("ROLE_USER")
                        .build());
        manager.createUser(
                User.withUsername("admin")
                        .password(passwordEncoder().encode("admin"))
                        .authorities("ROLE_ADMIN")
                        .build());
        return manager;
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
}

```

### 3.4.4 Oauth2AuthorizationServerConfig

```java

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;

import javax.sql.DataSource;
import java.time.Duration;

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

    /**
     * 配置授权服务器的安全信息，比如 ssl 配置、checktoken
     * 是否允许访问，是否允许客户端的表单身份验证等。
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        super.configure(security);
    }

    /**
     * 配置客户端的 service，也就是应用怎么获取到客户端的信息，
     * 一般来说是从内存或者数据库中获取，已经提供了他们的默认实现，你也可以自定义。
     * 从Mysql中加载客户端信息
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetails());
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
        // authenticationManager 在 WebSecurityConfig 中创建的
        endpoints.authenticationManager(authenticationManager);
    }
}

```

