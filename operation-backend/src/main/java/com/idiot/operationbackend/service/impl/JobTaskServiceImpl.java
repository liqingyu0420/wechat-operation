package com.idiot.operationbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.idiot.operationbackend.entity.JobTask;
import com.idiot.operationbackend.mappers.JobTaskMapper;
import com.idiot.operationbackend.service.facade.JobTaskService;
import com.idiot.operationbackend.support.Constants;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author wang xiao
 * @date Created in 14:55 2020/9/23
 */
@Service
public class JobTaskServiceImpl extends ServiceImpl<JobTaskMapper, JobTask> implements JobTaskService {


    @Override
    public List<JobTask> queryByInitJob() {
        long epochMilli = LocalDateTime.now().toInstant(Constants.DEFAULT_ZONE).toEpochMilli();
        return list(Wrappers.<JobTask>lambdaQuery().eq(JobTask::getStatus,Constants.WAITING)
                .gt(JobTask::getTaskTimer,epochMilli));
    }

    @Override
    public JobTask queryJobTask(String taskKey) {
        return getOne(Wrappers.<JobTask>lambdaQuery().eq(JobTask::getTaskKey,taskKey),false);
    }

    @Override
    public Page<JobTask> queryPageJobTask(int page, Integer status, List<String> ids) {
        Page<JobTask> queryPage = new Page<>(page,15);
        LambdaQueryWrapper<JobTask> queryWrapper = Wrappers.lambdaQuery();
        if (Objects.nonNull(status)) {
            queryWrapper.eq(JobTask::getStatus,status);
        }
        queryWrapper.in(JobTask::getAccountId,ids)
                .orderByDesc(JobTask::getCreateTime);
        return page(queryPage,queryWrapper);
    }

    @Override
    public boolean removeByTaskKey(String taskKey) {
        return remove(Wrappers.<JobTask>lambdaQuery().eq(JobTask::getTaskKey,taskKey));
    }
}
