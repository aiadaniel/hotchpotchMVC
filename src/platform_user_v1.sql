CREATE DATABASE IF NOT EXISTS hotchpotchMVC CHARACTER SET utf8;

USE hotchpotchMVC;

DROP TABLE IF EXISTS tb_platform_user;

CREATE TABLE tb_platform_user (
	id INTEGER AUTO_INCREMENT PRIMARY KEY,
	user_id INTEGER UNSIGNED UNIQUE COMMENT '�û�ȫ��id����������id',
	nickname VARCHAR(45) NOT NULL UNIQUE,
	avatar VARCHAR(255) DEFAULT '',
	dateCreated DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	identity_type TINYINT UNSIGNED DEFAULT 1 COMMENT '��¼���ͣ��û������ֻ������䡢��������',
	identifier VARCHAR(128) COMMENT '�ֻ������䡢�û������������ȵ�Ψһid',
	credential VARCHAR(255) NOT NULL COMMENT 'վ�������������token',
	randCredential VARCHAR(16) COMMENT 'վ�����������'
);
