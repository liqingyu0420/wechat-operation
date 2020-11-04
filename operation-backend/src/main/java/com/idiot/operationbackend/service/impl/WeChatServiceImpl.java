package com.idiot.operationbackend.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.idiot.operationbackend.config.PlatformProperties;
import com.idiot.operationbackend.entity.*;
import com.idiot.operationbackend.handler.FansInfoHandler;
import com.idiot.operationbackend.service.facade.*;
import com.idiot.operationbackend.support.Constants;
import com.idiot.operationbackend.support.CustomException;
import com.idiot.operationbackend.support.WxInputStreamResource;
import com.idiot.operationbackend.support.wechat.AesException;
import com.idiot.operationbackend.support.wechat.WXBizMsgCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * @author wang xiao
 * @date Created in 10:54 2020/9/11
 */
@Service
public class WeChatServiceImpl implements WeChatService, InitializingBean {


    private final Logger logger = LoggerFactory.getLogger(WeChatServiceImpl.class);


    private WXBizMsgCrypt pc;

    private String ticket;

    private final String ERROR_CODE = "errcode";

    private RestTemplate restTemplate;

    private PlatformProperties properties;

    private AccountService accountService;

    private AccountFansService fansService;

    private AccountTagService tagService;

    private FansInfoHandler fansInfoHandler;

    private GroupMsgService groupMsgService;

    private TemplateMsgService templateMsgService;

    private CustomerMsgService customerMsgService;

    private FansActionStatService fansActionStatService;

    private AccountPushService accountPushService;

    @Override
    public String notice(Map<String, String> param) {
        if (Objects.isNull(param)) {
            return Constants.SUCCESS;
        }
        String infoType = param.get("InfoType");
        String componentAppId = param.get("AppId");
        ticket = param.get("ComponentVerifyTicket");
        logger.info("接受微信验证票据数据,infoType:{},第三方平台appId:{},ticket:{}",infoType,componentAppId,ticket);
        return Constants.SUCCESS;
    }


    @Override
    public String decryptMsg(String msgSignature, String timestamp, String nonce, String data,boolean ticket)  {
        try {
            return pc.decryptMsg(msgSignature,timestamp,nonce,data,ticket);
        }catch (AesException e){
            logger.error("微信解密数据发生错误----->msgSignature:{},timestamp:{},nonce:{},data:{}",msgSignature,
                    timestamp,nonce,data);
            return null;
        }
    }

    @Override
    public String encryptMsg(String data) {
        String timestamp =  Long.toString(System.currentTimeMillis());
        String nonce = pc.getRandomStr();
        try {
            return  pc.encryptMsg(data,timestamp,nonce);
        }catch (AesException e){
            logger.error("微信加密数据发生错误----->timestamp:{},nonce:{},data:{}",timestamp,nonce,data);
            return null;
        }
    }

    @Override
    public void cacheAuthorizerAccessToken(String accountId, String authorizerAccessToken) {
        Constants.CACHE.put(String.format(Constants.AUTHORIZER_ACCESS_TOKEN,accountId),authorizerAccessToken);
    }

    @Override
    public boolean saveOrUpdateWechatAcc(Account account) {
        return accountService.saveOrUpdateAccount(account);
    }

    @Override
    public String getComponentAccessToken() {
        logger.info("获取第三方平台令牌 component_access_token start");
        String componentAppId = properties.getAppId();
        String componentAccessToken = Constants.CACHE.getIfPresent(String.format(Constants.COMPONENT_ACCESS_TOKEN,componentAppId));
        if ( !StringUtils.isEmpty(componentAccessToken)) {
            return componentAccessToken;
        }
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/component/api_component_token";
        HttpHeaders headers = new HttpHeaders();
        JSONObject params = new JSONObject();
        headers.setContentType(MediaType.APPLICATION_JSON);
        params.put("component_appid",componentAppId);
        params.put("component_appsecret",properties.getAppSecret());
        params.put("component_verify_ticket",ticket);
        String requestStr = params.toJSONString();
        logger.info("请求微信获取令牌component_access_token 第三方平台appId:{},ticket:{},请求参数:{}",componentAppId,ticket,requestStr);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestStr, headers);
        ResponseEntity<String> response = restTemplate.exchange(requestUrl, HttpMethod.POST, requestEntity, String.class);
        String  jsonStr = response.getBody();
        logger.info("请求微信获取令牌component_access_token 第三方平台appId:{},ticket:{},微信返回结果:{}",componentAppId,ticket,jsonStr);
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        componentAccessToken = jsonObject.getString("component_access_token");
        Constants.CACHE.put(String.format(Constants.COMPONENT_ACCESS_TOKEN,componentAppId),componentAccessToken);
        return componentAccessToken;
    }

    @Override
    public String getAuthorizerAccessToken(String accountId) {
        String authorizerAccessToken = Constants.CACHE.getIfPresent(String.format(Constants.AUTHORIZER_ACCESS_TOKEN,accountId));
        if ( !StringUtils.isEmpty(authorizerAccessToken)) {
            return authorizerAccessToken;
        }
        Account account = accountService.getById(accountId);
        if (Objects.isNull(account)) {
            logger.error("获取授权方令牌 authorizer_access_token 发生异常===> 公众号id:{}为空",accountId);
            return null;
        }
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/component/api_authorizer_token?component_access_token=COMPONENT_TOKEN";
        String componentAccessToken = getComponentAccessToken();
        String componentAppId = properties.getAppId();
        requestUrl = requestUrl.replace("COMPONENT_TOKEN",componentAccessToken);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject params = new JSONObject(4);
        params.put("component_appid",componentAppId);
        params.put("component_access_token",componentAccessToken);
        params.put("authorizer_appid",account.getAuthorizerAppId());
        params.put("authorizer_refresh_token",account.getAuthorizerRefreshToken());
        String requestStr = params.toJSONString();
        HttpEntity<String> requestEntity = new HttpEntity<String>(requestStr, headers);
        logger.info("获取授权方令牌 authorizer_access_token 第三方平台appId:{},公众号id:{},授权方appId:{},请求参数:{}",
                componentAppId,accountId,account.getAuthorizerAppId(),requestStr);
        ResponseEntity<String> response = restTemplate.exchange(requestUrl, HttpMethod.POST, requestEntity, String.class);
        String  jsonStr = response.getBody();
        logger.info("获取授权方令牌 authorizer_access_token 第三方平台appId:{},公众号id:{},授权方appId:{},微信返回结果:{}",
                componentAppId,accountId,account.getAuthorizerAppId(),jsonStr);
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        authorizerAccessToken = jsonObject.getString("authorizer_access_token");
        String authorizerRefreshToken  = jsonObject.getString("authorizer_refresh_token");
        account.setAuthorizerAccessToken(authorizerAccessToken);
        account.setAuthorizerRefreshToken(authorizerRefreshToken);
        Constants.CACHE.put(String.format(Constants.AUTHORIZER_ACCESS_TOKEN,accountId),authorizerAccessToken);
        accountService.saveOrUpdate(account);
        return authorizerAccessToken;
    }

    @Override
    public String getPreAuthCode() {
        String componentAppId = properties.getAppId();
        logger.info("请求微信获取预授权码 第三方平台appId:{} start",componentAppId);
        String requestUrl =  "https://api.weixin.qq.com/cgi-bin/component/api_create_preauthcode?component_access_token=COMPONENT_ACCESS_TOKEN";
        String componentAccessToken = getComponentAccessToken();
        requestUrl = requestUrl.replace("COMPONENT_ACCESS_TOKEN",componentAccessToken);
        HttpHeaders headers = new HttpHeaders();
        JSONObject params = new JSONObject(1);
        params.put("component_appid",componentAppId);
        headers.setContentType(MediaType.APPLICATION_JSON);
        String requestStr = params.toJSONString();
        HttpEntity<String> requestEntity = new HttpEntity<>(requestStr, headers);
        logger.info("请求微信获取预授权码 第三方平台appId:{},请求参数:{}",componentAppId,requestStr);
        ResponseEntity<String> response = restTemplate.exchange(requestUrl, HttpMethod.POST, requestEntity, String.class);
        String  jsonStr = response.getBody();
        logger.info("请求微信获取预授权码 第三方平台appId:{},微信返回结果:{}",componentAppId,jsonStr);
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        return jsonObject.getString("pre_auth_code");
    }

    @Override
    public String getPreAuthUrl() {
        String preAuthUrl = "https://mp.weixin.qq.com/cgi-bin/componentloginpage?component_appid=APPID&pre_auth_code=PREAUTHCODE&redirect_uri=URI&auth_type=1";
        String componentAppId = properties.getAppId();
        String preAuthCode = getPreAuthCode();
        preAuthUrl = preAuthUrl.replace("APPID",componentAppId)
                .replace("PREAUTHCODE",preAuthCode)
                .replace("URI",properties.getAuthCallBack());
        logger.info("生成预授权地址, preAuthUrl is {}",preAuthUrl);
        return preAuthUrl;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("WeChatService类--------------->正在初始化");
        pc = new WXBizMsgCrypt(properties.getToken(), properties.getSecret(), properties.getAppId());
        logger.info("cache size  is {}",Constants.CACHE.size());
        logger.info("WeChatService类--------------->初始化完成");
    }


    @Override
    public String getAuthorizationInfo(String authCode) {
        String componentAppId = properties.getAppId();
        logger.info("请求微信获取授权信息 第三方平台appId:{} start",componentAppId);
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/component/api_query_auth?component_access_token=COMPONENT_TOKEN";
        String componentToken = getComponentAccessToken();
        requestUrl = requestUrl.replace("COMPONENT_TOKEN",componentToken);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject param = new JSONObject(2);
        param.put("component_appid",componentAppId);
        param.put("authorization_code",authCode);
        String requestStr = param.toJSONString();
        HttpEntity<String>  entity = new HttpEntity<>(requestStr,headers);
        logger.info("请求微信获取授权信息 第三方平台appId:{},请求参数:{}",componentAppId,requestStr);
        ResponseEntity<String> response = restTemplate.exchange(requestUrl,HttpMethod.POST,entity,String.class);
        String jsonStr = response.getBody();
        logger.info("请求微信获取授权信息 第三方平台appId:{},微信返回结果:{}",componentAppId,jsonStr);
        return jsonStr;
    }

    @Override
    public String getAuthorizerInfo(String authorizerAppId) {
        String componentAppId = properties.getAppId();
        logger.info("请求微信获取授权方信息 第三方平台appId:{},授权方appId:{},start",componentAppId,authorizerAppId);
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/component/api_get_authorizer_info?component_access_token=COMPONENT_TOKEN";
        String componentToken = getComponentAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        requestUrl = requestUrl.replace("COMPONENT_TOKEN",componentToken);
        JSONObject params = new JSONObject(4);
        params.put("component_access_token",componentToken);
        params.put("component_appid",componentAppId);
        params.put("authorizer_appid",authorizerAppId);
        String requestStr = params.toJSONString();
        HttpEntity<String>  entity = new HttpEntity<> (requestStr,headers);
        logger.info("请求微信获取授权方信息 第三方平台appId:{},授权方appId:{},请求参数:{}",componentAppId,authorizerAppId,requestStr);
        ResponseEntity<String> response = restTemplate.exchange(requestUrl,HttpMethod.POST,entity,String.class);
        String jsonStr = response.getBody();
        logger.info("请求微信获取授权方信息 第三方平台appId:{},授权方appId:{},微信返回结果:{}",componentAppId,authorizerAppId,jsonStr);
        return jsonStr;
    }


    @Override
    public int syncAccountUser(String accountId) {

        String lockKey = String.format(Constants.LOCK_SYNC_USER,accountId);
        String lockValue = Constants.LOCK_CACHE.getIfPresent(lockKey);
        if (!StringUtils.isEmpty(lockValue)) {
            throw new CustomException(500,"当前公众号正在后台同步粉丝数据,请您稍等一会!");
        }
        Constants.LOCK_CACHE.put(lockKey,lockKey);
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=%s&next_openid=%s";
        String nextOpenId = "";
        String accessToken = getAuthorizerAccessToken(accountId);
        int count = 0;
        int total ;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<MultiValueMap<String, String>>  entity = new HttpEntity<> (headers);
        String jsonStr = null;
        JSONObject respJsonBody = null;
        do {
            if (StringUtils.isEmpty(nextOpenId)) {
                nextOpenId = "";
            }
            Constants.LOCK_CACHE.put(lockKey,lockKey);
            requestUrl = String.format(requestUrl,accessToken,nextOpenId);
            logger.info("公众号：{}同步粉丝数据---- start,时间:{}",accountId, LocalDateTime.now());
            ResponseEntity<String> responseEntity = restTemplate.exchange(requestUrl,HttpMethod.GET,entity,String.class);
            jsonStr = responseEntity.getBody();
            logger.info("公众号：{}同步粉丝数据---- end,时间:{},微信返回:{}",accountId, LocalDateTime.now(),jsonStr);
            respJsonBody = JSONObject.parseObject(jsonStr);
            total = respJsonBody.getIntValue("total");
            count += respJsonBody.getIntValue("count");
            nextOpenId = respJsonBody.getString("next_openid");
            JSONArray openIdArray = respJsonBody.getJSONObject("data").getJSONArray("openid");
            List<String> openIds = JSONArray.parseArray(openIdArray.toJSONString(),String.class);
            fansInfoHandler.doSyncUserTask(accountId,openIds);
        }while (count <= total);
        Constants.LOCK_CACHE.invalidate(lockKey);
        logger.info("公众号：{}同步粉丝数据----end,时间:{}",accountId, LocalDateTime.now());
        return count;
    }


    @Override
    @Async("asyncExecutor")
    public void syncUserTask(String accountId, List<String> openIds) {

        if (CollectionUtils.isEmpty(openIds)) {
            return;
        }
        int size = openIds.size();
        logger.info("公众号：{}同步粉丝请求粉丝信息,openId 大小:{}---- start,时间:{}",accountId,size,LocalDateTime.now());
        List<AccountFans> accountFans =  new ArrayList<>(size);

        for (String openId : openIds) {
            AccountFans fans = fansService.queryByAccountIdAndOpenId(accountId,openId);
            if (fans == null) {
                fans = new AccountFans();
                JSONObject fansObject = JSON.parseObject(getFansInfo(accountId,openId));
                fans.setAccountId(accountId);
                fans.setOpenId(openId);
                fans.setNickName(fansObject.getString("nickname"));
                fans.setHeadImgUrl(fansObject.getString("headimgurl"));
                fans.setSex(fansObject.getInteger("sex"));
                fans.setSubscribe(fansObject.getInteger("subscribe"));
                fans.setCity(fansObject.getString("city"));
                fans.setProvince(fansObject.getString("province"));
                fans.setSubscribeTime(fansObject.getLong("subscribe_time"));
                fans.setSubscribeScene(fansObject.getString("subscribe_scene"));
                fans.setUnionId(fansObject.getString("unionid"));
                fans.setRemark(fansObject.getString("remark"));
                fans.setGroupId(fansObject.getInteger("groupid"));
                fans.setTagIdList(JSONObject.toJSONString(fansObject.getJSONArray("tagid_list")));
                fans.setTags(fans.initTags());
                fans.setState(true);
                fans.setCreateTime(LocalDateTime.now().toEpochSecond(Constants.DEFAULT_ZONE));
                accountFans.add(fans);
            }
        }
        logger.info("公众号：{}同步粉丝请求粉丝信息,openId 大小:{}---- end,时间:{}",accountId,size,LocalDateTime.now());
        //  保存用户
        fansService.saveOrUpdateBatch(accountFans,1000);
    }


    @Override
    @Async("asyncExecutor")
    public void syncTag(String accountId) {

        String lockKey = String.format(Constants.LOCK_SYNC_TAG,accountId);
        String cacheValue = Constants.LOCK_CACHE.getIfPresent(lockKey);
        if (!StringUtils.isEmpty(cacheValue)) {
            throw new CustomException(500,"");
        }
        Constants.LOCK_CACHE.put(lockKey,lockKey);
        logger.info("公众号：{}同步标签 ---- start,时间:{}",accountId,LocalDateTime.now());
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/tags/get?access_token=%s";
        String accessToken =  getAuthorizerAccessToken(accountId);
        requestUrl = String.format(requestUrl,accessToken);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<MultiValueMap<String, String>>  entity = new HttpEntity<> (headers);
        ResponseEntity<String> respStr = restTemplate.exchange(requestUrl,HttpMethod.GET,entity,String.class);
        String respBody = respStr.getBody();
        logger.info("公众号：{}同步标签 ---- end,时间:{},微信返回:{}",accountId,LocalDateTime.now(),respBody);
        JSONArray jsonArray = JSONObject.parseObject(respBody).getJSONArray("tags");
        int size = jsonArray.size();
        List<AccountTag> accountTags = new ArrayList<>(size);
        JSONObject temp = null;
        AccountTag tempTag = null;
        String createTime  = Constants.DATE_TIME_FORMATTER.format(LocalDateTime.now());
        for (int i = 0; i < size; i++) {
            temp = jsonArray.getJSONObject(i);
            tempTag = new AccountTag();
            tempTag.setAccountId(accountId);
            tempTag.setFansCount(temp.getInteger("count"));
            tempTag.setName(temp.getString("name"));
            tempTag.setWxId(temp.getInteger("id"));
            tempTag.setCreateTime(createTime);
            accountTags.add(tempTag);
        }
        logger.info("公众号：{}同步标签 ---- end,时间:{}",accountId,LocalDateTime.now());
        // 新增 多余的 删掉缺少的 俗称覆盖
        boolean ifOverlay = tagService.overlayAccountTag(accountId,accountTags);
        Constants.LOCK_CACHE.invalidate(lockKey);

    }

    @Override
    public String getFansInfo(String accountId, String openId) {
        logger.info("查询粉丝信息，openId：{}----start,时间:{}",openId, LocalDateTime.now());
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s&lang=zh_CN";
        String accessToken =  getAuthorizerAccessToken(accountId);
        requestUrl = String.format(requestUrl,accessToken,openId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<MultiValueMap<String, String>>  entity = new HttpEntity<> (headers);
        ResponseEntity<String> respStr = restTemplate.exchange(requestUrl,HttpMethod.GET,entity,String.class);
        String jsonStr = respStr.getBody();
        logger.info("查询粉丝信息，openId：{}----end,时间:{},微信返回:{}",openId, LocalDateTime.now(),jsonStr);
        return jsonStr;
    }


    @Override
    public boolean confirmAccount(String accountId, String userId) {
        return accountService.updateUserIdByAccount(accountId,userId);
    }

    @Override
    public String getFansSummary(String accountId, String startDate, String endDate) {
        String requestUrl = "https://api.weixin.qq.com/datacube/getusersummary?access_token=%s";
        String accessToken = getAuthorizerAccessToken(accountId);
        requestUrl = String.format(requestUrl,accessToken);
        logger.info("获取微信用户增减数据，accountId：{}----start,时间:{}",accountId, LocalDateTime.now());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject params = new JSONObject(2);
        params.put("begin_date",startDate);
        params.put("end_date",endDate);
        String requestStr = params.toJSONString();
        HttpEntity<String>  entity = new HttpEntity<> (requestStr,headers);
        logger.info("获取微信用户增减数据，accountId：{}----end,时间:{},请求参数{}",accountId,
                LocalDateTime.now(),requestStr);
        ResponseEntity<String> respStr = restTemplate.exchange(requestUrl,HttpMethod.POST,entity,String.class);
        String jsonStr = respStr.getBody();
        logger.info("获取微信用户增减数据，accountId：{}----end,时间:{},微信返回{}",accountId,
                LocalDateTime.now(),jsonStr);
        return jsonStr;
    }


    @Override
    public String getFansCumulate(String accountId, String startDate, String endDate) {
        String requestUrl = "https://api.weixin.qq.com/datacube/getusercumulate?access_token=%s";
        String accessToken = getAuthorizerAccessToken(accountId);
        requestUrl = String.format(requestUrl,accessToken);
        logger.info("获取微信用户汇总数据，accountId：{}----start,时间:{}",accountId, LocalDateTime.now());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject params = new JSONObject(2);
        params.put("begin_date",startDate);
        params.put("end_date",endDate);
        String requestStr = params.toJSONString();
        HttpEntity<String>  entity = new HttpEntity<> (requestStr,headers);
        logger.info("获取微信用户汇总数据，accountId：{}----end,时间:{},请求参数{}",accountId,
                LocalDateTime.now(),requestStr);
        ResponseEntity<String> respStr = restTemplate.exchange(requestUrl,HttpMethod.POST,entity,String.class);
        String jsonStr = respStr.getBody();
        logger.info("获取微信用户汇总数据，accountId：{}----end,时间:{},微信返回{}",accountId,
                LocalDateTime.now(),jsonStr);
        return jsonStr;
    }

    @Override
    public String getArticleSummary(String accountId, String startDate, String endDate) {
        String requestUrl = "https://api.weixin.qq.com/datacube/getarticlesummary?access_token=%s";
        String accessToken = getAuthorizerAccessToken(accountId);
        requestUrl = String.format(requestUrl,accessToken);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        logger.info("获取微信图文阅读数据，accountId：{}----start,时间:{}",accountId, LocalDateTime.now());
        JSONObject params = new JSONObject(2);
        params.put("begin_date",startDate);
        params.put("end_date",endDate);
        String requestStr = params.toJSONString();
        HttpEntity<String>  entity = new HttpEntity<> (requestStr,headers);
        logger.info("获取微信图文阅读数据，accountId：{}----end,时间:{},请求参数{}",accountId,
                LocalDateTime.now(),requestStr);
        ResponseEntity<String> respStr = restTemplate.exchange(requestUrl,HttpMethod.POST,entity,String.class);
        String jsonStr = respStr.getBody();
        logger.info("获取微信图文阅读数据，accountId：{}----end,时间:{},微信返回{}",accountId,
                LocalDateTime.now(),jsonStr);
        return jsonStr;
    }

    @Override
    public boolean updateRemark(String accountId, String openId, String remark) {
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/user/info/updateremark?access_token=%s";
        String accessToken = getAuthorizerAccessToken(accountId);
        requestUrl = String.format(requestUrl,accessToken);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        logger.info("微信设置备注，accountId：{}----start,时间:{}",accountId, LocalDateTime.now());
        JSONObject params = new JSONObject(2);
        params.put("openid",openId);
        params.put("remark",remark);
        String requestStr = params.toJSONString();
        HttpEntity<String>  entity = new HttpEntity<> (requestStr,headers);
        logger.info("微信设置备注，accountId：{}----end,时间:{},请求参数{}",accountId,LocalDateTime.now(),requestStr);
        ResponseEntity<String> respStr = restTemplate.exchange(requestUrl,HttpMethod.POST,entity,String.class);
        String jsonStr = respStr.getBody();
        logger.info("微信设置备注，accountId：{}----end,时间:{},微信返回{}",accountId,LocalDateTime.now(),jsonStr);
        if (0 ==JSONObject.parseObject(jsonStr).getIntValue(ERROR_CODE)) {
            return fansService.updateFansRemark(accountId,openId,remark);
        }
        return false;
    }

    @Override
    public boolean createTag(String accountId, String tagLabel) {

        String requestUrl = "https://api.weixin.qq.com/cgi-bin/tags/create?access_token=%s";
        String accessToken = getAuthorizerAccessToken(accountId);
        requestUrl =  String.format(requestUrl,accessToken);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject params = new JSONObject(2);
        HashMap<String,String> map = new HashMap<>(2);
        map.put("name",tagLabel);
        params.put("tag",map);
        String requestStr = params.toJSONString();
        HttpEntity<String>  entity = new HttpEntity<> (requestStr,headers);
        logger.info("微信创建标签，accountId：{}----start,时间:{},请求参数:{}",accountId, LocalDateTime.now(),requestStr);
        ResponseEntity<String> respStr = restTemplate.exchange(requestUrl,HttpMethod.POST,entity,String.class);
        String jsonStr = respStr.getBody();
        logger.info("微信创建标签，accountId：{}----end,时间:{},微信返回{}",accountId,
                LocalDateTime.now(),jsonStr);
        JSONObject temp = JSONObject.parseObject(jsonStr).getJSONObject("tag");
        if (null != temp) {
            Integer wxId = temp.getInteger("id");
            return tagService.addTag(tagLabel,accountId,wxId);
        }
        return false;
    }


    @Override
    public boolean delTag(String accountId, Integer wxId) {
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/tags/delete?access_token=%s";
        String accessToken = getAuthorizerAccessToken(accountId);
        requestUrl =  String.format(requestUrl,accessToken);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject params = new JSONObject(2);
        HashMap<String,Object> map = new HashMap<>(2);
        map.put("id",wxId);
        params.put("tag",map);
        String requestStr = params.toJSONString();
        HttpEntity<String>  entity = new HttpEntity<> (requestStr,headers);
        logger.info("微信删除标签，accountId：{},wxId:{}----start,时间:{},请求参数:{}",accountId,wxId,
                LocalDateTime.now(),requestStr);
        ResponseEntity<String> respStr = restTemplate.exchange(requestUrl,HttpMethod.POST,entity,String.class);
        String jsonStr = respStr.getBody();
        logger.info("微信删除标签，accountId：{},wxId:{}----end,时间:{},微信返回{}",accountId,wxId,
                LocalDateTime.now(),jsonStr);
        if (0 ==JSONObject.parseObject(jsonStr).getIntValue(ERROR_CODE)) {
            return tagService.removeTag(accountId,wxId);
        }
        return false;
    }

    @Override
    public boolean addUserTag(String accountId, List<String> openIds, int wxId) {
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/tags/members/batchtagging?access_token=%s";
        String accessToken = getAuthorizerAccessToken(accountId);
        requestUrl =  String.format(requestUrl,accessToken);

        JSONObject params = new JSONObject(2);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        params.put("openid_list",openIds);
        params.put("tagid",wxId);
        int size = openIds.size();
        String requestStr = params.toJSONString();
        HttpEntity<String>  entity = new HttpEntity<> (requestStr,headers);
        logger.info("微信用户打标签，accountId：{},opedId size:{}----start,时间:{},请求参数:{}",accountId,size,
                LocalDateTime.now(),requestStr);
        ResponseEntity<String> respStr = restTemplate.exchange(requestUrl,HttpMethod.POST,entity,String.class);
        String jsonStr = respStr.getBody();
        logger.info("微信用户打标签，accountId：{},opedId size:{}----end,时间:{},微信返回：{}",accountId,size,
                LocalDateTime.now(),jsonStr);
        if (0 ==JSONObject.parseObject(jsonStr).getIntValue(ERROR_CODE)) {
            return tagService.addTagSize(accountId,wxId,size);
        }
        return false;
    }


    @Override
    public boolean removeUserTag(String accountId, List<String> openIds, int wxId) {
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/tags/members/batchuntagging?access_token=%s";
        String accessToken = getAuthorizerAccessToken(accountId);
        requestUrl =  String.format(requestUrl,accessToken);
        JSONObject params = new JSONObject(2);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        params.put("openid_list",openIds);
        params.put("tagid",wxId);
        int size = openIds.size();
        String requestStr = params.toJSONString();
        HttpEntity<String>  entity = new HttpEntity<> (requestStr,headers);
        logger.info("微信用户去除标签，accountId：{},opedId size:{}----start,时间:{},请求参数：{}",accountId,size,
                LocalDateTime.now(),requestStr);
        ResponseEntity<String> respStr = restTemplate.exchange(requestUrl,HttpMethod.POST,entity,String.class);
        String jsonStr = respStr.getBody();
        logger.info("微信用户去除标签，accountId：{},opedId size:{}----end,时间:{},微信返回：{}",accountId,size,
                LocalDateTime.now(),jsonStr);
        if (0 ==JSONObject.parseObject(jsonStr).getIntValue(ERROR_CODE)) {
            return tagService.subTagSize(accountId,wxId,size);
        }
        return true;
    }


    @Override
    public boolean upTag(String accountId, int wxId, String name) {

        String requestUrl = "https://api.weixin.qq.com/cgi-bin/tags/update?access_token=ACCESS_TOKEN";
        String accessToken = getAuthorizerAccessToken(accountId);
        requestUrl =  String.format(requestUrl,accessToken);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject params = new JSONObject(2);
        JSONObject  map = new JSONObject(2);
        map.put("name",name);
        map.put("id",wxId);
        params.put("tag",map);
        String requestStr = params.toJSONString();
        HttpEntity<String>  entity = new HttpEntity<> (requestStr,headers);
        logger.info("微信创建标签，accountId：{}----start,时间:{},请求参数:{}",accountId, LocalDateTime.now(),requestStr);
        ResponseEntity<String> respStr = restTemplate.exchange(requestUrl,HttpMethod.POST,entity,String.class);
        String jsonStr = respStr.getBody();
        logger.info("微信创建标签，accountId：{}----end,时间:{},微信返回{}",accountId,LocalDateTime.now(),jsonStr);
        int errorCode = JSONObject.parseObject(jsonStr).getIntValue(ERROR_CODE);
        return 0 == errorCode;
    }


    @Override
    public String addMaterial(String accountId, String type, WxInputStreamResource resource,String title, String introduction) {
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=%s&type=%s";
        String accessToken = getAuthorizerAccessToken(accountId);
        requestUrl = String.format(requestUrl,accessToken,type);
        MultiValueMap<String, Object> data = new LinkedMultiValueMap<>();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        data.add("media", resource);
        if (Constants.VIDEO.equals(type)) {
            JSONObject p = new JSONObject();
            p.put("title", title);
            p.put("introduction", introduction);
            data.add("description", p.toString());
        }
        HttpEntity<MultiValueMap<String,Object>> requestEntity = new HttpEntity<>(data,httpHeaders);
        logger.info("微信上传素材，accountId:{},type:{}----start,时间:{}",accountId,type, LocalDateTime.now());
        String resultJson = restTemplate.postForObject(requestUrl, requestEntity, String.class);
        logger.info("微信上传素材，accountId:{},type:{}----end,时间:{},微信返回结果:{}",accountId,type,
                LocalDateTime.now(),resultJson);
        return resultJson;
    }

    @Override
    public String upImage(String accountId, WxInputStreamResource resource) {
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/media/uploadimg?access_token=%s";
        String accessToken = getAuthorizerAccessToken(accountId);
        requestUrl = String.format(requestUrl,accessToken);
        MultiValueMap<String, Object> data = new LinkedMultiValueMap<>();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        data.add("media", resource);
        String requestStr = JSONObject.toJSONString(data);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestStr,httpHeaders);
        logger.info("微信上传图片，accountId:{},type:{}----start,时间:{}",accountId, LocalDateTime.now(),requestStr);
        String resultJson = restTemplate.postForObject(requestUrl, requestEntity, String.class);
        logger.info("微信上传传图片，accountId:{},type:{}----end,时间:{}",accountId, LocalDateTime.now(),resultJson);
        return  JSONObject.parseObject(resultJson).getString("url");
    }

    @Override
    public String addNews(List<Articles> articles, String accountId) {
        String requestUrl =  "https://api.weixin.qq.com/cgi-bin/material/add_news?access_token=%s";
        String accessToken = getAuthorizerAccessToken(accountId);
        requestUrl = String.format(requestUrl,accessToken);
        MultiValueMap<String,Object> valueMap = new LinkedMultiValueMap<>();
        JSONArray jsonArray = new JSONArray(articles.size());
        for (Articles article : articles) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title",article.getTitle());
            jsonObject.put("thumb_media_id",article.getThumbMediaId());
            jsonObject.put("show_cover_pic",article.getShowCoverPic());
            jsonObject.put("content",article.getContent());
            jsonObject.put("content_source_url",article.getContentSourceUrl());
            jsonArray.add(jsonObject);
        }
        valueMap.add("articles",jsonArray.toJSONString());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        String requestStr = JSONObject.toJSONString(valueMap);
        HttpEntity<String> httpEntity = new HttpEntity<>(requestStr,httpHeaders);
        logger.info("微信上传图文素材，accountId:{},articles:{}----start,时间:{},请求参数:{}",accountId,articles.toString(),
                LocalDateTime.now(),requestStr);
        String respJson = restTemplate.postForObject(requestUrl,httpEntity,String.class);
        logger.info("微信上传图文素材，accountId:{},articles:{}----end,时间:{},微信返回:{}",accountId,articles.toString(),
                LocalDateTime.now(),respJson);
        return respJson;
    }

    @Override
    public String createQrCode(QrCode qrCode, String accountId) {
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=%s";
        int type = qrCode.getType();
        JSONObject parentNode  = new JSONObject();
        String actionName = "";
        if (type == 0){
            actionName = "QR_STR_SCENE";
            parentNode.put("expire_seconds",2592000);
        }else {
            actionName = "QR_LIMIT_STR_SCENE";
        }
        parentNode.put("action_name",actionName);
        JSONObject childNode = new JSONObject();
        childNode.put("scene_str",qrCode.getId());
        JSONObject sceneNode  = new JSONObject();
        sceneNode.put("scene",childNode);
        parentNode.put("action_info",sceneNode);
        String accessToken = getAuthorizerAccessToken(accountId);
        requestUrl = String.format(requestUrl,accessToken);
        HttpHeaders  headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String requestStr = parentNode.toJSONString();
        HttpEntity<String> request = new HttpEntity<String>(requestStr, headers);
        logger.info("微信上生成二维码，accountId:{},QrCode:{}----start,时间:{},请求参数:{}",accountId,qrCode.toString(),
                LocalDateTime.now(),requestStr);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(requestUrl,request,String.class);
        String jsonStr = responseEntity.getBody();
        logger.info("微信上生成二维码，accountId:{},QrCode:{}----end,时间:{},微信返回：{}",accountId,qrCode.toString(),
                LocalDateTime.now(),jsonStr);
        return jsonStr;
    }


    @Override
    public boolean createMenu(List<AccountMenu> accountMenus, String accountId) {
        if (CollectionUtils.isEmpty(accountMenus)) {
            return false;
        }
        String param = parseToJson(accountMenus);
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=%s";
        String accessToken = getAuthorizerAccessToken(accountId);
        requestUrl = String.format(requestUrl,accessToken);
        HttpHeaders  headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<String>(param, headers);
        logger.info("微信创建公众号菜单，accountId:{},accountMenus:{}----start,时间:{},请求参数:{}",accountId,param,
                LocalDateTime.now(),param);
        String jsonStr = restTemplate.postForObject(requestUrl,request,String.class);
        logger.info("微信创建公众号菜单，accountId:{},accountMenus:{}----end,时间:{},微信返回:{}",accountId,param,
                LocalDateTime.now(),jsonStr);
        int errcode = JSONObject.parseObject(jsonStr).getIntValue(ERROR_CODE);
        return 0 == errcode;
    }


    @Override
    public boolean delMenu(String accountId) {
        String url = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=%s";
        String accessToken = getAuthorizerAccessToken(accountId);
        url = String.format(url,accessToken);
        logger.info("微信删除公众号菜单，accountId:{}----start,时间:{}",accountId,LocalDateTime.now());
        String jsonStr = restTemplate.getForObject(url,String.class);
        logger.info("微信创建公众号菜单，accountId:{}----end,时间:{},微信返回:{}",accountId,LocalDateTime.now(),jsonStr);
        int errcode = JSONObject.parseObject(jsonStr).getIntValue(ERROR_CODE);
        return 0 == errcode;
    }


    @Override
    public String getTemplate(String accountId) {
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/template/get_all_private_template?access_token=%s";
        String accessToken = getAuthorizerAccessToken(accountId);
        requestUrl = String.format(requestUrl,accessToken);
        logger.info("微信请求公众号模板消息列表，accountId:{}----start,时间:{}",accountId,LocalDateTime.now());
        String jsonStr = restTemplate.getForObject(requestUrl,String.class);
        logger.info("微信请求公众号模板消息列表，accountId:{}----end,时间:{},微信返回:{}",accountId,LocalDateTime.now(),jsonStr);
        return jsonStr;
    }


    @Override
    public void doGroupMsgTask(GroupMsg groupMsg) {

        if (Objects.isNull(groupMsg)) {
            return ;
        }
        logger.info("微信公众号群发消息任务，groupMsg:{}----start,时间:{}",groupMsg.toString(),LocalDateTime.now());
        List<String> openIds = null;
        String accountId = groupMsg.getAccountId();
        Integer sex = null;
        String province = null;
        String city = null;
        String tag = null;
        String subscribeTime = null;
        if (!groupMsg.getType()) {
            sex = groupMsg.getSelectSex();
            province = groupMsg.getSelectProvince();
            city = groupMsg.getSelectCity();
            tag = groupMsg.getSelectTag();
            subscribeTime = groupMsg.getSelectSubscribeTime();
            List<AccountFans> fans = fansService.queryAccountFans(accountId,sex,province,city,tag,subscribeTime);
            openIds = fans.stream().map(AccountFans::getOpenId).collect(Collectors.toList());
        }
        long count = sendGroupMessage(accountId,groupMsg.getContent(),groupMsg.getType(),
                groupMsg.getRepeatSend(),openIds, groupMsg.getMsgType());
        groupMsg.setStatus(count>0?Constants.SUCCESSED:Constants.FAILED);
        groupMsg.setSendNum(count);
        groupMsgService.updateById(groupMsg);

        logger.info("微信公众号群发消息任务，groupMsg:{}----end,时间:{}",groupMsg.toString(),LocalDateTime.now());
    }


    @Override
    public void doTemplateMsgTask(TemplateMsg templateMsg) {
        if (Objects.isNull(templateMsg)) {
            return;
        }
        logger.info("微信公众号模板消息任务，templateMsg:{}----start,时间:{}",templateMsg.toString(),LocalDateTime.now());
        String accountId = templateMsg.getAccountId();
        Integer sex = templateMsg.getSelectSex();
        String province = templateMsg.getSelectProvince();
        String city = templateMsg.getSelectCity();
        String tag = templateMsg.getSelectTag();
        String subscribeTime = templateMsg.getSelectSubscribeTime();
        List<AccountFans> fans = fansService.queryAccountFans(accountId,sex,province,city,tag,subscribeTime);
        long count = sendTemplateMessage(accountId,fans,templateMsg);
        templateMsg.setStatus(count>0?Constants.SUCCESSED:Constants.FAILED);
        templateMsg.setSendNum(count);
        templateMsgService.updateById(templateMsg);
        logger.info("微信公众号模板消息任务，templateMsg:{}----end,时间:{}",templateMsg.toString(),LocalDateTime.now());
    }


    @Override
    public void doCustomerMsgTask(CustomerMsg customerMsg) {
        if (Objects.isNull(customerMsg)) {
            return;
        }
        logger.info("微信公众号客服消息任务，customerMsg:{}----start,时间:{}",customerMsg.toString(),LocalDateTime.now());
        String contents = customerMsg.getContent();
        Integer sex = customerMsg.getSelectSex();
        String province = customerMsg.getSelectProvince();
        String city = customerMsg.getSelectCity();
        String tag = customerMsg.getSelectTag();
        String subscribeTime = customerMsg.getSelectSubscribeTime();
        String accountId = customerMsg.getAccountId();
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=%s";
        List<AccountFans> fans = fansService.queryAccountFans(accountId,sex,province,city,tag,subscribeTime);
        long count = sendCustomerMessage(accountId,fans,contents,requestUrl);
        customerMsg.setStatus(count>0?Constants.SUCCESSED:Constants.FAILED);
        customerMsg.setSuccessNum(count);
        customerMsgService.updateById(customerMsg);
        logger.info("微信公众号客服消息任务，customerMsg:{}----end,时间:{}",customerMsg.toString(),LocalDateTime.now());
    }


    @Override
    public void processScanQrCode(String accountId, String openId, String qrCodeId, String contents) {
        /**
         * 先保存新用户
         */
        AccountFans accountFans = fansService.queryByAccountIdAndOpenId(accountId,openId);
        if (Objects.isNull(accountFans)){
            accountFans = new AccountFans();
            JSONObject fansObject = JSON.parseObject(getFansInfo(accountId,openId));
            accountFans.setAccountId(accountId);
            accountFans.setOpenId(openId);
            accountFans.setNickName(fansObject.getString("nickname"));
            accountFans.setHeadImgUrl(fansObject.getString("headimgurl"));
            accountFans.setSex(fansObject.getInteger("sex"));
            accountFans.setSubscribe(fansObject.getInteger("subscribe"));
            accountFans.setCity(fansObject.getString("city"));
            accountFans.setProvince(fansObject.getString("province"));
            accountFans.setSubscribeTime(fansObject.getLong("subscribe_time"));
            accountFans.setSubscribeScene(fansObject.getString("subscribe_scene"));
            accountFans.setUnionId(fansObject.getString("unionid"));
            accountFans.setRemark(fansObject.getString("remark"));
            accountFans.setGroupId(fansObject.getInteger("groupid"));
            accountFans.setTagIdList(JSONObject.toJSONString(fansObject.getJSONArray("tagid_list")));
            accountFans.setTags(accountFans.initTags());
            accountFans.setState(true);
            fansService.save(accountFans);
        }
        FansActionStat var1 = new FansActionStat(accountId,openId,1);
        FansActionStat var2 = new FansActionStat(accountId,openId,4);
        fansActionStatService.saveBatchFansActionStat(var1,var2);
        String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=%s";
        sendCustomerMessage(openId,contents,accountFans.getNickName(),accountId,url);
    }


    @Override
    public void sendMessage(String accountId, String openId, String nikeName, String content,int pushType) {
        content = content.replace("<fans.nickname>",nikeName);
        JSONArray jsonArray = JSONArray.parseArray(content);
        int size = jsonArray.size();
        if (size == 0) {
            return;
        }
        JSONObject temp  = null;
        if (2==pushType){
            int target = ThreadLocalRandom.current().nextInt(0,size-1);
            temp = jsonArray.getJSONObject(target);
            sendCustomerMsg(accountId,openId,nikeName,temp);
        }else {
            for (int i = 0; i < size; i++) {
                temp = jsonArray.getJSONObject(i);
                sendCustomerMsg(accountId,openId,nikeName,temp);
            }
        }

    }

    private void sendCustomerMsg(String accountId,String openId,String nickName,JSONObject jsonObject) {
        String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=%s";
        String accessToken = getAuthorizerAccessToken(accountId);
        url = String.format(url,accessToken);
        String param = parseJson(openId,jsonObject).replace("<fans.nickname>",nickName);
        logger.info("微信客服消息,全部发送,accountId:{},openId:{}----start,时间:{}",accountId,openId,LocalDateTime.now());
        HttpHeaders  headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(param, headers);
        String jsonStr = restTemplate.postForObject(url,request,String.class);
        logger.info("微信客服消息,全部发送,accountId:{},openId:{}----end,时间:{},微信返回：{}",accountId,openId,LocalDateTime.now(),jsonStr);
    }

    @Override
    public void sendPushMessage(String accountId, String openId, int index) {

        AccountPush accountPush = accountPushService.queryAccountPush(accountId);
        AccountFans fans = fansService.queryByAccountIdAndOpenId(accountId, openId);
        if (Objects.nonNull(accountPush)) {
            logger.info("微信公众号:{}设置了智能回复 ",accountId);
            String trigger = accountPush.getPushTrigger();
            String triggerVar = String.valueOf(trigger.charAt(index));
            if (!StringUtils.isEmpty(accountPush.getQuiet())) {
                LocalTime localTime = LocalTime.now();
                LocalTime start = LocalTime.parse(accountPush.getQuiet().split("-")[0]);
                LocalTime end = LocalTime.parse(accountPush.getQuiet().split("-")[1]);
                if (localTime.isAfter(start) && localTime.isBefore(end)) {
                    logger.info("********************* 设置了智能回复且第{}位时 1 在安静时间内 *****************", index);
                } else  {
                    sendPushMessage(accountId,openId,fans.getNickName(),accountPush.getContent(),triggerVar,accountPush.getPushType());
                }
            }else {
                sendPushMessage(accountId,openId,fans.getNickName(),accountPush.getContent(),triggerVar,accountPush.getPushType());
            }
        }
    }


    public void  sendPushMessage(String accountId,String openId,String nickName,String content,String triggerVar,int pushType){
        if (Constants.FIRST.equals(triggerVar)){
            sendMessage(accountId, openId, nickName, content,pushType);
        }
    }


    @Override
    public JSONObject queryAccountMaterial(String accountId, String type, int offset, int count) {

        String requestUrl = "https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token=%s";
        String accessToken = getAuthorizerAccessToken(accountId);
        requestUrl = String.format(requestUrl,accessToken);

        HttpHeaders  headers = new HttpHeaders();
        JSONObject param = new JSONObject();
        param.put("type",type);
        param.put("offset",offset);
        param.put("count",count);
        String requestStr = param.toJSONString();
        headers.setContentType(MediaType.APPLICATION_JSON);
        logger.info("微信公众号:{}素材查询,type{}条发送,offset:{},count:{}----start,时间:{},请求参数:{}",accountId,type,offset,
                count,LocalDateTime.now(),requestStr);
        HttpEntity<String> request = new HttpEntity<>(requestStr, headers);
        String jsonStr = restTemplate.postForObject(requestUrl,request,String.class);
        logger.info("微信公众号:{}素材查询,type{}条发送,offset:{},count:{}----end,时间:{},微信返回:{}",accountId,type,offset,
                count,LocalDateTime.now(),jsonStr);
        return JSONObject.parseObject(jsonStr);
    }

    @Override
    public String getMaterialDetail(String accountId, String mediaId) {
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/material/get_material?access_token=%s";
        String accessToken = getAuthorizerAccessToken(accountId);
        requestUrl = String.format(requestUrl,accessToken);

        HttpHeaders  headers = new HttpHeaders();
        JSONObject param = new JSONObject();
        param.put("media_id",mediaId);
        String requestStr = param.toJSONString();
        headers.setContentType(MediaType.APPLICATION_JSON);
        logger.info("微信公众号:{}查询素材详情,mediaId is :{}----start,时间:{},请求参数:{}",accountId,mediaId,LocalDateTime.now(),requestStr);
        HttpEntity<String> request = new HttpEntity<>(requestStr, headers);
        String jsonStr = restTemplate.postForObject(requestUrl,request,String.class);
        logger.info("微信公众号:{}查询素材详情,mediaId is :{}----end,时间:{},微信返回:{}",accountId,mediaId,
                LocalDateTime.now(),jsonStr);
        return jsonStr;
    }

    @Override
    public boolean checkSignature( String signature, String timestamp, String nonce) {

        String[] paramArr = new String[] {properties.getToken(), timestamp, nonce};
        Arrays.sort(paramArr);
        String content  = paramArr[0].concat(paramArr[1]).concat(paramArr[2]);
        String ciphertext = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] digest = md.digest(content.getBytes());
            ciphertext = Constants.byteToStr(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return ciphertext != null && ciphertext.equals(signature.toUpperCase());
    }

    /**
     *  群发消息
     * @author wangxiao
     * @date 13:58 2020/9/24
     * @param accountId 公众号id
     * @param contents 群发内容json
     * @param toAll 是否全部
     * @param repeatSend 转载继续
     * @param openIds 发送用户群体
     * @return java.lang.Boolean
     */
    private long  sendGroupMessage(String accountId, String contents, boolean toAll,
                                   int repeatSend, List<String> openIds,int msgType) {
        logger.info("公众号:{}群发消息:{},toAll:{},repeatSend:{},openIds:{}",accountId,contents,toAll,repeatSend,openIds);
        String requestUrl =  "https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token=%s";
        String accessToken = getAuthorizerAccessToken(accountId);
        requestUrl = String.format(requestUrl,accessToken);
        JSONArray jsonArray = JSONArray.parseArray(contents);
        int size = jsonArray.size();
        if (size == 0) {
            return 0;
        }
        int  groupCount =0;
        JSONObject temp = null;
        for (int i = 0; i < size; i++) {
            temp = jsonArray.getJSONObject(i);
            logger.info("微信群发消息,accountId:{},openIds:{}----start,时间:{}",accountId,openIds,LocalDateTime.now());
            String param = parseToJson(temp,openIds,repeatSend,toAll,msgType);
            HttpHeaders  headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>(param, headers);
            String jsonStr = restTemplate.postForObject(requestUrl,request,String.class);
            logger.info("微信群发消息,accountId:{},openIds:{}----end,时间:{},微信返回：{}",accountId,openIds,LocalDateTime.now(),jsonStr);
            int errcode = JSONObject.parseObject(jsonStr).getIntValue(ERROR_CODE);
             if (0 == errcode) {
                 groupCount+=1;
             };
            logger.info("公众号:{}群发消息第{}条,发送结果:{}",accountId,i,0 == errcode);
        }
        return groupCount;
    }


    /**
     *  发送模板消息
     * @author wangxiao
     * @date 14:05 2020/9/24
     * @param fansList 用户集群
     * @param templateMsg 模板消息内容
     * @return boolean 是否成功
     */
    private long sendTemplateMessage (String accountId,List<AccountFans> fansList,TemplateMsg templateMsg) {
        logger.info("公众号:{}发送模板消息:,AccountFans size :{},templateMsg:{}",accountId, fansList.size(),templateMsg.toString());
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s";
        String accessToken = getAuthorizerAccessToken(accountId);
        requestUrl = String.format(requestUrl,accessToken);
        String templateData = templateMsg.getTemplateData();
        int templateCount = 0;
        for (AccountFans fans : fansList) {
            templateData = templateData.replace("<fans.nickname>",fans.getNickName());
            String json = parseJson(fans.getOpenId(),templateData,templateMsg.getTemplateId(),
                    templateMsg.getLinkUrl(),templateMsg.getLinkAppId(),templateMsg.getLinkAppPage());
            logger.info("微信模板消息消息,accountId:{},openId:{}----start,时间:{}",accountId,
                    fans.getOpenId(),LocalDateTime.now());
            HttpHeaders  headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>(json, headers);
            String jsonStr = restTemplate.postForObject(requestUrl,request,String.class);
            logger.info("微信模板消息消息,accountId:{},openId:{}----end,时间:{}，微信返回：{}",accountId,
                    fans.getOpenId(),LocalDateTime.now(),jsonStr);
            int errcode = JSONObject.parseObject(jsonStr).getIntValue(ERROR_CODE);
            if (0 == errcode) {
                templateCount+=1;
            };
        }
        return templateCount;
    }

    /**
     *  批量发送客服消息
     * @author wangxiao
     * @date 18:26 2020/9/24
     * @param accountId accountId
     * @param fansList fansList
     * @param contents contents
     * @param url url
     * @return long 发送人员数目
     */
    public long sendCustomerMessage (String accountId,List<AccountFans> fansList,String contents,String url) {
        if (CollectionUtils.isEmpty(fansList)) {
            return 0;
        }
        int count = 0;
        String nikeName = null;
        String openId = null;
        logger.info("微信客服消息,粉丝逐条发送,fansList size is:{}",fansList.size());
        for (AccountFans fans : fansList) {
            openId = fans.getOpenId();
            nikeName =fans.getNickName();
            logger.info("微信客服消息,粉丝逐条发送,accountId:{},openId:{},nikeName:{}",accountId,openId,nikeName);
            sendCustomerMessage(openId,contents,nikeName,accountId,url);
            count +=1;
        }
        return count;

    }

    /**
     *  单人 发送客服消息消息
     * @author wangxiao
     * @date 15:20 2020/9/24
     * @param openId openId
     * @param nikeName nikeName
     * @param contents contents
     * @param url url
     */
    private void sendCustomerMessage(String openId,String contents,String nikeName,String accountId,String url) {
        contents = contents.replace("<fans.nickname>",nikeName);
        JSONArray jsonArray = JSONArray.parseArray(contents);
        int size = jsonArray.size();
        if (size == 0) {
            return;
        }
        String accessToken = getAuthorizerAccessToken(accountId);
        url = String.format(url,accessToken);
        JSONObject temp  = null;
        for (int i = 0; i < size; i++) {
            temp = jsonArray.getJSONObject(i);
            String param = parseJson(openId,temp);
            logger.info("微信客服消息,单人第{}条发送,accountId:{},openId:{}----start,时间:{}",i,accountId,openId,LocalDateTime.now());
            HttpHeaders  headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>(param, headers);
            String jsonStr = restTemplate.postForObject(url,request,String.class);
            logger.info("微信客服消息,单人第{}条发送,accountId:{},openId:{}----end,时间:{},微信返回：{}",i,accountId,openId,LocalDateTime.now(),jsonStr);
        }
    }

    /**
     *  菜单转换 微信json格式
     * @author wangxiao
     * @date 10:59 2020/9/24
     * @param accountMenus accountMenus
     * @return java.lang.String
     */
    private String parseToJson (List<AccountMenu> accountMenus) {
        int size  = accountMenus.size();
        JSONObject jsonObject = new JSONObject(1);
        JSONArray button  = new JSONArray(size);
        JSONObject temp = null;
        for (AccountMenu menu : accountMenus) {
            temp = new JSONObject();
            String type = menu.getType();
            temp.put("type",type);
            temp.put("name",menu.getName());
            if ("media_id".equals(menu.getType()) || "view_limited".equals(type)) {
                temp.put("media_id",menu.getKey());
            }else if ("view".equals(type)){
                temp.put("url",menu.getKey());
            }else if ("miniprogram".equals(type)) {
                temp.put("url",menu.getKey());
                temp.put("appid",menu.getParam1());
                temp.put("pagepath",menu.getParam2());
            }else {
                temp.put("key",menu.getKey());
            }
            List<AccountMenu> subMenus = menu.getSubMenu();
            if (Objects.isNull(subMenus)) {
                subMenus = new ArrayList<>();
            }
            temp.put("sub_button",JSONArray.toJSONString(subMenus));
            button.add(temp);
        }
        jsonObject.put("button",button);
        String result = jsonObject.toJSONString();
        logger.info("自定义菜单 转换json 格式结果：{}",result);
        return result;
    }

    /**
     *  转换群发消息得json
     * @author wangxiao
     * @date 11:53 2020/9/24
     * @param jsonObject jsonObject
     * @param openIds openIds
     * @param repeatSend 转载是否继续
     * @param toAll 全部
     * @return java.lang.String
     */
    public String parseToJson(JSONObject jsonObject,List<String> openIds,int repeatSend,boolean toAll,int msgType ) {
        JSONObject parent = new JSONObject();
        JSONObject filter = new JSONObject();
        JSONObject target = new JSONObject();
        filter.put("is_to_all",toAll);
        if (toAll) {
            parent.put("filter",filter);
        }else {
            parent.put("touser",openIds);
        }
        if (Constants.VID == msgType) {
            target.put("media_id",jsonObject.getString("media_id"));
            target.put("title",jsonObject.getString("title"));
            target.put("description",jsonObject.getString("description"));
            parent.put("mpvideo",target);
            parent.put("msgtype","mpvideo");
        }else if (Constants.VOI == msgType) {
            target.put("media_id",jsonObject.getString("media_id"));
            parent.put("voice",target);
            parent.put("msgtype","voice");
        }else if (Constants.TEXT == msgType) {
            target.put("content",jsonObject.getString("content"));
            parent.put("voice",target);
            parent.put("msgtype","text");
        }else if (Constants.IMG == msgType) {
            target.put("media_ids",Arrays.asList(jsonObject.getString("media_id")));
            target.put("recommend","分享图片");
            target.put("need_open_comment",1);
            target.put("only_fans_can_comment",0);
            parent.put("images",target);
            parent.put("msgtype","image");
        }else if (Constants.NEWS == msgType) {
            target.put("media_id",jsonObject.getString("media_id"));
            parent.put("mpnews",target);
            parent.put("msgtype","mpnews");
            parent.put("send_ignore_reprint",repeatSend);
        }
        String result = parent.toJSONString();
        logger.info("群发消息 转换json 格式结果：{}",result);
        return result;
    }

    /**
     *  模板数据转换json
     * @author wangxiao
     * @date 14:16 2020/9/24
     * @param openId openiD
     * @param templateData 模板数据
     * @param templateId 模板id
     * @param linkUrl 链接地址
     * @param linkAppId 链接小程序appid
     * @return java.lang.String
     */
    private String parseJson (String openId,String templateData,String templateId,
                              String linkUrl,String linkAppId,String linkAppPage) {
        JSONObject parent = new JSONObject();
        parent.put("touser",openId);
        parent.put("template_id",templateId);
        parent.put("data",JSONObject.parseObject(templateData));
        if (!StringUtils.isEmpty(linkAppId)) {
            JSONObject  miniprogram = new JSONObject();
            miniprogram.put("appid",linkAppId);
            miniprogram.put("pagepath",linkAppPage);
            parent.put("miniprogram",miniprogram);
        }else if (!StringUtils.isEmpty(linkUrl)) {
            parent.put("url",linkUrl);
        }
        String result = parent.toJSONString();
        logger.info("模板消息 转换json 格式结果：{}",result);
        return result;
    }

    /**
     *  客服消息转换 json
     * @author wangxiao
     * @date 15:40 2020/9/24
     * @param openId  openId
     * @param jsonObject jsonObject
     * @return java.lang.String
     */
    private String parseJson(String openId, JSONObject jsonObject) {
        int type = jsonObject.getIntValue("type");
        JSONObject parent = new JSONObject();
        parent.put("touser",openId);
        JSONObject target = new JSONObject();
        if (Constants.VID == type) {
            target.put("media_id",jsonObject.getString("media_id"));
            target.put("thumb_media_id",jsonObject.getString("thumb_media_id"));
            target.put("description",jsonObject.getString("description"));
            parent.put("mpvideo",target);
        }else if (Constants.VOI == type) {
            target.put("media_id",jsonObject.getString("media_id"));
            parent.put("voice",target);
            parent.put("msgtype","voice");
        }else if (Constants.TEXT == type) {
            target.put("content",jsonObject.getString("content"));
            parent.put("text",target);
            parent.put("msgtype","text");
        }else if (Constants.IMG == type) {
            target.put("media_id",jsonObject.getString("mediaId"));
            parent.put("image",target);
            parent.put("msgtype","image");
        }else if (Constants.NEWS == type) {
            target.put("articles",jsonObject.getJSONArray("imagetextlist"));
            parent.put("news",target);
            parent.put("msgtype","news");
        }else if (Constants.MNEWS == type){
            target.put("media_id",jsonObject.getJSONArray("media_id"));
            parent.put("mpnews",target);
            parent.put("msgtype","mpnews");
        }
        String result = parent.toJSONString();
        logger.info("客服消息 转换json 格式结果：{}",result);
        return result;
    }

    @Resource(name = "rpcRestTemplate")
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Autowired
    public void setProperties(PlatformProperties properties) {
        this.properties = properties;
    }

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Autowired
    public void setFansService(AccountFansService fansService) {
        this.fansService = fansService;
    }

    @Autowired
    public void setTagService(AccountTagService tagService) {
        this.tagService = tagService;
    }

    @Resource
    public void setFansInfoHandler(FansInfoHandler fansInfoHandler) {
        this.fansInfoHandler = fansInfoHandler;
    }

    @Autowired
    public void setGroupMsgService(GroupMsgService groupMsgService) {
        this.groupMsgService = groupMsgService;
    }

    @Autowired
    public void setTemplateMsgService(TemplateMsgService templateMsgService) {
        this.templateMsgService = templateMsgService;
    }

    @Autowired
    public void setCustomerMsgService(CustomerMsgService customerMsgService) {
        this.customerMsgService = customerMsgService;
    }


    @Autowired
    public void setFansActionStatService(FansActionStatService fansActionStatService) {
        this.fansActionStatService = fansActionStatService;
    }

    @Autowired
    public void setAccountPushService(AccountPushService accountPushService) {
        this.accountPushService = accountPushService;
    }


    public static void main(String[] args) {
        System.out.println(ThreadLocalRandom.current().nextInt(0,10));
        System.out.println(ThreadLocalRandom.current().nextInt(0,10));
        System.out.println();
    }
}
