package com.idiot.operationbackend.support.job;

import com.idiot.operationbackend.entity.TemplateMsg;
import com.idiot.operationbackend.service.facade.WeChatService;

/**
 * 模板消息发送任务 中介一层是因为 构造参数 可能未知
 * @author wang xiao
 * @date Created in 17:55 2020/9/23
 */
public class TemplateJobTask implements BaseJobTask {

    private TemplateMsg templateMsg;

    private WeChatService weChatService;

    public TemplateJobTask() {
    }

    public TemplateJobTask(TemplateMsg templateMsg, WeChatService weChatService) {
        this.templateMsg = templateMsg;
        this.weChatService = weChatService;
    }

    @Override
    public void run() {
        weChatService.doTemplateMsgTask(templateMsg);
    }
}
