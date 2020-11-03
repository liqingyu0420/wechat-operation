package com.idiot.operationbackend.service.facade;

import com.baomidou.mybatisplus.extension.service.IService;
import com.idiot.operationbackend.entity.AuthUser;

import java.util.List;

/**
 * @author wang xiao
 * @date Created in 15:47 2020/9/10
 */
public interface AuthUserService extends IService<AuthUser> {

    /**
     *  查询用户
     * @author wangxiao
     * @date 16:17 2020/9/10
     * @param password password
     * @param userCode userCode
     * @return com.idiot.operationbackend.entity.AuthUser
     */
    AuthUser queryByUserCodeAndPassword(String password,String userCode);


    /**
     *  查询子账号 有效
     * @author wangxiao
     * @date 17:45 2020/9/12
     * @param userId userId
     * @return java.util.List<com.idiot.operationbackend.entity.AuthUser>
     */
    List<AuthUser> querySubAuthUser(String userId);


    /**
     *  查询子账号 全部
     * @author wangxiao
     * @date 17:45 2020/9/12
     * @param userId userId
     * @return java.util.List<com.idiot.operationbackend.entity.AuthUser>
     */
    List<AuthUser> queryAllSubAuthUser(String userId);


    /**
     *  查询账号
     * @author wangxiao
     * @date 18:48 2020/9/12
     * @param parentId parentId
     * @param id id
     * @return com.idiot.operationbackend.entity.AuthUser
     */
    AuthUser queryAuthUserByParentIdAndId(String parentId,String id);


    /**
     *  保存账号并且 加密密码 用来创建子账号
     * @author wangxiao
     * @date 19:24 2020/9/12
     * @param authUser authUser
     * @return boolean
     */
    boolean saveUserAndDigPassword(AuthUser authUser);


    /**
     *  查询认证授权id
     * @author wangxiao
     * @date 15:06 2020/9/22
     * @param userId userId
     * @return java.lang.String
     */
    String queryAuthId(String userId);
}
