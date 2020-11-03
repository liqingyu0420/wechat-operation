package com.idiot.operationbackend.support.job;

import com.idiot.operationbackend.entity.CustomerMsg;
import com.idiot.operationbackend.service.facade.WeChatService;

/**
 * 客服消息
 * @author wang xiao
 * @date Created in 14:33 2020/9/24
 */
public class CustomerJobTask implements BaseJobTask{

    private CustomerMsg customerMsg;

    private WeChatService weChatService;

    public CustomerJobTask() {
    }

    public CustomerJobTask(CustomerMsg customerMsg, WeChatService weChatService) {
        this.customerMsg = customerMsg;
        this.weChatService = weChatService;
    }

    @Override
    public void run() {
        weChatService.doCustomerMsgTask(customerMsg);
    }
}
