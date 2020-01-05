package com.atguigu.gmall0715.manage.mapper;

import com.atguigu.gmall0715.bean.SkuSaleAttrValue;
import com.atguigu.gmall0715.bean.SpuSaleAttr;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author sujie
 * @date 2019-12-30-18:11
 */
public interface SkuSaleAttrValueMapper extends Mapper<SkuSaleAttrValue> {
    /**
     * 查询指定spu的销售属性值id和skuid
     * @param spuId
     * @return
     */
    List<SkuSaleAttrValue> selectSkuSaleAttrValueListBySpu(String spuId);
}
