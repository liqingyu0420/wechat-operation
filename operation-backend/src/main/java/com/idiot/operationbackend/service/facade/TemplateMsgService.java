package com.idiot.operationbackend.service.facade;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.idiot.operationbackend.entity.TemplateMsg;

import java.util.List;

/**
 * @author wang xiao
 * @date Created in 19:26 2020/9/22
 */
public interface TemplateMsgService extends IService<TemplateMsg> {


    /**
     *  查询模板消息列表
     * @author wangxiao
     * @date 19:36 2020/9/22
     * @param page page
     * @param status status
     * @param ids ids
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.idiot.operationbackend.entity.TemplateMsg>
     */
    Page<TemplateMsg> queryPageTemplateMsg(int page, Integer status, List<String> ids);
}
