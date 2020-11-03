package com.idiot.operationbackend.controller.interactive;

import com.idiot.operationbackend.entity.Account;
import com.idiot.operationbackend.entity.AccountMenu;
import com.idiot.operationbackend.entity.MenuType;
import com.idiot.operationbackend.service.facade.AccountMenuService;
import com.idiot.operationbackend.service.facade.AccountService;
import com.idiot.operationbackend.service.facade.MenuTypeService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 自定义菜单
 * @author wang xiao
 * @date Created in 20:15 2020/9/21
 */
@RestController
@RequestMapping("/menu")
@Api(value = "AccountMenuController", tags ="微信公众号菜单")
public class AccountMenuController {

    private final Logger logger = LoggerFactory.getLogger(AccountMenuController.class);


    @Autowired
    private AccountMenuService menuService;


    @Autowired
    private AccountService accountService;

    @Autowired
    private WeChatService weChatService;

    @Autowired
    private MenuTypeService menuTypeService;


    @GetMapping("/{accountId}")
    @ApiOperation(value = "查询公众号菜单")
    public ResponseEntity<JsonResult<List<AccountMenu>>> accountMenu (@RequestHeader String token,
                                                                      @PathVariable String accountId) {

        String userId = JwtTokenUtil.getUserId(token);
        logger.info("用户:{}查询公众号{}菜单信息",userId,accountId);
        List<AccountMenu> accountMenus = menuService.queryMenuByAccountId(accountId);
        return ResponseEntity.ok(JsonResult.success(accountMenus));
    }

    @PostMapping("/{accountId}")
    @ApiOperation(value = "新增公众号菜单")
    public ResponseEntity<JsonResult<Boolean>> addAccountMenu (@RequestHeader String token,
                                                                        @PathVariable String accountId,
                                                                        @RequestBody List<AccountMenu> accountMenus ) {

        String userId = JwtTokenUtil.getUserId(token);
        logger.info("用户:{}添加公众号{}菜单信息",userId,accountId);
        List<Account> accounts = accountService.queryAccountByUserId(userId);
        boolean enable = accounts.stream().anyMatch(e->accountId.equals(e.getId()));
        if (!enable) {
            throw new CustomException(500,"该公众号不属于您的授权范围");
        }
        boolean addResult = menuService.saveBatch(accountMenus);
        return ResponseEntity.ok(JsonResult.success(addResult));
    }

    @PutMapping("/{accountId}")
    @ApiOperation(value = "修改公众号菜单")
    public ResponseEntity<JsonResult<Boolean>> upAccountMenu (@RequestHeader String token,
                                                               @PathVariable String accountId,
                                                               @RequestBody List<AccountMenu> accountMenus ) {

        String userId = JwtTokenUtil.getUserId(token);
        List<Account> accounts = accountService.queryAccountByUserId(userId);
        boolean enable = accounts.stream().anyMatch(e->accountId.equals(e.getId()));
        if (!enable) {
            throw new CustomException(500,"该公众号不属于您的授权范围");
        }
        logger.info("用户:{}修改公众号{}菜单信息",userId,accountId);
        boolean addResult = menuService.updateBatchById(accountMenus);
        return ResponseEntity.ok(JsonResult.success(addResult));
    }

    @GetMapping("/editing/{accountId}")
    @ApiOperation(value = "发布公众号菜单")
    public ResponseEntity<JsonResult<Boolean>> editingAccountMenu (@RequestHeader String token,
                                                              @PathVariable String accountId) {

        String userId = JwtTokenUtil.getUserId(token);
        List<Account> accounts = accountService.queryAccountByUserId(userId);
        boolean enable = accounts.stream().anyMatch(e->accountId.equals(e.getId()));
        if (!enable) {
            throw new CustomException(500,"该公众号不属于您的授权范围");
        }

        logger.info("用户:{}发布公众号{}菜单信息",userId,accountId);
        List<AccountMenu> accountMenus = menuService.queryMenuByAccountId(accountId);
        boolean editResult = weChatService.createMenu(accountMenus,accountId);
        return ResponseEntity.ok(JsonResult.success(editResult));
    }


    @DeleteMapping("/{accountId}")
    @ApiOperation(value = "删除公众号菜单")
    public ResponseEntity<JsonResult<Boolean>> delAccountMenu (@RequestHeader String token,
                                                               @PathVariable String accountId) {

        String userId = JwtTokenUtil.getUserId(token);
        List<Account> accounts = accountService.queryAccountByUserId(userId);
        boolean enable = accounts.stream().anyMatch(e->accountId.equals(e.getId()));
        if (!enable) {
            throw new CustomException(500,"该公众号不属于您的授权范围");
        }

        logger.info("用户:{}删除公众号{}菜单信息",userId,accountId);
        boolean editResult = weChatService.delMenu(accountId);
        return ResponseEntity.ok(JsonResult.success(editResult));
    }

    @GetMapping("/type")
    @ApiOperation(value = "菜单类型")
    public ResponseEntity<JsonResult<List<MenuType>>> menuType () {
        return ResponseEntity.ok(JsonResult.success(menuTypeService.list()));
    }

}
