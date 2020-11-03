package com.idiot.operationbackend.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.idiot.operationbackend.entity.FansMsg;
import com.idiot.operationbackend.mappers.FansMsgMapper;
import com.idiot.operationbackend.service.facade.FansMsgService;
import com.idiot.operationbackend.vo.AccountMsgData;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author wang xiao
 * @date Created in 14:12 2020/9/18
 */
@Service
public class FansMsgServiceImpl extends ServiceImpl<FansMsgMapper, FansMsg>
        implements FansMsgService {

    @Override
    public List<AccountMsgData> countFansMsgNumber(List<String> accountIds) {

        if (CollectionUtils.isEmpty(accountIds)) {
            return null;
        }
        return baseMapper.countAccountNumber(accountIds);
    }


    @Override
    public List<FansMsg> queryFansMsg(String accountId, int page) {
        int pageSize = 20;
        int limitPre = (page-1)* pageSize;
        LambdaQueryWrapper<FansMsg> wrapper = Wrappers.<FansMsg>lambdaQuery()
                .eq(FansMsg::getAccountId,accountId)
                .eq(FansMsg::getRead,0)
                .orderByDesc(FansMsg::getCreateTime)
                .last(" LIMIT "+ limitPre +",20");
        return list(wrapper);
    }


    @Override
    public boolean updateToRead(String msgId) {
        return update(Wrappers.<FansMsg>lambdaUpdate().eq(FansMsg::getId,msgId).set(FansMsg::getRead,1));
    }
}
