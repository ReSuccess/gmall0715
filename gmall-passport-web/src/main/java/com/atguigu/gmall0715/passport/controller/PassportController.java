package com.atguigu.gmall0715.passport.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall0715.bean.UserInfo;
import com.atguigu.gmall0715.passport.config.JwtUtil;
import com.atguigu.gmall0715.service.UserInfoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sujie
 * @date 2020-01-07-18:19
 */
@Controller
public class PassportController {
    @Value("${token.key}")
    private String signKey;

    @Reference
    private UserInfoService userInfoService;
    /**
     * 获取登录页面，并携带回调路径
     * @param request
     * @return
     */
    @RequestMapping("index")
    public String index(HttpServletRequest request){
        String originUrl = request.getParameter("originUrl");
        //前端用来登录后跳转
        //http://passport.atguigu.com/index?originUrl=https%3A%2F%2Fwww.jd.com%2F
        //originUrl=https%3A%2F%2Fwww.jd.com%2F 必须这么写，为什么
        request.setAttribute("originUrl",originUrl);
        return "index";
    }

    /**
     * 登录
     * @param request
     * @param userInfo
     * @return
     */
    @RequestMapping("login")
    @ResponseBody
    public String login(HttpServletRequest request, UserInfo userInfo){
        // 取得ip地址,此时必须经过域名，然后通过Nginx才可以获取到ip
        String remoteAddr = request.getHeader("X-forwarded-for");
        if(userInfo != null){
            UserInfo info = userInfoService.login(userInfo);
            //查看是否登录成功
            if(info == null){
                return "fail";
            }else{
              Map<String,Object> map = new HashMap<>(16);
              map.put("userId",  info.getId());
              map.put("nickName", info.getNickName());
              return JwtUtil.encode(signKey, map, remoteAddr);
            }
        }
        return "fail";
    }

    /**
     * 查看用户是否登录
     * 用来解析token中存放的数据
     *  直接将token ，salt 以参数的形式传入到控制器
     *  http://passport.atguigu.com/verify?token=xxxx&salt=xxx
     * @param request
     * @return
     */
    @RequestMapping("verify")
    @ResponseBody
    public String verify(HttpServletRequest request){
        // 从token 中获取userId  --- {解密token}  Map<String, Object> map1 = JwtUtil.decode(token, key, salt);
        String token = request.getParameter("token");
        String salt = request.getParameter("salt");
        Map<String,Object> map = JwtUtil.decode(token,signKey,salt);
        //查看是否登录
        if(map != null){
            String userId = (String)map.get("userId");
            UserInfo userInfo = userInfoService.verify(userId);
            if(userInfo != null){
                return "success";
            }
        }
        return "fail";
    }

}
