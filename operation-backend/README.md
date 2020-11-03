# Backend
* 公众号运营星后台应用程序


## Table of Contents
* 1 [Backend](#Backend)
  * 1.1 [Features](#features)
* 2 [环境依赖](#环境依赖)
  * 2.1 [JDK](#jdk)
  * 2.2 [Maven](#maven)
  * 2.3 [mysql](#mysql)
* 3 [属性配置](#属性配置)
  * 3.1 [第三方配置平台配置位置](#第三方配置平台配置位置)
  * 3.2 [属性说明](#属性说明)
  * 3.3 [附加属性](#附加属性)

> 文档

[中文说明](README.md) | [English Readme](README-ENGLISH.md)


## Features
- 授权管理公众号
- 同步公众号粉丝
- 同步标签,并对用户进行标签管理
- 上传media到微信服务器
- 群发消息设置和发送
- 模板消息设置和发送
- 支持特殊字符的过滤
- 用户消息统计，即未设置的特殊回复展示在界面客户可自定义回复
- 操作人员 主--子账号


# 环境依赖
## JDK 
JDK 1.8 代码中使用lambda表达式
## Maven
 [Maven](http://maven.apache.org/) 进行统一管理依赖
## mysql
mysql 8.0 一些表结构使用字段类型是json,如若不适用请自动更改为适当类型  

## 快速开始
- 新建数据库,并执行sql文件夹下的[dataSource.sql](https://github.com/7-idiot/weChat-Operation/blob/master/sql/dataBase.sql)
- 申请微信第三方平台账号
- 更改application的配置文件（更改地方包括:数据源和第三方平台信息）
- 运行 OperationBackendApplication.java 中main方法
- 访问:ip:port/wxoperate/doc.html 查看接口文档

# 属性配置
## 第三方配置平台配置位置
- 文件所在位置: src/resource/application-*yml
- 开始前缀: wechat.platform
## 属性说明 
| 序号 | 属性 |   说明 |
|:----|:----|:----|
| 1 | appId | 第三方平台的appId | 
| 2 | appSecret |  第三方平台的appSecret | 
| 3 | ticketUrl | 授权事件url,微信文档中说明会5分钟一调用回传ticket,需要解密（与第三方平台配置中保持一致）| 
| 4 | secret |  配置报文加解密的密钥（与第三方平台配置中保持一致） | 
| 5 | token |  配置在第三方平台中的token | 
| 6 | authCallBack | 授权回调地址，用来接收授权码（与第三方平台配置中保持一致） | 
| 6 | msgCallBack | 消息事件接受地址，扫描二维码，用户发送消息等都在该地址处理（与第三方平台配置中保持一致） | 

## 附加属性
> confirm-domain :本地授权回调函数中重定向地址，应该是前端路由地址，且传输值accountId，
>做用：授权成功后跳转到前端界面，展示刚刚成功的微信公众号，用于展示和用户确认，用户确认成功便开始同步粉丝、标签
 


