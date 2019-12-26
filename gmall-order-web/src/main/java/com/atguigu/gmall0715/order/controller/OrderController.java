package com.atguigu.gmall0715.order.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall0715.bean.UserAddress;
import com.atguigu.gmall0715.service.UserInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author sujie
 * @date 2019-12-25-20:51
 */
@Controller
public class OrderController {

    @Reference
    private UserInfoService userInfoService;

    @RequestMapping("trade")
    @ResponseBody
    public List<UserAddress> trade(String userId){

        return userInfoService.getUserAddressByUserId(userId);
    }
}

