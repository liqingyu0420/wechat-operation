package com.idiot.operationbackend.controller.account;

import com.idiot.operationbackend.entity.Account;
import com.idiot.operationbackend.entity.AuthUser;
import com.idiot.operationbackend.service.facade.*;
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
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 公众号 管理
 * @author wang xiao
 * @date Created in 19:50 2020/9/10
 */
@RestController
@RequestMapping("/account")
@Api(value = "AccountController", tags ="公众号")
public class AccountController {

    private final Logger logger = LoggerFactory.getLogger(AccountController.class);


    @Autowired
    private AccountService accountService;


    @Autowired
    private WeChatService weChatService;

    @Autowired
    private AccountFansService fansService;

    @Autowired
    private FansActionStatService fansActionStatService;



    @GetMapping
    @ApiOperation(value = "查询公众号列表")
    public ResponseEntity<JsonResult<List<Account>>> list (@RequestHeader String token) {
        String userId = JwtTokenUtil.getUserId(token);
        logger.info("用户:{}查询公众号列表",userId);

        List<Account> accounts  = accountService.queryAccountByUserId(userId);
        if (null == accounts){
            accounts = new ArrayList<>();
        }
        if (CollectionUtils.isEmpty(accounts)){
            throw new CustomException(501,"当前帐号和子账号下没有认证的公众号，请前往公众号管理中心添加认证公众号");
        }
        LocalDateTime end  = LocalDateTime.now();
        long  start = end.plusDays(-2).toEpochSecond(Constants.DEFAULT_ZONE);
        accounts.forEach(e->{
            e.setFansNum(fansService.countFans(e.getId()));
            e.setInteractFansNum(fansActionStatService.countInactiveFansNum(e.getId(),start,end.toEpochSecond(Constants.DEFAULT_ZONE)));
            e.clearField();
        });
        return ResponseEntity.ok(JsonResult.success(accounts));
    }



    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询微信公众号")
    public ResponseEntity<JsonResult<Account>> getAccount (@RequestHeader String token,
                                                           @PathVariable String id) {
        String userId = JwtTokenUtil.getUserId(token);
        logger.info("用户:{}查询公众号:{}",userId,id);
        Account account = accountService.getById(id);
        account.clearField();
        return ResponseEntity.ok(JsonResult.success(account));
    }

    @GetMapping("/preAuth")
    @ApiOperation(value = "获取预授权码地址，微信认证公众号")
    public ResponseEntity<JsonResult<String>> getPreAuthUrl ()   {
        return ResponseEntity.ok(JsonResult.success(weChatService.getPreAuthUrl()));
    }


    @GetMapping("/confirmAccount")
    @ApiOperation(value = "确认授权的微信公众号")
    public ResponseEntity<JsonResult<Boolean>> confirmAccount (@RequestHeader String token,
                                                               @RequestParam String accountId) {

        String userId = JwtTokenUtil.getUserId(token);
        logger.error("微信公众号：{}授权,用户：{}确认 ,",accountId,userId);
        boolean ifUpdate = weChatService.confirmAccount(accountId,userId);
        if (ifUpdate){
            logger.error("微信公众号：{}授权,用户：{} 成功确认 ,开始同步用户标签数据",accountId,userId);
            int userCount = weChatService.syncAccountUser(accountId);
            weChatService.syncTag(accountId);
            logger.warn("同步用户数量:{}",userCount);
            weChatService.syncTag(accountId);
        }

        return ResponseEntity.ok(JsonResult.success(ifUpdate));
    }


    @DeleteMapping("/{accountId}")
    @ApiOperation(value = "删除微信公众号")
    public ResponseEntity<JsonResult<Boolean>> delAccount (@RequestHeader String token,
                                                           @PathVariable String accountId) {

        String userId = JwtTokenUtil.getUserId(token);
        logger.info("用户:{}删除公众号{}",userId,accountId);
        List<Account> accounts = accountService.queryAccountByUserId(userId);
        boolean enable = accounts.stream().anyMatch(e->accountId.equals(e.getId()));
        if (!enable) {
            throw new CustomException(500,"该公众号不属于您的授权范围");
        }
        boolean ifUpdate = accountService.removeById(accountId);
        return ResponseEntity.ok(JsonResult.success(ifUpdate));
    }



}
