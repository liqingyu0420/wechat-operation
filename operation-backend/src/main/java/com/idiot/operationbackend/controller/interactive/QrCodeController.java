package com.idiot.operationbackend.controller.interactive;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.idiot.operationbackend.entity.*;
import com.idiot.operationbackend.service.facade.*;
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
import java.util.stream.Collectors;

/**
 * 二维码
 * @author wang xiao
 * @date Created in 18:03 2020/9/21
 */
@RestController
@RequestMapping("/qr")
@Api(value = "QrCodeController", tags ="二维码")
public class QrCodeController {


    private final Logger logger = LoggerFactory.getLogger(QrCodeController.class);

    @Autowired
    private AccountService accountService;

    @Autowired
    private QrCodeService qrCodeService;


    @Autowired
    private WeChatService weChatService;


    @GetMapping
    @ApiOperation(value = "查询二维码列表")
    public ResponseEntity<JsonResult<Page<QrCode>>> qrCodePage (@RequestHeader String token,@RequestParam String accountId,@RequestParam Integer page) {
        String userId = JwtTokenUtil.getUserId(token);
        logger.info("用户:{}查询二维码列表",userId);
        boolean enable = accountService.queryAccountByUserId(userId).stream()
                .anyMatch(e->accountId.equals(e.getId()));
        if (!enable) {
            throw new CustomException(500,"你没有权限操作该公众号");
        }
        return ResponseEntity.ok(JsonResult.success(qrCodeService.pageQrCode(page,accountId)));
    }


    @GetMapping("/{id}")
    @ApiOperation(value = "查询二维码详情")
    public ResponseEntity<JsonResult<QrCode>> qrCode (@RequestHeader String token,
                                                      @PathVariable String id) {
        String userId = JwtTokenUtil.getUserId(token);
        logger.info("用户:{}查询二维码:{}详情",userId,id);
        QrCode qrCode = qrCodeService.getById(id);
        return ResponseEntity.ok(JsonResult.success(qrCode));
    }

    @PostMapping
    @ApiOperation(value = "新增二维码")
    public ResponseEntity<JsonResult<Boolean>> addQrCode (@RequestHeader String token,
                                                          @RequestBody @Valid  QrCode qrCode) {
        String userId = JwtTokenUtil.getUserId(token);
        logger.info("用户:{}新增二维码",userId);
        String accountId = qrCode.getAccountId();
        Account account = accountService.getById(accountId);
        qrCode.setNikeName(account.getNickName());
        qrCode.setHeadImage(account.getHeadImage());
        qrCode.setCreateTime(LocalDateTime.now().format(Constants.DATE_TIME_FORMATTER));
        qrCodeService.save(qrCode);
        String wxJson = weChatService.createQrCode(qrCode,accountId);
        boolean upResult = qrCodeService.upQrCode(qrCode,wxJson);
        return ResponseEntity.ok(JsonResult.success(upResult));
    }

    @PutMapping
    @ApiOperation(value = "修改二维码")
    public ResponseEntity<JsonResult<Boolean>> upQrCode (@RequestHeader String token,
                                                          @RequestBody @Valid  QrCode qrCode) {
        String userId = JwtTokenUtil.getUserId(token);
        logger.info("用户:{}修改二维码",userId);
        String accountId = qrCode.getAccountId();
        boolean enable = accountService.queryAccountByUserId(userId).stream()
                .anyMatch(e->accountId.equals(e.getId()));
        if (!enable) {
            throw new CustomException(500,"你没有权限操作该公众号");
        }
        qrCode.setFollowNum(0L);
        qrCode.setNewNum(0L);
        qrCode.setTotalNum(0L);
        qrCode.setCreateTime(LocalDateTime.now().format(Constants.DATE_TIME_FORMATTER));
        boolean upResult = qrCodeService.updateById(qrCode);
        return ResponseEntity.ok(JsonResult.success(upResult));
    }
}
