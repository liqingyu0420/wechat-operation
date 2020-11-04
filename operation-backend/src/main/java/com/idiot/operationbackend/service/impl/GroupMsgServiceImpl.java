package com.idiot.operationbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.idiot.operationbackend.entity.GroupMsg;
import com.idiot.operationbackend.mappers.GroupMsgMapper;
import com.idiot.operationbackend.service.facade.GroupMsgService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author wang xiao
 * @date Created in 17:23 2020/9/22
 */
@Service
public class GroupMsgServiceImpl extends ServiceImpl<GroupMsgMapper, GroupMsg> implements GroupMsgService {


    @Override
    public Page<GroupMsg> queryPageGroupMsg(int page, String startDate, String endDate, Integer status, List<String> ids) {

        Page<GroupMsg> queryPage = new Page<>(page,15);
        LambdaQueryWrapper<GroupMsg> queryWrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(startDate)) {
            queryWrapper.ge(GroupMsg::getCreateTime,startDate);
        }
        if (StringUtils.isNotBlank(endDate)) {
            queryWrapper.le(GroupMsg::getCreateTime,endDate);
        }
        if (Objects.nonNull(status)) {
            queryWrapper.eq(GroupMsg::getStatus,status);
        }
        queryWrapper.in(GroupMsg::getAccountId,ids);
        queryWrapper.orderByDesc(GroupMsg::getCreateTime);
        return page(queryPage,queryWrapper);
    }


    @Override
    public boolean upGroupMsg(String msgId, long sendNum) {
        return update(Wrappers.<GroupMsg>lambdaUpdate().set(GroupMsg::getSendNum,sendNum).eq(GroupMsg::getMsgId,msgId));
    }
}
