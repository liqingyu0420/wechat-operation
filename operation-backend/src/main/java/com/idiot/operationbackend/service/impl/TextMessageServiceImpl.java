package com.idiot.operationbackend.service.impl;

import com.idiot.operationbackend.entity.FansActionStat;
import com.idiot.operationbackend.entity.FansMsg;
import com.idiot.operationbackend.handler.WeChatMessageFactory;
import com.idiot.operationbackend.service.facade.*;
import com.idiot.operationbackend.support.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 文本消息
 * @author wang xiao
 * @date Created in 14:29 2020/6/18
 */
@Service
public class TextMessageServiceImpl implements WeChatMessageService {

    @Autowired
    private FansMsgService fansMsgService;

    @Autowired
    private WeChatService weChatService;

    @Autowired
    private FansActionStatService fansActionStatService;

    @Override
    public String processMessage(Map<String, String> param) {
        String accountId = param.get("accountId");
        String openId = param.get("FromUserName");
        String content = param.get("Content");
        String msgId = param.get("MsgId");
        String createTime = param.get("CreateTime");
        FansMsg fansMsg = new FansMsg();
        fansMsg.setAccountId(accountId);
        fansMsg.setCreateTime(Long.parseLong(createTime));
        fansMsg.setContent(content);
        fansMsg.setFromUserName(openId);
        fansMsg.setMsgId(msgId);
        fansMsg.setMsgType("text");
        fansMsgService.save(fansMsg);
        FansActionStat var2 = new FansActionStat(accountId,openId,0);
        fansActionStatService.saveBatchFansActionStat(var2);
        weChatService.sendPushMessage(accountId,openId,1);
        return Constants.SUCCESS;
    }



    @Override
    public void afterPropertiesSet() throws Exception {
        WeChatMessageFactory.addService("text",this);
    }
}
