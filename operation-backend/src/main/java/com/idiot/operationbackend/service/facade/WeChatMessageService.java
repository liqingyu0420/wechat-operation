package com.idiot.operationbackend.service.facade;


import com.idiot.operationbackend.support.Constants;
import org.springframework.beans.factory.InitializingBean;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 微信消息处理类
 * @author wang xiao
 * @date Created in 11:31 2020/6/18
 */
public interface WeChatMessageService extends InitializingBean {

    /**
     *  处理消息回调
     * @author wangxiao
     * @date 11:33 2020/6/18
     * @param param param
     * @return java.lang.String
     */
    String processMessage(Map<String,String> param);


    /**
     *  默认不支持消息
     * @author wangxiao
     * @date 15:27 2020/7/1 
     * @param param param
     * @return java.lang.String
     */
    default String unSupportedMessage(Map<String,String> param) {
        return Constants.SUCCESS;
    }


}
