package com.atguigu.gmall0715.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * 查询es的条件
 * @author sujie
 * @date 2020-01-06-20:58
 */
@Data
public class SkuLsParams implements Serializable {
    String  keyword;

    String catalog3Id;

    String[] valueId;

    int pageNo=1;

    int pageSize=20;

}
