package com.atguigu.gmall0715.bean;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * 平台属性
 * @author sujie
 * @date 2019-12-27-16:35
 */
@Data
public class BaseAttrInfo implements Serializable {
    /**
     * @ GeneratedValue 可以让自增主键回显
     */
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @Column
    private String attrName;
    @Column
    private String catalog3Id;
    /**
     * @ Transient 表示该字段并不是数据库字段
     */
    @Transient
    private List<BaseAttrValue> attrValueList;


}
