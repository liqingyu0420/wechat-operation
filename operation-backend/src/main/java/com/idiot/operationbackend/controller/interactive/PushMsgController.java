package com.idiot.operationbackend.controller.interactive;

import com.idiot.operationbackend.entity.Account;
import com.idiot.operationbackend.entity.AccountPush;
import com.idiot.operationbackend.service.facade.AccountPushService;
import com.idiot.operationbackend.service.facade.AccountService;
import com.idiot.operationbackend.support.Constants;
import com.idiot.operationbackend.support.JsonResult;
import com.idiot.operationbackend.util.JwtTokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 推送
 * @author wang xiao
 * @date Created in 15:56 2020/9/18
 */
@RestController
@RequestMapping("/push")
@Api(value = "PushMsgController", tags ="公众号推送")
public class PushMsgController {



    private final Logger logger = LoggerFactory.getLogger(PushMsgController.class);


    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountPushService pushService;


    @GetMapping
    @ApiOperation(value = "查询推送列表")
    public ResponseEntity<JsonResult<List<AccountPush>>> getAccountPushList(@RequestHeader String token) {
        String userId = JwtTokenUtil.getUserId(token);
        logger.info("用户:{}查询推送消息列表",userId);
        List<Account> accounts = accountService.queryAccountByUserId(userId);
        List<String> accIds = accounts.stream().map(Account::getId).collect(Collectors.toList());
        List<AccountPush> pushList = pushService.queryAccountPush(accIds);
        List<AccountPush> result = new ArrayList<>(accIds.size());
        for (Account account : accounts) {
            AccountPush var = pushList.stream()
                    .filter(e->account.getId().equals(e.getAccountId()))
                    .findAny()
                    .map(e->{e.setSetUp(true);return  e;})
                    .orElseGet(()->nonPush(account));
            result.add(var);
        }
        return ResponseEntity.ok(JsonResult.success(result));
    }

    @PutMapping
    @ApiOperation(value = "修改智能推送")
    public ResponseEntity<JsonResult<Boolean>> upAccountPush(@RequestHeader String token,
                                                              @RequestBody AccountPush push) {
        String userId = JwtTokenUtil.getUserId(token);
        logger.info("用户:{}修改推送消息列表",userId);
        Optional.ofNullable(accountService.getById(push.getAccountId())).map(e->{
            push.setNikeName(e.getNickName());
            push.setHeadImage(e.getHeadImage());
            return  push;
        });
        push.setCreateTime(LocalDateTime.now().format(Constants.DATE_TIME_FORMATTER));
        boolean addResult = pushService.saveOrUpdate(push);
        return ResponseEntity.ok(JsonResult.success(addResult));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "查看推送详情")
    public ResponseEntity<JsonResult<AccountPush>> getAccountPush(@RequestHeader String token,
                                                                  @PathVariable String id,
                                                                  @RequestParam String accountId) {
        String userId = JwtTokenUtil.getUserId(token);
        logger.info("用户:{}查看推送消息:{}详情",userId,id);

        AccountPush accountPush = pushService.getById(id);
        if (Objects.isNull(accountPush)){
            Account account = accountService.getById(accountId);
            accountPush = nonPush(account);
        }
        return ResponseEntity.ok(JsonResult.success(accountPush));
    }

    @PutMapping("/enable/{id}")
    @ApiOperation(value = "开启或者关闭")
    public ResponseEntity<JsonResult<Boolean>> enableFollowReply(@RequestHeader String token,
                                                                 @PathVariable String id,
                                                                 @RequestParam Boolean enableVal) {
        String userId = JwtTokenUtil.getUserId(token);
        logger.info("用户:{}开启/关闭被关注回复",userId);
        boolean addResult = pushService.upEnable(id,enableVal);
        return ResponseEntity.ok(JsonResult.success(addResult));
    }


    private AccountPush nonPush (Account account){
        AccountPush push = new AccountPush();
        push.setNikeName(account.getNickName());
        push.setHeadImage(account.getHeadImage());
        push.setAccountId(account.getId());
        push.setEnable(false);
        push.setSetUp(false);
        return push;
    }
}
