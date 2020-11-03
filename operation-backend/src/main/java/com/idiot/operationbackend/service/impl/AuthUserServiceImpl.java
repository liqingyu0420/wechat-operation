package com.idiot.operationbackend.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.idiot.operationbackend.entity.AuthUser;
import com.idiot.operationbackend.mappers.AuthUserMapper;
import com.idiot.operationbackend.service.facade.AuthUserService;
import com.idiot.operationbackend.support.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;


/**
 * @author wang xiao
 * @date Created in 15:48 2020/9/10
 */
@Service
public class AuthUserServiceImpl extends ServiceImpl<AuthUserMapper, AuthUser>
        implements AuthUserService {


    @Override
    public AuthUser queryByUserCodeAndPassword(String password, String userCode) {
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        return getOne(Wrappers.<AuthUser>lambdaQuery()
                .eq(AuthUser::getUserCode,userCode)
                .eq(AuthUser::getPassword,password),false);
    }


    @Override
    public List<AuthUser> querySubAuthUser(String userId) {
        return list(Wrappers.<AuthUser>lambdaQuery().eq(AuthUser::getParentUserId,userId).eq(AuthUser::getState,"1"));
    }


    @Override
    public List<AuthUser> queryAllSubAuthUser(String userId) {
        return list(Wrappers.<AuthUser>lambdaQuery().eq(AuthUser::getParentUserId,userId));
    }

    @Override
    public AuthUser queryAuthUserByParentIdAndId(String parentId, String id) {
        return getOne(Wrappers.<AuthUser>lambdaQuery().eq(AuthUser::getId,id).
                eq(AuthUser::getParentUserId,parentId),false);
    }


    @Override
    public boolean saveUserAndDigPassword(AuthUser authUser) {
        String password = authUser.getPassword();
        if (!StringUtils.isEmpty(password)) {
            password = DigestUtils.md5DigestAsHex(password.getBytes());
            authUser.setPassword(password);
        }
        return save(authUser);
    }


    @Override
    public String queryAuthId(String userId) {
        AuthUser user  = getById(userId);
        if (Objects.isNull(user)) {
            throw new CustomException(500,"用户不存在");
        }
        return Objects.nonNull(user.getParentUserId())?user.getParentUserId():userId;
    }
}
