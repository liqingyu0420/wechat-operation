package com.idiot.operationbackend.service.facade;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.idiot.operationbackend.entity.CustomerMsg;

/**
 * @author wang xiao
 * @date Created in 15:14 2020/9/22
 */
public interface CustomerMsgService extends IService<CustomerMsg> {


    /**
     *  查询客服消息
     * @author wangxiao
     * @date 15:19 2020/9/22
     * @param status status
     * @param page page
     * @param startDate startDate
     * @param endDate endDate
     * @param authId authId
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.idiot.operationbackend.entity.CustomerMsg>
     */
    Page<CustomerMsg> queryPageCustomerMsg (Integer status,int page,String startDate,String endDate,String authId);
}
