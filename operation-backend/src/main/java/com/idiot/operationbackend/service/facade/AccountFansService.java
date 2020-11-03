package com.idiot.operationbackend.service.facade;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.idiot.operationbackend.entity.AccountFans;
import com.idiot.operationbackend.vo.NumberStatData;

import java.util.List;

/**
 * @author wang xiao
 * @date Created in 19:24 2020/9/10
 */
public interface AccountFansService extends IService<AccountFans> {


    /**
     *  openId和公众号id  查询用户
     * @author wangxiao
     * @date 18:59 2020/9/14
     * @param accountId accountId
     * @param openId openId
     * @return com.idiot.operationbackend.entity.AccountFans
     */
    AccountFans queryByAccountIdAndOpenId (String accountId,String openId);


    /**
     *  统计粉丝总数目
     * @author wangxiao
     * @date 14:26 2020/9/16
     * @param accountId accountId
     * @return int
     */
    int countFansByAccountId(String accountId);


    /**
     *  查询一段时间内的粉丝
     * @author wangxiao
     * @date 18:39 2020/9/16
     * @param accountId accountId
     * @param startDate startDate
     * @param endDate endDate
     * @return java.util.List<com.idiot.operationbackend.entity.AccountFans>
     */
    List<AccountFans> queryFansByDateAndDate (String accountId,String startDate,String endDate);


    /**
     *  粉丝属性得统计
     * @author wangxiao
     * @date 11:03 2020/9/17
     * @param accountId accountId
     * @param startDate startDate
     * @param endDate endDate
     * @return java.util.List<com.idiot.operationbackend.vo.NumberStatData>
     */
    List<NumberStatData> statByFansProperty (String accountId,String startDate,String endDate);


    /**
     *  粉丝列表
     * @author wangxiao
     * @date 19:18 2020/9/17
     * @param accountId accountId
     * @param sex sex
     * @param city city
     * @param province province
     * @param tagId tagId
     * @param label label
     * @param startTime startTime
     * @param endTime endTime
     * @param page page
     * @param pageSize pageSize
     * @param scene  scene
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.idiot.operationbackend.entity.AccountFans>
     */
    Page<AccountFans> pageFans(String accountId,Integer sex, String city,String province,String tagId,String label,String startTime,String endTime ,String scene, int page,int pageSize );

    /**
     *  更新备注
     * @author wangxiao
     * @date 19:42 2020/9/17
     * @param accountId accountId
     * @param openId openId
     * @param remark remark
     * @return boolean
     */
    boolean updateFansRemark(String accountId, String openId, String remark);


    /**
     *  用户标签里面 添加元素
     * @author wangxiao
     * @date 9:53 2020/9/18 
     * @param openIds openIds
     * @param accountId accountId
     * @param wxId wxId
     * @return boolean
     */
    boolean addItemForTagList(List<String> openIds,String accountId,int wxId);


    /**
     *  移除标签
     * @author wangxiao
     * @date 10:07 2020/9/18
     * @param openIds openIds
     * @param accountId accountId
     * @param wxId wxId
     * @return boolean
     */
    boolean removeItemForTagList(List<String> openIds,String accountId,int wxId);



    /**
     *  查询粉丝
     * @author wangxiao
     * @date 15:26 2020/9/18
     * @param openIds openIds
     * @param accountId accountId
     * @return java.util.List<com.idiot.operationbackend.entity.AccountFans>
     */
    List<AccountFans> queryAccountFans(List<String> openIds,String accountId);


    /**
     *  查询粉丝数量
     * @author wangxiao
     * @date 18:17 2020/9/23
     * @param accountId accountId
     * @param sex sex
     * @param province province
     * @param city city
     * @param tags tags
     * @param subscribeTime subscribeTime
     * @return java.util.List<com.idiot.operationbackend.entity.AccountFans>
     */
    List<AccountFans> queryAccountFans(String accountId,Integer sex,String province,String city,String tags,String subscribeTime);



    /**
     *  查询粉丝数量
     * @author wangxiao
     * @date 18:17 2020/9/23
     * @param accountId accountId
     * @param sex sex
     * @param province province
     * @param city city
     * @param tags tags
     * @param subscribeTime subscribeTime
     * @return  int size
     */
    int countAccountFans(String accountId,Integer sex,String province,String city,String tags,String subscribeTime);


    /**
     *  取关
     * @author wangxiao
     * @date 20:21 2020/9/24
     * @param accountId accountId
     * @param openId openId
     */
    void unsubscribe(String accountId,String openId);


    /**
     *  统计粉丝数
     * @author wangxiao
     * @date 15:59 2020/9/25
     * @param accountId accountId
     * @return int 结果
     */
    int countFans (String accountId);
}
