package com.idiot.operationbackend.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.idiot.operationbackend.entity.AccountFans;
import com.idiot.operationbackend.vo.NumberStatData;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author wang xiao
 * @date Created in 19:23 2020/9/10
 */
public interface AccountFansMapper extends BaseMapper<AccountFans> {


    /**
     *  粉丝属性 时间范围统计
     * @author wangxiao
     * @date 11:05 2020/9/17
     * @param accountId accountId
     * @param start start
     * @param end end
     * @return java.util.List<com.idiot.operationbackend.vo.NumberStatData>
     */
    @Select("SELECT   " +
            "  sex AS `key`,   " +
            "  COUNT( sex ) AS `number`,   " +
            "  'sex' AS `type`    " +
            "FROM   " +
            "  t_account_fans t    " +
            "WHERE   " +
            "  t.account_id = #{accountId}    " +
            "  AND t.state = 1    " +
            "  AND t.subscribe_time >= #{start}    " +
            "  AND t.subscribe_time <= #{end}    " +
            "GROUP BY   " +
            "  t.sex UNION   " +
            "SELECT   " +
            "  province AS `key`,   " +
            "  COUNT( province ) AS `number`,   " +
            "  'province' AS `type`    " +
            "FROM   " +
            "  t_account_fans t    " +
            "WHERE   " +
            "  t.account_id = #{accountId}    " +
            "  AND t.state = 1    " +
            "  AND t.subscribe_time >= #{start}    " +
            "  AND t.subscribe_time <= #{end}    " +
            "GROUP BY   " +
            "  t.province UNION   " +
            "SELECT   " +
            "  subscribe_scene AS `key`,   " +
            "  COUNT( subscribe_scene ) AS `number`,   " +
            "  'subscribeScene' AS `type`    " +
            "FROM   " +
            "  t_account_fans t    " +
            "WHERE   " +
            "  t.account_id = #{accountId}    " +
            "  AND t.state = 1    " +
            "  AND t.subscribe_time >= #{start}    " +
            "  AND t.subscribe_time <= #{end}    " +
            "GROUP BY   " +
            "  t.subscribe_scene")
    @Results(id = "NumberStatData",value = {
                    @Result(column = "key",property = "key"),
                    @Result(column = "number",property = "number"),
                    @Result(column = "type",property = "type")
            })
    List<NumberStatData> statPropertyByDate(@Param("accountId") String accountId,@Param("start") long start,@Param("end")  long end);


    /**
     *  粉丝属性 全部统计 （不想写动态SQL）
     * @author wangxiao
     * @date 11:05 2020/9/17
     * @param accountId accountId
     * @return java.util.List<com.idiot.operationbackend.vo.NumberStatData>
     */
    @Select("SELECT   " +
            "  sex AS `key`,   " +
            "  COUNT( sex ) AS `number`,   " +
            "  'sex' AS `type`    " +
            "FROM   " +
            "  t_account_fans t    " +
            "WHERE   " +
            "  t.account_id = #{accountId}    " +
            "  AND t.state = 1    " +
            "GROUP BY   " +
            "  t.sex UNION   " +
            "SELECT   " +
            "  province AS `key`,   " +
            "  COUNT( province ) AS `number`,   " +
            "  'province' AS `type`    " +
            "FROM   " +
            "  t_account_fans t    " +
            "WHERE   " +
            "  t.account_id = #{accountId}    " +
            "  AND t.state = 1    " +
            "GROUP BY   " +
            "  t.province UNION   " +
            "SELECT   " +
            "  subscribe_scene AS `key`,   " +
            "  COUNT( subscribe_scene ) AS `number`,   " +
            "  'subscribeScene' AS `type`    " +
            "FROM   " +
            "  t_account_fans t    " +
            "WHERE   " +
            "  t.account_id = #{accountId}    " +
            "  AND t.state = 1    " +
            "GROUP BY   " +
            "  t.subscribe_scene")
    @ResultMap(value = "NumberStatData")
    List<NumberStatData> statPropertyByAll(@Param("accountId") String accountId);


}
