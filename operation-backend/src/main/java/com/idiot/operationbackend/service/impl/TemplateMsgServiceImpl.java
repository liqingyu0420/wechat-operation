package com.idiot.operationbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.idiot.operationbackend.entity.TemplateMsg;
import com.idiot.operationbackend.mappers.TemplateMsgMapper;
import com.idiot.operationbackend.service.facade.TemplateMsgService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author wang xiao
 * @date Created in 19:27 2020/9/22
 */
@Service
public class TemplateMsgServiceImpl  extends ServiceImpl<TemplateMsgMapper,TemplateMsg>  implements TemplateMsgService {


    @Override
    public Page<TemplateMsg> queryPageTemplateMsg(int page, Integer status, List<String> ids) {
        Page<TemplateMsg> queryPage = new Page<>(page,15);
        LambdaQueryWrapper<TemplateMsg> queryWrapper = Wrappers.<TemplateMsg>
                lambdaQuery().orderByDesc(TemplateMsg::getCreateTime);
        if (Objects.nonNull(status)) {
            queryWrapper.eq(TemplateMsg::getStatus,status);
        }
        queryWrapper.in(TemplateMsg::getAccountId,ids);
        return page(queryPage,queryWrapper);
    }
}
