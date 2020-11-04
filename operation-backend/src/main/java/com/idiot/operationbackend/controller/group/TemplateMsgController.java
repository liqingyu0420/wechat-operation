package com.idiot.operationbackend.controller.group;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.idiot.operationbackend.entity.Account;
import com.idiot.operationbackend.entity.JobTask;
import com.idiot.operationbackend.entity.TemplateMsg;
import com.idiot.operationbackend.handler.JobScheduleHandler;
import com.idiot.operationbackend.service.facade.AccountService;
import com.idiot.operationbackend.service.facade.TemplateMsgService;
import com.idiot.operationbackend.service.facade.WeChatService;
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
 * 模板消息
 * @author wang xiao
 * @date Created in 19:15 2020/9/22
 */
@RestController
@RequestMapping("/template")
@Api(value = "TemplateMsgController", tags ="模板消息")
public class TemplateMsgController {

    private final Logger logger = LoggerFactory.getLogger(TemplateMsgController.class);

    @Autowired
    private AccountService accountService;

    @Autowired
    private TemplateMsgService templateMsgService;


    @Autowired
    private WeChatService weChatService;

    @Autowired
    private JobScheduleHandler jobScheduleHandler;


    @GetMapping
    @ApiOperation(value = "查询模板消息")
    public ResponseEntity<JsonResult<Page<TemplateMsg>>> pageTemplateMsg (@RequestHeader String token,
                                                                          @RequestParam int page,
                                                                          @RequestParam(required = false) Integer status) {
        String userId = JwtTokenUtil.getUserId(token);
        logger.info("用户：{}查询模板消息",userId);
        List<Account> accounts = accountService.queryAccountByUserId(userId);
        List<String> ids = accounts.stream().map(Account::getId).collect(Collectors.toList());
        Page<TemplateMsg> templateMsgPage = templateMsgService.queryPageTemplateMsg(page,status,ids);
        return ResponseEntity.ok(JsonResult.success(templateMsgPage));
    }

    @PostMapping
    @ApiOperation(value = "新增模板消息")
    public ResponseEntity<JsonResult<Boolean>> addTemplateMsg (@RequestHeader String token,
                                                               @RequestBody @Valid TemplateMsg templateMsg) {
        String userId = JwtTokenUtil.getUserId(token);
        String accountId = templateMsg.getAccountId();
        boolean enable = accountService.queryAccountByUserId(userId).stream()
                .anyMatch(e->accountId.equals(e.getId()));
        if (!enable) {
            throw new CustomException(500,"你没有权限操作该公众号");
        }
        Account account = accountService.getById(accountId);
        if (Objects.isNull(account)) {
            throw new CustomException(500,"选择的公众号不存在");
        }
        String sendTime = templateMsg.getSendTime();
        templateMsg.setHeadImage(account.getHeadImage());
        templateMsg.setNickName(account.getNickName());
        templateMsg.setStatus(Constants.WAITING);
        templateMsg.setCreateTime(LocalDateTime.now().format(Constants.DATE_TIME_FORMATTER));
        boolean addResult = templateMsgService.save(templateMsg);
        JobTask job = new JobTask("模板消息定时推送任务",sendTime,templateMsg.getId(),Constants.TEMPLATE);
        job.setAccountId(accountId);
        job.setNikeName(account.getNickName());
        job.setHeadImage(account.getHeadImage());
        if (addResult){
            jobScheduleHandler.saveToDB(job);
            jobScheduleHandler.addAndRegisterJob(job);
        }
        logger.info("用户：{}查询公众号:{}模板消息,templateMsg:{}",userId,accountId,templateMsg.toString());
        return ResponseEntity.ok(JsonResult.success(addResult));
    }

    @GetMapping("/{accountId}")
    @ApiOperation(value = "模板消息列表")
    public ResponseEntity<JsonResult<Object>> getTemplate (@RequestHeader String token,
                                                           @PathVariable String accountId) {
        String userId = JwtTokenUtil.getUserId(token);
        boolean enable = accountService.queryAccountByUserId(userId).stream()
                .anyMatch(e->accountId.equals(e.getId()));
        if (!enable) {
            throw new CustomException(500,"你没有权限操作该公众号");
        }
        String wxJson =weChatService.getTemplate(accountId);
        JSONObject jsonObject = JSON.parseObject(wxJson);
        Constants.convert(jsonObject);
        return ResponseEntity.ok(JsonResult.success(jsonObject));
    }


    @DeleteMapping("/{msgId}")
    @ApiOperation(value = "删除模板消息")
    public ResponseEntity<JsonResult<Boolean>> delTemplateMsg (@RequestHeader String token,
                                                           @PathVariable String msgId ) {
        String userId = JwtTokenUtil.getUserId(token);
        TemplateMsg templateMsg = templateMsgService.getById(msgId);
        if (Objects.isNull(templateMsg)){
            throw new CustomException(500,"数据不存在");
        }
        String accountId = templateMsg.getAccountId();
        boolean enable = accountService.queryAccountByUserId(userId).stream()
                .anyMatch(e->accountId.equals(e.getId()));
        if (!enable) {
            throw new CustomException(500,"你没有权限操作该公众号");
        }
        boolean removeResult = templateMsgService.removeById(msgId);
        if (removeResult){
            jobScheduleHandler.cancelJob(msgId);
            jobScheduleHandler.removeToDB(msgId);
        }
        return ResponseEntity.ok(JsonResult.success(removeResult));
    }


    @GetMapping("/detail")
    @ApiOperation(value = "查询模板消息")
    public ResponseEntity<JsonResult<TemplateMsg>> getTemplateMsg (@RequestHeader String token,
                                                            @RequestParam String msgId ) {
        String userId = JwtTokenUtil.getUserId(token);
        TemplateMsg templateMsg = templateMsgService.getById(msgId);
        return ResponseEntity.ok(JsonResult.success(templateMsg));
    }

}
