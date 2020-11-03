package com.idiot.operationbackend.service.impl;

import com.idiot.operationbackend.entity.FansActionStat;
import com.idiot.operationbackend.entity.FansMsg;
import com.idiot.operationbackend.handler.WeChatMessageFactory;
import com.idiot.operationbackend.service.facade.FansActionStatService;
import com.idiot.operationbackend.service.facade.FansMsgService;
import com.idiot.operationbackend.service.facade.WeChatMessageService;
import com.idiot.operationbackend.service.facade.WeChatService;
import com.idiot.operationbackend.support.Constants;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * @author wang xiao
 * @date Created in 20:43 2020/9/24
 */
public class ImageMessageServiceImpl  implements WeChatMessageService {

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
        fansMsg.setMsgType("image");
        fansMsgService.save(fansMsg);
        FansActionStat var2 = new FansActionStat(accountId,openId,0);
        fansActionStatService.saveBatchFansActionStat(var2);
        weChatService.sendPushMessage(accountId,openId,1);
        return Constants.SUCCESS;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        WeChatMessageFactory.addService("image",this);
    }
}
