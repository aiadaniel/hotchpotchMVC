CREATE DATABASE IF NOT EXISTS hotchpotchMVC CHARACTER SET utf8;

USE hotchpotchMVC;

DROP TABLE IF EXISTS tb_platform_user;

CREATE TABLE tb_platform_user (
	id INTEGER AUTO_INCREMENT PRIMARY KEY,
	user_id INTEGER UNSIGNED UNIQUE COMMENT '用户全局id，区别主键id',
	nickname VARCHAR(45) NOT NULL UNIQUE,
	avatar VARCHAR(255) DEFAULT '',
	dateCreated DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	identity_type TINYINT UNSIGNED DEFAULT 1 COMMENT '登录类型，用户名、手机、邮箱、第三方等',
	identifier VARCHAR(128) COMMENT '手机、邮箱、用户名、第三方等的唯一id',
	credential VARCHAR(255) NOT NULL COMMENT '站内密码或者三方token',
	randCredential VARCHAR(16) COMMENT '站内密码随机数'
);
