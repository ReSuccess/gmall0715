package com.atguigu.gmall0715.gmalllistweb.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall0715.bean.SkuLsParams;
import com.atguigu.gmall0715.bean.SkuLsResult;
import com.atguigu.gmall0715.service.ListService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


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
    public String getList(SkuLsParams skuLsParams, Model model){
        SkuLsResult skuLsResult = listService.search(skuLsParams);
        //sku列表
        if(skuLsResult != null){
            model.addAttribute("skuLsInfoList",skuLsResult.getSkuLsInfoList());
        }
        return "list";
    }
}
