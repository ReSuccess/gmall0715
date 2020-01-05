package com.atguigu.gmall0715.service;

import com.atguigu.gmall0715.bean.*;

import java.util.List;

/**
 * @author sujie
 * @date 2019-12-27-16:46
 */
public interface ManageService {
    /**
     * 查询所有一级分类的信息
     * @return
     */
    List<BaseCatalog1> getCatalog1();

    /**
     * 根据一级分类id 查询二级分类信息
     * @param baseCatalog2
     * @return
     */
    List<BaseCatalog2> getCatalog2(BaseCatalog2 baseCatalog2);

    /**
     *根据二级分类id 查询三级分类信息
     * @param baseCatalog3
     * @return
     */
    List<BaseCatalog3> getCatalog3(BaseCatalog3 baseCatalog3);

    /**
     * 根据三级分类id 获取平台属性 信息
     * @param baseAttrInfo
     * @return
     */
    List<BaseAttrInfo> attrInfoList(BaseAttrInfo baseAttrInfo);

    /**
     * 添加平台属性，和平台属性值
     * @param baseAttrInfo
     */
    void saveAttrInfo(BaseAttrInfo baseAttrInfo);

    /**
     * 根据平台属性id 查询平台属性值
     * @param attrId
     */
    List<BaseAttrValue> getAttrValueList(String attrId);

    /**
     * 根据平台属性id 查询平台属性信息
     * @param attrId
     * @return
     */
    BaseAttrInfo getBaseAttrInfo(String attrId);

    /**
     * 根据三级分类id 查询SPU信息
     * @param spuInfo
     * @return
     */
    List<SpuInfo> spuList(SpuInfo spuInfo);

    /**
     * 查询到所有销售属性
     * @return
     */
    List<BaseSaleAttr> baseSaleAttrList();

    /**
     * 保存spu信息
     * @param spuInfo
     */
    void saveSpuInfo(SpuInfo spuInfo);

    /**
     * 查询指定spu的图片信息
     * @param spuImage
     * @return
     */
    List<SpuImage> spuImageList(SpuImage spuImage);

    /**
     * 根据三级分类id查询平台属性和平台属性值
     * @param catalog3Id
     * @return
     */
    List<BaseAttrInfo> attrInfoList(String catalog3Id);

    /**
     * 查询所有销售属性+销售属性值
     * @param spuId
     * @return
     */
    List<SpuSaleAttr> spuSaleAttrList(String spuId);

    /**
     * 保存sku信息
     * @param skuInfo
     */
    void saveSkuInfo(SkuInfo skuInfo);

    /**
     * 查询sku详情页面的基本信息，图片，价格，名称等
     * @param skuId
     * @return
     */
    SkuInfo getSkuInfoPage(String skuId);

    /**
     * 查询sku 销售属性值和销售属性 相关信息
     * @param skuInfo
     * @return
     */
    List<SpuSaleAttr> getSpuSaleAttrListCheckBySku(SkuInfo skuInfo);

    /**
     * 查询所有的sku销售属性值id和skuid
     * @param spuId
     * @return
     */
    List<SkuSaleAttrValue> getSkuSaleAttrValueListBySpu(String spuId);
}
