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
}
