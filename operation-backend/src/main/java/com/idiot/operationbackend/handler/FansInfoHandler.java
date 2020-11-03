package com.idiot.operationbackend.handler;

import com.idiot.operationbackend.service.facade.WeChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *  粉丝信息
 * @author wang xiao
 * @date Created in 18:41 2020/9/14
 */

@Component
public class FansInfoHandler {




    private WeChatService weChatService;



    @Autowired
    @Lazy
    public void setWeChatService(WeChatService weChatService) {
        this.weChatService = weChatService;
    }

    /**
     * 卸载这里原因是 jdk 代理 异步方法不生效
     */
    public void  doSyncUserTask (String accountId,List<String> openIds) {
        weChatService.syncUserTask(accountId,openIds);
    }


}
