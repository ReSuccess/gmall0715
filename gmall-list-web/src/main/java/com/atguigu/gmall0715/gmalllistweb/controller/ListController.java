package com.atguigu.gmall0715.gmalllistweb.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall0715.bean.SkuLsParams;
import com.atguigu.gmall0715.bean.SkuLsResult;
import com.atguigu.gmall0715.service.ListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author sujie
 * @date 2020-01-06-21:08
 */
@Controller
public class ListController {
    @Reference
    private ListService listService;
    /**
     * http://list.gmall.com/list.html?catalog3Id=61
     * 获取过滤列表
     * @param skuLsParams
     * @return
     */
    @RequestMapping("list.html")
    @ResponseBody
    public String getList(SkuLsParams skuLsParams){
        SkuLsResult skuLsResult = listService.search(skuLsParams);
        return JSON.toJSONString(skuLsResult);
    }
}
