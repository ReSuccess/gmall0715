package com.atguigu.gmall0715.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall0715.bean.UserAddress;
import com.atguigu.gmall0715.bean.UserInfo;
import com.atguigu.gmall0715.config.RedisUtil;
import com.atguigu.gmall0715.service.UserInfoService;
import com.atguigu.gmall0715.user.mapper.UserAddressMapper;
import com.atguigu.gmall0715.user.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * @author sujie
 * @date 2019-12-25-20:24
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {
    public String userKey_prefix="user:";
    public String userinfoKey_suffix=":info";
    public int userKey_timeOut=60*60*24*7;

    @Autowired(required = false)
    UserInfoMapper userInfoMapper;
    @Autowired(required = false)
    UserAddressMapper userAddressMapper;
    @Autowired
    RedisUtil redisUtil;


    @Override
    public List<UserInfo> findAll() {
        return userInfoMapper.selectAll();
    }

    @Override
    public List<UserAddress> getUserAddressByUserId(String userId) {
        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);
        return userAddressMapper.select(userAddress);
    }

    @Override
    public UserInfo login(UserInfo userInfo) {
        //md5加密
        String passwd = userInfo.getPasswd();
        String newPasswd = DigestUtils.md5DigestAsHex(passwd.getBytes());
        userInfo.setPasswd(newPasswd);
        UserInfo info = userInfoMapper.selectOne(userInfo);
        if(info != null){
            //将用户信息存放到jedis中
            Jedis jedis = null;
            try {
                jedis = redisUtil.getJedis();
                //生成key
                String userKey = userKey_prefix + info.getId() + userinfoKey_suffix;
                jedis.setex(userKey,userKey_timeOut, JSON.toJSONString(info));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(jedis != null){
                    jedis.close();
                }
            }
            return info;
        }
        return null;
    }

    @Override
    public UserInfo verify(String userId) {
        Jedis jedis = redisUtil.getJedis();
        String userKey = userKey_prefix + userId +userinfoKey_suffix;
        String userInfoJson = jedis.get(userKey);
        if(!StringUtils.isEmpty(userInfoJson)){
            return JSON.parseObject(userInfoJson,UserInfo.class);
        }
        return null;
    }
}
