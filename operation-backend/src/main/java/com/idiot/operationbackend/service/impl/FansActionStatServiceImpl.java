package com.idiot.operationbackend.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.idiot.operationbackend.entity.FansActionStat;
import com.idiot.operationbackend.mappers.FansActionStatMapper;
import com.idiot.operationbackend.service.facade.FansActionStatService;
import com.idiot.operationbackend.support.Constants;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wang xiao
 * @date Created in 11:33 2020/9/16
 */
@Service
public class FansActionStatServiceImpl extends ServiceImpl<FansActionStatMapper, FansActionStat>
        implements FansActionStatService {

    @Override
    public int countInactiveFansNum(String accountId, long startDate, long endDate) {
        return count(Wrappers.<FansActionStat>lambdaQuery().eq(FansActionStat::getAccountId,accountId)
                .ne(FansActionStat::getAction,"3")
                .ge(FansActionStat::getCreateTime,startDate)
                .le(FansActionStat::getCreateTime,endDate));
    }


    @Override
    public List<FansActionStat> queryFansActionStat(String accountId, String startDate, String endDate) {

        long start =  Constants.toLocalDateTime(startDate).toEpochSecond(Constants.DEFAULT_ZONE);
        long end = Constants.toLocalDateTime(endDate).plusDays(1).toEpochSecond(Constants.DEFAULT_ZONE);
        return list(Wrappers.<FansActionStat>lambdaQuery().eq(FansActionStat::getAccountId,accountId)
                .ge(FansActionStat::getCreateTime,start)
                .le(FansActionStat::getCreateTime,end)
                .orderByAsc(FansActionStat::getCreateTime));
    }


    @Override
    public List<FansActionStat> queryFansActionStat(String accountId, LocalDateTime startDate, LocalDateTime endDate) {
        long start =  startDate.toEpochSecond(Constants.DEFAULT_ZONE);
        long end = endDate.toEpochSecond(Constants.DEFAULT_ZONE);
        return list(Wrappers.<FansActionStat>lambdaQuery().eq(FansActionStat::getAccountId,accountId)
                .ge(FansActionStat::getCreateTime,start)
                .le(FansActionStat::getCreateTime,end)
                .orderByAsc(FansActionStat::getCreateTime));
    }

    @Override
    public List<FansActionStat> queryFansActionStat(String accountId, long start, long end) {
          return list(Wrappers.<FansActionStat>lambdaQuery().eq(FansActionStat::getAccountId,accountId)
                .ge(FansActionStat::getCreateTime,start)
                .le(FansActionStat::getCreateTime,end)
                .orderByAsc(FansActionStat::getCreateTime));
    }

    @Override
    public boolean saveBatchFansActionStat(FansActionStat... params) {
        return saveBatch(Arrays.stream(params).collect(Collectors.toList()));
    }
}
