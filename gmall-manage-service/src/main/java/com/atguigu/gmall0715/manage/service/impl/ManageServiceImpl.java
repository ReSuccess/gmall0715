package com.atguigu.gmall0715.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall0715.bean.*;
import com.atguigu.gmall0715.manage.mapper.*;
import com.atguigu.gmall0715.service.ManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author sujie
 * @date 2019-12-27-16:47
 */
@Service
public class ManageServiceImpl implements ManageService {
    @Autowired
    private BaseCatalog1Mapper baseCatalog1Mapper;
    @Autowired
    private BaseCatalog2Mapper baseCatalog2Mapper;
    @Autowired
    private BaseCatalog3Mapper baseCatalog3Mapper;
    @Autowired
    private BaseAttrInfoMapper baseAttrInfoMapper;
    @Autowired
    private BaseAttrValueMapper baseAttrValueMapper;
    @Autowired
    private SpuInfoMapper spuInfoMapper;
    @Autowired
    private BaseSaleAttrMapper baseSaleAttrMapper;
    @Autowired
    private SpuSaleAttrMapper spuSaleAttrMapper;
    @Autowired
    private SpuSaleAttrValueMapper spuSaleAttrValueMapper;
    @Autowired
    private SpuImageMapper spuImageMapper;
    @Autowired
    private SkuInfoMapper skuInfoMapper;
    @Autowired
    private SkuAttrValueMapper skuAttrValueMapper;
    @Autowired
    private SkuSaleAttrValueMapper skuSaleAttrValueMapper;
    @Autowired
    private SkuImageMapper skuImageMapper;

    @Override
    public List<BaseCatalog1> getCatalog1() {
        return baseCatalog1Mapper.selectAll();
    }

    @Override
    public List<BaseCatalog2> getCatalog2(BaseCatalog2 baseCatalog2) {
        return baseCatalog2Mapper.select(baseCatalog2);
    }

    @Override
    public List<BaseCatalog3> getCatalog3(BaseCatalog3 baseCatalog3) {
        return baseCatalog3Mapper.select(baseCatalog3);
    }

    @Override
    public List<BaseAttrInfo> attrInfoList(BaseAttrInfo baseAttrInfo) {
        return baseAttrInfoMapper.select(baseAttrInfo);
    }

    @Override
    @Transactional(rollbackFor = {})
    public void saveAttrInfo(BaseAttrInfo baseAttrInfo) {
        if(baseAttrInfo.getId()==null){
            //保存平台属性
            baseAttrInfoMapper.insert(baseAttrInfo);
        }else{
            //更新平台属性
            baseAttrInfoMapper.updateByPrimaryKey(baseAttrInfo);
        }
        //删除平台属性值，在重新添加
        BaseAttrValue baseAttrvalue = new BaseAttrValue();
        baseAttrvalue.setAttrId(baseAttrInfo.getId());
        baseAttrValueMapper.delete(baseAttrvalue);

        // int i = 1/0;

        //保存平台属性值
        List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();
        if(attrValueList!=null && attrValueList.size()>0){
            for (BaseAttrValue baseAttrValue : attrValueList) {
                //插入对应的平台属性id
                baseAttrValue.setAttrId(baseAttrInfo.getId());
                //保存
                baseAttrValueMapper.insert(baseAttrValue);
            }
        }
    }

    @Override
    public List<BaseAttrValue> getAttrValueList(String attrId) {
        BaseAttrValue baseAttrValue = new BaseAttrValue();
        baseAttrValue.setAttrId(attrId);
        return baseAttrValueMapper.select(baseAttrValue);
    }

    @Override
    public BaseAttrInfo     getBaseAttrInfo(String  attrId) {
        BaseAttrInfo baseAttrInfo = baseAttrInfoMapper.selectByPrimaryKey(attrId);
        if(baseAttrInfo!=null){
            baseAttrInfo.setAttrValueList(getAttrValueList(attrId));
        }
        return baseAttrInfo;
    }

    @Override
    public List<SpuInfo> spuList(SpuInfo spuInfo) {
        return spuInfoMapper.select(spuInfo);
    }

    @Override
    public List<BaseSaleAttr> baseSaleAttrList() {
        return baseSaleAttrMapper.selectAll() ;
    }

    @Override
    @Transactional(rollbackFor = {})
    public void saveSpuInfo(SpuInfo spuInfo) {
        //更新或者保存产品信息
        if(spuInfo.getId()==null ||spuInfo.getId().length()==0){
            spuInfo.setId(null);
            spuInfoMapper.insert(spuInfo);
        }else {
            spuInfoMapper.updateByPrimaryKeySelective(spuInfo);
        }
        //保存或者更新图片信息，采用先删除在保存措施，不进行更新操作
        //删除图片信息
        SpuImage spuImage = new SpuImage();
        spuImage.setSpuId(spuInfo.getId());
        spuImageMapper.delete(spuImage);
        //保存图片信息
        List<SpuImage> spuImageList = spuInfo.getSpuImageList();
        if(spuImageList!=null && spuImageList.size()>0){
            for (SpuImage image : spuImageList) {
                image.setId(null);
                image.setSpuId(spuInfo.getId());
                spuImageMapper.insert(image);
            }
        }
        //保存或者更新商品销售属性和商品销售属性值，采用先删除在保存措施，不进行更新操作
        //删除商品销售属性
        SpuSaleAttr spuSaleAttr = new SpuSaleAttr();
        spuSaleAttr.setSpuId(spuInfo.getId());
        spuSaleAttrMapper.delete(spuSaleAttr);
        //删除商品销售属性值
        SpuSaleAttrValue spuSaleAttrValue = new SpuSaleAttrValue();
        spuSaleAttrValue.setSpuId(spuInfo.getId());
        spuSaleAttrValueMapper.delete(spuSaleAttrValue);

        //保存商品销售属性
        List<SpuSaleAttr> spuSaleAttrList = spuInfo.getSpuSaleAttrList();
        if(spuSaleAttrList != null && spuSaleAttrList.size()>0){
            for (SpuSaleAttr saleAttr : spuSaleAttrList) {
                saleAttr.setId(null);
                saleAttr.setSpuId(spuInfo.getId());
                spuSaleAttrMapper.insert(saleAttr);
                //保存商品销售属性值
                List<SpuSaleAttrValue> spuSaleAttrValueList = saleAttr.getSpuSaleAttrValueList();
                if(spuSaleAttrValueList != null && spuSaleAttrValueList.size()>0){
                    for (SpuSaleAttrValue saleAttrValue : spuSaleAttrValueList) {
                        saleAttrValue.setId(null);
                        saleAttrValue.setSpuId(spuInfo.getId());
                        spuSaleAttrValueMapper.insert(saleAttrValue);
                    }
                }
            }
        }
    }

    @Override
    public List<SpuImage> spuImageList(SpuImage spuImage) {
        return spuImageMapper.select(spuImage);
    }

    @Override
    public List<BaseAttrInfo> attrInfoList(String catalog3Id) {
        return baseAttrInfoMapper.selectBaseAttrInfoListByCatalog3Id(catalog3Id);
    }

    @Override
    public List<SpuSaleAttr> spuSaleAttrList(String spuId) {
        return spuSaleAttrMapper.selectSpuSaleAttrListBySpuId(spuId);
    }

    @Override
    @Transactional(rollbackFor = {})
    public void saveSkuInfo(SkuInfo skuInfo) {
        //不考虑更新
        //保存sku基本信息
        skuInfoMapper.insert(skuInfo);
        //保存sku图片信息
        List<SkuImage> skuImageList = skuInfo.getSkuImageList();
        if(skuImageList != null && skuImageList.size()>0){
            for (SkuImage skuImage : skuImageList) {
                skuImage.setId(null);
                skuImage.setSkuId(skuInfo.getId());
                skuImageMapper.insert(skuImage);
            }
        }
        //保存sku平台属性信息
        List<SkuAttrValue> skuAttrValueList = skuInfo.getSkuAttrValueList();
        if(skuAttrValueList != null && skuAttrValueList.size()>0){
            for (SkuAttrValue skuAttrValue : skuAttrValueList) {
                skuAttrValue.setId(null);
                skuAttrValue.setSkuId(skuInfo.getId());
                skuAttrValueMapper.insert(skuAttrValue);
            }
        }
        //保存sku销售属性信息
        List<SkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
        if(skuSaleAttrValueList != null && skuSaleAttrValueList.size()>0){
            for (SkuSaleAttrValue skuSaleAttrValue : skuSaleAttrValueList) {
                skuSaleAttrValue.setId(null);
                skuSaleAttrValue.setSkuId(skuInfo.getId());
                skuSaleAttrValueMapper.insert(skuSaleAttrValue);
            }
        }
    }
}
