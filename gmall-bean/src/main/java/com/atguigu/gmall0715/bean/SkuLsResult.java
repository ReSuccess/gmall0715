package com.atguigu.gmall0715.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 接收过滤后es的返回值
 * @author sujie
 * @date 2020-01-06-20:59
 */
@Data
public class SkuLsResult implements Serializable {
    List<SkuLsInfo> skuLsInfoList;

    long total;

    long totalPages;

    List<String> attrValueIdList;

}
