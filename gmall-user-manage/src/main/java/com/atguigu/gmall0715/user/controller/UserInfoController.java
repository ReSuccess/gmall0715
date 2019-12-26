package com.atguigu.gmall0715.user.controller;

import com.atguigu.gmall0715.bean.UserInfo;
import com.atguigu.gmall0715.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author sujie
 * @date 2019-12-25-20:31
 */
@Controller
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @RequestMapping("findAll")
    @ResponseBody
    public List<UserInfo> findAll(){
        List<UserInfo> userInfoList = userInfoService.findAll();
        return userInfoList;
    }
}

