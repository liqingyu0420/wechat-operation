package com.idiot.operationbackend.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.idiot.operationbackend.support.Constants;
import com.idiot.operationbackend.support.CustomException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * jwt token 工具
 * @author wang xiao
 * @date Created in 19:50 2020/9/10
 */
public class JwtTokenUtil {

    /**
     * 过期时间为2小时
     */
    private static final long EXPIRE_TIME = 1000*60*60*2;

    /**
     * Token私钥
     */
    private static final String TOKEN_SECRET = "2fba06fd5a77b06a0cf04b9c0967e6d6";

    /**
     * 生成签名
     * @param userName
     * @param userId
     */
    public static String sign(String userName,String userId){
        return createToken(userName, userId, EXPIRE_TIME,TimeUnit.MILLISECONDS,TOKEN_SECRET);
    }

    /**
     * 生成签名
     * @param userName
     * @param userId
     * @param expireTime
     */
    public static String sign(String userName,String userId,long expireTime){
        return createToken(userName, userId, expireTime,TimeUnit.MILLISECONDS,TOKEN_SECRET);
    }

    public static String sign(String userName,String userId,long expireTime,String secret){
        return createToken(userName, userId, expireTime,TimeUnit.MILLISECONDS,secret);
    }
    public static String sign(String userName,String userId,long expireTime,TimeUnit timeUnit){
        return createToken(userName, userId, expireTime,timeUnit,TOKEN_SECRET);
    }


    /**
     * 验证token
     * @param token
     * @return boolean
     */
    public static boolean verity(String token){
        return verityToken(token, TOKEN_SECRET);
    }

    /**
     * 验证token
     * @param token
     * @param secret
     * @return boolean
     */
    public static boolean verity(String token,String secret){
        return verityToken(token, secret);
    }


    /**
     * 获取用户信息
     * @param token
     * @param secret
     * @return String
     */
    public static String getUserId (String token,String secret) {
        return resolvingToken(token, secret).get("userId").asString();
    }
    /**
     * 获取用户信息
     * @param token
     * @return String
     */
    public static String getUserId (String token) {
        return resolvingToken(token, TOKEN_SECRET).get("userId").asString();
    }

    /**
     * 获取用户信息
     * @param token
     * @param secret
     * @return String
     */
    public static String getUserName (String token,String secret) {
        return resolvingToken(token, secret).get("userName").asString();
    }
    /**
     * 获取用户信息
     * @param token
     * @return String
     */
    public static String getUserName (String token) {
        return resolvingToken(token, TOKEN_SECRET).get("userName").asString();
    }



    /**
     * 生成token
     * @param userName 用户名称
     * @param userId 用户id
     * @param expireTime 过期时间
     * @param timeUnit 时间
     * @param secret 密钥
     * @return String
     */
    private static String createToken(String userName, String userId, long expireTime, TimeUnit timeUnit,String secret){
        expireTime = timeUnit.convert(expireTime,TimeUnit.MILLISECONDS);
        Date date = new Date(System.currentTimeMillis() + expireTime);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        HashMap<String,Object> header = new HashMap<>(2);
        header.put("typ","JWT");
        header.put("alg","HS256");
        return JWT.create().withHeader(header).withClaim("userName",userName)
                .withClaim("userId",userId).withExpiresAt(date).sign(algorithm);
    }

    /**
     * 解析Token
     * @param token
     * @param secret
     * @return
     */
    private static Map<String, Claim> resolvingToken(String token,String secret){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getClaims();
        }catch (Exception e){
            throw new CustomException(401,"token is disabled");
        }
    }

    /**
     * 验证token
     * @param token
     * @param secret
     * @return
     */
    private static boolean verityToken(String token, String secret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        }catch (Exception e){
            throw new CustomException(401,"token is disabled");
        }
    }
}
