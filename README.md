## 介绍
该应用可以支持大陆地区新生儿的疫苗接种时间的计算。帮助你确定婴儿接种时间。

## 功能需求
1. 一类二类疫苗列表说明，接种时间。
2. 设置新生儿出生时间，选择的二类疫苗。帮用户设置出一个接种时间表
3. 记录用户选择数据，下次进来不用再次选择
4. 列表按颜色区分出过期/已经接种、将要接种、未接种三种状态。

## 技术实现
1. 使用Spring Boot技术。
2. IDE使用IntelliJ IDEA
3. 产生一篇教程
4. 尽量不使用数据库，
5. 用户选择信息存储在cookies,尽量不在服务器进行存储。保护用户隐私
6. 使用FreeMarker模版引擎


## 建表

~~~ sql
SET character_set_client = utf8mb4 ;

CREATE TABLE `vaccine_url_tb` (
  `code` int(11) NOT NULL AUTO_INCREMENT,
  `batch_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `batch_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `vaccine_batch_tb` (
  `code` int(11) NOT NULL AUTO_INCREMENT,
  `page_code` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `product_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `norm` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `batch_no` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `batch_num` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `exp_date` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `manufacturer` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `inspection_num` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `certificate_no` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `report_num` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `issue_date` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `issue_conclusion` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `batch_issuing_agency` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `url_code` int(11) DEFAULT NULL,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `vaccine_db`.`vaccine_wechat_user_tb` (
  `open_id` VARCHAR(100) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_general_ci' NOT NULL,
  `login_code` VARCHAR(100) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_general_ci' NOT NULL,
`session_key` VARCHAR(100) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_general_ci' NOT NULL,
`src` VARCHAR(45)  CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_general_ci' NOT NULL,
  PRIMARY KEY (`open_id`));
  
  CREATE TABLE `vaccine_db`.`vaccine_access_log` (
  `code` INT NOT NULL AUTO_INCREMENT,
  `open_id` VARCHAR(100) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_general_ci' NULL,
  `user_agent` VARCHAR(450) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_general_ci' NULL,
  `user_ip` VARCHAR(45) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_general_ci' NULL,
  `service_category` VARCHAR(45) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_general_ci' NULL,
  `query_param` VARCHAR(45) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_general_ci' NULL,
   
  `access_date` DATETIME NULL,
  PRIMARY KEY (`code`));


~~~
===

欢迎订阅公众号：海哥聊技术

![http://guohai.org/assets/wechat.jpg](http://guohai.org/assets/wechat.jpg)
