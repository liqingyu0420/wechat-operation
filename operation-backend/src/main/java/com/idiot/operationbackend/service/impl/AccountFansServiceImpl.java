package com.idiot.operationbackend.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.idiot.operationbackend.entity.AccountFans;

import com.idiot.operationbackend.mappers.AccountFansMapper;
import com.idiot.operationbackend.service.facade.AccountFansService;
import com.idiot.operationbackend.support.Constants;
import com.idiot.operationbackend.vo.NumberStatData;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author wang xiao
 * @date Created in 19:24 2020/9/10
 */
@Service
public class AccountFansServiceImpl extends ServiceImpl<AccountFansMapper, AccountFans>
        implements AccountFansService {



    @Override
    public AccountFans queryByAccountIdAndOpenId(String accountId, String openId) {
        return getOne(Wrappers.<AccountFans>lambdaQuery().eq(AccountFans::getAccountId,accountId)
                .eq(AccountFans::getOpenId,openId),false);
    }


    @Override
    public int countFansByAccountId(String accountId) {
        return count(Wrappers.<AccountFans>lambdaQuery().eq(AccountFans::getAccountId,accountId)
                .eq(AccountFans::getState,1));
    }


    @Override
    public List<AccountFans> queryFansByDateAndDate(String accountId, String startDate, String endDate) {
        long  end = Constants.toLocalDateTime(endDate).plusDays(1)
                .toEpochSecond(Constants.DEFAULT_ZONE);
        long  start =Constants.toLocalDateTime(endDate)
                .toEpochSecond(Constants.DEFAULT_ZONE);
        return list(Wrappers.<AccountFans>lambdaQuery()
                .eq(AccountFans::getAccountId,accountId)
                .eq(AccountFans::getState,1)
                .ge(AccountFans::getSubscribeTime,start)
                .le(AccountFans::getSubscribeTime,end));
    }


    @Override
    public List<NumberStatData> statByFansProperty(String accountId, String startDate, String endDate) {
        List<NumberStatData> numberStatData = null;
        if (StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)) {
            long start = Constants.toEpochSecond(Constants.toLocalDateTime(startDate));
            long end = Constants.toEpochSecond(Constants.toLocalDateTime(endDate).plusDays(1));
            numberStatData = baseMapper.statPropertyByDate(accountId,start,end);
        }else {
            numberStatData = baseMapper.statPropertyByAll(accountId);
        }
        return numberStatData;
    }


    @Override
    public Page<AccountFans> pageFans(String accountId, Integer sex, String city, String province, String tagId,
                                      String label, String startTime, String endTime,String scene,
                                      int page, int pageSize) {
        Page<AccountFans> queryPage = new Page<>(page,pageSize);
        queryPage.setSearchCount(true);
        LambdaQueryWrapper<AccountFans> queryWrapper = Wrappers.lambdaQuery();
        execSqlPre(accountId,sex,province,city,queryWrapper);
        if (StringUtils.isNotBlank(label)) {
            queryWrapper.eq(AccountFans::getNickName,label).or()
                    .eq(AccountFans::getRemark,label);
        }
        if (StringUtils.isNotBlank(startTime)) {
           long start =  Constants.parseLocalDateTime(startTime).toEpochSecond(Constants.DEFAULT_ZONE);
           queryWrapper.ge(AccountFans::getSubscribeTime,start);
        }
        if (StringUtils.isNotBlank(endTime)) {
            long end =  Constants.parseLocalDateTime(startTime).plusDays(1).toEpochSecond(Constants.DEFAULT_ZONE);
            queryWrapper.le(AccountFans::getSubscribeTime,end);
        }
        if (StringUtils.isNotBlank(scene)) {
            queryWrapper.eq(AccountFans::getSubscribeScene,scene);
        }
        if (StringUtils.isNotBlank(tagId)) {
            queryWrapper.last(" AND  FIND_IN_SET('"+tagId+"',tags)");
        }else {
            queryWrapper.orderByDesc(AccountFans::getSubscribeTime);
        }
        return page(queryPage,queryWrapper);
    }

    @Override
    public boolean updateFansRemark(String accountId, String openId, String remark) {
        return update(Wrappers.<AccountFans>lambdaUpdate()
                .eq(AccountFans::getAccountId,accountId)
                .eq(AccountFans::getOpenId,openId)
                .set(AccountFans::getRemark,remark));
    }

    @Override
    public boolean addItemForTagList(List<String> openIds, String accountId, int wxId) {
        if (CollectionUtils.isEmpty(openIds)){
            return false;
        }
        int size = openIds.size();
        List<AccountFans> fans = new ArrayList<>(size);
        for (String openId : openIds) {
            AccountFans temp = queryByAccountIdAndOpenId(accountId,openId);
            if (Objects.isNull(temp)){
                continue;
            }
            List<Integer> jsonArray = JSONArray.parseArray(temp.getTagIdList(),Integer.class);
            jsonArray.add(wxId);
            jsonArray = jsonArray.stream().distinct().collect(Collectors.toList());
            temp.setTagIdList(JSONArray.toJSONString(jsonArray));
            temp.setTags(temp.initTags());
            fans.add(temp);

        }
        return updateBatchById(fans);
    }


    @Override
    public boolean removeItemForTagList(List<String> openIds, String accountId, int wxId) {
        if (CollectionUtils.isEmpty(openIds)){
            return false;
        }
        int size = openIds.size();
        List<AccountFans> fans = new ArrayList<>(size);
        for (String openId : openIds) {
            AccountFans temp = queryByAccountIdAndOpenId(accountId,openId);
            if (Objects.isNull(temp)){
                continue;
            }
            List<Integer> jsonArray = JSONArray.parseArray(temp.getTagIdList(),Integer.class);
            jsonArray.remove(Integer.valueOf(wxId));
            jsonArray = jsonArray.stream().distinct().collect(Collectors.toList());
            temp.setTagIdList(JSONObject.toJSONString(jsonArray));
            temp.setTags(temp.initTags());
            fans.add(temp);
        }
        return updateBatchById(fans);
    }

    @Override
    public List<AccountFans> queryAccountFans(List<String> openIds, String accountId) {
        if (CollectionUtils.isEmpty(openIds)){
            return new ArrayList<>();
        }
       return list(Wrappers.<AccountFans>lambdaQuery()
               .eq(AccountFans::getAccountId,accountId)
               .eq(AccountFans::getState,"1")
               .in(AccountFans::getOpenId,openIds));
    }


    @Override
    public List<AccountFans> queryAccountFans(String accountId, Integer sex, String province, String city, String tags, String subscribeTime) {
        LambdaQueryWrapper<AccountFans> queryWrapper =  Wrappers.lambdaQuery();
        execSqlPre(accountId, sex, province, city,queryWrapper);
        String [] dateArray = subscribeTime.split(",");
        if (StringUtils.isNotBlank(subscribeTime)) {
            long start = Constants.toLocalDateTime(dateArray[0])
                    .toEpochSecond(Constants.DEFAULT_ZONE);
            long end = Constants.toLocalDateTime(dateArray[1])
                    .toEpochSecond(Constants.DEFAULT_ZONE);
            queryWrapper.ge(AccountFans::getSubscribeTime,start)
                    .le(AccountFans::getSubscribeTime,end);
        }

        execTagSql(tags, queryWrapper);
        return list(queryWrapper);
    }


    @Override
    public int countAccountFans(String accountId, Integer sex, String province, String city, String tags, String subscribeTime) {
        LambdaQueryWrapper<AccountFans> queryWrapper =  Wrappers.lambdaQuery();
        execSqlPre(accountId, sex, province, city,queryWrapper);
        if (StringUtils.isNotBlank(subscribeTime)) {
            String [] dateArray = subscribeTime.split(",");
            long start = Constants.toLocalDateTime(dateArray[0])
                    .toEpochSecond(Constants.DEFAULT_ZONE);
            long end = Constants.toLocalDateTime(dateArray[1])
                    .toEpochSecond(Constants.DEFAULT_ZONE);
            queryWrapper.ge(AccountFans::getSubscribeTime,start)
                    .le(AccountFans::getSubscribeTime,end);
        }

        execTagSql(tags, queryWrapper);
        return count(queryWrapper);
    }


    @Override
    public void unsubscribe(String accountId, String openId) {
        update(Wrappers.<AccountFans>lambdaUpdate().set(AccountFans::getState,0)
                .eq(AccountFans::getAccountId,accountId).eq(AccountFans::getOpenId,openId));
    }

    @Override
    public int countFans(String accountId) {
        return  count(Wrappers.<AccountFans>lambdaQuery().eq(AccountFans::getState,1)
                .eq(AccountFans::getAccountId,accountId));
    }



    /**
     *  sql 开头
     * @author wangxiao
     * @date 15:28 2020/10/29
     * @param accountId  accountId
     * @param sex sex
     * @param province province
     * @param city city
     * @param queryWrapper queryWrapper
     */
    private void execSqlPre(String accountId, Integer sex, String province, String city,LambdaQueryWrapper<AccountFans> queryWrapper) {
        queryWrapper.eq(AccountFans::getAccountId,accountId).eq(AccountFans::getState,1);
        if (Objects.nonNull(sex)) {
            queryWrapper.eq(AccountFans::getSex,sex);
        }
        if (StringUtils.isNotBlank(province)) {
            queryWrapper.eq(AccountFans::getProvince,province);
        }
        if (StringUtils.isNotBlank(city)) {
            queryWrapper.eq(AccountFans::getCity,city);
        }
    }

    /**
     *  tag sql
     * @author wangxiao
     * @date 15:29 2020/10/29
     * @param tags tags
     * @param queryWrapper queryWrapper
     */
    private void execTagSql(String tags, LambdaQueryWrapper<AccountFans> queryWrapper) {
        if (StringUtils.isNotBlank(tags)) {
            List<String> strings = Arrays.stream(tags.split(",")).collect(Collectors.toList());
            String sql = " AND (%s)";
            StringBuilder buffer = new StringBuilder();
            for (int i = 0; i < strings.size(); i++) {
                if (i>0) {
                    buffer.append(" OR ");
                }
                buffer.append(" FIND_IN_SET('").append(strings.get(i)).append("',tags) ");
            }
            sql = String.format(sql,buffer.toString());
            queryWrapper.last(sql);
        }
    }


}
