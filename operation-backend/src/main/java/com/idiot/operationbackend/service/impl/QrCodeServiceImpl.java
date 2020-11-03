package com.idiot.operationbackend.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.idiot.operationbackend.entity.QrCode;
import com.idiot.operationbackend.mappers.QrCodeMapper;
import com.idiot.operationbackend.service.facade.QrCodeService;
import com.idiot.operationbackend.support.Constants;
import com.idiot.operationbackend.support.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wang xiao
 * @date Created in 18:41 2020/9/21
 */
@Service
public class QrCodeServiceImpl extends ServiceImpl<QrCodeMapper, QrCode>
        implements QrCodeService {


    @Override
    public Page<QrCode> pageQrCode(int page, List<String> accountId) {
        Page<QrCode> queryPage = new Page<>(page,20);
        LambdaQueryWrapper<QrCode> queryWrapper = Wrappers.<QrCode>lambdaQuery()
                .in(QrCode::getAccountId,accountId)
                .orderByDesc(QrCode::getCreateTime);
        return page(queryPage,queryWrapper);
    }


    @Override
    public Page<QrCode> pageQrCode(int page, String accountId) {
        Page<QrCode> queryPage = new Page<>(page,20);
        LambdaQueryWrapper<QrCode> queryWrapper = Wrappers.<QrCode>lambdaQuery()
                .eq(QrCode::getAccountId,accountId)
                .orderByDesc(QrCode::getCreateTime);
        return page(queryPage,queryWrapper);
    }

    @Override
    public boolean upQrCode(QrCode qrCode, String wxJson) {
        if (StringUtils.isEmpty(wxJson)) {
            return removeById(qrCode.getId());
        }
        JSONObject jsonObject = JSON.parseObject(wxJson);
        String ticket = jsonObject.getString("ticket");
        String url = jsonObject.getString("url");
        if (StringUtils.isEmpty(ticket)) {
            removeById(qrCode.getId());
            String msg = jsonObject.getString("errmsg");
            throw new CustomException(500,"微信二维码生成错误"+msg);
        }
        qrCode.setUrl(url);
        qrCode.setTicket(ticket);
        long expire = jsonObject.getLongValue("expire_seconds");
        LocalDateTime now = LocalDateTime.now().plusSeconds(expire);
        qrCode.setExpireTime(now.format(Constants.DATE_TIME_FORMATTER));
        return updateById(qrCode);
    }
}
