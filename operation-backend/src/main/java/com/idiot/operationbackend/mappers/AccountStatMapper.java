package com.idiot.operationbackend.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.idiot.operationbackend.entity.AccountStat;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author wang xiao
 * @date Created in 14:52 2020/9/15
 */
public interface AccountStatMapper extends BaseMapper<AccountStat> {

    /**
     *  查询 两个日期的统计数据
     * @author wangxiao
     * @date 10:33 2020/9/16
     * @param accountId
     * @param stateDate
     * @param beforeDate
     * @return java.util.List<com.idiot.operationbackend.entity.AccountFansStat>
     */
    @Select("SELECT " +
            " t.id, " +
            " t.account_id, " +
            " t.stat_date, " +
            " t.new_num, " +
            " t.cancel_num, " +
            " t.inactive_num, " +
            " t.total_fans_num, " +
            " t.add_num  " +
            "FROM " +
            " t_account_stat t  " +
            "WHERE " +
            " t.account_id =  #{accountId}  " +
            " AND t.stat_date = #{stateDate} " +
            " UNION " +
            "SELECT " +
            " t.id, " +
            " t.account_id, " +
            " t.stat_date, " +
            " t.new_num, " +
            " t.cancel_num, " +
            " t.inactive_num, " +
            " t.total_fans_num, " +
            " t.add_num  " +
            "FROM " +
            " t_account_stat t  " +
            "WHERE " +
            " t.account_id = #{accountId}  " +
            " AND t.stat_date = #{beforeDate} ")
    @Results(id = "AccountFansStatMap",value = {
            @Result(column = "id",id = true,property = "id"),
            @Result(column = "account_id",property = "accountId"),
            @Result(column = "new_num",property = "newNum"),
            @Result(column = "cancel_num",property = "cancelNum"),
            @Result(column = "inactive_num",property = "inactiveNum"),
            @Result(column = "total_fans_num",property = "totalFansNum"),
            @Result(column = "add_num",property = "addNum"),
            @Result(column = "stat_date",property = "statDate"),
    })
    List<AccountStat> selectByDateAndAccId(@Param("accountId") String accountId, @Param("stateDate") String stateDate,
                                           @Param("beforeDate")  String beforeDate);




}
