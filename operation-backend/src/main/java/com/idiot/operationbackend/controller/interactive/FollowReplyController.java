package com.idiot.operationbackend.controller.interactive;

import com.idiot.operationbackend.entity.*;
import com.idiot.operationbackend.service.facade.AccountService;
import com.idiot.operationbackend.service.facade.FollowReplyService;
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
 *  被关注回复
 * @author wang xiao
 * @date Created in 17:30 2020/9/21
 */
@RestController
@RequestMapping("/followReply")
@Api(value = "FollowReplyController", tags ="被关注回复")
public class FollowReplyController {

    private final Logger logger = LoggerFactory.getLogger(FollowReplyController.class);

    @Autowired
    private AccountService accountService;

    @Autowired
    private FollowReplyService followReplyService;


    @GetMapping
    @ApiOperation(value = "查询智能回复列表")
    public ResponseEntity<JsonResult<List<FollowReply>>> getAccountPushList(@RequestHeader String token) {
        String userId = JwtTokenUtil.getUserId(token);
        logger.info("用户:{}查询推送消息列表",userId);
        List<Account> accounts = accountService.queryAccountByUserId(userId);
        List<String> accIds = accounts.stream().map(Account::getId).collect(Collectors.toList());
        List<FollowReply> pushList = followReplyService.queryFollowReply(accIds);
        List<FollowReply> result = new ArrayList<>(accIds.size());
        for (Account account : accounts) {
            FollowReply var = pushList.stream()
                    .filter(e->account.getId().equals(e.getAccountId()))
                    .findAny()
                    .map(e->{e.setSetUp(true); return e;})
                    .orElseGet(()->nonReply(account));
            result.add(var);
        }
        return ResponseEntity.ok(JsonResult.success(result));
    }


    @GetMapping("/{id}")
    @ApiOperation(value = "查看被关注回复详情")
    public ResponseEntity<JsonResult<FollowReply>> getFollowReply(@RequestHeader String token,
                                                                  @PathVariable String id,
                                                                  @RequestParam String accountId) {
        String userId = JwtTokenUtil.getUserId(token);
        logger.info("用户:{}查看被关注回复:{}详情",userId,id);
        FollowReply followReply = followReplyService.getById(id);
        if (Objects.isNull(followReply)){
            Account account = accountService.getById(accountId);
            followReply = nonReply(account);
        }
        return ResponseEntity.ok(JsonResult.success(followReply));
    }

    @PutMapping
    @ApiOperation(value = "修改被关注回复")
    public ResponseEntity<JsonResult<Boolean>> upFollowReply(@RequestHeader String token,
                                                             @RequestBody FollowReply reply) {
        String userId = JwtTokenUtil.getUserId(token);
        logger.info("用户:{}修改被关注回复",userId);
        Optional.ofNullable(accountService.getById(reply.getAccountId())).map(e -> {
            reply.setSetUp(true);
            reply.setNikeName(e.getNickName());
            reply.setHeadImage(e.getHeadImage());
            return  reply;
        });
        reply.setCreateTime(LocalDateTime.now().format(Constants.DATE_TIME_FORMATTER));
        boolean addResult = followReplyService.saveOrUpdate(reply);
        return ResponseEntity.ok(JsonResult.success(addResult));
    }


    @PutMapping("/enable/{id}")
    @ApiOperation(value = "开启或者关闭")
    public ResponseEntity<JsonResult<Boolean>> enableFollowReply(@RequestHeader String token,
                                                             @PathVariable String id,
                                                             @RequestParam Boolean enableVal) {
        String userId = JwtTokenUtil.getUserId(token);
        logger.info("用户:{}开启/关闭被关注回复",userId);
        boolean addResult = followReplyService.upEnable(id,enableVal);
        return ResponseEntity.ok(JsonResult.success(addResult));
    }



    private FollowReply nonReply (Account account){
        FollowReply reply = new FollowReply();
        reply.setNikeName(account.getNickName());
        reply.setHeadImage(account.getHeadImage());
        reply.setAccountId(account.getId());
        reply.setEnable(false);
        reply.setSetUp(false);
        return reply;
    }

}
