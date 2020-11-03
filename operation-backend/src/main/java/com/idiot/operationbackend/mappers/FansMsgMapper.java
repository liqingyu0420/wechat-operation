package com.idiot.operationbackend.mappers;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.idiot.operationbackend.entity.FansMsg;
import com.idiot.operationbackend.vo.AccountMsgData;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author wang xiao
 * @date Created in 14:10 2020/9/18
 */
public interface FansMsgMapper extends BaseMapper<FansMsg> {


    /**
     *  查询 公众号未读消息
     * @author wangxiao
     * @date 14:38 2020/9/18
     * @param accountIds
     * @return java.util.List<com.idiot.operationbackend.vo.AccountMsgData>
     */
    @Select(value = {"<script>",
            "SELECT " +
            " t.account_id AS account_id, " +
            " COUNT( t.account_id ) AS `no_number`  " +
            "FROM " +
            " t_fans_msg t  " +
            "WHERE " +
            " t.`read` = 0  " +
            " AND t.account_id IN   " +
            " <foreach collection=\"accountIds\" index=\"index\" item=\"item\"  open=\"(\"  separator=\",\" close=\")\"> " +
            "        #{item}   " +
            " </foreach>"   +
            "GROUP BY " +
            " t.account_id;"
            ,"</script>"})

    @Results(value = {
         @Result(column = "account_id",property = "accountId"),
         @Result(column = "no_number",property = "number")
    })
    List<AccountMsgData> countAccountNumber(@Param("accountIds") List<String> accountIds);
}
