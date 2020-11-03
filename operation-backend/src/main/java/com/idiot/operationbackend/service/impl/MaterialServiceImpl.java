package com.idiot.operationbackend.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.idiot.operationbackend.entity.Material;
import com.idiot.operationbackend.mappers.MaterialMapper;
import com.idiot.operationbackend.service.facade.MaterialService;
import com.idiot.operationbackend.support.CustomException;
import com.idiot.operationbackend.support.enums.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.*;

/**
 * @author wang xiao
 * @date Created in 18:41 2020/9/18
 */
@Service
public class MaterialServiceImpl extends ServiceImpl<MaterialMapper, Material>
        implements MaterialService {


    @Override
    public boolean addMaterial(Material material, String wxJson) {
        if(Objects.isNull(wxJson)|| Objects.isNull(material)){
            return false;
        }
        JSONObject jsonObject = JSON.parseObject(wxJson);
        String mediaId = jsonObject.getString("media_id");
        String wxUrl = jsonObject.getString("url");
        if (StringUtils.isEmpty(mediaId) || StringUtils.isEmpty(wxUrl)){
            return false;
        }
        material.setMediaId(mediaId);
        material.setUrl(wxUrl);
        return save(material);
    }


    @Override
    public Page<Material> pageMaterial(String accountId, String type,int page) {
        int code  = Optional.ofNullable(MediaType.labelOf(type)).map(MediaType::getCode).orElseThrow(()-> new CustomException(500,"素材类型选择错误"));
        Page<Material> queryPage = new Page<>(page,15);
        queryPage.setSearchCount(true);
        LambdaQueryWrapper<Material> queryWrapper = Wrappers.<Material>lambdaQuery()
                .eq(Material::getAccountId,accountId)
                .eq(Material::getType,code)
                .orderByDesc(Material::getCreateTime);
        return page(queryPage,queryWrapper);
    }


    @Override
    public List<Material> queryByIds(String idsStr) {
        if (StringUtils.isEmpty(idsStr)) {
            return null;
        }
        List<String> ids = new ArrayList<>(Arrays.asList(idsStr.split(",")));
        return baseMapper.selectBatchIds(ids);
    }
}
