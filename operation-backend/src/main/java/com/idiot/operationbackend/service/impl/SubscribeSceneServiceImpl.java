package com.idiot.operationbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.idiot.operationbackend.entity.SubscribeScene;
import com.idiot.operationbackend.mappers.SubscribeSceneMapper;
import com.idiot.operationbackend.service.facade.SubscribeSceneService;
import org.springframework.stereotype.Service;

/**
 * @author wang xiao
 * @date Created in 17:49 2020/9/16
 */
@Service
public class SubscribeSceneServiceImpl extends ServiceImpl<SubscribeSceneMapper, SubscribeScene>
        implements SubscribeSceneService {
}
