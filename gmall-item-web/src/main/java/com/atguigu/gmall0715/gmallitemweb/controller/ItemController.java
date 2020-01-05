package com.atguigu.gmall0715.gmallitemweb.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall0715.bean.SkuInfo;
import com.atguigu.gmall0715.bean.SkuSaleAttrValue;
import com.atguigu.gmall0715.bean.SpuSaleAttr;
import com.atguigu.gmall0715.service.ManageService;
import org.apache.catalina.manager.ManagerServlet;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sujie
 * @date 2020-01-03-19:34
 */
@Controller
public class ItemController {
    @Reference
    private ManageService manageService;

    /**
     * 详情页数据渲染
     * @param skuId
     * @param model
     * @return
     */
    @RequestMapping("{skuId}.html")
    public String skuInfoPage(@PathVariable("skuId") String skuId, Model model){
       //详情页基本数据
        SkuInfo skuInfo = manageService.getSkuInfoPage(skuId);
        model.addAttribute("skuInfo",skuInfo);
        //存储spu和sku数据
        List<SpuSaleAttr> spuSaleAttrList = manageService.getSpuSaleAttrListCheckBySku(skuInfo);
        model.addAttribute("saleAttrList",spuSaleAttrList);
        //存储spu指定的所有销售属性可能组合的json集合 “属性值1|属性值2|属性值3：skuId”
        List<SkuSaleAttrValue> skuSaleAttrValueList = manageService.getSkuSaleAttrValueListBySpu(skuInfo.getSpuId());
        //将信息存入到map中
        Map<String,String> valuesSkuMap = new HashMap<>(16);
        //用于存储skuSaleAttrValueId拼接出的map中的key值
        StringBuilder valueIdsKey=new StringBuilder();
        for(int i = 0;i<skuSaleAttrValueList.size();i++){
            SkuSaleAttrValue skuSaleAttrValue = skuSaleAttrValueList.get(i);
            if(valueIdsKey.length()!=0){
                valueIdsKey.append("|");
            }
            valueIdsKey.append(skuSaleAttrValue.getSaleAttrValueId());
            //判断该属性是否是和下一条属性是同一个sku，如果不是则将现在的key和skuid存入到map中
            if((i+1) == skuSaleAttrValueList.size()||!skuSaleAttrValue.getSkuId().equals(skuSaleAttrValueList.get(i+1).getSkuId())){
                valuesSkuMap.put(valueIdsKey.toString(),skuSaleAttrValue.getSkuId());
                //将存储key拼串的值为空
                valueIdsKey.delete(0, valueIdsKey.length());
            }
        }
        //把map变成json串
        String valuesSkuJson  = JSON.toJSONString(valuesSkuMap);
        model.addAttribute("valuesSkuJson",valuesSkuJson);
        return "item";
    }
}
