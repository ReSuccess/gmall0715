package com.atguigu.gmall0715.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall0715.bean.UserAddress;
import com.atguigu.gmall0715.bean.UserInfo;
import com.atguigu.gmall0715.service.UserInfoService;
import com.atguigu.gmall0715.user.mapper.UserAddressMapper;
import com.atguigu.gmall0715.user.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author sujie
 * @date 2019-12-25-20:24
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired(required = false)
    UserInfoMapper userInfoMapper;
    @Autowired(required = false)
    UserAddressMapper userAddressMapper;
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
}
