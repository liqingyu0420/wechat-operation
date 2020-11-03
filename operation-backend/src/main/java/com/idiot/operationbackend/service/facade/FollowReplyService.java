package com.idiot.operationbackend.service.facade;

import com.baomidou.mybatisplus.extension.service.IService;
import com.idiot.operationbackend.entity.FollowReply;

import java.util.List;

/**
 * @author wang xiao
 * @date Created in 17:35 2020/9/21
 */
public interface FollowReplyService extends IService<FollowReply> {

    /**
     *  查询公告号被关注回复
     * @author wangxiao
     * @date 17:31 2020/9/18
     * @param ids ids
     * @return java.util.List<com.idiot.operationbackend.entity.FollowReply>
     */
    List<FollowReply> queryFollowReply(List<String> ids);



    /**
     * 查询智能回复 (按道理只有一条)
     * @author wangxiao
     * @date 19:31 2020/9/24
     * @param accountId accountId
     * @return com.idiot.operationbackend.entity.FollowReply
     */
    FollowReply queryFollowReplyByAccountId (String accountId);


    /**
     *  更改开关
     * @author wangxiao
     * @date 15:29 2020/10/20
     * @param id id
     * @param val val
     * @return boolean
     */
    boolean upEnable (String id,Boolean val);
}
