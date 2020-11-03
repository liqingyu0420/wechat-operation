package com.idiot.operationbackend.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.idiot.operationbackend.entity.AccountStat;
import com.idiot.operationbackend.mappers.AccountStatMapper;
import com.idiot.operationbackend.service.facade.AccountStatService;
import com.idiot.operationbackend.service.facade.FansActionStatService;
import com.idiot.operationbackend.service.facade.WeChatService;
import com.idiot.operationbackend.support.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 公众号数据统计
 * @author wang xiao
 * @date Created in 14:54 2020/9/15
 */
@Service
public class AccountStatServiceImpl extends ServiceImpl<AccountStatMapper, AccountStat>
        implements AccountStatService {



    @Autowired
    private WeChatService weChatService;


    @Autowired
    private FansActionStatService actionStatService;


    @Override
    public List<AccountStat> queryByDateAndAccIds(List<String> accountIds, String statDate) {
        return list(Wrappers.<AccountStat>lambdaQuery().eq(AccountStat::getStatDate,statDate)
                .in(AccountStat::getAccountId,accountIds));
    }


    @Override
    public AccountStat queryByDateAndAccountId(String accountId, String statDate) {
        return getOne(Wrappers.<AccountStat>lambdaQuery()
                .eq(AccountStat::getAccountId,accountId)
                .eq(AccountStat::getStatDate,statDate));
    }


    @Override
    @Async("asyncExecutor")
    public void statAccountFansData(String accountId) {
        String yesterday = Constants.DATE_FORMATTER.format(LocalDate.now().plusDays(-1));
        String beforeDay = Constants.DATE_FORMATTER.format(LocalDate.now().plusDays(-2));
        String jsonStr = weChatService.getFansSummary(accountId,yesterday,yesterday);
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        JSONArray jsonArray = jsonObject.getJSONArray("list");
        int size  = jsonArray.size();

        long newNum = 0;
        long cancelNum = 0;
        long addNum ;
        JSONObject tempObject = null;
        // 昨日增减
        if (size >0 ){
            for (int i = 0; i < size; i++) {
                tempObject = jsonArray.getJSONObject(i);
                newNum += tempObject.getInteger("new_user");
                cancelNum += tempObject.getInteger("cancel_user");
            }
        }

        long totalNum = 0;
        // 昨日汇总
        String jsonStrTotal = weChatService.getFansCumulate(accountId,yesterday,yesterday);
        JSONObject jsonObjectTotal = JSONObject.parseObject(jsonStrTotal);
        JSONArray jsonArrayTotal = new JSONArray(0);
        if (Objects.nonNull(jsonObjectTotal)) {
            jsonArrayTotal = jsonObjectTotal.getJSONArray("list");
        }
        int totalSize = jsonArrayTotal.size();
        if (totalSize > 0) {
            totalNum = jsonArrayTotal.getJSONObject(0).getInteger("cumulate_user");
        }

        // 昨日和前日 数据
        // fixme 查询次数过多可以放在 观察者模式中异步执行（先看时间后期优化）
        List<AccountStat> statData = baseMapper.selectByDateAndAccId(accountId,yesterday,beforeDay);
        // 昨日活跃数量
        long inactiveNum =  inactiveFansNum(accountId,yesterday);
        // 7天
        long sevenInactiveNum =  inactiveSevenFansNum(accountId,yesterday);
        // 15 天
        long fifteenInactiveNum =  inactiveFifteenFansNum(accountId,yesterday);
        if (Objects.isNull(statData)){
            statData = new ArrayList<>();
        }
        AccountStat ydData = statData.stream().filter(e->yesterday.equals(e.getStatDate())).findFirst().orElseGet(AccountStat::new);
        addNum = newNum-cancelNum;
        ydData.setTotalFansNum(totalNum);
        ydData.setNewNum(newNum);
        ydData.setCancelNum(cancelNum);
        ydData.setStatDate(yesterday);
        ydData.setAccountId(accountId);
        ydData.setAddNum(addNum);
        ydData.setInactiveNum(inactiveNum);
        ydData.setSevenNum(sevenInactiveNum);
        ydData.setFifteenNum(fifteenInactiveNum);
        ydData.setPageReadNum(0L);
        statData.stream().filter(e->beforeDay.equals(e.getStatDate())).findFirst()
                .map(e->{
                    ydData.setAddRate(Constants.calcRate(ydData.getAddNum(), e.getAddNum()));
                    ydData.setNewRate(Constants.calcRate(ydData.getNewNum(), e.getNewNum()));
                    ydData.setCancelRate(Constants.calcRate(ydData.getCancelNum(), e.getCancelNum()));
                    ydData.setInactiveRate(Constants.calcRate(ydData.getInactiveNum(), e.getInactiveNum()));
                    ydData.setPageReadRate(Constants.calcRate(ydData.getPageReadNum(),e.getPageReadNum()));
                    ydData.setInactiveRate(Constants.calcRate(ydData.getInactiveNum(),e.getInactiveNum()));
                    return ydData;
        });
        saveOrUpdate(ydData);
    }


    @Override
    public int inactiveFansNum(String accountId, String statDate) {
        LocalDateTime localDate = Constants.toLocalDateTime(statDate);
        long start =  localDate.toEpochSecond(Constants.DEFAULT_ZONE);
        long end =  localDate.plusDays(1).toEpochSecond(Constants.DEFAULT_ZONE);
        return actionStatService.countInactiveFansNum(accountId,start,end);
    }

    @Override
    public int inactiveSevenFansNum(String accountId, String statDate) {
        LocalDateTime localDate = Constants.toLocalDateTime(statDate);
        long start =  localDate.plusDays(-7).toEpochSecond(Constants.DEFAULT_ZONE);
        long end =  localDate.toEpochSecond(Constants.DEFAULT_ZONE);
        return actionStatService.countInactiveFansNum(accountId,start,end);
    }

    @Override
    public int inactiveFifteenFansNum(String accountId, String statDate) {
        LocalDateTime localDate = Constants.toLocalDateTime(statDate);
        long start =  localDate.plusDays(-15).toEpochSecond(Constants.DEFAULT_ZONE);
        long end =  localDate.toEpochSecond(Constants.DEFAULT_ZONE);
        return actionStatService.countInactiveFansNum(accountId,start,end);
    }

    @Override
    public List<AccountStat> queryAccountStatByDate(String accountId, String startDate, String endDate) {
        return list(Wrappers.<AccountStat>lambdaQuery()
                .eq(AccountStat::getAccountId,accountId)
                .ge(AccountStat::getStatDate,startDate)
                .le(AccountStat::getStatDate,endDate));
    }

    @Override
    public List<AccountStat> queryAccountStatByDate(String accountId, LocalDateTime startDate, LocalDateTime endDate) {
        String startStr = startDate.format(Constants.DATE_FORMATTER);
        String endStr = endDate.format(Constants.DATE_FORMATTER);
        return list(Wrappers.<AccountStat>lambdaQuery()
                .eq(AccountStat::getAccountId,accountId)
                .ge(AccountStat::getStatDate,startStr)
                .le(AccountStat::getStatDate,endStr)
                .orderByAsc(AccountStat::getStatDate));
    }
}
