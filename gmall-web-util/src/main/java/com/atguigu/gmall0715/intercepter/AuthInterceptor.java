package com.atguigu.gmall0715.intercepter;

import com.alibaba.fastjson.JSON;
import com.atguigu.gmall0715.annotation.LoginRequire;
import com.atguigu.gmall0715.controller.CookieUtil;
import com.atguigu.gmall0715.controller.WebConst;
import com.atguigu.gmall0715.util.HttpClientUtil;
import io.jsonwebtoken.impl.Base64UrlCodec;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @author sujie
 * @date 2020-01-07-21:10
 */
@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 登录成功的时候，https://www.jd.com/?newToken=eyJhbGciOiJIUzI1NiJ9.eyJuaWNrTmFtZSI6IkF0Z3VpZ3UiLCJ1c2VySWQiOiIxIn0.3Z6Lwc4nZ3FDIOx-SEkd_hiImKqCrB-reiYFB6X8RBo
        String token = request.getParameter("newToken");
        //判断token是否存在
        //点击登录，cookie中没有token
        if(token != null){
            // 将token 放入cookie 中！
            CookieUtil.setCookie(request, response, "token", token, WebConst.COOKIE_MAXAGE,false);
        }
        //是否已经登录，cookie中有token
        if(token == null){
            token = CookieUtil.getCookieValue(request, "token", false);
        }
        //判断获取的token是否存在
        if(token != null){
            //解析token中的数据
            Map map = getUserMapByToken(token);
            //获取token中的数据用于回显登录名信息
            String nickName = (String)map.get("nickName");
            //保存到作用域
            request.setAttribute("nickName",nickName);
        }
        //手动设置的注解
        //使得该注解注释的controller方法判断是否需要验证是否登录
        // 获取用户访问的控制器上是否有  注解 @LoginRequire
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        //获取方法上的注解
        LoginRequire methodAnnotation = handlerMethod.getMethodAnnotation(LoginRequire.class);
        if(methodAnnotation != null){
            // 直接认证！用户是否登录！ http://passport.atguigu.com/verify?token=xxxx&salt=xxx
            String salt = request.getHeader("X-forwarded-for");
            //远程调用
            // http://passport.atguigu.com/verify?token=eyJhbGciOiJIUzI1NiJ9.eyJuaWNrTmFtZSI6IkF0Z3VpZ3UiLCJ1c2VySWQiOiIxIn0.3Z6Lwc4nZ3FDIOx-SEkd_hiImKqCrB-reiYFB6X8RBo&salt=192.168.67.1
            String result = HttpClientUtil.doGet(WebConst.VERIFY_ADDRESS + "?token=" + token + "&salt=" + salt);
            if("success".equals(result)){
                // 用户已经登录状态！
                Map map = getUserMapByToken(token);
                String userId = (String)map.get("userId");

                //保存到域中
                request.setAttribute("userId", userId);
                //放行
                return true;
            }else{
                //未登录
                // 当LoginRequire的注解中的属性autoRedirect =true 时必须登录！
                if(methodAnnotation.autoRedirect()){
                    // 应该跳转到登录页面！http://item.gmall.com/37.html -----> http://passport.atguigu.com/index?originUrl=http%3A%2F%2Fitem.gmall.com%2F37.html
                    // 得到用户访问的url 路径
                    String requestURL = request.getRequestURL().toString();
                    System.out.println("<<<<<" + requestURL + ">>>>>");
                    // 将 http://item.gmall.com/37.html 转换 http%3A%2F%2Fitem.gmall.com%2F37.html
                    String encodeURL = URLEncoder.encode(requestURL, "UTF-8");
                    System.out.println("<<<<<" + encodeURL +">>>>>");

                    // 重定向
                    response.sendRedirect(WebConst.LOGIN_ADDRESS + "?originUrl=" + encodeURL);
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 用于解析token中的用户数据
     * @param token
     * @return
     */
    private Map getUserMapByToken(String token) {
        // token = eyJhbGciOiJIUzI1NiJ9.eyJuaWNrTmFtZSI6IkF0Z3VpZ3UiLCJ1c2VySWQiOiIxIn0.3Z6Lwc4nZ3FDIOx-SEkd_hiImKqCrB-reiYFB6X8RBo
        // 解密 得到token 的中间部分
        String tokenUserInfo = StringUtils.substringBetween(token, ".");
        System.out.println("++++++ "+tokenUserInfo+" ++++++");
        //解码
        Base64UrlCodec base64UrlCodec = new Base64UrlCodec();
        byte[] decode = base64UrlCodec.decode(tokenUserInfo);
        //byte -> string -> map
        String tokenJson = new String(decode);
        return JSON.parseObject(tokenJson,Map.class);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }
}
