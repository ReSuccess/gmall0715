package com.atguigu.gmall0715.manage.mapper;

import com.atguigu.gmall0715.bean.SpuSaleAttr;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author sujie
 * @date 2019-12-29-23:05
 */
public interface SpuSaleAttrMapper extends Mapper<SpuSaleAttr> {
    /**
     * 根据spuId查询所有
     * @param spuId
     * @return
     */
    List<SpuSaleAttr> selectSpuSaleAttrListBySpuId(String spuId);
}
