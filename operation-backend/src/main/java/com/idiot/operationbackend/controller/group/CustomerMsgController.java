package com.idiot.operationbackend.controller.group;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.idiot.operationbackend.entity.Account;
import com.idiot.operationbackend.entity.CustomerMsg;
import com.idiot.operationbackend.entity.JobTask;
import com.idiot.operationbackend.handler.JobScheduleHandler;
import com.idiot.operationbackend.service.facade.AccountService;
import com.idiot.operationbackend.service.facade.AuthUserService;
import com.idiot.operationbackend.service.facade.CustomerMsgService;
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

import javax.validation.Valid;
import java.time.LocalDateTime;

/**
 * 客服消息
 * @author wang xiao
 * @date Created in 14:48 2020/9/22
 */
@RestController
@RequestMapping("/customer")
@Api(value = "CustomerMsgController", tags ="客服消息")
public class CustomerMsgController {

    private final Logger logger = LoggerFactory.getLogger(CustomerMsgController.class);

    @Autowired
    private AuthUserService userService;

    @Autowired
    private CustomerMsgService customerMsgService;


    @Autowired
    private JobScheduleHandler jobScheduleHandler;

    @Autowired
    private AccountService accountService;


    @GetMapping
    @ApiOperation(value = "查询客服消息")
    public ResponseEntity<JsonResult<Page<CustomerMsg>>> pageCustomerMsg (@RequestHeader String token,
                                                                          @RequestParam int page,
                                                                          @RequestParam(required = false) Integer status,
                                                                          @RequestParam(required = false) String startDate,
                                                                          @RequestParam(required = false) String endDate) {
        String userId = JwtTokenUtil.getUserId(token);
        // fixme 使用authId 是因为刚开始设计的是多个公众号发送客服消息
        String authId = userService.queryAuthId(userId);
        logger.info("用户：{}查询客服消息列表 page:{},authId:{}",userId,page,authId);
        Page<CustomerMsg> customerMsgPage = customerMsgService.queryPageCustomerMsg(status,page,startDate,endDate,authId);
        customerMsgPage.getRecords().forEach(e->{
            Account account = accountService.getById(e.getAccountId());
            e.setHeadImage(account.getHeadImage());
            e.setNickName(account.getNickName());
        });
        return ResponseEntity.ok(JsonResult.success(customerMsgPage));

    }






    // fixme 预估人数暂时还没有
    @PostMapping
    @ApiOperation(value = "新增客服消息状")
    public ResponseEntity<JsonResult<Boolean>> addCustomerMsg (@RequestHeader String token,
                                                              @RequestBody @Valid CustomerMsg customerMsg) {
        String userId = JwtTokenUtil.getUserId(token);
        String authId = userService.queryAuthId(userId);
        logger.info("用户：{}新增客服消息详情列表，customerMsg:{}",userId,customerMsg.toString());
        customerMsg.setAuthId(authId);
        customerMsg.setCreateTime(LocalDateTime.now().format(Constants.DATE_TIME_FORMATTER));
        customerMsg.setStatus(Constants.WAITING);
        String sendTime = customerMsg.getSendTime();
        boolean addResult =  customerMsgService.save(customerMsg);
        JobTask customerJob = new JobTask("客服消息定时推送任务",sendTime,customerMsg.getId(),Constants.CUSTOMER);
        jobScheduleHandler.saveToDB(customerJob);
        jobScheduleHandler.addAndRegisterJob(customerJob);
        return ResponseEntity.ok(JsonResult.success(addResult));
    }

}
