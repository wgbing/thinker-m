package com.jyou.thinker.domain;

import cn.hutool.core.util.NumberUtil;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * 数据字典
 */
@Data
@Entity
@Table(name = "sys_dictionary")
public class Dictionary extends BaseEntity implements Serializable {

    /* 父级字典项Id */
    @ManyToOne(targetEntity = Dictionary.class)
    @JoinColumn(name = "parent_id")
    private Long parentId;
    /* 配置Key值 */
    @Column(name = "config_key",nullable = false,unique = true)
    private String configKey;
    /* 字典项名称 */
    @Column(name = "name", nullable = false)
    private String name;
    /* 国标编码 */
    @Column(name = "code")
    private String code;
    /* 字典项值 */
    @Column(name = "value")
    private String value;
    /* 备注 */
    @Column(name = "note")
    private String note;
    /* 是否锁定 */
    @Column(name = "locked", nullable = false)
    private Boolean locked;
    /* 排序字段 */
    @Column(name = "order_no")
    private Integer orderNo;

    /**
     * ztree属性
     */
    @Transient
    private Boolean open;

    @Transient
    private Boolean isParent;

    @Transient
    private Integer size;

    public Integer getSize() {
        if (size == null) {
            return 0;
        }
        return size;
    }

    @Transient
    private List<?> list;

    public void checkParent() {
        if(NumberUtil.compare(this.size,0) == 1) {
            this.isParent = true;
        } else {
            this.isParent = false;
        }
    }
}
