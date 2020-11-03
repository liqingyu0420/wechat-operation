package com.idiot.operationbackend.service.impl;

import com.idiot.operationbackend.entity.*;
import com.idiot.operationbackend.handler.ScanQrHandler;
import com.idiot.operationbackend.handler.WeChatMessageFactory;
import com.idiot.operationbackend.service.facade.*;
import com.idiot.operationbackend.support.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 微信 event 消息
 * @author wang xiao
 * @date Created in 14:17 2020/6/18
 */
@Service
public class EventMessageServiceImpl implements WeChatMessageService {


    private final Logger logger = LoggerFactory.getLogger(EventMessageServiceImpl.class);

    @Autowired
    private QrCodeService qrCodeService;

    @Autowired
    private ScanQrHandler scanQrHandler;

    @Autowired
    private FollowReplyService followReplyService;

    @Autowired
    private FansActionStatService fansActionStatService;

    @Autowired
    private WeChatService weChatService;



    @Autowired
    private AccountFansService fansService;

    @Override
    public String processMessage(Map<String, String> param) {
        String eventType = param.get("Event");
        try {
            return eventMap.getOrDefault(eventType,this::unSupportedMessage).apply(param);
        }catch (Exception e){
            String errorStr = Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.joining("\n"));
            logger.error(errorStr);
            return Constants.SUCCESS;
        }

    }


    /**
     * 所有事件函数 集合
     */
    private final Map<String, Function<Map<String,String>,String> > eventMap = new HashMap<>();


    /**
     *  subscribe
     * 扫描二维码 （用户未关注时，进行关注后的事件推送）
     * @author wangxiao
     * @date 18:54 2020/7/1
     * @param param param
     * @return java.lang.String
     */
    private String subscribeEvent (Map<String,String> param) {
        logger.info("*********************  subscribeEvent param is {}*****************",param.toString());
        String key = param.get("EventKey");
        String accountId = param.get("accountId");
        String openId = param.get("FromUserName");
        if (!StringUtils.isEmpty(key)) {
            logger.info("********************* 扫描二维码 关注 *****************");
            key = key.replace("qrscene_","");
            QrCode qrCode = qrCodeService.getById(key);
            if (Objects.nonNull(qrCode)) {
                qrCode.setNewNum(qrCode.getNewNum()+1);
                qrCode.setTotalNum(qrCode.getTotalNum()+1);
                qrCodeService.updateById(qrCode);
                scanQrHandler.addElement(accountId,openId,key,qrCode.getContent());
            }
            return Constants.SUCCESS;
        }
        FansActionStat var2 = new FansActionStat(accountId,openId,2);
        fansActionStatService.saveBatchFansActionStat(var2);
        // 关注事件
        FollowReply followReply = followReplyService.queryFollowReplyByAccountId(accountId);
        if (Objects.nonNull(followReply)) {
           // 设置了关注回复
            logger.info("********************* 设置了关注回复 *****************");
            weChatService.sendMessage(accountId,openId,"",followReply.getContent());
            return Constants.SUCCESS;
        }
        // 是否有智能推送
        weChatService.sendPushMessage(accountId,openId,0);
        return Constants.SUCCESS;

    }


    /**
     *  SCAN
     *  扫描二维码 （用户关注时）
     * @author wangxiao
     * @date 18:54 2020/7/1
     * @param param param
     * @return java.lang.String
     */
    private String scanEvent (Map<String,String> param) {
        logger.info("************** SCAN Received weChat   ****************");
        String key = param.get("EventKey");
        String accountId = param.get("accountId");
        String openId = param.get("FromUserName");
        if (!StringUtils.isEmpty(key)) {
            logger.info("************** SCAN Received weChat  event key : {} ****************",key);
            QrCode qrCode = qrCodeService.getById(key);
            if (Objects.nonNull(qrCode)) {
                qrCode.setFollowNum(qrCode.getFollowNum()+1);
                qrCode.setTotalNum(qrCode.getTotalNum()+1);
                qrCodeService.updateById(qrCode);
                scanQrHandler.addElement(accountId,openId,key,qrCode.getContent());
            }
            return Constants.SUCCESS;
        }
        return Constants.SUCCESS;
    }
    
    /**
     *  LOCATION 位置上报
     * @author wangxiao
     * @date 19:09 2020/7/1
     * @param param param
     * @return java.lang.String
     */
    private String locationEvent (Map<String,String> param) {
        String latitude = param.get("Latitude");
        String longitude = param.get("Longitude");
        String precision = param.get("Precision");
        logger.info("**** LOCATION Received weChat  latitude : {},longitude: {},precision: {} ****",latitude,longitude,precision);
        return Constants.SUCCESS;
    }


    /**
     *  CLICK 自定义菜单
     * @author wangxiao
     * @date 19:09 2020/7/1
     * @param param param
     * @return java.lang.String
     */
    private String clickEvent (Map<String,String> param) {
        String key = param.get("EventKey");
        logger.info("************** CLICK Received weChat  event key : {} ****************",key);
        String accountId = param.get("accountId");
        String openId = param.get("FromUserName");
        FansActionStat var2 = new FansActionStat(accountId,openId,5);
        fansActionStatService.saveBatchFansActionStat(var2);
        weChatService.sendPushMessage(accountId,openId,2);
        return Constants.SUCCESS;
    }


    /**
     *  VIEW 点击菜单跳转链接
     * @author wangxiao
     * @date 19:09 2020/7/1
     * @param param param
     * @return java.lang.String
     */
    private String viewEvent (Map<String,String> param) {
        String key = param.get("EventKey");
        logger.info("************** VIEW Received weChat  event key : {} ****************",key);
        String accountId = param.get("accountId");
        String openId = param.get("FromUserName");
        FansActionStat var2 = new FansActionStat(accountId,openId,5);
        fansActionStatService.saveBatchFansActionStat(var2);
        return Constants.SUCCESS;
    }

    /**
     *  取关事件
     * @author wangxiao
     * @date 20:17 2020/9/24
     * @param param param
     * @return java.lang.String
     */
    private String unsubscribeEvent(Map<String,String> param) {
        logger.info("************** unsubscribe Received weChat   ****************");
        String accountId = param.get("accountId");
        String openId = param.get("FromUserName");
        FansActionStat var2 = new FansActionStat(accountId,openId,3);
        fansActionStatService.saveBatchFansActionStat(var2);
        fansService.unsubscribe(accountId,openId);
        return Constants.SUCCESS;
    }


    @Override
    public void afterPropertiesSet()  {
        logger.info("微信消息,事件处理类--------------->正在初始化");
        synchronized (eventMap) {
            eventMap.put("subscribe",this::subscribeEvent);
            eventMap.put("SCAN",this::scanEvent);
            eventMap.put("LOCATION",this::locationEvent);
            eventMap.put("CLICK",this::clickEvent);
            eventMap.put("VIEW",this::viewEvent);
            eventMap.put("unsubscribe",this::unsubscribeEvent);
        }
        logger.info("微信消息,事件处理类--------------->初始化完成");
        WeChatMessageFactory.addService("event",this);

    }




}
