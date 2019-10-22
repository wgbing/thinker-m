package com.jyou.thinker.domain;

import cn.hutool.core.util.NumberUtil;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

/**
 * TODO: 权限
 * @author wgbing
 * @date 2019-08-15 20:03
 */
@Data
@Entity
@Table(name = "sys_permission")
public class Permission extends BaseEntity implements Serializable {

    /** 权限名称 */
    @Column(name = "name", nullable = false)
    private String name;

    /** 父级权限ID */
    @Column(name = "parent_id")
    private Long parentId; //父类Id

    /** 父级权限名称 */
    @Column(name = "parent_name")
    private String parentName;

    /** 权限KEY值：这个权限KEY是唯一的，新增时要注意 */
    @Column(name = "res_key", nullable = false,unique = true)
    private String resKey;

    /** 权限对应的请求资源URL：例如：/videoType/query 不需要项目名和http://xxx:8080 */
    @Column(name = "res_url")
    private String resUrl;

    /** 权限级别 位于权限树种的几级节点，例如根节点就是1级 */
    @Column(name = "level")
    private Integer level;

    /** 权限类型（暂时仅用菜单分类） 1=菜单  2=按扭．．在spring security安全权限中，涉及精确到按扭控制 */
    @Column(name = "type")
    private Integer type;

    /** 权限描述 */
    @Column(name = "description")
    private String description;

    /** 权限可见性（是否对管理员可见， 暂时不用）*/
    @Column(name = "visible", nullable = false)
    private Boolean visible;

    /** 排序 */
    @Column(name = "sort_no")
    private Integer sortNo;

    /** 图标 */
    @Column(name = "icon")
    private String icon;

    /**
     * ztree属性
     */
    @Transient
    private Boolean open;

    @Transient
    private Boolean isParent;

    @Transient
    private Boolean checked;

    @Transient
    private Integer size;

    public Integer getSize() {
        if (size == null) {
            return 0;
        }
        return size;
    }

    @Transient
    private Integer hasPermCount;

    @Transient
    private List<Permission> child;

    public void checkParent() {
        if(NumberUtil.compare(this.size,0) == 1) {
            this.isParent = true;
        } else {
            this.isParent = false;
        }
    }
}