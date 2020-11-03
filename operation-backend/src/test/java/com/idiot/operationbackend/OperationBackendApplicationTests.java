package com.idiot.operationbackend;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.parser.ParserConfig;
import com.idiot.operationbackend.entity.Account;
import com.idiot.operationbackend.entity.AccountFans;
import com.idiot.operationbackend.entity.AuthUser;

import com.idiot.operationbackend.service.facade.*;
import com.idiot.operationbackend.support.Constants;
import com.idiot.operationbackend.support.wechat.AesException;
import com.idiot.operationbackend.support.wechat.WXBizMsgCrypt;
import com.idiot.operationbackend.util.JwtTokenUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
class OperationBackendApplicationTests {

    @Autowired
    WeChatService weChatService;

    @Autowired
    private AccountTagService tagService;

    @Autowired
    private AccountFansService fansService;

    @Test
    void contextLoads() {

    }

    String encodingAesKey = "FDl8GfVXfGWwKs9LKc11xE6N2f8DM6MB8cyMm6xYsac";
    String token = "eNoUNRR4e7V85KLb";
    String timestamp = "1409304348";
    String nonce = "xxxxxx";
    String appId = "wxbc8cf6d177029077";
    String replyMsg = "我是中文abcd123";
    String xmlFormat = "<xml><ToUserName><![CDATA[toUser]]></ToUserName><Encrypt><![CDATA[%1$s]]></Encrypt></xml>";
    String afterAesEncrypt = "jn1L23DB+6ELqJ+6bruv21Y6MD7KeIfP82D6gU39rmkgczbWwt5+3bnyg5K55bgVtVzd832WzZGMhkP72vVOfg==";
    String randomStr = "aaaabbbbccccdddd";

    String replyMsg2 = "<xml><ToUserName><![CDATA[oia2Tj我是中文jewbmiOUlr6X-1crbLOvLw]]></ToUserName><FromUserName><![CDATA[gh_7f083739789a]]></FromUserName><CreateTime>1407743423</CreateTime><MsgType><![CDATA[video]]></MsgType><Video><MediaId><![CDATA[eYJ1MbwPRJtOvIEabaxHs7TX2D-HV71s79GUxqdUkjm6Gs2Ed1KF3ulAOA9H1xG0]]></MediaId><Title><![CDATA[testCallBackReplyVideo]]></Title><Description><![CDATA[testCallBackReplyVideo]]></Description></Video></xml>";
    String afterAesEncrypt2 = "jn1L23DB+6ELqJ+6bruv23M2GmYfkv0xBh2h+XTBOKVKcgDFHle6gqcZ1cZrk3e1qjPQ1F4RsLWzQRG9udbKWesxlkupqcEcW7ZQweImX9+wLMa0GaUzpkycA8+IamDBxn5loLgZpnS7fVAbExOkK5DYHBmv5tptA9tklE/fTIILHR8HLXa5nQvFb3tYPKAlHF3rtTeayNf0QuM+UW/wM9enGIDIJHF7CLHiDNAYxr+r+OrJCmPQyTy8cVWlu9iSvOHPT/77bZqJucQHQ04sq7KZI27OcqpQNSto2OdHCoTccjggX5Z9Mma0nMJBU+jLKJ38YB1fBIz+vBzsYjrTmFQ44YfeEuZ+xRTQwr92vhA9OxchWVINGC50qE/6lmkwWTwGX9wtQpsJKhP+oS7rvTY8+VdzETdfakjkwQ5/Xka042OlUb1/slTwo4RscuQ+RdxSGvDahxAJ6+EAjLt9d8igHngxIbf6YyqqROxuxqIeIch3CssH/LqRs+iAcILvApYZckqmA7FNERspKA5f8GoJ9sv8xmGvZ9Yrf57cExWtnX8aCMMaBropU/1k+hKP5LVdzbWCG0hGwx/dQudYR/eXp3P0XxjlFiy+9DMlaFExWUZQDajPkdPrEeOwofJb";



    @Test
    public void testNormal() throws ParserConfigurationException, SAXException, IOException {
        try {
            WXBizMsgCrypt pc = new WXBizMsgCrypt(token, encodingAesKey, appId);
            String afterEncrpt = pc.encryptMsg(replyMsg2, timestamp, nonce);
            System.out.println(afterEncrpt);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            StringReader sr = new StringReader(afterEncrpt);
            InputSource is = new InputSource(sr);
            Document document = db.parse(is);

            Element root = document.getDocumentElement();
            NodeList nodelist1 = root.getElementsByTagName("Encrypt");
            NodeList nodelist2 = root.getElementsByTagName("MsgSignature");

            String encrypt = nodelist1.item(0).getTextContent();
            String msgSignature = nodelist2.item(0).getTextContent();
            String fromXML = String.format(xmlFormat, encrypt);

            // 第三方收到公众号平台发送的消息
            String afterDecrpt = pc.decryptMsg(msgSignature, timestamp, nonce, fromXML,false);
            System.out.println(afterDecrpt);
            assertEquals(replyMsg2, afterDecrpt);
        } catch (AesException e) {
            fail("正常流程，怎么就抛出异常了？？？？？？");
        }
    }

    @Test
    public void testAesEncrypt() {
        try {
            WXBizMsgCrypt pc = new WXBizMsgCrypt(token, encodingAesKey, appId);
            assertEquals(afterAesEncrypt, pc.encrypt(randomStr, replyMsg));
        } catch (AesException e) {
            e.printStackTrace();
            fail("no异常");
        }
    }

    @Test
    public void testAesEncrypt2() {
        try {
            WXBizMsgCrypt pc = new WXBizMsgCrypt(token, encodingAesKey, appId);
            assertEquals(afterAesEncrypt2, pc.encrypt(randomStr, replyMsg2));

        } catch (AesException e) {
            e.printStackTrace();
            fail("no异常");
        }
    }

    @Test
    public void testIllegalAesKey() {
        try {
            new WXBizMsgCrypt(token, "abcde", appId);
        } catch (AesException e) {
            assertEquals(AesException.ILLEGAL_AES_KEY, e.getCode());
            return;
        }
        fail("错误流程不抛出异常？？？");
    }

    @Test
    public void testValidateSignatureError() throws ParserConfigurationException, SAXException,
            IOException {
        try {
            WXBizMsgCrypt pc = new WXBizMsgCrypt(token, encodingAesKey, appId);
            String afterEncrpt = pc.encryptMsg(replyMsg, timestamp, nonce);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            StringReader sr = new StringReader(afterEncrpt);
            InputSource is = new InputSource(sr);
            Document document = db.parse(is);

            Element root = document.getDocumentElement();
            NodeList nodelist1 = root.getElementsByTagName("Encrypt");

            String encrypt = nodelist1.item(0).getTextContent();
            String fromXML = String.format(xmlFormat, encrypt);
            pc.decryptMsg("12345", timestamp, nonce, fromXML,false); // 这里签名错误
        } catch (AesException e) {
            assertEquals(AesException.VALIDATE_SIGNATURE_ERROR, e.getCode());
            return;
        }
        fail("错误流程不抛出异常？？？");
    }

    @Test
    public void testVerifyUrl() throws AesException {
        WXBizMsgCrypt wxcpt = new WXBizMsgCrypt("QDG6eK",
                "jWmYm7qr5nMoAUwZRjGtBxmz3KA1tkAj3ykkR6q2B2C", "wx5823bf96d3bd56c7");
        String verifyMsgSig = "5c45ff5e21c57e6ad56bac8758b79b1d9ac89fd3";
        String timeStamp = "1409659589";
        String nonce = "263014780";
        String echoStr = "P9nAzCzyDtyTWESHep1vC5X9xho/qYX3Zpb4yKa9SKld1DsH3Iyt3tP3zNdtp+4RPcs8TgAE7OaBO+FZXvnaqQ==";
        wxcpt.verifyUrl(verifyMsgSig, timeStamp, nonce, echoStr);
        // 只要不抛出异常就好
    }


    @Test
    public void  queryUser () {
        System.out.println(JwtTokenUtil.sign("admin","1305345329560653826"));
    }

}
