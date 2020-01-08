package com.atguigu.gmall0715.passport.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.Base64UrlCodec;

import java.util.Map;

/**
 * 用于生成token和解析token中携带的数据
 * @author sujie
 * @date 2020-01-07-18:34
 */
public class JwtUtil {
    /**
     * 生成token
     * @param key 公共部分
     * @param param 私有部分，即用户部分信息
     * @param salt 盐  服务的ip
     * @return
     */
    public static String encode(String key,Map<String,Object> param,String salt){
        if(salt!=null){
            key+=salt;
        }
        // 将最新的key 进行base64UrlCodec
        Base64UrlCodec base64UrlCodec = new Base64UrlCodec();
        key = base64UrlCodec.encode(key);

        JwtBuilder jwtBuilder = Jwts.builder().signWith(SignatureAlgorithm.HS256,key);

        jwtBuilder = jwtBuilder.setClaims(param);

        return jwtBuilder.compact();

    }

    /**
     * 获取token中 存放的用户数据
     * @param token
     * @param key
     * @param salt
     * @return
     */
    public  static Map<String,Object> decode(String token , String key, String salt){
        Claims claims=null;
        if (salt!=null){
            key+=salt;
        }
        Base64UrlCodec base64UrlCodec = new Base64UrlCodec();
        key = base64UrlCodec.encode(key);

        try {
            claims= Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        } catch ( JwtException e) {
            return null;
        }
        return  claims;
    }

}
