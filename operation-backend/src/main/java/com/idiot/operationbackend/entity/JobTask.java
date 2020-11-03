package com.idiot.operationbackend.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.idiot.operationbackend.support.Constants;

import java.time.LocalDateTime;

/**
 * 定时任务
 * @author wang xiao
 * @date Created in 14:43 2020/9/23
 */
@TableName("t_job_task")
public class JobTask {

    @TableId
    private String id;

    private String accountId;

    private String nikeName;

    private String headImage;

    private String taskLabel;

    private Long taskTimer;

    private Integer status;

    private Integer type;

    private String taskKey;

    private String createTime;

    private String updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getNikeName() {
        return nikeName;
    }

    public void setNikeName(String nikeName) {
        this.nikeName = nikeName;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getTaskLabel() {
        return taskLabel;
    }

    public void setTaskLabel(String taskLabel) {
        this.taskLabel = taskLabel;
    }

    public Long getTaskTimer() {
        return taskTimer;
    }

    public void setTaskTimer(Long taskTimer) {
        this.taskTimer = taskTimer;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTaskKey() {
        return taskKey;
    }

    public void setTaskKey(String taskKey) {
        this.taskKey = taskKey;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }


    public JobTask() {
    }

    public JobTask(String taskLabel, String sendTime, String taskKey,Integer type) {
        this.taskLabel = taskLabel;
        this.taskTimer = Constants.toEpochMilli(sendTime);
        this.taskKey = taskKey;
        this.type = type;
        this.status =Constants.WAITING;
        this.createTime = LocalDateTime.now().format(Constants.DATE_TIME_FORMATTER);
    }

    @Override
    public String toString() {
        return "JobTask{" +
                "id='" + id + '\'' +
                ", accountId='" + accountId + '\'' +
                ", nikeName='" + nikeName + '\'' +
                ", headImage='" + headImage + '\'' +
                ", taskLabel='" + taskLabel + '\'' +
                ", taskTimer=" + taskTimer +
                ", status=" + status +
                ", type=" + type +
                ", taskKey='" + taskKey + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }
}
