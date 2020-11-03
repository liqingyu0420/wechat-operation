package com.idiot.operationbackend.service.facade;

import com.baomidou.mybatisplus.extension.service.IService;
import com.idiot.operationbackend.entity.AccountStat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wang xiao
 * @date Created in 14:53 2020/9/15
 */
public interface AccountStatService extends IService<AccountStat> {

    /**
     *  查询 公众号列表下的 粉丝统计
     * @author wangxiao
     * @date 15:18 2020/9/15
     * @param accountIds
     * @param statDate
     * @return java.util.List<com.idiot.operationbackend.entity.AccountFansStat>
     */
    List<AccountStat> queryByDateAndAccIds(List<String> accountIds,String statDate);


    /**
     *  单独查询公众号列表下粉丝统计
     * @author wangxiao
     * @date 15:21 2020/9/15
     * @param accountId
     * @param statDate
     * @return com.idiot.operationbackend.entity.AccountFansStat
     */
    AccountStat queryByDateAndAccountId(String accountId, String statDate);



    /**
     *  统计粉丝数据
     * @author wangxiao
     * @date 17:19 2020/9/15
     * @param accountId
     * @return void
     */
    void  statAccountFansData (String accountId);


    /**
     * 昨日活跃数量
     * @author wangxiao
     * @date 10:28 2020/9/16
     * @param accountId
     * @param statDate
     * @return int
     */
    int inactiveFansNum (String accountId,String statDate);



    /**
     * 七天活跃数量
     * @author wangxiao
     * @date 10:28 2020/9/16
     * @param accountId
     * @param statDate
     * @return int
     */
    int inactiveSevenFansNum (String accountId,String statDate);


    /**
     * 15天活跃数量
     * @author wangxiao
     * @date 10:28 2020/9/16
     * @param accountId
     * @param statDate
     * @return int
     */
    int inactiveFifteenFansNum (String accountId,String statDate);


    /**
     *  查询区间内 粉丝数据统计
     * @author wangxiao
     * @date 12:00 2020/9/17
     * @param accountId
     * @param startDate
     * @param endDate
     * @return java.util.List<com.idiot.operationbackend.entity.AccountStat>
     */
    List<AccountStat> queryAccountStatByDate (String accountId, String startDate,String endDate);

    /**
     *  查询区间内 粉丝数据统计
     * @author wangxiao
     * @date 12:00 2020/9/17
     * @param accountId
     * @param startDate
     * @param endDate
     * @return java.util.List<com.idiot.operationbackend.entity.AccountStat>
     */
    List<AccountStat> queryAccountStatByDate (String accountId, LocalDateTime startDate, LocalDateTime endDate);
}
