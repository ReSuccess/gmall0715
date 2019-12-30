package com.atguigu.gmall0715.bean;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author sujie
 * @date 2019-12-30-16:57
 */
@Data
public class SkuImage implements Serializable {
    @Id
    @Column
    String id;
    @Column
    String skuId;
    @Column
    String imgName;
    @Column
    String imgUrl;
    @Column
    String spuImgId;
    @Column
    String isDefault;

}
