package com.idiot.operationbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.idiot.operationbackend.entity.CustomerMsg;
import com.idiot.operationbackend.mappers.CustomerMsgMapper;
import com.idiot.operationbackend.service.facade.CustomerMsgService;
import org.springframework.stereotype.Service;

import java.util.Objects;


/**
 * @author wang xiao
 * @date Created in 15:15 2020/9/22
 */
@Service
public class CustomerMsgServiceImpl extends ServiceImpl<CustomerMsgMapper, CustomerMsg>
        implements CustomerMsgService {

    @Override
    public Page<CustomerMsg> queryPageCustomerMsg(Integer status, int page, String startDate, String endDate, String authId) {

        Page<CustomerMsg> queryPage = new Page<>(page,15);
        LambdaQueryWrapper<CustomerMsg> queryWrapper = Wrappers.<CustomerMsg>lambdaQuery();
        queryWrapper.eq(CustomerMsg::getAuthId,authId);
        if (Objects.nonNull(status)) {
            queryWrapper.eq(CustomerMsg::getStatus,status);
        }
        if (StringUtils.isNotBlank(startDate)) {
            queryWrapper.ge(CustomerMsg::getCreateTime,startDate);
        }
        if (StringUtils.isNotBlank(endDate)) {
            queryWrapper.le(CustomerMsg::getCreateTime,endDate);
        }
        queryWrapper.orderByDesc(CustomerMsg::getCreateTime);
        return page(queryPage,queryWrapper);
    }
}
