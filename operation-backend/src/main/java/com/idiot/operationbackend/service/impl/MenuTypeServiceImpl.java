package com.idiot.operationbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.idiot.operationbackend.entity.MenuType;
import com.idiot.operationbackend.mappers.MenuTypeMapper;
import com.idiot.operationbackend.service.facade.MenuTypeService;
import org.springframework.stereotype.Service;

/**
 * @author wang xiao
 * @date Created in 11:52 2020/9/22
 */
@Service
public class MenuTypeServiceImpl extends ServiceImpl<MenuTypeMapper, MenuType> implements MenuTypeService {
}
