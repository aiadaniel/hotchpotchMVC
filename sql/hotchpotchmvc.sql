-- MySQL dump 10.16  Distrib 10.1.21-MariaDB, for Win32 (AMD64)
--
-- Host: localhost    Database: localhost
-- ------------------------------------------------------
-- Server version	10.1.21-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `board_administrator`
--

DROP TABLE IF EXISTS `board_administrator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `board_administrator` (
  `board_id` int(11) NOT NULL,
  `platformuser_id` int(11) NOT NULL,
  PRIMARY KEY (`board_id`,`platformuser_id`),
  KEY `FK_6lxw03xnrhj9egj9y90bnstse` (`platformuser_id`),
  CONSTRAINT `FK_6lxw03xnrhj9egj9y90bnstse` FOREIGN KEY (`platformuser_id`) REFERENCES `tb_platform_user` (`id`),
  CONSTRAINT `FK_rcaxsqv53epfcalle0qu4nceo` FOREIGN KEY (`board_id`) REFERENCES `tb_board` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `board_administrator`
--

LOCK TABLES `board_administrator` WRITE;
/*!40000 ALTER TABLE `board_administrator` DISABLE KEYS */;
INSERT INTO `board_administrator` VALUES (2,10000001),(22,10000004),(22,10000007);
/*!40000 ALTER TABLE `board_administrator` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hibernate_sequence`
--

DROP TABLE IF EXISTS `hibernate_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hibernate_sequence`
--

LOCK TABLES `hibernate_sequence` WRITE;
/*!40000 ALTER TABLE `hibernate_sequence` DISABLE KEYS */;
INSERT INTO `hibernate_sequence` VALUES (38);
/*!40000 ALTER TABLE `hibernate_sequence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hibernate_sequences`
--

DROP TABLE IF EXISTS `hibernate_sequences`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hibernate_sequences` (
  `sequence_name` varchar(255) NOT NULL,
  `next_val` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`sequence_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hibernate_sequences`
--

LOCK TABLES `hibernate_sequences` WRITE;
/*!40000 ALTER TABLE `hibernate_sequences` DISABLE KEYS */;
INSERT INTO `hibernate_sequences` VALUES ('tb_platform_user',10000401);
/*!40000 ALTER TABLE `hibernate_sequences` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_board`
--

DROP TABLE IF EXISTS `tb_board`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_board` (
  `id` int(11) NOT NULL,
  `dateCreated` datetime DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `version` int(11) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `replyCount` int(11) NOT NULL,
  `category_id` int(11) DEFAULT NULL,
  `last_reply_id` int(11) DEFAULT NULL,
  `last_thread_id` int(11) DEFAULT NULL,
  `postCount` int(11) NOT NULL,
  `last_post_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_2wuuikmopts17tgauc0w6satx` (`category_id`),
  KEY `FK_qh7gy6mu5bl84js2u4ya417tn` (`last_reply_id`),
  KEY `FK_l4u3scdou4moicpxoc2gsusev` (`last_thread_id`),
  KEY `FK_pcmptkrm5pht22e4q47hji89d` (`last_post_id`),
  CONSTRAINT `FK_2wuuikmopts17tgauc0w6satx` FOREIGN KEY (`category_id`) REFERENCES `tb_category` (`id`),
  CONSTRAINT `FK_l4u3scdou4moicpxoc2gsusev` FOREIGN KEY (`last_thread_id`) REFERENCES `tb_thread` (`id`),
  CONSTRAINT `FK_pcmptkrm5pht22e4q47hji89d` FOREIGN KEY (`last_post_id`) REFERENCES `tb_post` (`id`),
  CONSTRAINT `FK_qh7gy6mu5bl84js2u4ya417tn` FOREIGN KEY (`last_reply_id`) REFERENCES `tb_reply` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_board`
--

LOCK TABLES `tb_board` WRITE;
/*!40000 ALTER TABLE `tb_board` DISABLE KEYS */;
INSERT INTO `tb_board` VALUES (2,'2017-04-27 20:30:58','\0',5,'哈哈哈','笑话大全',2,1,37,17,12,34),(22,'2017-05-09 09:57:26','\0',1,'哈哈哈','每日趣闻',0,1,NULL,NULL,0,NULL);
/*!40000 ALTER TABLE `tb_board` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_category`
--

DROP TABLE IF EXISTS `tb_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_category` (
  `id` int(11) NOT NULL,
  `dateCreated` datetime DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `version` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_category`
--

LOCK TABLES `tb_category` WRITE;
/*!40000 ALTER TABLE `tb_category` DISABLE KEYS */;
INSERT INTO `tb_category` VALUES (1,NULL,'\0',0,'娱乐'),(18,NULL,'\0',0,'体育板块'),(20,NULL,'\0',0,'综合');
/*!40000 ALTER TABLE `tb_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_platform_user`
--

DROP TABLE IF EXISTS `tb_platform_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_platform_user` (
  `id` int(11) NOT NULL,
  `dateCreated` datetime DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `version` int(11) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `birthday` datetime DEFAULT NULL,
  `credential` varchar(255) DEFAULT NULL,
  `dateLastActived` datetime DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `identifier` varchar(128) DEFAULT NULL,
  `ipLastActived` varchar(255) DEFAULT NULL,
  `login_type` tinyint(4) DEFAULT NULL,
  `nickname` varchar(45) NOT NULL,
  `randCredential` varchar(12) DEFAULT NULL,
  `sex` tinyint(2) DEFAULT NULL,
  `integration` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_pl3nyqlub6gwicwdp9la5t8r9` (`nickname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_platform_user`
--

LOCK TABLES `tb_platform_user` WRITE;
/*!40000 ALTER TABLE `tb_platform_user` DISABLE KEYS */;
INSERT INTO `tb_platform_user` VALUES (10000001,'2017-04-27 20:29:13','\0',4,'5c2bd76a-09ea-49e5-8b3a-a0bb091dc997.png','2017-04-27 20:29:13','17C473E38CF0EB6F32DFF2E3E343AA34','2017-04-27 20:29:13',NULL,NULL,NULL,1,'aassdd','0BALgF',0,0),(10000002,'2017-04-27 20:29:22','\0',6,'6ea916a7-ebb9-4e32-8914-4264203b7cf0.png','2017-04-27 20:29:22','7DFEBCD585956FE1A2D4098710D7D8D1','2017-04-27 20:29:22',NULL,NULL,NULL,1,'rrttyy','wLTnHB',0,0),(10000003,'2017-04-27 20:29:29','\0',0,NULL,'2017-04-27 20:29:29','79A439EA5D46DA401FBEECA6A0C7181E','2017-04-27 20:29:29',NULL,NULL,NULL,1,'aasshh','5sOjjd',0,0),(10000004,'2017-04-27 20:29:48','\0',0,NULL,'2017-04-27 20:29:48','B2EA79F0508F81AC2E747D4BFEA112D1','2017-04-27 20:29:48',NULL,NULL,NULL,1,'aaeerr','LG3Bmk',0,0),(10000005,'2017-04-27 20:29:52','\0',0,NULL,'2017-04-27 20:29:52','599FA70FCA74278D79B11411EC8035EF','2017-04-27 20:29:52',NULL,NULL,NULL,1,'ssddff','3tmfOr',0,0),(10000006,'2017-04-27 20:29:56','\0',0,NULL,'2017-04-27 20:29:56','26BAC7CAD3AB7DAF2C829268D1689841','2017-04-27 20:29:56',NULL,NULL,NULL,1,'ttrryy','CSYvc7',0,0),(10000007,'2017-04-27 20:30:00','\0',0,NULL,'2017-04-27 20:30:00','4F9EF522C8DAC175D77B6C30768A1155','2017-04-27 20:30:00',NULL,NULL,NULL,1,'vvbbnn','kKFy3H',0,0),(10000008,'2017-04-27 20:30:04','\0',0,NULL,'2017-04-27 20:30:04','2AC3B0AF2ADE8D413C62147947841A02','2017-04-27 20:30:04',NULL,NULL,NULL,1,'oollpp','aBHROh',0,0),(10000009,'2017-04-27 20:30:07','\0',0,NULL,'2017-04-27 20:30:07','21E7D9B92C8B11108007AA1DD78FC362','2017-04-27 20:30:07',NULL,NULL,NULL,1,'qqhhmm','Lz4JmC',0,0),(10000010,'2017-04-27 20:30:14','\0',0,NULL,'2017-04-27 20:30:14','CB34FF9F4127F421C48B8410E4B0EB46','2017-04-27 20:30:14',NULL,NULL,NULL,1,'kkllss','YBeAJK',0,0),(10000151,'2017-05-03 16:32:30','\0',0,NULL,'2017-05-03 16:32:30','03F2219E22560CF2F8E24C0496C88A2B','2017-05-03 16:32:30',NULL,NULL,NULL,1,'ddffhh','rJTFF7',0,0),(10000251,'2017-05-19 19:30:21','\0',0,NULL,'2017-05-19 19:30:21','6E9B16C0DF5F1FF0F1E83F2E6E2019FF','2017-05-19 19:30:21',NULL,NULL,NULL,1,'daniel','PPMMNE',0,0),(10000252,'2017-05-19 19:37:33','\0',0,NULL,'2017-05-19 19:37:33','92C0010EE921879400FE66BDA979F1CA','2017-05-19 19:37:33',NULL,NULL,NULL,1,'stefan','nlPGJ5',0,0),(10000301,'2017-05-19 19:46:51','\0',0,NULL,'2017-05-19 19:46:51','42D1D70C9DB648D37CD4BF989803091E','2017-05-19 19:46:51',NULL,NULL,NULL,1,'stefan2','HBa5em',0,0);
/*!40000 ALTER TABLE `tb_platform_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_post`
--

DROP TABLE IF EXISTS `tb_post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_post` (
  `id` int(11) NOT NULL,
  `dateCreated` datetime DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `version` int(11) DEFAULT NULL,
  `content` longtext,
  `dateLastReplied` datetime DEFAULT NULL,
  `down` int(11) NOT NULL,
  `hit` int(11) NOT NULL,
  `ipCreated` varchar(255) DEFAULT NULL,
  `prise` int(11) NOT NULL,
  `readonly` bit(1) NOT NULL,
  `replyCount` int(11) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `topped` bit(1) NOT NULL,
  `author_id` int(11) DEFAULT NULL,
  `author_last_replied_id` int(11) DEFAULT NULL,
  `board_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_gi04hydkshrrlkqxv3xokcuy9` (`author_id`),
  KEY `FK_fu9lr58khapu4ffl5g3339h5p` (`author_last_replied_id`),
  KEY `FK_kk0oov6dlynj4rjfnlb0qbvxg` (`board_id`),
  CONSTRAINT `FK_fu9lr58khapu4ffl5g3339h5p` FOREIGN KEY (`author_last_replied_id`) REFERENCES `tb_platform_user` (`id`),
  CONSTRAINT `FK_gi04hydkshrrlkqxv3xokcuy9` FOREIGN KEY (`author_id`) REFERENCES `tb_platform_user` (`id`),
  CONSTRAINT `FK_kk0oov6dlynj4rjfnlb0qbvxg` FOREIGN KEY (`board_id`) REFERENCES `tb_board` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_post`
--

LOCK TABLES `tb_post` WRITE;
/*!40000 ALTER TABLE `tb_post` DISABLE KEYS */;
INSERT INTO `tb_post` VALUES (23,'2017-05-09 10:21:54','\0',0,'PROPAGATION_REQUIRED--支持当前事务，如果当前没有事务，就新建一个事务。这是最常见的选择。  PROPAGATION_SUPPORTS--支持当前事务，如果当前没有事务，就以非事务方式执行。  PROPAGATION_MANDATORY--支持当前事务，如果当前没有事务，就抛出异常。  PROPAGATION_REQUIRES_NEW--新建事务，如果当前存在事务，把当前事务挂起。  PROPAGATION_NOT_SUPPORTED--以非事务方式执行操作，如果当前存在事务，就把当前事务挂起。  PROPAGATION_NEVER--以非事务方式执行，如果当前存在事务，则抛出异常。',NULL,0,0,NULL,0,'\0',0,'趣闻一哥1则','\0',10000001,NULL,2),(24,'2017-05-09 10:24:14','\0',0,'爱词霸英语为广大网友提供在线翻译、在线词典、英语口语、英语学习资料、汉语词典,金山词霸下载等服务,致力于为您提供优质权威的在线英语服务,是5000万英语学习者的好帮手。',NULL,0,0,NULL,0,'\0',0,'趣闻一哥2则','\0',10000002,NULL,2),(25,'2017-05-09 10:24:26','\0',0,'SLF4J：即简单日志门面（Simple Logging Facade for Java），不是具体的日志解决方案，它只服务于各种各样的日志系统。按照官方的说法，SLF4J是一个用于日志系统的简单Facade，允许最终用户在部署其应用时使用其所希望的日志系统。      在使用SLF4J的时候，不需要在代码中或配置文件中指定你打算使用那个具体的日志系统，SLF4J提供了统一的记录日志的接口，只要按照其提供的方法记录即可，最终日志的格式、记录级别、输出方式等通过具体日志系统的配置来实现，因此可以在应用中灵活切换日志系统。  ',NULL,0,0,NULL,0,'\0',0,'趣闻一哥3则','\0',10000003,NULL,2),(26,'2017-05-09 10:25:05','\0',0,'真的好好吃哦2',NULL,0,0,NULL,0,'\0',0,'趣闻一哥4则','\0',10000004,NULL,2),(27,'2017-05-09 10:25:37','\0',0,'Redis 优化之 tcp-backlogAndy_Zhou 发表于10个月前 分享到: 一键分享 QQ空间 微信 腾讯微博 新浪微博 QQ好友 有道云笔记 原 Redis 优化之 tcp-backlog 收藏...',NULL,0,0,NULL,0,'\0',0,'趣闻一哥5则','\0',10000005,NULL,2),(28,'2017-05-09 10:25:53','\0',0,'真的好好吃哦6',NULL,0,0,NULL,0,'\0',0,'趣闻一哥6则','\0',10000006,NULL,2),(29,'2017-05-09 10:26:02','\0',0,'真的好好吃哦7',NULL,0,0,NULL,0,'\0',0,'趣闻一哥7则','\0',10000007,NULL,2),(30,'2017-05-09 10:26:09','\0',0,'真的好好吃哦8',NULL,0,0,NULL,0,'\0',0,'趣闻一哥8则','\0',10000008,NULL,2),(31,'2017-05-09 10:26:15','\0',0,'真的好好吃哦9',NULL,0,0,NULL,0,'\0',0,'趣闻一哥9则','\0',10000009,NULL,2),(32,'2017-05-09 10:26:46','\0',0,'真的好好吃哦10',NULL,0,0,NULL,0,'\0',0,'趣闻一哥10则','\0',10000010,NULL,2),(33,'2017-05-09 10:26:53','\0',0,'真的好好吃哦11',NULL,0,0,NULL,0,'\0',0,'趣闻一哥11则','\0',10000001,NULL,2),(34,'2017-05-09 10:27:11','\0',5,'真的好好吃哦12','2017-05-09 11:09:18',0,0,NULL,0,'\0',2,'趣闻一哥12则','\0',10000151,10000002,2);
/*!40000 ALTER TABLE `tb_post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_reply`
--

DROP TABLE IF EXISTS `tb_reply`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_reply` (
  `id` int(11) NOT NULL,
  `dateCreated` datetime DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `version` int(11) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `floor` int(11) NOT NULL,
  `ipCreated` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `author_id` int(11) DEFAULT NULL,
  `thread_id` int(11) DEFAULT NULL,
  `post_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_adir5l8xd37rcotowye3t8i9m` (`author_id`),
  KEY `FK_rfa1x1yt71jlreda2ffiafm2u` (`thread_id`),
  KEY `FK_1kwn853oomtsbhj3k8tpqy136` (`post_id`),
  CONSTRAINT `FK_1kwn853oomtsbhj3k8tpqy136` FOREIGN KEY (`post_id`) REFERENCES `tb_post` (`id`),
  CONSTRAINT `FK_adir5l8xd37rcotowye3t8i9m` FOREIGN KEY (`author_id`) REFERENCES `tb_platform_user` (`id`),
  CONSTRAINT `FK_rfa1x1yt71jlreda2ffiafm2u` FOREIGN KEY (`thread_id`) REFERENCES `tb_thread` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_reply`
--

LOCK TABLES `tb_reply` WRITE;
/*!40000 ALTER TABLE `tb_reply` DISABLE KEYS */;
INSERT INTO `tb_reply` VALUES (35,NULL,'\0',0,'指定分隔符导出数据（貌似必须在本机才能正常执行）',1,NULL,'回复1则',10000001,NULL,34),(37,NULL,'\0',0,'真的好好吃哦2',2,NULL,'回复2则',10000002,NULL,34);
/*!40000 ALTER TABLE `tb_reply` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_thread`
--

DROP TABLE IF EXISTS `tb_thread`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_thread` (
  `id` int(11) NOT NULL,
  `dateCreated` datetime DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `version` int(11) DEFAULT NULL,
  `content` longtext,
  `dateLastReplied` datetime DEFAULT NULL,
  `down` int(11) NOT NULL,
  `hit` int(11) NOT NULL,
  `ipCreated` varchar(255) DEFAULT NULL,
  `prise` int(11) NOT NULL,
  `readonly` bit(1) NOT NULL,
  `replyCount` int(11) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `topped` bit(1) NOT NULL,
  `author_id` int(11) DEFAULT NULL,
  `author_last_replied_id` int(11) DEFAULT NULL,
  `board_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_dt2197gu1dfl5smx06512tony` (`author_id`),
  KEY `FK_qu8098kapoeu02ooe4806hnjm` (`author_last_replied_id`),
  KEY `FK_hsc3i3nb3h4jpnupxptw6gi5` (`board_id`),
  CONSTRAINT `FK_dt2197gu1dfl5smx06512tony` FOREIGN KEY (`author_id`) REFERENCES `tb_platform_user` (`id`),
  CONSTRAINT `FK_hsc3i3nb3h4jpnupxptw6gi5` FOREIGN KEY (`board_id`) REFERENCES `tb_board` (`id`),
  CONSTRAINT `FK_qu8098kapoeu02ooe4806hnjm` FOREIGN KEY (`author_last_replied_id`) REFERENCES `tb_platform_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_thread`
--

LOCK TABLES `tb_thread` WRITE;
/*!40000 ALTER TABLE `tb_thread` DISABLE KEYS */;
INSERT INTO `tb_thread` VALUES (3,'2017-05-05 17:06:12','\0',0,'PROPAGATION_REQUIRED--支持当前事务，如果当前没有事务，就新建一个事务。这是最常见的选择。  PROPAGATION_SUPPORTS--支持当前事务，如果当前没有事务，就以非事务方式执行。  PROPAGATION_MANDATORY--支持当前事务，如果当前没有事务，就抛出异常。  PROPAGATION_REQUIRES_NEW--新建事务，如果当前存在事务，把当前事务挂起。  PROPAGATION_NOT_SUPPORTED--以非事务方式执行操作，如果当前存在事务，就把当前事务挂起。  PROPAGATION_NEVER--以非事务方式执行，如果当前存在事务，则抛出异常。',NULL,0,0,NULL,0,'\0',0,'趣闻一哥2则','\0',10000002,NULL,2),(4,'2017-05-05 17:06:15','\0',0,'SLF4J：即简单日志门面（Simple Logging Facade for Java），不是具体的日志解决方案，它只服务于各种各样的日志系统。按照官方的说法，SLF4J是一个用于日志系统的简单Facade，允许最终用户在部署其应用时使用其所希望的日志系统。      在使用SLF4J的时候，不需要在代码中或配置文件中指定你打算使用那个具体的日志系统，SLF4J提供了统一的记录日志的接口，只要按照其提供的方法记录即可，最终日志的格式、记录级别、输出方式等通过具体日志系统的配置来实现，因此可以在应用中灵活切换日志系统。  ',NULL,0,0,NULL,0,'\0',0,'趣闻一哥3则','\0',10000002,NULL,2),(5,'2017-05-05 17:06:20','\0',0,'爱词霸英语为广大网友提供在线翻译、在线词典、英语口语、英语学习资料、汉语词典,金山词霸下载等服务,致力于为您提供优质权威的在线英语服务,是5000万英语学习者的好帮手。',NULL,0,0,NULL,0,'\0',0,'趣闻一哥4则','\0',10000003,NULL,2),(6,'2017-05-05 17:06:25','\0',0,'Redis 优化之 tcp-backlogAndy_Zhou 发表于10个月前 分享到: 一键分享 QQ空间 微信 腾讯微博 新浪微博 QQ好友 有道云笔记 原 Redis 优化之 tcp-backlog 收藏...',NULL,0,0,NULL,0,'\0',0,'趣闻一哥5则','\0',10000004,NULL,2),(7,'2017-05-05 17:06:45','\0',0,'真的好好吃哦5',NULL,0,0,NULL,0,'\0',0,'趣闻一哥6则','\0',10000005,NULL,2),(8,'2017-05-08 17:36:20','\0',0,'SLF4J：即简单日志门面（Simple Logging Facade for Java），不是具体的日志解决方案，它只服务于各种各样的日志系统。按照官方的说法，SLF4J是一个用于日志系统的简单Facade，允许最终用户在部署其应用时使用其所希望的日志系统。      在使用SLF4J的时候，不需要在代码中或配置文件中指定你打算使用那个具体的日志系统，SLF4J提供了统一的记录日志的接口，只要按照其提供的方法记录即可，最终日志的格式、记录级别、输出方式等通过具体日志系统的配置来实现，因此可以在应用中灵活切换日志系统。  ',NULL,0,0,NULL,0,'\0',0,'趣闻一哥7则','\0',10000001,NULL,2),(9,'2017-05-08 17:52:52','\0',0,'爱词霸英语为广大网友提供在线翻译、在线词典、英语口语、英语学习资料、汉语词典,金山词霸下载等服务,致力于为您提供优质权威的在线英语服务,是5000万英语学习者的好帮手。',NULL,0,0,NULL,0,'\0',0,'趣闻一哥8则','\0',10000003,NULL,2),(10,'2017-05-08 17:53:50','\0',0,'PROPAGATION_REQUIRED--支持当前事务，如果当前没有事务，就新建一个事务。这是最常见的选择。  PROPAGATION_SUPPORTS--支持当前事务，如果当前没有事务，就以非事务方式执行。  PROPAGATION_MANDATORY--支持当前事务，如果当前没有事务，就抛出异常。  PROPAGATION_REQUIRES_NEW--新建事务，如果当前存在事务，把当前事务挂起。  PROPAGATION_NOT_SUPPORTED--以非事务方式执行操作，如果当前存在事务，就把当前事务挂起。  PROPAGATION_NEVER--以非事务方式执行，如果当前存在事务，则抛出异常。',NULL,0,0,NULL,0,'\0',0,'趣闻一哥9则','\0',10000004,NULL,2),(11,'2017-05-08 17:54:11','\0',0,'Spring MVC Test also provides client-side support for testing code that uses the RestTemplate. Client-side tests mock the server responses and also do not use a running server.',NULL,0,0,NULL,0,'\0',0,'趣闻一哥10则','\0',10000005,NULL,2),(12,'2017-05-08 17:54:26','\0',0,'Spring Boot provides an option to write full, end-to-end integration tests that include a running server. If this is your goal please have a look at the Spring Boot reference page. For more information on the differences between out-of-container and end-to-end integration tests, see the section called “Differences between Out-of-Container and End-to-End Integration Tests”.',NULL,0,0,NULL,0,'\0',0,'趣闻一哥11则','\0',10000006,NULL,2),(13,'2017-05-08 17:57:36','\0',0,'真的好好吃哦2',NULL,0,0,NULL,0,'\0',0,'趣闻一哥12则','\0',10000007,NULL,2),(14,'2017-05-08 17:57:51','\0',0,'Redis 优化之 tcp-backlogAndy_Zhou 发表于10个月前 分享到: 一键分享 QQ空间 微信 腾讯微博 新浪微博 QQ好友 有道云笔记 原 Redis 优化之 tcp-backlog 收藏...',NULL,0,0,NULL,0,'\0',0,'趣闻一哥13则','\0',10000008,NULL,2),(15,'2017-05-08 18:05:06','\0',0,'Redis 优化之 tcp-backlogAndy_Zhou 发表于10个月前 分享到: 一键分享 QQ空间 微信 腾讯微博 新浪微博 QQ好友 有道云笔记 原 Redis 优化之 tcp-backlog 收藏...',NULL,0,0,NULL,0,'\0',0,'趣闻一哥13则','\0',10000008,NULL,2),(17,'2017-05-08 20:37:13','\0',0,'真的好好吃哦6',NULL,0,0,NULL,0,'\0',0,'趣闻一哥16则','\0',10000007,NULL,2);
/*!40000 ALTER TABLE `tb_thread` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-06-13 19:36:28
