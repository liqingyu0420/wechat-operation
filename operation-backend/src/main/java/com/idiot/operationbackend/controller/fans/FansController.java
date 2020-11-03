package com.idiot.operationbackend.controller.fans;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.idiot.operationbackend.entity.AccountFans;

import com.idiot.operationbackend.entity.JobTask;
import com.idiot.operationbackend.entity.SubscribeScene;
import com.idiot.operationbackend.service.facade.AccountFansService;
import com.idiot.operationbackend.service.facade.AccountTagService;
import com.idiot.operationbackend.service.facade.SubscribeSceneService;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 粉丝管理
 * @author wang xiao
 * @date Created in 17:55 2020/9/17
 */
@RestController
@RequestMapping("/fans")
@Api(value = "FansController", tags ="粉丝管理")
public class FansController {


    @Autowired
    private AccountFansService fansService;


    @Autowired
    private WeChatService weChatService;

    @Autowired
    private SubscribeSceneService sceneService;


    private final Logger logger = LoggerFactory.getLogger(FansController.class);


    @GetMapping("/{accountId}")
    @ApiOperation(value = "查询公众号粉丝列表")
    public ResponseEntity<JsonResult<Page<AccountFans>>> getPageFans (@RequestHeader String token,
                                                   @PathVariable String accountId,
                                                   @RequestParam Integer page,
                                                   @RequestParam Integer pageSize,
                                                   @RequestParam(required = false) Integer sex,
                                                   @RequestParam(required = false) String city,
                                                   @RequestParam(required = false) String tagId,
                                                   @RequestParam(required = false) String province,
                                                   @RequestParam(required = false) String label,
                                                   @RequestParam(required = false) String startTime,
                                                   @RequestParam(required = false) String scene,
                                                   @RequestParam(required = false) String endTime) {
        String userId = JwtTokenUtil.getUserId(token);
        logger.info("用户:{}查询公众号:{}粉丝数据",userId,accountId);
        Page<AccountFans> fansPage = fansService.pageFans(accountId,sex,city,province,tagId,label,
                startTime,endTime,scene,page,pageSize);
        return ResponseEntity.ok(JsonResult.success(fansPage));
    }



    @PostMapping("/addRemark")
    @ApiOperation(value = "粉丝添加/修改备注")
    public ResponseEntity<JsonResult<Boolean>> addRemark(@RequestHeader String token,
                                                         @RequestParam String remark,
                                                         @RequestParam String openId,
                                                         @RequestParam String accountId) {
        if (StringUtils.isEmpty(remark)){
            throw new CustomException(500,"备注不能为空!");
        }
        String userId = JwtTokenUtil.getUserId(token);
        logger.info("用户:{}设置公众号:{}粉丝:{}备注信息:{}",userId,accountId,openId,remark);
        boolean ifUp = weChatService.updateRemark(accountId,openId,remark);
        return ResponseEntity.ok(JsonResult.success(ifUp));
    }


    @PostMapping("/markTag")
    @ApiOperation(value = "批量标签 最少两个人")
    public ResponseEntity<JsonResult<Boolean>> markTag(@RequestHeader String token,
                                                       @RequestParam String accountId,
                                                       @RequestParam String openIds,
                                                       @RequestParam Integer wxId) {
        String userId = JwtTokenUtil.getUserId(token);
        logger.info("用户:{}设置公众号:{}添加粉丝:{}标签信息:{}",userId,accountId,openIds,wxId);
        List<String> opens = new ArrayList<String>(Arrays.asList(openIds.split(",")));
        boolean ifAdd = weChatService.addUserTag(accountId,opens,wxId);
        if (ifAdd){
            fansService.addItemForTagList(opens,accountId,wxId);
        }
        return ResponseEntity.ok(JsonResult.success(ifAdd));
    }


    @PostMapping("/unMarkTag")
    @ApiOperation(value = "批量减少标签 最少两个人")
    public ResponseEntity<JsonResult<Boolean>> unMarkTag(@RequestHeader String token,
                                                         @RequestParam String accountId,
                                                         @RequestParam String openIds,
                                                         @RequestParam Integer wxId) {
        String userId = JwtTokenUtil.getUserId(token);
        logger.info("用户:{}设置公众号:{}添加粉丝:{}标签信息:{}",userId,accountId,openIds,wxId);
        List<String> opens = new ArrayList<String>(Arrays.asList(openIds.split(",")));
        boolean ifAdd = weChatService.removeUserTag(accountId,opens,wxId);
        if (ifAdd){
            fansService.removeItemForTagList(opens,accountId,wxId);
        }
        return ResponseEntity.ok(JsonResult.success(ifAdd));
    }

    @GetMapping("/subscribeScene")
    @ApiOperation(value = "查询粉丝关注公众号来源类型")
    public ResponseEntity<JsonResult<List<SubscribeScene>>> getSubscribeScene () {
        return ResponseEntity.ok(JsonResult.success(sceneService.list()));
    }

    @GetMapping("/syncUser")
    @ApiOperation(value = "同步粉丝")
    public ResponseEntity<JsonResult<Integer>> syncUser (@RequestParam String accountId) {
       int var =  weChatService.syncAccountUser(accountId);
       return ResponseEntity.ok(JsonResult.success(var));
    }

    @GetMapping("/preFansNum")
    @ApiOperation(value = "预发送粉丝数目")
    public ResponseEntity<JsonResult<Integer>> preFansNum (@RequestHeader String token,
                                                        @RequestParam String accountId,
                                                        @RequestParam(required = false)   Integer selectSex,
                                                        @RequestParam(required = false)   String selectProvince,
                                                        @RequestParam(required = false)   String selectCity,
                                                        @RequestParam(required = false)   String selectTag,
                                                        @RequestParam(required = false)   String subscribeTime) {
        String userId = JwtTokenUtil.getUserId(token);
        int size = fansService.countAccountFans(accountId,selectSex,selectProvince,selectCity,selectTag,subscribeTime);
        return ResponseEntity.ok(JsonResult.success(size));
    }



}
