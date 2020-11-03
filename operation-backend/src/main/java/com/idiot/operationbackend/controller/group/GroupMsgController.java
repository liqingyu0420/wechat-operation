package com.idiot.operationbackend.controller.group;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.idiot.operationbackend.entity.Account;
import com.idiot.operationbackend.entity.GroupMsg;
import com.idiot.operationbackend.entity.JobTask;
import com.idiot.operationbackend.handler.JobScheduleHandler;
import com.idiot.operationbackend.service.facade.AccountService;
import com.idiot.operationbackend.service.facade.GroupMsgService;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 高级群发
 * @author wang xiao
 * @date Created in 16:29 2020/9/22
 */
@RestController
@RequestMapping("/group")
@Api(value = "GroupMsgController", tags ="高级群发")
public class GroupMsgController {


    private final Logger logger = LoggerFactory.getLogger(GroupMsgController.class);

    @Autowired
    private AccountService accountService;

    @Autowired
    private GroupMsgService groupMsgService;

    @Autowired
    private JobScheduleHandler jobScheduleHandler;

    @GetMapping
    @ApiOperation(value = "查询高级群发")
    public ResponseEntity<JsonResult<Page<GroupMsg>>> pageGroupMsg (@RequestHeader String token,
                                                                    @RequestParam int page,
                                                                    @RequestParam(required = false) Integer status,
                                                                    @RequestParam(required = false) String startDate,
                                                                    @RequestParam(required = false) String endDate) {
        String userId = JwtTokenUtil.getUserId(token);
        logger.info("用户：{}查询高级群发列表 page:{}",userId,page);
        List<Account> accounts = accountService.queryAccountByUserId(userId);
        List<String> ids = accounts.stream().map(Account::getId).collect(Collectors.toList());
        Page<GroupMsg> groupMsgPage = groupMsgService.queryPageGroupMsg(page,startDate,endDate,status,ids);
        return ResponseEntity.ok(JsonResult.success(groupMsgPage));
    }


    @DeleteMapping("/{msgId}")
    @ApiOperation(value = "删除高级群发")
    public ResponseEntity<JsonResult<Boolean>> delGroupMsg(@RequestHeader String token,
                                                           @PathVariable String msgId) {
        String userId = JwtTokenUtil.getUserId(token);
        logger.info("用户：{}高级群发msgId:{}",userId,msgId);
        GroupMsg groupMsg = groupMsgService.getById(msgId);
        if (Objects.isNull(groupMsg)){
          throw new CustomException(500,"数据不存在");
        }
        String accountId = groupMsg.getAccountId();
        boolean enable = accountService.queryAccountByUserId(userId).stream()
                .anyMatch(e->accountId.equals(e.getId()));
        if (!enable) {
            throw new CustomException(500,"你没有权限操作该公众号");
        }
        boolean delResult = groupMsgService.removeById(msgId);
        if (delResult) {
            jobScheduleHandler.cancelJob(msgId);
            jobScheduleHandler.removeToDB(msgId);
        }

        return ResponseEntity.ok(JsonResult.success(delResult));
    }


    @PostMapping
    @ApiOperation(value = "新建高级群发")
    public ResponseEntity<JsonResult<Boolean>> addGroupMsg(@RequestHeader String token,
                                                           @RequestBody @Valid  GroupMsg groupMsg) {
        String userId = JwtTokenUtil.getUserId(token);
        String accountId = groupMsg.getAccountId();
        boolean enable = accountService.queryAccountByUserId(userId).stream()
                .anyMatch(e->accountId.equals(e.getId()));
        if (!enable) {
            throw new CustomException(500,"你没有权限操作该公众号");
        }
        Account account = accountService.getById(accountId);
        if (Objects.isNull(account)) {
            throw new CustomException(500,"选择的公众号不存在");
        }
        String sendTime = groupMsg.getSendTime();
        groupMsg.setNickName(account.getNickName());
        groupMsg.setHeadImage(account.getHeadImage());
        groupMsg.setStatus(Constants.WAITING);
        groupMsg.setCreateTime(LocalDateTime.now().format(Constants.DATE_TIME_FORMATTER));
        logger.info("用户：{}高级群发groupMsg:{}",userId,groupMsg.toString());
        boolean saveResult = groupMsgService.save(groupMsg);
        JobTask job = new JobTask("高级群发消息定时推送任务",sendTime,groupMsg.getId(),Constants.GROUP);

        job.setAccountId(accountId);
        job.setNikeName(account.getNickName());
        job.setHeadImage(account.getHeadImage());
        if (saveResult) {
            jobScheduleHandler.saveToDB(job);
            jobScheduleHandler.addAndRegisterJob(job);
        }
        return ResponseEntity.ok(JsonResult.success(saveResult));
    }


}
