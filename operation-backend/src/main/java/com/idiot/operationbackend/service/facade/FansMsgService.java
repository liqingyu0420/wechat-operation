package com.idiot.operationbackend.service.facade;

import com.baomidou.mybatisplus.extension.service.IService;
import com.idiot.operationbackend.entity.FansMsg;
import com.idiot.operationbackend.vo.AccountMsgData;

import java.util.List;

/**
 * @author wang xiao
 * @date Created in 14:10 2020/9/18
 */
public interface FansMsgService extends IService<FansMsg> {



    /**
     *  查询公众号未读粉丝数量
     * @author wangxiao
     * @date 14:25 2020/9/18
     * @param accountIds
     * @return java.util.List<com.idiot.operationbackend.vo.AccountMsgData>
     */
    List<AccountMsgData> countFansMsgNumber(List<String> accountIds);


    /**
     *  查询公众号粉丝信息列表
     * @author wangxiao
     * @date 15:07 2020/9/18
     * @param accountId
     * @param page
     * @return java.util.List<com.idiot.operationbackend.entity.FansMsg>
     */
    List<FansMsg> queryFansMsg (String accountId,int page);


    /**
     *  更新为已读
     * @author wangxiao
     * @date 15:41 2020/9/18
     * @param msgId
     * @return boolean
     */
    boolean updateToRead (String msgId);
}
