package com.idiot.operationbackend.handler;

import com.idiot.operationbackend.service.facade.WeChatMessageService;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wang xiao
 * @date Created in 11:42 2020/9/11
 */
public class WeChatMessageFactory {



    private final static Map<String, WeChatMessageService> SERVICE_MAP = new HashMap<>(16);


    public static WeChatMessageService getService (String key) {
        return SERVICE_MAP.get(key);
    }

    public static void addService (String key, WeChatMessageService messageService) {
        SERVICE_MAP.put(key, messageService);
    }
}
