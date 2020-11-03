package com.idiot.operationbackend.service.facade;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.idiot.operationbackend.entity.JobTask;

import java.util.List;

/**
 * @author wang xiao
 * @date Created in 14:53 2020/9/23
 */
public interface JobTaskService extends IService<JobTask> {

    /**
     * 查询需要加载的定时任务
     * @author wangxiao
     * @date 14:57 2020/9/23
     * @return java.util.List<com.idiot.operationbackend.entity.JobTask>
     */
    List<JobTask> queryByInitJob();


    /**
     *  查询定时任务
     * @author wangxiao
     * @date 15:38 2020/9/23
     * @param taskKey taskKey
     * @return com.idiot.operationbackend.entity.JobTask
     */
    JobTask queryJobTask(String taskKey);


    /**
     *  查询分页定时
     * @author wangxiao
     * @date 17:06 2020/9/23
     * @param page page
     * @param status status
     * @param ids ids
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.idiot.operationbackend.entity.JobTask>
     */
    Page<JobTask> queryPageJobTask(int page,Integer status,List<String> ids);

    /**
     *  删除定时
     * @author wangxiao
     * @date 17:39 2020/10/27
     * @param taskKey taskKey
     * @return boolean
     */
    boolean removeByTaskKey (String taskKey);
}
