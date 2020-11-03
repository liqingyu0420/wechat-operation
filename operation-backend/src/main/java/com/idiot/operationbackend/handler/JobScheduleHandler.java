package com.idiot.operationbackend.handler;

import com.idiot.operationbackend.entity.CustomerMsg;
import com.idiot.operationbackend.entity.GroupMsg;
import com.idiot.operationbackend.entity.JobTask;
import com.idiot.operationbackend.entity.TemplateMsg;
import com.idiot.operationbackend.service.facade.*;
import com.idiot.operationbackend.support.Constants;


import com.idiot.operationbackend.support.job.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author wang xiao
 * @date Created in 15:24 2020/9/23
 */
@Component
public class JobScheduleHandler implements InitializingBean {

    /**
     * 添加进来的job 未注册
     */
    private final JobContainer<String, JobTask> taskContainer = new JobContainer<>();

    /**
     * 注册进来的 ScheduledFuture
     */
    private final JobContainer<String, ScheduledFuture<?>> futureContainer = new JobContainer<>();

    private final Logger logger = LoggerFactory.getLogger(JobScheduleHandler.class);

    private  ReentrantLock lock ;

    @Autowired
    private WeChatService weChatService;

    @Autowired
    @Qualifier(value = "taskScheduler")
    private ThreadPoolTaskScheduler taskScheduler;


    @Autowired
    private JobTaskService jobTaskService;

    @Autowired
    private GroupMsgService groupMsgService;


    @Autowired
    private TemplateMsgService templateMsgService;


    @Autowired
    private CustomerMsgService customerMsgService;


    /**
     *  添加任务
     * @author wangxiao
     * @date 16:05 2020/9/23
     * @param jobTask 任务
     * @return java.lang.Boolean
     */
    public Boolean addJob(JobTask jobTask) {
        String taskKey = jobTask.getTaskKey();
        logger.info("任务:[任务id:{},描述:{}],添加任务------>start",jobTask.getId(),jobTask.getTaskLabel());
        lock = new ReentrantLock();
        lock.lock();
        try{
            if (isAdd(taskKey)) {
                logger.info("任务:[任务id:{},描述:{}],添加任务------>已经存在",jobTask.getId(),jobTask.getTaskLabel());
                return false;
            }
            this.taskContainer.put(taskKey,jobTask);
            logger.info("任务:[任务id:{},描述:{}],添加任务------>end",jobTask.getId(),jobTask.getTaskLabel());
        }finally {
            lock.unlock();
            logger.info("任务:[任务id:{},描述:{}],添加任务------>lock unlock",jobTask.getId(),jobTask.getTaskLabel());
        }
        return null;
    }

    /**
     *  执行注册任务
     * @author wangxiao
     * @date 16:00 2020/9/23
     * @param taskKey taskKey
     */
    private void registerTask(String  taskKey) {
        JobTask jobTask = taskContainer.get(taskKey);
        if (Objects.isNull(jobTask)) {
            return;
        }
        if (isRegister(taskKey)){
            logger.info("任务:[任务id:{},描述:{}]注册------->已经注册",jobTask.getId(),jobTask.getTaskLabel());
        }
        logger.info("任务:[任务id:{},描述:{}]注册------->start",jobTask.getId(),jobTask.getTaskLabel());
        lock = new ReentrantLock();
        lock.lock();
        try {
            Long taskTime = jobTask.getTaskTimer();
            BaseJobTask scheduledTask = getTaskByKey(taskKey);
            if (Objects.isNull(scheduledTask)) {
                logger.info("任务:[任务id:{},描述:{}]注册------->is null", jobTask.getId(), jobTask.getTaskLabel());
                return;
            }
            ScheduledFuture<?> scheduledFuture = taskScheduler.schedule(scheduledTask, new Date(taskTime));
            futureContainer.put(taskKey, scheduledFuture);
        }finally {
            lock.unlock();
            logger.info("任务:[任务id:{},描述:{}]注册------->lock unlock", jobTask.getId(), jobTask.getTaskLabel());
        }
        logger.info("任务:[任务id:{},描述:{}]注册------->end",jobTask.getId(),jobTask.getTaskLabel());
    }

    /**
     *  添加并注册
     * @author wangxiao
     * @date 16:35 2020/9/23
     * @param jobTask jobTask
     */
    public void addAndRegisterJob(JobTask jobTask) {
        String taskKey = jobTask.getTaskKey();
        if (LocalDateTime.now().toEpochSecond(Constants.DEFAULT_ZONE) > jobTask.getTaskTimer()) {
            return;
        }
        addJob(jobTask);
        registerTask(taskKey);
    }

   /**
    * 取消任务
    * @author wangxiao
    * @date 16:02 2020/9/23
    * @param taskKey taskKey
    * @return java.lang.Boolean
    */
    public Boolean cancelJob(String taskKey) {
        logger.info("任务:[任务taskKey:{}]取消任务------->start",taskKey);
        boolean taskStartFlag = futureContainer.containsKey(taskKey);
        logger.info("任务:[任务taskKey:{}是否存在:{}]取消任务------->start",taskKey,taskStartFlag);
        if (taskStartFlag) {
            ScheduledFuture<?> scheduledFuture = futureContainer.get(taskKey);
            scheduledFuture.cancel(true);
            futureContainer.remove(taskKey);
        }
        taskContainer.remove(taskKey);
        logger.info("任务:[任务taskKey:{}]取消任务------->end",taskKey);
        return taskStartFlag;
    }

    /**
     *  重新添加
     * @author wangxiao
     * @date 16:25 2020/9/23
     * @param taskKey taskKey
     * @return java.lang.Boolean
     */
    public Boolean restart(String taskKey) {
        JobTask jobTask = taskContainer.get(taskKey);
        logger.info("任务:[任务id:{},公众号:{},描述:{}]重启",jobTask.getId(),jobTask.getAccountId(),jobTask.getTaskLabel());
        this.cancelJob(taskKey);
        return this.addJob(jobTask);
    }


    /**
     *  是否已经注册成功
     * @author wangxiao
     * @date 15:59 2020/9/23
     * @param taskKey taskKey
     * @return java.lang.Boolean
     */
    private Boolean isRegister(String taskKey) {
        if (futureContainer.containsKey(taskKey)) {
            if (!futureContainer.get(taskKey).isCancelled()) {
                return true;
            }
        }
        return false;
    }

    /**
     *  是否已经添加成功
     * @author wangxiao
     * @date 15:59 2020/9/23
     * @param taskKey taskKey
     * @return java.lang.Boolean
     */
    private Boolean isAdd(String taskKey) {
        return taskContainer.containsKey(taskKey);
    }

    /**
     * 获取任务
     * @author wangxiao
     * @date 16:29 2020/9/23
     * @param taskKey 任务key
     * @return com.idiot.operationbackend.support.job.BaseScheduledTask
     */
    private BaseJobTask getTaskByKey (String taskKey) {
        JobTask jobTask = taskContainer.get(taskKey);
        if (Objects.isNull(jobTask)){
            return null;
        }
        if (Constants.GROUP == jobTask.getType()) {
            GroupMsg groupMsg = groupMsgService.getById(taskKey);
            return new GroupJobTask(groupMsg,weChatService);
        }else if (Constants.TEMPLATE == jobTask.getType()) {
            TemplateMsg templateMsg = templateMsgService.getById(taskKey);
            return new TemplateJobTask(templateMsg,weChatService);
        }else if (Constants.CUSTOMER == jobTask.getType()) {
            CustomerMsg customerMsg = customerMsgService.getById(taskKey);
            return new CustomerJobTask(customerMsg,weChatService);
        }
        return null;
    }

    public void saveToDB(JobTask jobTask) {
        jobTaskService.saveOrUpdate(jobTask);
    }

    public void removeToDB (String taskKey){
        jobTaskService.removeByTaskKey(taskKey);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        List<JobTask> jobTaskList = jobTaskService.queryByInitJob();
        if (CollectionUtils.isEmpty(jobTaskList)) {
            return;
        }
        int size = jobTaskList.size();;
        logger.error("!!!!初始化启动加载job!!!!--------->start,job size is:{}",size);
        for (JobTask jobTask : jobTaskList) {
            addAndRegisterJob(jobTask);
        }
        logger.error("!!!!初始化启动加载job!!!!--------->end,  job size is:{}",size);
    }
}
