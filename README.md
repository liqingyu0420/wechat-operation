# [wechat-operation](#wechat-operation)
![运营星](https://img-blog.csdnimg.cn/20201102164123661.jpg#pic_center)
微信公众号深度运营平台————运营星,使公众号运营更加灵活,快速提升用户体验感!
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![GitHub Stars](https://img.shields.io/github/stars/7-idiot/wechat-operation)](https://github.com/7-idiot/wechat-operation/stargazers)
[![GitHub Forks](https://img.shields.io/github/forks/7-idiot/wechat-operation)](https://github.com/7-idiot/wechat-operatione/fork)
[![GitHub issues](https://img.shields.io/github/issues/7-idiot/wechat-operation.svg)](https://github.com/7-idiot/wechat-operation/issues)
[![Percentage of issues still open](http://isitmaintained.com/badge/open/7-idiot/wechat-operation.svg)](https://github.com/7-idiot/wechat-operation/issues "Percentage of issues still open")
## [Features](#features)
* 微信第三方平台
* 一键授权公众号,帮助管理微信公众号.
* 实时同步粉丝信息,及时统计分析用户数据。
* 标签、备注同步功能使用户管理更加便捷
* 一键智能群发,随时设置关注回复,特殊回复
* 自定义菜单,方便更改公众号功能,让推送更加及时
* 在线文案媒体编辑 (todo 未开源)
* 粉丝信息单独回复，增加用户最优体验感 （未开源）

## [技术文档](#技术文档)
- [backend](https://github.com/7-idiot/weChat-Operation/tree/master/operation-backend):Springboot+Mysql+Docker
- [frontend](https://github.com/7-idiot/weChat-Operation/tree/master/operation-frontend):Vue+ant
## [示例](#示例)

*在线体验地址暂无，及时更新文档输出*

* 默认初始账号 admin/admin123!@#

* 示例图组

  1. 首页 数据展示

     ![首页数据](https://img-blog.csdnimg.cn/20201102203925952.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MjA0MzAzMA==,size_16,color_FFFFFF,t_70#pic_center)
     -------------------------------------------
     ![首页数据2](https://img-blog.csdnimg.cn/20201102203943120.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MjA0MzAzMA==,size_16,color_FFFFFF,t_70#pic_center)

  2. 公众号管理

     ![公众号管理](https://img-blog.csdnimg.cn/20201102204123140.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MjA0MzAzMA==,size_16,color_FFFFFF,t_70#pic_center)

  3. 互动推送

     ![互动消息](https://img-blog.csdnimg.cn/20201102204235787.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MjA0MzAzMA==,size_16,color_FFFFFF,t_70#pic_center)
     
  4. 粉丝管理
  
   ![fans](https://img-blog.csdnimg.cn/20201102204353763.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MjA0MzAzMA==,size_16,color_FFFFFF,t_70#pic_center)
  
  5. 用户管理
  
  ![user](https://img-blog.csdnimg.cn/20201102204417761.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MjA0MzAzMA==,size_16,color_FFFFFF,t_70#pic_center)

## [快速开始](#快速开始)
-------------------------------------------
*能够使用的接口都是在公众平台获取到的*
-------------------------------------------
1. 申请微信第三方平台账号,并在配置开发资料
   ![相关信息](https://img-blog.csdnimg.cn/20201102172549685.png#pic_center)
2.  git Clone 项目 按照[技术文档](https://github.com/7-idiot/weChat-Operation#%E6%8A%80%E6%9C%AF%E6%96%87%E6%A1%A3)配置和发布 
3. 访问web界面入口
4. *推荐发布方式* 后端直接使用DockerFile构建DockerImage，前端部署nginx 
5. **无第三方平台账号** 情况下,该项目不能运营公众号,但单独一个公众号的运营还是可以修改相关代码来完成 ,eg:</br>
   *  消息回调 
   *  发送模板消息  
   *  发送客服消息  
   *  群发消息   
   *  数据统计  
   *  二维码  
-------------------------------------------
