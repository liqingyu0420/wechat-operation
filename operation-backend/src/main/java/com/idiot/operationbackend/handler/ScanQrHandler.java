package com.idiot.operationbackend.handler;

import com.idiot.operationbackend.controller.WeChatController;
import com.idiot.operationbackend.service.facade.WeChatService;
import com.idiot.operationbackend.support.CustomException;
import com.idiot.operationbackend.support.ScanQrInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.Executors;

import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 *  扫描二维码 处理器
 * @author wangxiao
 * @date 18:04 2020/9/24
 */
@Component
public class ScanQrHandler implements InitializingBean {

    @Autowired
    private WeChatService weChatService;

    private static final ArrayDeque<ScanQrInfo> SCAN_QUEUE = new ArrayDeque<ScanQrInfo>(16);

    private final Logger logger = LoggerFactory.getLogger(WeChatController.class);

    public  void addElement (String accountId,String openId,String qrCodeId,String contents) {
        ScanQrInfo scanQrInfo = new ScanQrInfo(accountId,openId,qrCodeId,contents);
        logger.info("SCAN_QUEUE is add ");
        SCAN_QUEUE.offerLast(scanQrInfo);
    }

    private ScanQrInfo getElement (){
        return SCAN_QUEUE.pollFirst();
    }
    /**
     *  是否时空
     * @author wangxiao
     * @date 18:04 2020/9/24
     * @return boolean
     */
    private boolean isEmpty () {
        return SCAN_QUEUE.isEmpty();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("扫描二维码推送信息单线程服务---------------->start");

        Executors.newSingleThreadExecutor().execute(this::expire);
        logger.info("扫描二维码推送信息单线程服务------------------>end");
    }


    @SuppressWarnings("InfiniteLoopStatement")
    private void expire ()  {
        while(true) {
            if (isEmpty()){
                try {
                    TimeUnit.SECONDS.sleep(1);
                }catch (InterruptedException e){
                    logger.error("扫描二维码推送信息单线程服务 异常终止");
                }

            }
            ScanQrInfo scanQrInfo = getElement();
            if (Objects.nonNull(scanQrInfo)) {
                String openId = scanQrInfo.getOpenId();
                String accountId = scanQrInfo.getAccountId();
                String qrCodeId = scanQrInfo.getQrCodeId();
                String contents = scanQrInfo.getQrContents();
                logger.info("扫描二维码推送信息,qrCodeId:{},openId:{},accountId:{}----->start",qrCodeId,openId,accountId);
                try {
                    weChatService.processScanQrCode(accountId,openId,qrCodeId,contents);
                }catch (Exception e){
                    String errorStr = Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.joining("\n"));
                    logger.error("扫描二维码推送信息,------>is error {}",errorStr);
                }
            }
        }
    }


}
