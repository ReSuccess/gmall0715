package com.atguigu.gmall0715.manage.mapper;

import com.atguigu.gmall0715.bean.BaseAttrInfo;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author sujie
 * @date 2019-12-27-16:42
 */
public interface BaseAttrInfoMapper extends Mapper<BaseAttrInfo> {
    /**
     * 根据三级分类id获取平台属性详细信息（+平台属性值）
     * @param catalog3Id
     * @return
     */
    List<BaseAttrInfo> selectBaseAttrInfoListByCatalog3Id(String catalog3Id);
}
