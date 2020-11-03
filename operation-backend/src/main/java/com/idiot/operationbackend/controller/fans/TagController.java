package com.idiot.operationbackend.controller.fans;

import com.idiot.operationbackend.entity.AccountTag;
import com.idiot.operationbackend.service.facade.AccountService;
import com.idiot.operationbackend.service.facade.AccountTagService;
import com.idiot.operationbackend.service.facade.WeChatService;
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
import java.util.List;
import java.util.Objects;

/**
 * 标签管理
 * @author wang xiao
 * @date Created in 19:51 2020/9/17
 */
@RestController
@RequestMapping("/tag")
@Api(value = "TagController", tags ="标签管理")
public class TagController {

    @Autowired
    private AccountTagService tagService;

    @Autowired
    private WeChatService weChatService;

    @Autowired
    private AccountService accountService;

    private final Logger logger = LoggerFactory.getLogger(TagController.class);

    @GetMapping("/list")
    @ApiOperation(value = "查询公众号标签列表")
    public ResponseEntity<JsonResult<List<AccountTag>>> getTagList (@RequestHeader String token,
                                                                    @RequestParam String accountId) {
        if (StringUtils.isEmpty(accountId)) {
            throw new CustomException(500,"请选择公众号!");
        }
        String userId =  JwtTokenUtil.getUserId(token);
        logger.info("用户:{}查询公众号:{}标签",userId,accountId);
        return ResponseEntity.ok(JsonResult.success(tagService.queryAccountTag(accountId)));
    }

    @PostMapping("/addTag")
    @ApiOperation(value = "添加标签")
    public ResponseEntity<JsonResult<Boolean>> addTag (@RequestHeader String token,
                                                       @RequestParam String name,
                                                       @RequestParam String accountId) {
        if (StringUtils.isEmpty(name)) {
            throw new CustomException(500,"请填写标签内容!");
        }
        String userId =  JwtTokenUtil.getUserId(token);
        logger.info("用户:{}查询公众号:{}添加标签",userId,accountId);
        return ResponseEntity.ok(JsonResult.success(weChatService.createTag(accountId,name)));
    }

    @PutMapping("/upTag")
    @ApiOperation(value = "编辑标签")
    public ResponseEntity<JsonResult<Boolean>> upTag (@RequestHeader String token,
                                                      @RequestBody @Valid AccountTag accountTag ) {

        String userId =  JwtTokenUtil.getUserId(token);
        logger.info("用户:{}编辑标签:{}",userId,accountTag);
        boolean ifUp = weChatService.upTag(accountTag.getAccountId(),accountTag.getWxId(),accountTag.getName());
        if (ifUp){
            tagService.updateById(accountTag);
        }
        return ResponseEntity.ok(JsonResult.success(ifUp));
    }

    @GetMapping("/syncTag")
    @ApiOperation(value = "同步标签")
    public ResponseEntity<JsonResult<Boolean>> syncTag (@RequestParam String accountId) {
        weChatService.syncTag(accountId);
        return ResponseEntity.ok(JsonResult.success(true));
    }

    @DeleteMapping("/delTag/{id}")
    @ApiOperation(value = "删除标签")
    public ResponseEntity<JsonResult<Boolean>>  delTag (@PathVariable String id,
                                                        @RequestHeader String token) {
        String userId = JwtTokenUtil.getUserId(token);
        AccountTag tag = tagService.getById(id);
        if (Objects.isNull(tag)){
            throw new CustomException(500,"选择的标签不存在");
        }
        String accountId = tag.getAccountId();
        boolean enable = accountService.queryAccountByUserId(userId).stream()
                .anyMatch(e->accountId.equals(e.getId()));
        if (!enable) {
            throw new CustomException(500,"你没有权限操作该公众号");
        }
        boolean isRemove = weChatService.delTag(accountId,tag.getWxId());
        return ResponseEntity.ok(JsonResult.success(isRemove));
    }

}
