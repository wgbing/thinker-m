package com.jyou.thinker.domain;

import cn.hutool.core.util.NumberUtil;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * TODO: 组织机构
 * @author wgbing
 * @date 2019-08-15 19:53
 */
@Data
@Entity
@Table(name = "sys_org")
public class Org extends BaseEntity {
    /* 父级组织 */
    @ManyToOne(targetEntity = Org.class)
    @JoinColumn(name = "parent_id")
    private Long parentId;
    /* 组织名称 */
    @Column(name = "name")
    private String name;
    /* 组织简称 */
    @Column(name = "short_name")
    private String shortName;
    /* 组织类型*/
    @Column(name = "type")
    private Integer type;
    /* 备注 */
    @Column(name = "remark")
    private String remark;
    /* 排序序号 */
    @Column(name = "sort_no")
    private Integer sortNo;
    /* 是否启用 true=正常；false=停用 */
    @Column(name = "enable")
    private Boolean enable;
    /** 是否删除 */
    @Column
    private Boolean deleted;
    /**
     * ztree属性
     */
    @Transient
    private Boolean open;

    @Transient
    private Boolean isParent;

    @Transient
    private Integer size;

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