package com.idiot.operationbackend.controller;


import com.idiot.operationbackend.entity.AuthUser;
import com.idiot.operationbackend.service.facade.AuthUserService;
import com.idiot.operationbackend.support.Constants;
import com.idiot.operationbackend.support.CustomException;
import com.idiot.operationbackend.support.JsonResult;
import com.idiot.operationbackend.util.JwtTokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 认证 用户
 * @author wang xiao
 * @date Created in 15:06 2020/9/10
 */
@RestController
@RequestMapping("/auth")
@Api(value = "AuthController", tags ="认证、授权登录")
public class AuthController  {


    private final Logger logger = LoggerFactory.getLogger(AuthController.class);



    @Autowired
    private AuthUserService userService;



    @PostMapping("/login")
    @ApiOperation(value = "账号登录")
    public ResponseEntity<JsonResult<AuthUser>> login(@RequestBody @Valid AuthUser authUser) {
        logger.info("用户：{}账号密码登录",authUser.getUserCode());
        authUser =userService.queryByUserCodeAndPassword(authUser.getPassword(),authUser.getUserCode());
        if (Objects.isNull(authUser)) {
            throw new CustomException(500,"账号或者密码错误！请检查大小写");
        }
        if (!authUser.getState()) {
            throw new CustomException(500,"您的账号已经被冻结！请联系父属账号");
        }
        authUser.setPassword("");
        String  token = JwtTokenUtil.sign(authUser.getNickName(),authUser.getId());
        return ResponseEntity.ok().header("AUTH_TOKEN",token)
                .body(JsonResult.success(authUser));
    }


    @GetMapping("/subManage")
    @ApiOperation(value = "子账号管理")
    public ResponseEntity<JsonResult<List<AuthUser>>> subUserManage(@RequestHeader String token) {
        String userId = JwtTokenUtil.getUserId(token);
        logger.info("用户：{}子账号管理",userId);
        List<AuthUser> authUsers = userService.queryAllSubAuthUser(userId);
        return ResponseEntity.ok(JsonResult.success(authUsers));
    }


    @PostMapping("/subManage/{userId}")
    @ApiOperation(value = "子账号冻结/解冻")
    public ResponseEntity<JsonResult<Boolean>> manageSubUser(@PathVariable String userId,
                                                             @RequestHeader String  token) {
        String parentUserId = JwtTokenUtil.getUserId(token);
        logger.info("用户：{}操作子账号:{}",parentUserId,userId);
        AuthUser authUser = userService.queryAuthUserByParentIdAndId(parentUserId,userId);
        if (Objects.isNull(authUser)) {
            throw new CustomException(500,"您的账号无权操作其他人的账号，只能管理自己的子账号");
        }
        boolean before = authUser.getState();
        authUser.setState(!before);
        userService.updateById(authUser);
        return ResponseEntity.ok(JsonResult.success(true));
    }


    @GetMapping("/subPre")
    @ApiOperation(value = "获取子账号名称前缀")
    public ResponseEntity<JsonResult<String>> getSubPre(@RequestHeader String  token) {
        String parentUserId = JwtTokenUtil.getUserId(token);
        AuthUser user = userService.getById(parentUserId);
        if (Objects.isNull(user)){
            throw new CustomException(500,"账号不存在");
        }
        return ResponseEntity.ok(JsonResult.success(user.getUserCode()+"@"));
    }

    @PostMapping("/subCreate")
    @ApiOperation(value = "子账号创建")
    public ResponseEntity<JsonResult<Boolean>> createSubUser(@RequestBody AuthUser authUser,
                                                             @RequestHeader String  token) {
        String parentUserId = JwtTokenUtil.getUserId(token);
        logger.info("用户：{}创建 子账号",parentUserId);
        AuthUser parentUser = userService.getById(parentUserId);
        if (Objects.isNull(parentUser) || !StringUtils.isEmpty(parentUser.getParentUserId())) {
            throw new CustomException(500,"您的账号是子账号无权创建子账号");
        }
        if (Objects.isNull(authUser) || StringUtils.isEmpty(authUser.getUserCode())
                || StringUtils.isEmpty(authUser.getPassword())) {
            throw new CustomException(500,"您创建账号信息录入不全，请您补全信息!");
        }
        if (! authUser.getUserCode().startsWith(parentUser.getUserCode())) {
            throw new CustomException(500,"您创建账号信息非法，账号开始部分不容修改！！");
        }
        authUser.setState(true);
        authUser.setId(null);
        String avatarUrl = authUser.getAvatarUrl();
        if (StringUtils.isEmpty(avatarUrl)) {
            avatarUrl = Constants.DEFAULT_HEAD_IMG;
        }
        authUser.setAvatarUrl(avatarUrl);
        authUser.setParentUserId(parentUserId);
        authUser.setCreateTime(Constants.DATE_TIME_FORMATTER.format(LocalDateTime.now()));
        boolean ifSave = userService.saveUserAndDigPassword(authUser);
        return ResponseEntity.ok(JsonResult.success(ifSave));
    }


    @PutMapping("/subUpdate")
    @ApiOperation(value = "子账号更新信息")
    public ResponseEntity<JsonResult<Boolean>> upSubUser(@RequestBody AuthUser authUser,
                                                         @RequestHeader String  token) {
        String parentUserId = JwtTokenUtil.getUserId(token);
        logger.info("用户：{}更新 子账号",parentUserId);
        AuthUser targetUser = userService.queryAuthUserByParentIdAndId(parentUserId,authUser.getId());
        if (Objects.isNull(targetUser)) {
            throw new CustomException(500,"您的账号无权操作其他人的账号，只能管理自己的子账号");
        }
        targetUser.setNickName(authUser.getNickName());
        targetUser.setAvatarUrl(authUser.getAvatarUrl());
        boolean ifSave = userService.updateById(targetUser);
        return ResponseEntity.ok(JsonResult.success(ifSave));
    }







}
