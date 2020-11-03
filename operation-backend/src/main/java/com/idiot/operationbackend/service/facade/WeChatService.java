package com.idiot.operationbackend.service.facade;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.idiot.operationbackend.entity.*;
import com.idiot.operationbackend.support.Constants;
import com.idiot.operationbackend.support.WxInputStreamResource;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.InputStream;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 微信处理
 * @author wang xiao
 * @date Created in 10:53 2020/9/11
 */
public interface WeChatService {

  /**
   *  授权回调
   * @author wangxiao
   * @date 14:17 2020/9/11
   * @param param param
   * @return java.lang.String
   */
    String notice(Map<String,String> param);


  /**
   *  获取令牌
   * @author wangxiao
   * @date 17:25 2020/9/11
   * @return java.lang.String
   */
  String getComponentAccessToken ();


  /**
   *  获取授权方令牌
   * @author wangxiao
   * @date 16:00 2020/9/12 
   * @param accountId 公众号列表id
   * @return java.lang.String
   */
  String getAuthorizerAccessToken (String accountId);


  /**
   *  获取预授权码
   * @author wangxiao
   * @date 18:10 2020/9/11
   * @return java.lang.String
   */
  String getPreAuthCode ();


  /**
   *  获取预授权地址
   * @author wangxiao
   * @date 19:34 2020/9/11
   * @return java.lang.String
   */
  String getPreAuthUrl ();

  /**
   *  微信解密数据
   * @author wangxiao
   * @date 19:41 2020/9/11
   * @param msgSignature msgSignature
   * @param timestamp timestamp
   * @param nonce nonce
   * @param data data
   * @param ticket  是否是ticket
   * @return java.lang.String
   */
  String decryptMsg(String msgSignature,String timestamp,String nonce,String data, boolean ticket);


  /**
   *  微信加密数据
   * @author wangxiao
   * @date 19:49 2020/9/11
   * @param data data
   * @return java.lang.String
   */
  String encryptMsg(String data);


  /**
   *  获取微信授权信息
   * @author wangxiao
   * @date 10:52 2020/9/12
   * @param authCode authCode
   * @return String
   */
  String  getAuthorizationInfo(String authCode);


  /**
   *  获取授权方信息
   * @author wangxiao
   * @date 13:14 2020/9/12
   * @param authorizerAppId 授权方appId
   * @return java.lang.String
   */
  String getAuthorizerInfo(String authorizerAppId);



  /**
   *  保存或更新 微信公众号
   * @author wangxiao
   * @date 16:15 2020/9/12
   * @param account 公众号信息
   * @return boolean
   */
  boolean saveOrUpdateWechatAcc(Account account);



  /**
   *  缓存authorizerAccessToken
   * @author wangxiao
   * @date 16:40 2020/9/12
   * @param accountId accountId
   * @param authorizerAccessToken authorizerAccessToken
   */
  void cacheAuthorizerAccessToken(String accountId,String authorizerAccessToken);


  /**
   *  同步用户数据
   * @author wangxiao
   * @date 18:51 2020/9/14
   * @param accountId accountId
   * @param openIds openIds
   */
  void syncUserTask(String accountId,List<String> openIds);



  /**
   *  同步用户
   * @author wangxiao
   * @date 14:10 2020/9/14
   * @param accountId accountId
   * @return int
   */
  int syncAccountUser(String accountId);

  /**
   *  同步标签信息
   * @author wangxiao
   * @date 20:16 2020/9/14
   * @param accountId accountId
   */
  void syncTag(String accountId);


  /**
   * 获取粉丝信息
   * @author wangxiao
   * @date 19:09 2020/9/14
   * @param accountId accountId
   * @param openId openId
   * @return java.lang.String
   */
  String getFansInfo(String accountId,String openId);


  /**
   *  微信授权后确认公众号
   * @author wangxiao
   * @date 20:12 2020/9/14
   * @param accountId accountId
   * @param userId userId
   * @return boolean
   */
  boolean confirmAccount(String accountId,String userId);


  /**
   *  获取微信用户增减数据
   * @author wangxiao
   * @date 17:57 2020/9/15
   * @param accountId accountId
   * @param endDate  结束日期
   * @param startDate  开始日期
   * @return java.lang.String
   */
  String getFansSummary(String accountId, String startDate, String endDate);


  /**
   *  查询汇总数据
   * @author wangxiao
   * @date 20:20 2020/9/15
   * @param accountId accountId
   * @param startDate startDate
   * @param endDate endDate
   * @return java.lang.String
   */
  String getFansCumulate(String accountId,String startDate,String endDate);


  /**
   *  查询图文阅读数据
   * @author wangxiao
   * @date 16:16 2020/9/17
   * @param accountId accountId
   * @param startDate startDate
   * @param endDate endDate
   * @return java.lang.String
   */
  String getArticleSummary(String accountId,String startDate,String endDate );

  /**
   *  添加备注
   * @author wangxiao
   * @date 19:38 2020/9/17
   * @param accountId accountId
   * @param openId openId
   * @param remark remark
   * @return java.lang.String
   */
  boolean updateRemark(String accountId,String openId,String remark);

  /**
   *  创建标签
   * @author wangxiao
   * @date 20:05 2020/9/17
   * @param accountId accountId
   * @param tagLabel tagLabel
   * @return boolean
   */
  boolean createTag (String accountId,String tagLabel);


    /**
     *  删除标签
     * @author wangxiao
     * @date 20:05 2020/9/17
     * @param accountId accountId
     * @param wxId wxId
     * @return boolean
     */
    boolean delTag (String accountId,Integer wxId);


  /**
   *  用户打标签
   * @author wangxiao
   * @date 20:36 2020/9/17
   * @param accountId accountId
   * @param openIds openIds
   * @param wxId wxId
   * @return boolean
   */
  boolean addUserTag(String accountId,List<String> openIds,int wxId);


  /**
   *  用户移除标签
   * @author wangxiao
   * @date 20:36 2020/9/17
   * @param accountId accountId
   * @param openIds openIds
   * @param wxId wxId
   * @return boolean
   */
  boolean removeUserTag(String accountId,List<String> openIds,int wxId);


  /**
   *  编辑标签
   * @author wangxiao
   * @date 10:32 2020/9/18
   * @param accountId accountId
   * @param wxId wxId
   * @param name name
   * @return boolean
   */
  boolean upTag(String accountId,int wxId,String name);


    /**
     *  上传其他素材
     * @author wangxiao
     * @date 14:20 2020/9/21
     * @param accountId accountId
     * @param type type
     * @param resource resource
     * @param title  param
     * @param introduction introduction
     * @return java.lang.String
     */
   String addMaterial(String accountId, String type, WxInputStreamResource resource, String title, String introduction);


    /**
     *  上传图片
     * @author wangxiao
     * @date 14:20 2020/9/21
     * @param accountId accountId
     * @param resource resource
     * @return java.lang.String
     */
    String upImage(String accountId,  WxInputStreamResource resource);


   /**
    *  新增图文素材
    * @author wangxiao
    * @date 15:59 2020/9/21
    * @param articles articles
    * @param accountId accountId
    * @return java.lang.String
    */
   String addNews (List<Articles> articles,String accountId);


   /**
    *  生成二维码
    * @author wangxiao
    * @date 19:29 2020/9/21
    * @param qrCode qrCode
    * @param accountId accountId
    * @return java.lang.String
    */
   String createQrCode(QrCode qrCode,String accountId);


   /**
    *  创建菜单
    * @author wangxiao
    * @date 11:22 2020/9/22
    * @param accountMenus accountMenus
    * @param accountId accountId
    * @return boolean
    */
   boolean createMenu(List<AccountMenu> accountMenus,String accountId);



   /**
    *  删除菜单
    * @author wangxiao
    * @date 11:22 2020/9/22
    * @param accountId accountId
    * @return boolean
    */
   boolean delMenu(String accountId);


   /**
    *  获取微信模板消息列表
    * @author wangxiao
    * @date 20:01 2020/9/22
    * @param accountId
    * @return java.lang.String
    */
   String getTemplate(String accountId);


   /**
    *  群发消息 任务
    * @author wangxiao
    * @date 17:35 2020/9/23
    * @param groupMsg  groupMsg
    */
   void doGroupMsgTask(GroupMsg groupMsg);


   /**
    *  模板消息 任务
    * @author wangxiao
    * @date 13:45 2020/9/24
    * @param templateMsg templateMsg
    */
   void doTemplateMsgTask (TemplateMsg templateMsg);


   /**
    *  客服消息
    * @author wangxiao
    * @date 14:36 2020/9/24
    * @param customerMsg customerMsg
    */
   void doCustomerMsgTask (CustomerMsg customerMsg);


   /**
    *  处理扫描二维码
    * @author wangxiao
    * @date 18:23 2020/9/24
    * @param accountId 公众号id
    * @param openId 用户
    * @param qrCodeId 二维码id
    * @param contents 二维码内容
    */
   void processScanQrCode (String accountId,String openId,String qrCodeId,String contents);



   /**
    *  发送消息
    * @author wangxiao
    * @date 19:49 2020/9/24
    * @param accountId accountId
    * @param openId openId
    * @param nikeName  nikeName
    * @param content content
    * @return void
    */
   void sendMessage (String accountId,String openId,String nikeName,String content);



   /**
    *  推送智能推送
    * @author wangxiao
    * @date 20:42 2020/9/24
    * @param accountId accountId
    * @param openId openId
    * @param index index
    */
   void sendPushMessage(String accountId, String openId, int index);


   /**
    *  查询公众号素材
    * @author wangxiao
    * @date 19:24 2020/10/27
    * @param accountId accountId
    * @param type  素材的类型，图片（image）、视频（video）、语音 （voice）、图文（news）
    * @param offset offset
    * @param count count
    * @return JSONObject
    */
   JSONObject queryAccountMaterial (String accountId, String type, int offset, int count );


   /**
    *  查询公众号素材详情
    * @author wangxiao
    * @date 19:50 2020/10/28
    * @param accountId  accountId
    * @param mediaId mediaId
    * @return java.lang.String
    */
   String getMaterialDetail(String accountId, String mediaId);


  /**
   * 不支持的消息
   * @author wangxiao
   * @date 15:27 2020/7/1
   * @param param param
   * @return java.lang.String
   */
  default String unSupportedMessage(Map<String,String> param) {
    return Constants.SUCCESS;
  }




    /**
     *  map to xml
     * @author wangxiao
     * @date 12:02 2020/10/26
     * @param map
     * @return java.lang.String
     */
    default String map2Xml(Map<String,String> map) {
        String xmlResult = "";
        StringBuffer buffer = new StringBuffer();
        buffer.append("<xml>");
        for (String key : map.keySet()) {
            System.out.println(key + "========" + map.get(key));
            String value = "<![CDATA[" + map.get(key) + "]]>";
            buffer.append("<" + key + ">" + value + "</" + key + ">");
            System.out.println();
        }
        buffer.append("</xml>");
        xmlResult = buffer.toString();
        return xmlResult;
    }

    /**
     *  document 转 map
     * @author wangxiao
     * @date 19:36 2020/9/11
     * @param xml xml
     * @return java.util.Map<java.lang.String,java.lang.String>
     */
    default Map<String,String> xmlToMap(String xml) {
        Map<String,String>map = new HashMap<>(8);
        Document doc = null;
        try {
            doc = DocumentHelper.parseText(xml);
            Element rootElt = doc.getRootElement();
            @SuppressWarnings("unchecked")
            List<Element> list = rootElt.elements();
            for (Element element : list) {
                map.put(element.getName(),element.getText());
            }
        } catch (DocumentException e ) {
            e.printStackTrace();
        }
        return map;
    }
}
