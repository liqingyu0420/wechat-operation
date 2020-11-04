package com.idiot.operationbackend.support;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author wang xiao
 * @date Created in 11:52 2020/9/11
 */
public class Constants {

    private static final Logger logger = LoggerFactory.getLogger("Constants");
    public static final String SUCCESS = "success";
    public static final String FAIL = "fail";
    public static final String FIRST = "1";

    public static final Locale DEFAULT_LOCALE = Locale.CHINA;

    public static final ZoneOffset DEFAULT_ZONE= ZoneOffset.of("+8");

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss",DEFAULT_LOCALE);


    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd",DEFAULT_LOCALE);


    public static final String DEFAULT_HEAD_IMG = "http://pic.51yuansu.com/pic3/cover/01/69/80/595f67c2aff1e_610.jpg";

    /**
     * 第三方 ACCESS_TOKEN
     */
    public static final String COMPONENT_ACCESS_TOKEN = "COMPONENT_ACCESS_TOKEN_%s";
    /**
     * 授权方 ACCESS_TOKEN
     */
    public static final String AUTHORIZER_ACCESS_TOKEN = "AUTHORIZER_ACCESS_TOKEN_%s";

    /**
     * 同步用户锁
     */
    public static final String LOCK_SYNC_USER = "LOCK_SYNC_USER_%s";

    /**
     * 同步标签锁
     */
    public static final String LOCK_SYNC_TAG = "LOCK_SYNC_TAG_%s";

    /**
     * 时间 00:00:00
     */
    public static final LocalTime DEFAULT_TIME = LocalTime.of(0,0,0);

    /**
     * 0.00
     */
    public static final BigDecimal ZERO_RATE = BigDecimal.valueOf(0.00);

    /**
     * 粉丝动作 0-粉丝消息,1-新关注,2-关注,3-取关,4-扫描二维码,5-菜单点击
     */
    public static final int NEW = 1;

    public static final int MSG = 0;

    public static final int SUBSCRIBE = 2;

    public static final int CANCEL = 3;

    public static final int SCAN = 4;

    public static final int MENU = 5;


    public static final long IMAGE_MAX_SIZE = 1024*1024*10;
    public static final long VOICE_MAX_SIZE = 1024*1024*2;
    public static final long VIDEO_MAX_SIZE = 1024*1024*10;
    public static final long THUMB_MAX_SIZE = 64*1024;

    public static final String IMAGE_SUPPORTED= "bmp,png,jpeg,jpg,gif";
    public static final String VOICE_SUPPORTED= "mp3,wma,wav,amr";
    public static final String VIDEO_SUPPORTED= "mp4";
    public static final String THUMB_SUPPORTED= "JPG";


    public static final String IMAGE = "image";

    public static final String VOICE = "voice";

    public static final String VIDEO = "video";

    public static final String THUMB = "thumb";


    /**
     *  发送状态码 '0'-已发送,'1'-未发送,'2'-发送中, '3'-发送终止,4'-发送失败
     */
     public static final int SUCCESSED = 0;

     public static final int WAITING = 1;

     public static final int RUNNING = 2;

     public static final int END = 3;

     public static final int FAILED = 4;


    /**
     *  发送消息状态码 类型,1-图文,2-图片,3-文字,4-音频,5-视频"
     */
    public static final int NEWS = 1;

    public static final int IMG = 2;

    public static final int TEXT = 3;

    public static final int VOI = 4;

    public static final int VID = 5;

    public static final int MNEWS= 6;

    /**
     *  消息任务类型 群发，模板 ，客服
     */
    public static final int GROUP =1;
    public static final int TEMPLATE = 2;
    public static final int CUSTOMER = 3;


    /**
     *  计算 比率
     * @author wangxiao
     * @date 10:58 2020/9/17
     * @param now now
     * @param before before
     * @return java.math.BigDecimal
     */
    public static BigDecimal calcRate(Long now, Long before) {
        long nowNum  =  null != now? now :0;
        long beforeNum  =  null != before? before :0;
        if (0 == beforeNum) {
            return BigDecimal.valueOf(100);
        } else if (0 == nowNum) {
            return Constants.ZERO_RATE;
        }else {
            return BigDecimal.valueOf(((nowNum - beforeNum) * 100) / beforeNum).setScale(2);
        }
    }

    // 以下三个方法 感觉是多余 但是懒得删除 --- start
    /**
     *  时间格式是yyyy-mm-dd
     * @author wangxiao
     * @date 10:45 2020/9/17
     * @param dateStr dateStr
     * @return java.time.LocalDateTime
     */
    public static LocalDateTime toLocalDateTime (String dateStr) {
        return LocalDate.parse(dateStr,DATE_FORMATTER).atTime(0,0,0);
    }


    /**
     *  时间格式是yyyy-mm-dd hh:mm:ss
     * @author wangxiao
     * @date 10:45 2020/9/17
     * @param dateStr dateStr
     * @return java.time.LocalDateTime
     */
    public static LocalDateTime parseLocalDateTime (String dateStr) {
        return LocalDateTime.parse(dateStr,DATE_TIME_FORMATTER);
    }

    public static long  toEpochMilli (String dateStr) {
        return LocalDateTime.parse(dateStr,DATE_TIME_FORMATTER).toInstant(DEFAULT_ZONE).toEpochMilli();
    }



    /**
     *  描述转时间（多余）
     * @author wangxiao
     * @date 10:57 2020/9/17
     * @param epochSecond epochSecond
     * @return java.time.LocalDateTime
     */
    public static LocalDateTime toLocalDateTime (long epochSecond ) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(epochSecond),DEFAULT_ZONE);

    }

    /**
     *  时间转秒数（多余）
     * @author wangxiao
     * @date 10:57 2020/9/17
     * @param dateTime dateTime
     * @return java.time.LocalDateTime
     */
    public static long toEpochSecond (LocalDateTime dateTime) {
        return dateTime.toEpochSecond(DEFAULT_ZONE);
    }
    // 以上三个方法 感觉是多余 但是懒得删除 --- end


    public static final Cache<String,String> CACHE = CacheBuilder.newBuilder()
            .concurrencyLevel(8)
            .expireAfterWrite(100, TimeUnit.MINUTES)
            .initialCapacity(10)
            .maximumSize(100)
            .recordStats()
            .removalListener(notification -> logger.warn("cache {} was removed, cause is {}" ,notification.getKey(), notification.getCause()))
            .build();


    public static final Cache<String,String> LOCK_CACHE = CacheBuilder.newBuilder()
            .concurrencyLevel(2)
            .expireAfterWrite(10, TimeUnit.SECONDS)
            .initialCapacity(2)
            .maximumSize(10)
            .recordStats()
            .removalListener(notification -> logger.warn("cache {} was removed, cause is {}" ,notification.getKey(), notification.getCause()))
            .build();

    /**
     * 下划线和驼峰 序列化 时候
     */
    public static  SerializeConfig CAMEL_CASE_CONFIG = new SerializeConfig();
    public static  SerializeConfig SNAKE_CASE_CONFIG = new SerializeConfig();

    static {
        SNAKE_CASE_CONFIG.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
        CAMEL_CASE_CONFIG.propertyNamingStrategy = PropertyNamingStrategy.CamelCase;
    }

    public static String byteToStr(byte[] byteArray) {
        StringBuilder strDigest = new StringBuilder();
        for (byte b : byteArray) {
            strDigest.append(byteToHexStr(b));
        }
        return strDigest.toString();
    }

    private static String byteToHexStr(byte mByte) {
        final char[] digit = { '0', '1' , '2', '3',
                '4' , '5', '6', '7' , '8',
                '9', 'A' , 'B', 'C', 'D' , 'E', 'F'};
        char[] tempArr = new char[2];
        tempArr[0] = digit[(mByte >>> 4) & 0X0F];
        tempArr[1] = digit[mByte & 0X0F];
        return new String(tempArr);
    }

    /**
     *  jsonObject 里面的下划线转 驼峰 应该有更好的方法
     * @author https://www.cnblogs.com/luchangyou/p/6278370.html
     * @date 15:06 2020/10/28
     * @param json json
     */
    public static void convert(Object json) {
        if (json instanceof JSONArray) {
            JSONArray arr = (JSONArray) json;
            for (Object obj : arr) {
                convert(obj);
            }
        } else if (json instanceof JSONObject) {
            JSONObject jo = (JSONObject) json;
            Set<java.lang.String> keys = jo.keySet();
            String[] array = keys.toArray(new String[0]);
            for (String key : array) {
                Object value = jo.get(key);
                String[] key_strs = key.split("_");
                if (key_strs.length > 1) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < key_strs.length; i++) {
                        String ks = key_strs[i];
                        if (!"".equals(ks)) {
                            if (i == 0) {
                                sb.append(ks);
                            } else {
                                int c = ks.charAt(0);
                                if (c >= 97 && c <= 122) {
                                    int v = c - 32;
                                    sb.append((char) v);
                                    if (ks.length() > 1) {
                                        sb.append(ks.substring(1));
                                    }
                                } else {
                                    sb.append(ks);
                                }
                            }
                        }
                    }
                    jo.remove(key);
                    jo.put(sb.toString(), value);
                }
                convert(value);
            }
        }
    }
}
