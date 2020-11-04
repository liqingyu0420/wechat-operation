package com.idiot.operationbackend.controller.articles;


import com.alibaba.fastjson.JSONObject;

import com.idiot.operationbackend.entity.Articles;
import com.idiot.operationbackend.service.facade.AccountService;
import com.idiot.operationbackend.service.facade.ArticlesService;
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

import javax.validation.Valid;
import java.time.Instant;
import java.util.List;


/**
 * 图文
 * @author wang xiao
 * @date Created in 15:13 2020/9/21
 */

@RestController
@RequestMapping("/articles")
@Api(value = "ArticlesController", tags ="图文文章")
public class ArticlesController {


    private final Logger logger = LoggerFactory.getLogger(ArticlesController.class);

    @Autowired
    private ArticlesService articlesService;

    @Autowired
    private WeChatService weChatService;

    @Autowired
    private AccountService accountService;


    @GetMapping("/wechat")
    @ApiOperation(value = "从微信服务器查询公众号图文")
    public ResponseEntity<JsonResult<JSONObject>> wechatArticlesPage (@RequestHeader String token,
                                                                             @RequestParam String accountId,
                                                                             @RequestParam int page){
        String userId = JwtTokenUtil.getUserId(token);
        page= page>0?page-1:0;
        logger.info("用户：{}查询微信服务器上公众号{}图文-------------start",userId,accountId);
        JSONObject wxJson = weChatService.queryAccountMaterial(accountId,"news",page,8);
        logger.info("用户：{}查询微信服务器上公众号{}图文-------------end",userId,accountId);
        return ResponseEntity.ok(JsonResult.success(wxJson));
    }


    @PostMapping("/add")
    @ApiOperation(value = "新增并发布公众号图文")
    public ResponseEntity<JsonResult<Boolean>> addAndEditArticles (@RequestHeader String token,
                                                                   @RequestParam String accountId,
                                                                   @RequestBody @Valid List<Articles> articles){
        String userId = JwtTokenUtil.getUserId(token);
        logger.info("用户：{}新增并发布微信公众号{}图文-------------start",userId,accountId);
        boolean enable = accountService.queryAccountByUserId(userId)
                .stream()
                .anyMatch(e->accountId.equals(e.getId()));
        if (!enable) {
            throw new CustomException(500,"你没有权限操作该公众号");
        }
        String wxJson = weChatService.addNews(articles,accountId);
        Boolean addResult  = articlesService.addArticles(wxJson,articles);
        logger.info("用户：{}新增并发布微信公众号{}图文-------------end,结果:{}",userId,accountId,addResult);
        return ResponseEntity.ok(JsonResult.success(addResult));
    }

    @GetMapping("/edition")
    @ApiOperation(value = "发布公众号图文")
    public ResponseEntity<JsonResult<Boolean>> editingArticles (@RequestHeader String token,
                                                            @RequestParam String accountId,
                                                            @RequestParam String mediaId){
        String userId = JwtTokenUtil.getUserId(token);
        logger.info("用户：{}发布微信公众号{}图文-------------start",userId,accountId);
        boolean enable = accountService.queryAccountByUserId(userId).stream()
                .anyMatch(e->accountId.equals(e.getId()));
        if (!enable) {
            throw new CustomException(500,"你没有权限操作该公众号");
        }
        List<Articles> articles = articlesService.queryByArticlesByMediaId(mediaId);
        String wxJson = weChatService.addNews(articles,accountId);
        Boolean addResult  = articlesService.upArticles(wxJson,articles);
        logger.info("用户：{}发布微信公众号{}图文-------------end,结果:{}",userId,accountId,addResult);
        return ResponseEntity.ok(JsonResult.success(addResult));
    }


    @PostMapping("/new")
    @ApiOperation(value = "新增公众号图文")
    public ResponseEntity<JsonResult<Boolean>> saveArticles (@RequestHeader String token,
                                                            @RequestParam String accountId,
                                                            @RequestBody @Valid List<Articles> articles){
        String userId = JwtTokenUtil.getUserId(token);
        logger.info("用户：{}新增微信公众号{}图文-------------start",userId,accountId);
        boolean enable = accountService.queryAccountByUserId(userId).stream()
                .anyMatch(e->accountId.equals(e.getId()));
        if (!enable) {
            throw new CustomException(500,"你没有权限操作该公众号");
        }
        String tempMediaId = Instant.now().toString();
        for (Articles article : articles) {
            article.setRelease(false);
            article.setMediaId(tempMediaId);
        }
        Boolean addResult  = articlesService.saveBatch(articles);
        logger.info("用户：{}新增微信公众号{}图文-------------end,结果:{}",userId,accountId,addResult);
        return ResponseEntity.ok(JsonResult.success(addResult));
    }


    @GetMapping("/rank")
    @ApiOperation(value = "图文排行榜 2小时更新一次")
    public ResponseEntity<JsonResult<Boolean>> rankArticles (@RequestHeader String token){
        String userId = JwtTokenUtil.getUserId(token);
        logger.info("用户：{}查询图文排行榜-------------start",userId);
        return ResponseEntity.ok(JsonResult.success(false));
    }






}
