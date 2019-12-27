package com.atguigu.gmall0715.bean;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * 一级分类
 * @author sujie
 * @date 2019-12-27-16:33
 */
@Data
public class BaseCatalog1 implements Serializable {
    @Id
    @Column
    private String id;
    @Column
    private String name;

}
