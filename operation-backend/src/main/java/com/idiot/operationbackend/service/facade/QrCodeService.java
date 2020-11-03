package com.idiot.operationbackend.service.facade;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.idiot.operationbackend.entity.QrCode;

import java.util.List;

/**
 * @author wang xiao
 * @date Created in 18:40 2020/9/21
 */
public interface QrCodeService extends IService<QrCode> {

    /**
     *  二维码查询
     * @author wangxiao
     * @date 19:11 2020/9/21
     * @param page
     * @param accountId
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.idiot.operationbackend.entity.QrCode>
     */
    Page<QrCode> pageQrCode (int page, List<String> accountId);


    /**
     *  二维码查询
     * @author wangxiao
     * @date 19:11 2020/9/21
     * @param page
     * @param accountId
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.idiot.operationbackend.entity.QrCode>
     */
    Page<QrCode> pageQrCode (int page, String accountId);


    /**
     *  微信返回 后二维码修改
     * @author wangxiao
     * @date 19:52 2020/9/21
     * @param qrCode
     * @param wxJson
     * @return boolean
     */
    boolean upQrCode (QrCode qrCode,String wxJson);
}
