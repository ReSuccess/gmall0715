package com.atguigu.gmall0715.service;

import com.atguigu.gmall0715.bean.UserAddress;
import com.atguigu.gmall0715.bean.UserInfo;

import java.util.List;

/**
 * @author sujie
 * @date 2019-12-25-20:22
 */
public interface UserInfoService {
    /**
     * 查询所有用户信息
     * @return
     */
    List<UserInfo> findAll();

    /**
     * 根据用户id查询用户收货地址
     * @param userId
     * @return
     */
    List<UserAddress> getUserAddressByUserId(String userId);

    /**
     * 登录，并返回登录信息
     * @return 登录用户信息
     * @param userInfo
     */
    UserInfo login(UserInfo userInfo);

    /**
     * 查询redis中是否存在该用户信息，即是否登录
     * @param userId
     * @return
     */
    UserInfo verify(String userId);
}
