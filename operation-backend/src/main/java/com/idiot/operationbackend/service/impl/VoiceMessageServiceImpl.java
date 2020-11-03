package com.idiot.operationbackend.service.impl;

import com.idiot.operationbackend.entity.FansActionStat;
import com.idiot.operationbackend.handler.WeChatMessageFactory;
import com.idiot.operationbackend.service.facade.FansActionStatService;
import com.idiot.operationbackend.service.facade.WeChatMessageService;
import com.idiot.operationbackend.service.facade.WeChatService;
import com.idiot.operationbackend.support.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 音频消息
 * @author wang xiao
 * @date Created in 9:49 2020/9/25
 */
@Service
public class VoiceMessageServiceImpl implements WeChatMessageService {

    @Autowired
    private WeChatService weChatService;

    @Autowired
    private FansActionStatService fansActionStatService;

    @Override
    public String processMessage(Map<String, String> param) {
        String accountId = param.get("accountId");
        String openId = param.get("FromUserName");
        FansActionStat var2 = new FansActionStat(accountId,openId,0);
        fansActionStatService.saveBatchFansActionStat(var2);
        weChatService.sendPushMessage(accountId,openId,1);
        return Constants.SUCCESS;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        WeChatMessageFactory.addService("voice",this);
    }
}
