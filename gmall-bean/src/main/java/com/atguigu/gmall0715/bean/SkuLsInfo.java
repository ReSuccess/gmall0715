package com.atguigu.gmall0715.bean;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * sku 存到 es中所需要的属性实体类
 * @author sujie
 * @date 2020-01-06-19:59
 */
@Data
public class SkuLsInfo implements Serializable {
    String id;

    BigDecimal price;

    String skuName;

    String catalog3Id;

    String skuDefaultImg;
    /**
     * 热点数据
     */
    Long hotScore=0L;

    List<SkuLsAttrValue> skuAttrValueList;

}
