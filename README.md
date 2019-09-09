## 介绍
该应用可以支持大陆地区新生儿的疫苗接种时间的计算。帮助你确定婴儿接种时间。

后来随着娃的实际接种开始，发现更依赖的是疫苗查询功能，目前该程序已经写到0.7版本了。也在微信小程序内上架了。欢迎大家使用

![wc](github.jpeg)

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

[create sql script](https://github.com/guohai163/vaccine/wiki/SQL)

===

欢迎订阅公众号：海哥聊技术

![http://guohai.org/assets/wechat.jpg](http://guohai.org/assets/wechat.jpg)
