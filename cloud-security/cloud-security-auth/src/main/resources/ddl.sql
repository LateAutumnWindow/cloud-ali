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