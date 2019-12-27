package com.atguigu.gmall0715.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall0715.bean.*;
import com.atguigu.gmall0715.service.ManageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author sujie
 * @date 2019-12-27-16:51
 */
@RestController
@CrossOrigin
public class ManageController {
    @Reference
    private ManageService manageService;

    /**
     * 获取一级分类
     * http://localhost:8082/getCatalog1
     * @return
     */
    @RequestMapping("getCatalog1")
    public List<BaseCatalog1> getCatalog1(){
        return manageService.getCatalog1();
    }

    /**
     * 根据一级分类id 获取二级分类id
     * http://localhost:8082/getCatalog2?catalog1Id=2
     * @return
     */
    @RequestMapping("getCatalog2")
    public List<BaseCatalog2> getCatalog2(BaseCatalog2 baseCatalog2){
        return manageService.getCatalog2(baseCatalog2);
    }

    /**
     * 根据二级分类id获取 三级分类信息
     * http://localhost:8082/getCatalog3?catalog2Id=13
     * @param baseCatalog3
     * @return
     */
    @RequestMapping("getCatalog3")
    public List<BaseCatalog3> getCatalog3(BaseCatalog3 baseCatalog3){
        return manageService.getCatalog3(baseCatalog3);
    }

    /**
     * 根据三级分类id 获取平台属性信息
     * http://localhost:8082/attrInfoList?catalog3Id=86
     * @return
     */
    @RequestMapping("attrInfoList")
    public List<BaseAttrInfo> attrInfoList(BaseAttrInfo baseAttrInfo){
        return manageService.attrInfoList(baseAttrInfo);

    }

    /**
     * 添加平台属性，和平台属性值
     * http://localhost:8082/saveAttrInfo
     * @param baseAttrInfo
     */
    @RequestMapping("saveAttrInfo")
    public void saveAttrInfo(@RequestBody BaseAttrInfo baseAttrInfo){
        manageService.saveAttrInfo(baseAttrInfo);
    }

    /**
     * 根据平台属性id查询平台属性值
     * http://localhost:8082/getAttrValueList?attrId=100
     * @return
     */
    @RequestMapping("getAttrValueList")
    public List<BaseAttrValue> getAttrValueList(String attrId){
//        完成功能
//        return manageService.getAttrValueList(attrId);

        //业务逻辑
        BaseAttrInfo baseAttrInfo =manageService.getBaseAttrInfo(attrId);
        return baseAttrInfo.getAttrValueList();
    }

}
