package com.idiot.operationbackend.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import javax.validation.constraints.NotEmpty;

/**
 *   图文详情
 * @author wang xiao
 * @date Created in 10:42 2020/9/21
 */
@TableName("t_account_articles")
public class Articles {

    @TableId
    private  String id;

    private String accountId;

    private String thumbMediaId;

    private String thumbUrl;

    @NotEmpty(message = "请填写标题")
    private String title;
    @NotEmpty(message = "请填写内容")
    private String content;
    @NotEmpty(message = "请填写内容链接地址")
    private String contentSourceUrl;

    private String createTime;

    private Integer showCoverPic;

    private String url;

    private Boolean release;


    private String  mediaId;

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

    public String getThumbMediaId() {
        return thumbMediaId;
    }

    public void setThumbMediaId(String thumbMediaId) {
        this.thumbMediaId = thumbMediaId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentSourceUrl() {
        return contentSourceUrl;
    }

    public void setContentSourceUrl(String contentSourceUrl) {
        this.contentSourceUrl = contentSourceUrl;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getShowCoverPic() {
        return showCoverPic;
    }

    public void setShowCoverPic(Integer showCoverPic) {
        this.showCoverPic = showCoverPic;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getRelease() {
        return release;
    }

    public void setRelease(Boolean release) {
        this.release = release;
    }

    @Override
    public String toString() {
        return "Articles{" +
                "id='" + id + '\'' +
                ", accountId='" + accountId + '\'' +
                ", thumbMediaId='" + thumbMediaId + '\'' +
                ", thumbUrl='" + thumbUrl + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", contentSourceUrl='" + contentSourceUrl + '\'' +
                ", createTime='" + createTime + '\'' +
                ", showCoverPic=" + showCoverPic +
                ", url='" + url + '\'' +
                ", release=" + release +
                ", mediaId='" + mediaId + '\'' +
                '}';
    }
}
