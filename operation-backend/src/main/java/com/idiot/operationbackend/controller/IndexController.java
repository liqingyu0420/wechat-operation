package com.idiot.operationbackend.controller;



import com.idiot.operationbackend.entity.*;
import com.idiot.operationbackend.service.facade.*;
import com.idiot.operationbackend.support.Constants;
import com.idiot.operationbackend.support.CustomException;
import com.idiot.operationbackend.support.JsonResult;
import com.idiot.operationbackend.util.JwtTokenUtil;
import com.idiot.operationbackend.vo.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.idiot.operationbackend.support.Constants.calcRate;

/**
 * 运营星 首页
 * @author wang xiao
 * @date Created in 10:55 2020/9/15
 */
@RestController
@RequestMapping("/index")
@Api(value = "IndexController", tags ="首页")
public class IndexController {


    private final Logger logger = LoggerFactory.getLogger(IndexController.class);

    private final String HOUR = "H";

    private final String WEEK = "W";

    private final String DAY = "D";

    private final String MONTH = "M";

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountStatService accountStatService;

    @Autowired
    private AccountFansService fansService;

    @Autowired
    private FansActionStatService actionStatService;

    @Autowired
    private ArticleStatService articleStatService;

    @Autowired
    private SubscribeSceneService sceneService;




    @GetMapping("/summary")
    @ApiOperation(value = "查询公众号昨日统计数据概览")
    public ResponseEntity<JsonResult<AccountStat>> getFansStat (@RequestHeader String token) {
        String userId = JwtTokenUtil.getUserId(token);
        logger.info("用户:{}查询首页数据统计概览---start",userId);
        List<Account> accounts = accountService.queryAccountByUserId(userId);
        List<String> accountIds =  accounts.stream().map(Account::getId).collect(Collectors.toList());
        // 查询数据  不知道怎么优化
        String yesterdayStr = Constants.DATE_FORMATTER.format(LocalDate.now().plusDays(-1));
        List<AccountStat> ydStatData = accountStatService.queryByDateAndAccIds(accountIds,yesterdayStr);
        String beforeYesterdayStr = Constants.DATE_FORMATTER.format(LocalDate.now().plusDays(-2));
        List<AccountStat> beforeYdStat = accountStatService.queryByDateAndAccIds(accountIds,beforeYesterdayStr);

        logger.info("用户:{}首页概览数据计算中---ing",userId);
        // 所有公众号昨日数据
        long totalNum = ydStatData.stream().mapToLong(AccountStat::getTotalFansNum).sum();
        long addNum = ydStatData.stream().mapToLong(AccountStat::getAddNum).sum();
        long cancelNum = ydStatData.stream().mapToLong(AccountStat::getCancelNum).sum();
        long newNum = ydStatData.stream().mapToLong(AccountStat::getNewNum).sum();
        long inactiveNum = ydStatData.stream().mapToLong(AccountStat::getInactiveNum).sum();
        // 所有公众号前日数据
        long bfCancelNum = beforeYdStat.stream().mapToLong(AccountStat::getCancelNum).sum();
        long bfAddNum = beforeYdStat.stream().mapToLong(AccountStat::getAddNum).sum();
        long bfNewNum = beforeYdStat.stream().mapToLong(AccountStat::getNewNum).sum();
        long bfInactiveNum = beforeYdStat.stream().mapToLong(AccountStat::getInactiveNum).sum();
        long bfTotalNum = beforeYdStat.stream().mapToLong(AccountStat::getTotalFansNum).sum();

        AccountStat result = new AccountStat();
        result.setAddNum(addNum);
        result.setNewNum(newNum);
        result.setCancelNum(cancelNum);
        result.setInactiveNum(inactiveNum);
        result.setTotalFansNum(totalNum);
        result.setAddRate(calcRate(addNum,bfAddNum));
        result.setCancelRate(calcRate(cancelNum,bfCancelNum));
        result.setNewRate(calcRate(newNum,bfNewNum));
        result.setInactiveRate(calcRate(inactiveNum,bfInactiveNum));
        result.setTotalFansRate(calcRate(totalNum,bfTotalNum));
        logger.info("用户:{}查询首页数据统计概览---end",userId);
        return ResponseEntity.ok(JsonResult.success(result));
    }

    @GetMapping("/single/{accountId}")
    @ApiOperation(value = "查询单个公众号昨日统计数据概览")
    public ResponseEntity<JsonResult<AccountStat>> getSingleFansStat (@RequestHeader String token,
                                                                          @PathVariable String accountId) {
        String userId = JwtTokenUtil.getUserId(token);
        logger.info("用户:{}查询首页单个公众号：{}数据统计概览---start",userId,accountId);
        String yesterdayStr = Constants.DATE_FORMATTER.format(LocalDate.now().plusDays(-1));
        AccountStat fansStat = accountStatService.queryByDateAndAccountId(accountId,yesterdayStr);
        logger.info("用户:{}查询首页单个公众号：{}数据统计概览---end",userId,accountId);
        return ResponseEntity.ok(JsonResult.success(fansStat));
    }


    @GetMapping("/growth/{accountId}")
    @ApiOperation(value = "查询单个公众号--粉丝增长")
    public ResponseEntity<JsonResult<Map<String,Object>>> getFansGrowthStat (@PathVariable String accountId,
                                                              @RequestHeader String token,
                                                              @RequestParam String type,
                                                              @RequestParam String startDate,
                                                              @RequestParam String endDate) {
        String userId = JwtTokenUtil.getUserId(token);
        if (!checkDate(startDate,endDate)){
            throw new CustomException(500,"请选择开始和结束时间!");
        }
        Map<String,Object> result = new HashMap<>(8);
        logger.info("用户:{}查询首页单个公众号：{}粉丝增长,type:{},startDate:{},endDate:{} --->start",
                userId,accountId,type,startDate,endDate);
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end= LocalDate.parse(endDate);
        long days = end.toEpochDay() - start.toEpochDay();
        List<GeneralStatData> statDataList = statFansGrowthData(accountId, type, start, end);
        long newNum =0;
        long addNum =0;
        long inactiveNum =0;
        long cancelNum =0;
        BigDecimal ave =BigDecimal.ZERO;
        if (CollectionUtils.isEmpty(statDataList)) {
            newNum = statDataList.parallelStream().mapToLong(GeneralStatData::getNewNum).sum();
            addNum = statDataList.parallelStream().mapToLong(GeneralStatData::getAddNum).sum();
            inactiveNum = statDataList.parallelStream().mapToLong(GeneralStatData::getInactiveNum).sum();
            cancelNum = statDataList.parallelStream().mapToLong(GeneralStatData::getCancelNum).sum();
            if (days == 0){
                days = 1;
            }
            ave = new BigDecimal(newNum/days).setScale(0);
        }
        result.put("newNum",newNum);
        result.put("addNum",addNum);
        result.put("inactiveNum",inactiveNum);
        result.put("cancelNum",cancelNum);
        result.put("aveNum",ave);
        result.put("tableData",statDataList);
        logger.info("用户:{}查询首页单个公众号：{}粉丝增长,type:{},startDate:{},endDate:{} --->end",
                userId,accountId,type,startDate,endDate);
        return ResponseEntity.ok(JsonResult.success(result));
    }


    @GetMapping("/property/{accountId}")
    @ApiOperation(value = "查询单个公众号--粉丝属性")
    public ResponseEntity<JsonResult<Map<String,Object>>> getFansPropertyStat(
                                                                @RequestHeader String token,
                                                                @PathVariable String accountId,
                                                                @RequestParam(required = false) String startDate,
                                                                @RequestParam(required = false) String endDate) {
        String userId = JwtTokenUtil.getUserId(token);
        logger.info("用户:{}查询首页单个公众号：{}粉丝属性,startDate:{},endDate:{} --->start",userId,accountId,startDate,endDate);
        Map<String,Object> result = new HashMap<>(4);
        List<NumberStatData> numberStatData = fansService.statByFansProperty(accountId,startDate,endDate);
        statFansProperty(numberStatData,result);
        logger.info("用户:{}查询首页单个公众号：{}粉丝属性,startDate:{},endDate:{} --->end",userId,accountId,startDate,endDate);
        return ResponseEntity.ok(JsonResult.success(result));
    }

    @GetMapping("/inactive/{accountId}")
    @ApiOperation(value = "查询单个公众号--粉丝活跃度")
    public ResponseEntity<JsonResult<List<AccountStat>>> getFansInactiveStat(@RequestHeader String token,
                                                                              @PathVariable String accountId,
                                                                              @RequestParam String startDate,
                                                                              @RequestParam String endDate) {
        String userId = JwtTokenUtil.getUserId(token);
        if (!checkDate(startDate,endDate)){
            throw new CustomException(500,"请选择查询时间范围!");
        }
        logger.info("用户:{}查询首页单个公众号：{}粉丝活跃度,startDate:{},endDate:{} --->start",userId,accountId,startDate,endDate);
        List<AccountStat> accountStats = accountStatService.queryAccountStatByDate(accountId,startDate,endDate);
        logger.info("用户:{}查询首页单个公众号：{}粉丝活跃度,startDate:{},endDate:{} --->end",userId,accountId,startDate,endDate);
        return ResponseEntity.ok(JsonResult.success(accountStats));
    }


    @GetMapping("/remain/{accountId}")
    @ApiOperation(value = "查询单个公众号--粉丝忠诚度")
    public ResponseEntity<JsonResult<List<RemainStatData>>> getFansRemainStat(@RequestHeader String token,
                                                                              @PathVariable String accountId,
                                                                              @RequestParam String date) {
        String userId = JwtTokenUtil.getUserId(token);
        if (StringUtils.isEmpty(date)){
            throw new CustomException(500,"请选择查询时间!");
        }
        LocalDateTime endDate = Constants.toLocalDateTime(date);
        LocalDateTime startDate = Constants.toLocalDateTime(date).plusDays(-7);
        logger.info("用户:{}查询首页单个公众号：{}粉丝忠诚度,date:{} --->start",userId,accountId,date);
        List<AccountStat> accountStats = accountStatService.queryAccountStatByDate(accountId,startDate,endDate);
        List<RemainStatData> remainStatData = statFansRemainData(accountStats);
        if (Objects.isNull(remainStatData)) {
            remainStatData = new ArrayList<>();
        }
        logger.info("用户:{}查询首页单个公众号：{}粉丝忠诚度,date:{} --->end",userId,accountId,date);
        return ResponseEntity.ok(JsonResult.success(remainStatData));
    }

    @GetMapping("/articles/{accountId}")
    @ApiOperation(value = "查询单个公众号--图文影响力")
    public ResponseEntity<JsonResult<List<ArticleStatData>>> getFansPageStat(@RequestHeader String token,
                                                                             @PathVariable String accountId,
                                                                             @RequestParam String startDate,
                                                                             @RequestParam String endDate) {
        String userId = JwtTokenUtil.getUserId(token);
        if (!checkDate(startDate,endDate)){
            throw new CustomException(500,"请选择查询时间!");
        }
        logger.info("用户:{}查询首页单个公众号：{}图文影响力,startDate:{},endDate:{} --->start",userId,accountId,startDate,endDate);
        List<ArticleStat> articleStats = articleStatService.queryArticleStatByDate(accountId,startDate,endDate);
        List<ArticleStatData> articleStatData = statArticleData(articleStats);
        if (CollectionUtils.isEmpty(articleStatData)){
            articleStatData = new ArrayList<>();
        }
        logger.info("用户:{}查询首页单个公众号：{}图文影响力,startDate:{},endDate:{} --->start",userId,accountId,startDate,endDate);
        return ResponseEntity.ok(JsonResult.success(articleStatData));
    }


    @GetMapping("/interactMsg/{accountId}")
    @ApiOperation(value = "查询单个公众号--互动消息")
    public ResponseEntity<JsonResult<Map<String,Object>>> getFansInteractMsg(@RequestHeader String token,
                                                                             @PathVariable String accountId,
                                                                             @RequestParam String startDate,
                                                                             @RequestParam String endDate) {
        String userId = JwtTokenUtil.getUserId(token);
        if (!checkDate(startDate,endDate)){
            throw new CustomException(500,"请选择查询时间!");
        }
        Map<String,Object> map = new HashMap<>(2);
        logger.info("用户:{}查询首页单个公众号：{}互动消息,startDate:{},endDate:{} --->start",userId,accountId,startDate,endDate);
        List<FansActionStat> fansActionStats = actionStatService.queryFansActionStat(accountId,startDate,endDate);
        long start = Constants.toLocalDateTime(startDate).toEpochSecond(Constants.DEFAULT_ZONE);
        long end = Constants.toLocalDateTime(endDate).toEpochSecond(Constants.DEFAULT_ZONE);
        // 时段统计
        List<InteractMsgStatData> list = statInteractMsgDataTime(fansActionStats,start,end);
        map.put("list",list);
        // 类型统计
        Map<Integer,Long> type = countStatDataType(fansActionStats);
        map.put("type",type);
        logger.info("用户:{}查询首页单个公众号：{}互动消息,startDate:{},endDate:{} --->start",userId,accountId,startDate,endDate);
        return ResponseEntity.ok(JsonResult.success(map));
    }


    @GetMapping("/type")
    @ApiOperation(value = "发送消息状态码")
    public ResponseEntity<JsonResult<List<BaseType>>> customerStatus () {

        List<BaseType> baseTypes = new ArrayList<>(4);
        BaseType success  = new BaseType(Constants.SUCCESSED,"发送成功");
        BaseType waiting  = new BaseType(Constants.WAITING,"未发送");
        BaseType run  = new BaseType(Constants.RUNNING,"发送中");
        BaseType end  = new BaseType(Constants.END,"发送中止");
        BaseType fail  = new BaseType(Constants.FAILED,"发送失败");
        baseTypes.add(success);
        baseTypes.add(waiting);
        baseTypes.add(run);
        baseTypes.add(end);
        baseTypes.add(fail);
        return ResponseEntity.ok(JsonResult.success(baseTypes));
    }

    @GetMapping("/subscribeScene")
    @ApiOperation(value = "查询粉丝关注公众号来源类型")
    public ResponseEntity<JsonResult<List<SubscribeScene>>> getSubscribeScene () {
        return ResponseEntity.ok(JsonResult.success(sceneService.list()));
    }





    /**
     *  统计粉丝数据  好难受写不出完美的代码
     * @author wangxiao
     * @date 16:00 2020/9/16 
     * @param accountId accountId
     * @param type type
     * @param startDate startDate
     * @param endDate endDate
     * @return java.util.List<com.idiot.operationbackend.vo.StatData>
     */
    private List<GeneralStatData> statFansGrowthData(String accountId, String type, LocalDate startDate, LocalDate endDate) {

        // 粉丝增长数据 因为能选择今天 需要在 用户动作分析里面取数据。或者一小时分析一次数据
        long disValue = 0;
        if (HOUR.equals(type)) {
            disValue = 7200;
        }else if (DAY.equals(type)){
            disValue = 86400;
        }else if (WEEK.equals(type)){
            disValue = 604800;
        }else if (MONTH.equals(type)){
            // 默认 30 天
            disValue = 2592000;
        }else {
            throw new CustomException(500,"请你选择正确的时间统计类型!");
        }
        LocalDateTime startLocalDate =  startDate.atTime(Constants.DEFAULT_TIME);
        LocalDateTime endLocalDate =  endDate.plusDays(1).atTime(Constants.DEFAULT_TIME);
        List<FansActionStat> fansActionStats = actionStatService.queryFansActionStat(accountId,startLocalDate,endLocalDate);
        if (CollectionUtils.isEmpty(fansActionStats)) {
            return new ArrayList<>();
        }
        long start = startLocalDate.toEpochSecond(Constants.DEFAULT_ZONE);
        long end = endLocalDate.toEpochSecond(Constants.DEFAULT_ZONE);
        List<GeneralStatData> statDataList = new ArrayList<>();
        GeneralStatData statData =  null;
        List<FansActionStat> tempStatList = null;
        while (start<=end){
            // 划分成不同时间段
            long finalStart = start;
            long finalDisValue = disValue;
            tempStatList = fansActionStats.stream()
                    .filter(e->e.getCreateTime() >= finalStart && e.getCreateTime()<=  finalStart + finalDisValue)
                    .sorted(Comparator.comparingLong(FansActionStat::getCreateTime))
                    .collect(Collectors.toList());
            statData = countStatData(tempStatList,finalStart,finalStart+finalDisValue,type);
            statDataList.add(statData);
            start += finalDisValue;
        }
        return statDataList;
    }

    /**
     *  校验日期
     * @author wangxiao
     * @date 11:58 2020/9/17
     * @param start start
     * @param end end
     * @return boolean
     */
    private boolean checkDate (String start, String end) {
        return !(StringUtils.isEmpty(start) | StringUtils.isEmpty(end));
    }



    /**
     *  统计 粉丝增长 时间段
     * @author wangxiao
     * @date 16:45 2020/9/16
     * @param fansActionStats fansActionStats
     * @param start start
     * @param end end
     * @param type type
     * @return com.idiot.operationbackend.vo.StatData
     */
    private GeneralStatData countStatData (List<FansActionStat> fansActionStats, long start, long end, String type) {

        LocalDateTime startDateTime = LocalDateTime.ofEpochSecond(start,0,Constants.DEFAULT_ZONE);
        LocalDateTime endDateTime = LocalDateTime.ofEpochSecond(end,0,Constants.DEFAULT_ZONE);
        GeneralStatData statData = new GeneralStatData();
        long newNum = 0;
        long cancelNum = 0;
        long inactiveNum = 0;
        if (!CollectionUtils.isEmpty(fansActionStats)) {
            newNum = fansActionStats.parallelStream().filter(e->Constants.NEW == e.getAction()).count();
            cancelNum = fansActionStats.parallelStream().filter(e->Constants.CANCEL == e.getAction()).count();
            inactiveNum = fansActionStats.parallelStream().filter(e->Constants.NEW != e.getAction() && Constants.CANCEL != e.getAction()).count();
        }
        String label = "";

        if (HOUR.equals(type)) {
            label = String.format("%s %s:%s-%s:%s",startDateTime.toLocalDate(),startDateTime.getHour(),"00",endDateTime.getHour(),"00");
        }else if (DAY.equals(type)){
            label = String.format("%s",startDateTime.toLocalDate());
        }else if (WEEK.equals(type)){
            label = String.format("%s/%s-%s/%s",startDateTime.getMonthValue(),startDateTime.getDayOfMonth()
                    ,endDateTime.getMonthValue(),endDateTime.getDayOfMonth());
        }else if (MONTH.equals(type)){
            label =  String.format("%s-%s",startDateTime.getYear(),startDateTime.getMonthValue());
        }
        statData.setDateLabel(label);
        statData.setAddNum(newNum-cancelNum);
        statData.setCancelNum(cancelNum);
        statData.setNewNum(newNum);
        statData.setInactiveNum(inactiveNum);
        return statData;
    }


    /**
     *  统计粉丝属性
     * @author wangxiao
     * @date 18:57 2020/9/16
     * @param fansData fansData
     * @param map map
     */
    private void statFansProperty(List<NumberStatData> fansData,Map<String,Object> map) {
      if (CollectionUtils.isEmpty(fansData)) {
          Map<String,List<NumberStatData>> numData = empty().stream().collect(Collectors.groupingBy(NumberStatData::getType));
          map.putAll(numData);
          map.put("province",new ArrayList<>());
      }else {
          Map<String,List<NumberStatData>> numData = fansData.stream().collect(Collectors.groupingBy(NumberStatData::getType));
          map.putAll(numData);
      }

    }


    private List<NumberStatData> empty () {
        List<NumberStatData> statDataList = new ArrayList<>();
        NumberStatData numberStatData1 = new NumberStatData("0",0L,"sex");
        NumberStatData numberStatData2 = new NumberStatData("1",0L,"sex");
        NumberStatData numberStatData3 = new NumberStatData("2",0L,"sex");
        NumberStatData numberStatData4 = new NumberStatData("ADD_SCENE_SEARCH",0L,"subscribeScene");
        NumberStatData numberStatData5 = new NumberStatData("ADD_SCENE_ACCOUNT_MIGRATION",0L,"subscribeScene");
        NumberStatData numberStatData6 = new NumberStatData("ADD_SCENE_PROFILE_CARD",0L,"subscribeScene");
        NumberStatData numberStatData7 = new NumberStatData("ADD_SCENE_QR_CODE",0L,"subscribeScene");
        NumberStatData numberStatData8 = new NumberStatData("ADD_SCENE_PROFILE_LINK",0L,"subscribeScene");
        NumberStatData numberStatData9 = new NumberStatData("ADD_SCENE_PROFILE_ITEM",0L,"subscribeScene");
        NumberStatData numberStatData10 = new NumberStatData("ADD_SCENE_PAID",0L,"subscribeScene");
        NumberStatData numberStatData11 = new NumberStatData("ADD_SCENE_WECHAT_ADVERTISEMENT",0L,"subscribeScene");
        NumberStatData numberStatData12 = new NumberStatData("ADD_SCENE_OTHERS",0L,"subscribeScene");
        statDataList.add(numberStatData1);
        statDataList.add(numberStatData2);
        statDataList.add(numberStatData3);
        statDataList.add(numberStatData4);
        statDataList.add(numberStatData5);
        statDataList.add(numberStatData6);
        statDataList.add(numberStatData7);
        statDataList.add(numberStatData8);
        statDataList.add(numberStatData9);
        statDataList.add(numberStatData10);
        statDataList.add(numberStatData11);
        statDataList.add(numberStatData12);
        return  statDataList;
    }
    /**
     *  统计留存粉丝
     * @author wangxiao
     * @date 14:22 2020/9/17
     * @param accountStats accountStats
     * @return java.util.List<com.idiot.operationbackend.vo.RemainStatData>
     */
    private List<RemainStatData> statFansRemainData(List<AccountStat> accountStats) {
        if (CollectionUtils.isEmpty(accountStats)) {
            return null;
        }
        int size = accountStats.size();
        List<RemainStatData> remainStatDataList = new ArrayList<>(size);
        String statDate = null;
        Long newNum = null;
        Long totalNum = null;
        RemainStatData remain = null;
        for (AccountStat temp : accountStats) {
            statDate = temp.getStatDate();
            newNum = temp.getNewNum();
            totalNum = temp.getTotalFansNum();
            remain = new RemainStatData(statDate,newNum);
            remain.setDay1Num(calcRemain(accountStats,totalNum,statDate,1));
            remain.setDay2Num(calcRemain(accountStats,totalNum,statDate,2));
            remain.setDay3Num(calcRemain(accountStats,totalNum,statDate,3));
            remain.setDay4Num(calcRemain(accountStats,totalNum,statDate,4));
            remain.setDay5Num(calcRemain(accountStats,totalNum,statDate,5));
            remain.setDay6Num(calcRemain(accountStats,totalNum,statDate,6));
            remain.setDay7Num(calcRemain(accountStats,totalNum,statDate,7));
            remainStatDataList.add(remain);
        }
        return remainStatDataList;
    }
    /**
     *  计算留存数
     * @author wangxiao
     * @date 14:35 2020/9/17
     * @param accountStats 数组
     * @param nowTotal 当前总数
     * @param targetIndex 几天前下表
     * @param nowStatDate  当前统计日期
     * @return long
     */
    private long calcRemain (List<AccountStat> accountStats,long nowTotal,String nowStatDate,int targetIndex) {
        long remainNum;
        LocalDate nowDate = LocalDate.parse(nowStatDate,Constants.DATE_FORMATTER);
        String targetStatDate = nowDate.plusDays(-targetIndex).format(Constants.DATE_FORMATTER);
        remainNum = accountStats.stream()
                .filter(e->targetStatDate.equals(e.getStatDate()))
                .findAny().map(e->e.getTotalFansNum()-nowTotal)
                .orElse(0L);
        return remainNum;
    }

    /**
     *  统计图文影响力
     * @author wangxiao
     * @date 16:06 2020/9/17
     * @param articleStats articleStats
     * @return java.util.List<com.idiot.operationbackend.vo.ArticleStatData>
     */
    private List<ArticleStatData> statArticleData (List<ArticleStat> articleStats) {
        if (CollectionUtils.isEmpty(articleStats)){
            return null;
        }
        Map<String,List<ArticleStat>> temp = articleStats.stream().collect(Collectors.groupingBy(ArticleStat::getStatDate));
        List<ArticleStatData> articleStatData = new ArrayList<>(temp.size());
        List<ArticleStat> var = null;
        ArticleStatData result =  null;
        for (Map.Entry<String,List<ArticleStat>> entry : temp.entrySet()) {
            var = entry.getValue();
            result = new ArticleStatData(entry.getKey(),var);
            result.setShareUser(var.stream().mapToLong(ArticleStat::getShareUser).sum());
            result.setShareCount(var.stream().mapToLong(ArticleStat::getShareCount).sum());
            result.setAddToFavCount(var.stream().mapToLong(ArticleStat::getAddToFavCount).sum());
            result.setAddToFavUser(var.stream().mapToLong(ArticleStat::getAddToFavUser).sum());
            result.setIntPageReadCount(var.stream().mapToLong(ArticleStat::getIntPageReadCount).sum());
            result.setIntPageReadUser(var.stream().mapToLong(ArticleStat::getIntPageReadUser).sum());
            result.setOriPageReadCount(var.stream().mapToLong(ArticleStat::getOriPageReadCount).sum());
            result.setOriPageReadUser(var.stream().mapToLong(ArticleStat::getOriPageReadUser).sum());
            result.setTargetUser(var.stream().mapToLong(ArticleStat::getTargetUser).sum());
            articleStatData.add(result);
        }
        return articleStatData;
    }

    /**
     *  统计 时段 互动消息
     * @author wangxiao
     * @date 17:06 2020/9/17
     * @param fansActionStats fansActionStats
     * @param start start
     * @param end end
     * @return List
     */
    private List<InteractMsgStatData> statInteractMsgDataTime(List<FansActionStat> fansActionStats,long start,long end){
        long disValue =  7200;
        List<InteractMsgStatData> statDataList = new ArrayList<>();
        InteractMsgStatData statData =  null;
        List<FansActionStat> tempStatList = null;
        while (start<=end){
            long finalStart = start;
            tempStatList = fansActionStats.stream()
                    .filter(e->e.getCreateTime() >= finalStart && e.getCreateTime()<=  finalStart + disValue)
                    .sorted(Comparator.comparingLong(FansActionStat::getCreateTime))
                    .collect(Collectors.toList());
            statData = countStatDataTime(tempStatList,finalStart,finalStart+disValue);
            statDataList.add(statData);
            start += disValue;
        }
       return statDataList;
    }


    /**
     *  统计 时段粉丝互动消息
     * @author wangxiao
     * @date 16:45 2020/9/16
     * @param fansActionStats fansActionStats
     * @param start start
     * @param end end
     * @return com.idiot.operationbackend.vo.StatData
     */
    private InteractMsgStatData countStatDataTime (List<FansActionStat> fansActionStats, long start, long end) {

        LocalDateTime startDateTime = LocalDateTime.ofEpochSecond(start,0,Constants.DEFAULT_ZONE);
        LocalDateTime endDateTime = LocalDateTime.ofEpochSecond(end,0,Constants.DEFAULT_ZONE);
        InteractMsgStatData statData = new InteractMsgStatData();
        long unSubscribe = 0;
        long subscribe = 0;
        long scan = 0;
        long menu = 0;
        long msg = 0 ;
        if (!CollectionUtils.isEmpty(fansActionStats)) {
            subscribe = fansActionStats.parallelStream().filter(e->Constants.SUBSCRIBE == e.getAction()).count();
            unSubscribe = fansActionStats.parallelStream().filter(e->Constants.CANCEL == e.getAction()).count();
            scan = fansActionStats.parallelStream().filter(e->Constants.SCAN == e.getAction()).count();
            menu = fansActionStats.parallelStream().filter(e->Constants.MENU == e.getAction()).count();
            msg = fansActionStats.parallelStream().filter(e->Constants.MSG == e.getAction()).count();
        }
        String label = String.format("%s:%s-%s:%s",startDateTime.getHour(),"00",endDateTime.getHour(),"00");
        statData.setDateLabel(label);
        statData.setMenu(menu);
        statData.setScan(scan);
        statData.setSubscribe(subscribe);
        statData.setUnSubscribe(unSubscribe);
        statData.setMsg(msg);
        statData.setTotal(unSubscribe+subscribe+scan+menu+msg);
        return statData;
    }
    /**
     *  统计类型 粉丝互动消息
     * @author wangxiao
     * @date 17:35 2020/9/17
     * @param fansActionStats fansActionStats
     * @return java.util.Map<java.lang.Integer,java.lang.Long>
     */
    private Map<Integer,Long> countStatDataType (List<FansActionStat> fansActionStats) {
       return fansActionStats.stream().collect(Collectors.groupingBy(FansActionStat::getAction,Collectors.counting()));
    }
}
