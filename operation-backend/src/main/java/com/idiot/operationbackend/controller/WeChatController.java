package com.idiot.operationbackend.controller;

import com.alibaba.fastjson.JSONObject;
import com.idiot.operationbackend.entity.Account;
import com.idiot.operationbackend.handler.WeChatMessageFactory;
import com.idiot.operationbackend.service.facade.AccountService;
import com.idiot.operationbackend.service.facade.WeChatMessageService;
import com.idiot.operationbackend.service.facade.WeChatService;
import com.idiot.operationbackend.support.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;


/**
 * @author wang xiao
 * @date Created in 10:38 2020/9/11
 */
@RestController
@RequestMapping("/wechat")
@Api(value = "WeChatController", tags ="微信")
public class WeChatController {

    private final Logger logger = LoggerFactory.getLogger(WeChatController.class);


    @Value("${confirm-domain}")
    private String confirmDomain;


    @Autowired
    private WeChatService weChatService;

    @Autowired
    private AccountService accountService;



    @PostMapping("/notice")
    @ApiOperation(value = "验证票据接受url")
    public String noticeCallBack(HttpServletRequest request)  {
        String nonce = request.getParameter("nonce");
        String timestamp = request.getParameter("timestamp");
        // 默认aes
        String encryptType = request.getParameter("encrypt_type");
        String msgSignature = request.getParameter("msg_signature");
        logger.info("验证票据事件,param参数依次是nonce:{},timestamp:{},encrypt_type:{},msg_signature:{}",nonce,
                timestamp,encryptType,msgSignature);
        try {
            String postData =  readBody(request);;
            logger.info("微信验证票据回调,postData before decry :{}",postData);
            postData = weChatService.decryptMsg(msgSignature,timestamp,nonce,postData,true);
            logger.info("微信验证票据回调,postData after decry :{}",postData);
            return weChatService.notice(weChatService.xmlToMap(postData));
        }catch (Exception e) {
            logger.info("微信验证票据回调 ------->失败,message is :{}",e.getMessage());
            return Constants.SUCCESS;
        }
    }


    @GetMapping("/authCallback")
    @ApiOperation(value = "授权地址回调")
    public void authCallBack(@RequestParam(name = "auth_code") String authCode,
                             @RequestParam(name = "expires_in") String expiresIn,
                             HttpServletResponse response) throws IOException {
        logger.info("微信授权回调 ------->start,authCode is :{},expiresIn:{}",authCode,expiresIn);
        try {
            // 请求微信数据
            String authorizationInfoStr =  weChatService.getAuthorizationInfo(authCode);
            JSONObject authorizationInfoObj = JSONObject.parseObject(authorizationInfoStr);
            JSONObject authorizationInfo = authorizationInfoObj.getJSONObject("authorization_info");
            String authorizerAppId = authorizationInfo.getString("authorizer_appid");
            String authorizerInfoStr = weChatService.getAuthorizerInfo(authorizerAppId);
            JSONObject authorizerInfoObj = JSONObject.parseObject(authorizerInfoStr);
            // 授权信息
            Account account = new Account(authorizerAppId);
            String authorizerAccessToken = authorizationInfo.getString("authorizer_access_token");
            account.setAuthorizerAccessToken(authorizerAccessToken);
            account.setExpiresIn(authorizationInfo.getInteger("expires_in"));
            account.setAuthorizerRefreshToken(authorizationInfo.getString("authorizer_refresh_token"));
            // 授权方信息
            JSONObject authorizerInfo = authorizerInfoObj.getJSONObject("authorizer_info");
            account.setHeadImage(authorizerInfo.getString("head_img"));
            account.setNickName(authorizerInfo.getString("nick_name"));
            account.setServiceTypeInfo(authorizerInfo.getJSONObject("service_type_info").toJSONString());
            account.setVerifyTypeInfo(authorizerInfo.getJSONObject("verify_type_info").toJSONString());
            account.setUserName(authorizerInfo.getString("user_name"));
            account.setPrincipalName(authorizerInfo.getString("principal_name"));
            account.setBusinessInfo(authorizerInfo.getJSONObject("business_info").toJSONString());
            account.setAlias(authorizerInfo.getString("alias"));
            account.setQrcodeUrl(authorizerInfo.getString("qrcode_url"));
            account.setState(0);
            // 保存 数据和token
            boolean ifResult = weChatService.saveOrUpdateWechatAcc(account);
            weChatService.cacheAuthorizerAccessToken(account.getId(),authorizerAccessToken);
            // 重定向到 用户确认中介页面
            String param = ifResult?account.getId():null;
            response.sendRedirect(confirmDomain+"?accountId="+param);
        }catch (Exception e) {
            response.sendRedirect(confirmDomain+"?accountId=");
            logger.error("微信授权回调 ------->失败,authCode is :{},expiresIn:{},error message is :{}",authCode,expiresIn,e.getMessage());
        }
    }


    @PostMapping("/msgCallback/{appId}")
    @ApiOperation(value = "消息回调")
    public String receiveWeChatMessage (@PathVariable String appId,
                                        HttpServletRequest request) throws Exception {
        String nonce = request.getParameter("nonce");
        String timestamp = request.getParameter("timestamp");
        // 默认aes
        String encryptType = request.getParameter("encrypt_type");
        String msgSignature = request.getParameter("msg_signature");
        logger.info("接受微信事件回调,param参数依次是nonce:{},timestamp:{},encrypt_type:{},msg_signature:{}",nonce,
                timestamp,encryptType,msgSignature);
        request.setCharacterEncoding("UTF-8");
        String postData = readBody(request);
        logger.info("接受微信事件回调,postData before decry :{}",postData);
        postData = weChatService.decryptMsg(msgSignature,timestamp,nonce,postData,false);
        logger.info("接受微信事件回调,postData after decry :{}",postData);
        Map<String,String> xmlMap =  weChatService.xmlToMap(postData);
        String msgType = xmlMap.get("MsgType");
        Account account =  accountService.queryByAppId(appId);
        xmlMap.put("accountId",account.getId());
        WeChatMessageService messageService = WeChatMessageFactory.getService(msgType);
        if (Objects.isNull(messageService)) {
            logger.error("接受到消息暂无处理类处理：{}",msgType);
            return Constants.FAIL;
        }else {
            return messageService.processMessage(xmlMap);
        }
    }


    private String readBody (HttpServletRequest request) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(request.getInputStream());
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        int result = bis.read();
        while(result != -1) {
            buf.write((byte) result);
            result = bis.read();
        }
        return  buf.toString("utf-8");
    }

}
