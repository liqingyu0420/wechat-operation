package com.idiot.operationbackend.support.job;

import com.idiot.operationbackend.entity.GroupMsg;
import com.idiot.operationbackend.service.facade.WeChatService;

/**
 *  群发任务 中介一层是因为 构造参数 可能未知
 * @author wang xiao
 * @date Created in 15:09 2020/9/23
 */
public class GroupJobTask implements BaseJobTask {

    private GroupMsg groupMsg;

    private WeChatService weChatService;

    public GroupJobTask() {
    }

    public GroupJobTask(GroupMsg groupMsg, WeChatService weChatService) {
        this.groupMsg = groupMsg;
        this.weChatService = weChatService;
    }

    @Override
    public void run() {
        weChatService.doGroupMsgTask(groupMsg);

    }




}
