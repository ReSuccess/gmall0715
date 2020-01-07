package com.atguigu.gmall0715.service;

import com.atguigu.gmall0715.bean.SkuLsInfo;
import com.atguigu.gmall0715.bean.SkuLsParams;
import com.atguigu.gmall0715.bean.SkuLsResult;

/**
 * 首页搜索后的页面
 * @author sujie
 * @date 2020-01-06-20:11
 */
public interface ListService {
    /**
     * 上架sku信息，即上传信息到es中
     * @param skuLsInfo
     */
    void saveSkuLsInfo(SkuLsInfo skuLsInfo);

    /**
     * 根据查询条件查询es获取数据
     * @param skuLsParams
     * @return
     */
    SkuLsResult search(SkuLsParams skuLsParams);
}
