package com.atguigu.gmall0715.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall0715.bean.BaseSaleAttr;
import com.atguigu.gmall0715.bean.SpuInfo;
import com.atguigu.gmall0715.service.ManageService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * SPU相关业务处理器
 *  — spu 产品添加
 *  — spu 产品属性+产品属性值
 *  — spu 产品介绍图片+海报
 * @author sujie
 * @date 2019-12-29-20:47
 */
@RestController
@CrossOrigin
public class SpuManageController {
    @Reference
    private ManageService manageService;

    /**
     * 显示所有spu信息
     * http://192.168.116.1:8082/spuList?catalog3Id=63
     * @param spuInfo
     * @return
     */
    @RequestMapping("spuList")
    public List<SpuInfo> spuList(SpuInfo spuInfo){
       return manageService.spuList(spuInfo);
    }

    /**
     * http://192.168.116.1:8082/baseSaleAttrList
     * 查询销售属性
     * @return
     */
    @RequestMapping("baseSaleAttrList")
    public List<BaseSaleAttr> baseSaleAttrList(){
        return manageService.baseSaleAttrList();
    }

    /**
     * 保存spu
     * http://192.168.116.1:8082/saveSpuInfo
     * @param spuInfo
     */
    @RequestMapping("saveSpuInfo")
    public void saveSpuInfo(@RequestBody SpuInfo spuInfo){
        manageService.saveSpuInfo(spuInfo);
    }
}
