package com.idiot.operationbackend.controller.fans;

import com.idiot.operationbackend.entity.AccountFans;
import com.idiot.operationbackend.entity.FansMsg;
import com.idiot.operationbackend.service.facade.AccountFansService;
import com.idiot.operationbackend.service.facade.AccountService;
import com.idiot.operationbackend.service.facade.FansMsgService;
import com.idiot.operationbackend.support.CustomException;
import com.idiot.operationbackend.support.JsonResult;
import com.idiot.operationbackend.util.JwtTokenUtil;

import com.idiot.operationbackend.vo.AccountMsgData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 粉丝消息
 * @author wang xiao
 * @date Created in 11:20 2020/9/18
 */
@RestController
@RequestMapping("/fansMsg")
@Api(value = "FansMsgController", tags ="粉丝消息")
public class FansMsgController {


    private final Logger logger = LoggerFactory.getLogger(FansMsgController.class);


    @Autowired
    private FansMsgService msgService;

    @Autowired
    private AccountService accountService;


    @Autowired
    private AccountFansService fansService;


    @GetMapping("/msgRead")
    @ApiOperation(value = "查询公众号未读粉丝消息数量")
    public ResponseEntity<JsonResult<List<AccountMsgData>>> getFansMsgReadNum(@RequestHeader String token,
                                                                              @RequestParam String accountIds){
        String userId = JwtTokenUtil.getUserId(token);
        if (StringUtils.isEmpty(accountIds)){
            throw new CustomException(500,"请选择公众号");
        }
        List<String> accountId = new ArrayList<>(Arrays.asList(accountIds.split(",")));
        logger.info("用户:{}查询公众号:{}未读粉丝信息数目",userId,accountIds);
        List<AccountMsgData> accountMsgData = msgService.countFansMsgNumber(accountId);
        if (!CollectionUtils.isEmpty(accountMsgData)) {
            accountMsgData.forEach(e->{
                Optional.ofNullable(accountService.getById(e.getAccountId()))
                        .map(account -> {
                            e.setAccountName(account.getNickName());
                            e.setAccountUrl(account.getHeadImage());
                            return e;
                        });
            });
        }
        return ResponseEntity.ok(JsonResult.success(accountMsgData));
    }



    @GetMapping("/{accountId}")
    @ApiOperation(value = "查询公众号未读粉丝信息")
    public ResponseEntity<JsonResult<List<FansMsg>>> getFansMsg (@RequestParam int page,
                                                                 @PathVariable String accountId,
                                                                 @RequestHeader String token) {
        String userId = JwtTokenUtil.getUserId(token);
        logger.info("用户:{}查询公众号:{}第{}页未读粉丝信息列表，",userId,accountId,page);
        List<FansMsg> fansMsgs = msgService.queryFansMsg(accountId,page);

        if (!CollectionUtils.isEmpty(fansMsgs)) {
            List<String> opedIds = fansMsgs.stream().map(FansMsg::getFromUserName).collect(Collectors.toList());
            List<AccountFans> fansList = fansService.queryAccountFans(opedIds,accountId);
            // fixme 设置用户信息  日后想办法优化
            fansMsgs.forEach(e->{
                fansList.stream()
                        .filter(fans-> e.getFromUserName().equals(fans.getOpenId()))
                        .findFirst()
                        .map(f->{
                            e.setNikeName(f.getNickName());
                            e.setHeadImage(f.getHeadImgUrl());
                            return e;
                        });
            });
        }
        return ResponseEntity.ok(JsonResult.success(fansMsgs));
    }

    @GetMapping("/clearMsg")
    @ApiOperation(value = "标记已读")
    public ResponseEntity<JsonResult<Boolean>> clearMsg (@RequestHeader String token,
                                                         @RequestParam String msgId) {
        String userId = JwtTokenUtil.getUserId(token);
        logger.info("用户:{}标记消息:{}为已读，",userId,msgId);
        boolean ifRead = msgService.updateToRead(msgId);
        return ResponseEntity.ok(JsonResult.success(ifRead));
    }



}
