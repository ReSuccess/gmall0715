package com.atguigu.gmall0715.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall0715.bean.SkuImage;
import com.atguigu.gmall0715.bean.SkuInfo;
import com.atguigu.gmall0715.bean.SpuImage;
import com.atguigu.gmall0715.bean.SpuSaleAttr;
import com.atguigu.gmall0715.service.ManageService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author sujie
 * @date 2019-12-30-18:17
 */
@RestController
@CrossOrigin
public class SkuManageController {
    @Reference
    private ManageService manageService;

    /**
     * 查询指定spu的图片信息
     * http://192.168.116.1:8082/spuImageList?spuId=61
     * @param spuImage
     * @return
     */
    @RequestMapping("spuImageList")
    public List<SpuImage> spuImageList(SpuImage spuImage){
       return manageService.spuImageList(spuImage);
    }

    /**
     * 查询所有销售属性
     * http://192.168.116.1:8082/spuSaleAttrList?spuId=61
     * @param spuId
     * @return
     */
    @RequestMapping("spuSaleAttrList")
    public List<SpuSaleAttr> spuSaleAttrList(String spuId){
        return manageService.spuSaleAttrList(spuId);
    }

    /**
     * 保存sku信息
     * http://192.168.116.1:8082/saveSkuInfo
     * @param skuInfo
     */
    @RequestMapping("saveSkuInfo")
    public void saveSkuInfo(@RequestBody SkuInfo skuInfo){
        manageService.saveSkuInfo(skuInfo);

    }
}
