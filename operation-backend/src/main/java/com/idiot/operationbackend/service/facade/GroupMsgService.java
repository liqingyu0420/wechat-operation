package com.idiot.operationbackend.service.facade;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.idiot.operationbackend.entity.GroupMsg;

import java.util.List;

/**
 * @author wang xiao
 * @date Created in 17:22 2020/9/22
 */
public interface GroupMsgService extends IService<GroupMsg> {

    /**
     *  高级群发分页
     * @author wangxiao
     * @date 17:39 2020/9/22
     * @param page page
     * @param startDate startDate
     * @param endDate endDate
     * @param status status
     * @param ids ids
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.idiot.operationbackend.entity.GroupMsg>
     */
    Page<GroupMsg> queryPageGroupMsg (int page, String startDate, String endDate,Integer status, List<String> ids);
}
