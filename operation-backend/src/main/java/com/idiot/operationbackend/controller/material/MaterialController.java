package com.idiot.operationbackend.controller.material;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.idiot.operationbackend.entity.Material;
import com.idiot.operationbackend.service.facade.AccountService;
import com.idiot.operationbackend.service.facade.MaterialService;
import com.idiot.operationbackend.service.facade.WeChatService;
import com.idiot.operationbackend.support.*;
import com.idiot.operationbackend.support.enums.MediaType;
import com.idiot.operationbackend.util.JwtTokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;


/**
 * 素材
 * @author wang xiao
 * @date Created in 17:57 2020/9/18
 */
@RestController
@RequestMapping("/material")
@Api(value = "MaterialController", tags ="素材")
public class MaterialController {


    private final Logger logger = LoggerFactory.getLogger(MaterialController.class);

    private final String splitStr = ",";

    @Autowired
    private WeChatService weChatService;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private AccountService accountService;

    @GetMapping
    @ApiOperation(value = "查询公众号素材")
    public ResponseEntity<JsonResult<Page<Material>>> materialPage (@RequestHeader String token,
                                                          @RequestParam String accountId,
                                                          @RequestParam String type,
                                                          @RequestParam int page){
        String userId = JwtTokenUtil.getUserId(token);
        logger.info("用户：{}查询微信公众号{}素材,素材类型:{}-------------start",userId,accountId,type);
        Page<Material> materialPage = materialService.pageMaterial(accountId,type,page);
        logger.info("用户：{}查询微信公众号{}素材,素材类型:{}-------------end",userId,accountId,type);
        return ResponseEntity.ok(JsonResult.success(materialPage));
    }


    @PostMapping("/upImage")
    @ApiOperation(value = "上传图片并获得微信url")
    public ResponseEntity<JsonResult<String>> upImage ( @RequestHeader String token,
                                                         @RequestParam String accountId,
                                                            MultipartFile file){

        String userId = JwtTokenUtil.getUserId(token);
        boolean enable = accountService.queryAccountByUserId(userId).stream()
                .anyMatch(e->accountId.equals(e.getId()));
        if (!enable) {
            throw new CustomException(500,"你没有权限操作该公众号");
        }
        long size  = file.getSize();
        String fileName = file.getName();
        logger.info("用户：{}上传图片获取url-------------start",userId);
        String wxResult = null;
        try {
            WxInputStreamResource inputStreamResource = new WxInputStreamResource(file.getInputStream(),fileName,size);
            wxResult = weChatService.upImage(accountId,inputStreamResource);
        }catch (IOException e) {
            logger.error("上传文件发生IO异常:{}",e.getMessage());
        }
        return ResponseEntity.ok(JsonResult.success(wxResult));
    }

    @PostMapping
    @ApiOperation(value = "公众号上传素材(其他素材,分别有图片（image）、语音（voice）、视频（video）和缩略图（thumb）)")
    @ApiParam(value = "type",name = "type",allowableValues = "image,voice,video,thumb")
    @CrossOrigin("*")
    public ResponseEntity<JsonResult<Boolean>> addMaterial (@RequestHeader String token,
                                                            @RequestParam String accountId,
                                                            @RequestParam String type,
                                                            @RequestParam(required = false) String title,
                                                            @RequestParam(required = false) String description,
                                                            MultipartFile file){

        String userId = JwtTokenUtil.getUserId(token);
        boolean enable = accountService.queryAccountByUserId(userId).stream()
                .anyMatch(e->accountId.equals(e.getId()));
        if (!enable) {
            throw new CustomException(500,"你没有权限操作该公众号");
        }
        long size  = file.getSize();
        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        logger.info("type is:{},fileName is :{}  suffix is :{}",type,fileName,suffix);
        switch (type){
            case Constants.IMAGE:
                validImg(size,suffix);
                break;
            case Constants.VIDEO:
                validVideo(size,suffix);
                break;
            case Constants.VOICE:
                validVoice(size,suffix);
                break;
            case Constants.THUMB:
                validThumb(size,suffix);
                break;
            default:
                throw new CustomException(500,"其他类型素材暂不支持上传");
        }
        logger.info("用户：{}上传素材到微信公众号{}服务器,素材名称：{},素材类型:{}-------------start",userId,accountId,fileName,type);
        Material material = new Material();
        material.setAccountId(accountId);
        material.setCreateTime(LocalDateTime.now().format(Constants.DATE_TIME_FORMATTER));
        material.setType(MediaType.labelOf(type));
        material.setTitle(title);
        material.setDescription(description);
        material.setAuthor(userId);
        String wxResult = null;
        try {
            WxInputStreamResource inputStreamResource = new WxInputStreamResource(file.getInputStream(),fileName,size);
            wxResult = weChatService.addMaterial(accountId,type,inputStreamResource,title,description);
        }catch (IOException e) {
            logger.error("上传文件发生IO异常:{}",e.getMessage());
        }
        boolean addResult = materialService.addMaterial(material,wxResult);
        logger.info("用户：{}上传素材到微信公众号{}服务器,素材名称：{},素材类型:{}-------------end,结果:{}",userId,accountId,fileName,type,addResult);
        return ResponseEntity.ok(JsonResult.success(addResult));
    }

    @GetMapping("/materialDetail")
    @ApiOperation(value = "查询素材详情")
    public ResponseEntity<JsonResult<Object>> queryWechatMaterialDetail (@RequestParam String accountId,
                                                                         @RequestParam String meidaId) {
        String wxJson = weChatService.getMaterialDetail(accountId, meidaId);
        JSONObject jsonObject = JSON.parseObject(wxJson);
        return ResponseEntity.ok(JsonResult.success(jsonObject));
    }

    private void validImg(long size, String suffixName) {
        if (size > Constants.IMAGE_MAX_SIZE) {
            throw new CustomException(500,"文件太大,图片文件的大小最大为10M,请重新上传!");
        }
        if (!Arrays.asList(Constants.IMAGE_SUPPORTED.split(splitStr)).contains(suffixName)) {
            throw new CustomException(500,"图片格式不支持,请选择bmp/png/jpeg/jpg/gif的任意一种!");
        }
    }

    private void validVoice(long size, String suffixName) {
        if (size > Constants.VOICE_MAX_SIZE) {
            throw new CustomException(500,"文件太大,音频文件的大小最大为2M,请重新上传!");
        }
        if (!Arrays.asList(Constants.VOICE_SUPPORTED.split(splitStr)).contains(suffixName)) {
            throw new CustomException(500,"图片格式不支持,请选择mp3/wma/wav/amr的任意一种!");
        }
    }

    private void validVideo(long size, String suffixName) {
        if (size > Constants.VIDEO_MAX_SIZE) {
            throw new CustomException(500,"文件太大,视频文件的大小最大为10M,请重新上传!");
        }
        if (!suffixName.endsWith(Constants.VIDEO_SUPPORTED)) {
            throw new CustomException(500,"视频格式不支持,请选择mp4格式!");
        }
    }

    private void validThumb(long size, String suffixName) {
        if (size > Constants.THUMB_MAX_SIZE) {
            throw new CustomException(500,"文件太大,缩略图文件的大小最大为64K,请重新上传!");
        }
        if (!suffixName.endsWith(Constants.THUMB_SUPPORTED)) {
            throw new CustomException(500,"图片格式不支持,请选择JPG格式!");
        }
    }


}
