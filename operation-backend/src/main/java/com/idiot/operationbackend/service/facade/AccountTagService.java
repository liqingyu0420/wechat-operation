package com.idiot.operationbackend.service.facade;

import com.baomidou.mybatisplus.extension.service.IService;
import com.idiot.operationbackend.entity.AccountTag;

import java.util.List;

/**
 * 用户标签
 * @author wangxiao
 */
public interface AccountTagService extends IService<AccountTag> {

    /**
     *  覆盖之前的标签
     * @author wangxiao
     * @date 10:06 2020/9/15
     * @param accountId
     * @param newTags
     * @return boolean
     */
    boolean overlayAccountTag(String accountId, List<AccountTag> newTags);


    /**
     *  查询标签
     * @author wangxiao
     * @date 10:10 2020/9/15
     * @param accountId
     * @return java.util.List<com.idiot.operationbackend.entity.AccountTag>
     */
    List<AccountTag> queryAccountTag(String accountId);


    /**
     *  新增
     * @author wangxiao
     * @date 10:29 2020/9/15
     * @param accountTags
     * @return boolean
     */
    boolean addAccountTag (List<AccountTag> accountTags);


    /**
     *  删除
     * @author wangxiao
     * @date 10:29 2020/9/15
     * @param accountTags
     * @return boolean
     */
    boolean delAccountTag (List<AccountTag> accountTags);


    /**
     *  查询标签列表
     * @author wangxiao
     * @date 19:59 2020/9/17
     * @param accountId
     * @return java.util.List<com.idiot.operationbackend.entity.AccountTag>
     */
    List<AccountTag> getTagByAccountId (String accountId);

    /**
     *  创建标签
     * @author wangxiao
     * @date 20:25 2020/9/17
     * @param label
     * @param accountId
     * @param wxId
     * @return boolean
     */
    boolean addTag(String label,String accountId,int wxId);

    /**
     *  更新标签用户数(增加)
     * @author wangxiao
     * @date 21:04 2020/9/17
     * @param accountId
     * @param wxId
     * @param size
     * @return boolean
     */
    boolean addTagSize(String accountId, int wxId, int size);



    /**
     *  更新标签用户数(减少)
     * @author wangxiao
     * @date 21:04 2020/9/17
     * @param accountId
     * @param wxId
     * @param size
     * @return boolean
     */
    boolean subTagSize(String accountId, int wxId, int size);


    /**
     *  获取标签
     * @author wangxiao
     * @date 21:09 2020/9/17
     * @param accountId
     * @param wxId
     * @return com.idiot.operationbackend.entity.AccountTag
     */
    AccountTag getAccountTag(String accountId,int wxId);


    /**
     *  移除标签
     * @author wangxiao
     * @date 16:09 2020/10/29
     * @param accountId accountId
     * @param wxId wxId
     * @return boolean
     */
    boolean removeTag(String accountId,int wxId);


    /**
     *  查询tag id的名称
     * @author wangxiao
     * @date 15:22 2020/10/22
     * @param tagId tagId json
     * @return java.util.List<java.lang.String>
     */
    List<String> queryTagName(String tagId);
}
