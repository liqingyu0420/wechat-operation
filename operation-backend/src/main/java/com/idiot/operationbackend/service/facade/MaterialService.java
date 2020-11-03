package com.idiot.operationbackend.service.facade;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.idiot.operationbackend.entity.Material;

import java.util.List;

/**
 * @author wang xiao
 * @date Created in 18:40 2020/9/18
 */
public interface MaterialService extends IService<Material> {

    /**
     *  添加素材
     * @author wangxiao
     * @date 14:38 2020/9/21
     * @param material material
     * @param wxJson wxJson
     * @return boolean
     */
    boolean addMaterial(Material material,String wxJson);


    /**
     *  查询素材
     * @author wangxiao
     * @date 15:02 2020/9/21
     * @param accountId accountId
     * @param type type
     * @param page page
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.idiot.operationbackend.entity.Material>
     */
    Page<Material> pageMaterial(String accountId,String type,int page);


    /**
     *  批量查询
     * @author wangxiao
     * @date 17:03 2020/9/21
     * @param idsStr idsStr
     * @return java.util.List<com.idiot.operationbackend.entity.Material>
     */
    List<Material> queryByIds(String idsStr);
}
