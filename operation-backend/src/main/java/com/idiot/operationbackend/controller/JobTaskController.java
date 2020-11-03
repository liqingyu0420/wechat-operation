package com.idiot.operationbackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.idiot.operationbackend.entity.Account;
import com.idiot.operationbackend.entity.JobTask;
import com.idiot.operationbackend.handler.JobScheduleHandler;
import com.idiot.operationbackend.service.facade.AccountService;
import com.idiot.operationbackend.service.facade.JobTaskService;
import com.idiot.operationbackend.support.Constants;
import com.idiot.operationbackend.support.CustomException;
import com.idiot.operationbackend.support.JsonResult;
import com.idiot.operationbackend.util.JwtTokenUtil;
import com.idiot.operationbackend.vo.BaseType;
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
import java.util.stream.Collectors;

/**
 * 定时任务
 * @author wang xiao
 * @date Created in 16:47 2020/9/23
 */
@RestController
@RequestMapping("/job")
@Api(value = "JobTaskController", tags ="定时任务")
public class JobTaskController {



    private final Logger logger = LoggerFactory.getLogger(JobTaskController.class);

    @Autowired
    private AccountService accountService;

    @Autowired
    private JobTaskService jobTaskService;

    @Autowired
    private JobScheduleHandler scheduleHandler;


    @GetMapping
    @ApiOperation(value = "定时任务分页列表")
    public ResponseEntity<JsonResult<Page<JobTask>>> pageJobTask (@RequestHeader String token,
                                                      @RequestParam int page,
                                                      @RequestParam(required = false) Integer status) {
        String userId = JwtTokenUtil.getUserId(token);
        logger.info("用户：{}定时任务分页列表",userId);
        List<Account> accounts = accountService.queryAccountByUserId(userId);
        List<String> ids = accounts.stream().map(Account::getId).collect(Collectors.toList());
        Page<JobTask> jobTaskPage = jobTaskService.queryPageJobTask(page,status,ids);
        return ResponseEntity.ok(JsonResult.success(jobTaskPage));
    }

    @DeleteMapping
    @ApiOperation(value = "暂停任务")
    public ResponseEntity<JsonResult<Boolean>> cancelJobTask (@RequestHeader String token,
                                                            @RequestParam String taskKey,
                                                            @RequestParam String accountId) {
        String userId = JwtTokenUtil.getUserId(token);
        boolean enable = accountService.queryAccountByUserId(userId)
                .stream()
                .anyMatch(e->accountId.equals(e.getId()));
        if (!enable) {
            throw new CustomException(500,"你没有权限操作该公众号");
        }
        logger.info("用户：{}暂停定时任务,targetKey:{}",userId,taskKey);
        JobTask jobTask = jobTaskService.queryJobTask(taskKey);
        scheduleHandler.cancelJob(taskKey);
        jobTask.setStatus(Constants.END);
        jobTask.setUpdateTime(LocalDateTime.now().format(Constants.DATE_TIME_FORMATTER));
        boolean upResult = jobTaskService.updateById(jobTask);
        return ResponseEntity.ok(JsonResult.success(upResult));
    }

    @GetMapping("/type")
    @ApiOperation(value = "定时任务类型")
    public ResponseEntity<JsonResult<List<BaseType>>> customerStatus () {

        List<BaseType> baseTypes = new ArrayList<>(4);
        BaseType group = new BaseType(Constants.GROUP,"群发消息");
        BaseType template  = new BaseType(Constants.TEMPLATE,"模板消息");
        BaseType customer  = new BaseType(Constants.CUSTOMER,"客服消息");
        baseTypes.add(group);
        baseTypes.add(template);
        baseTypes.add(customer);
        return ResponseEntity.ok(JsonResult.success(baseTypes));
    }
}
