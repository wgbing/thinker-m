package com.jyou.thinker.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * TODO: 角色
 * @author wgbing
 * @date 2019-08-15 20:06
 */
@Data
@Entity
@Table(name = "sys_role")
public class Role extends BaseEntity implements Serializable {

    /** 角色名称 */
    @Column(name = "name", nullable = false)
    private String name;

    /** 角色描述 */
    @Column(name = "description")
    private String description;

    // 数据权限类型 全部 、 本级 、 自定义
    @Column(name = "data_scope")
    private String dataScope = "本级";

    /** 是否删除：true=已删除；false=未删除 */
    @Column
    private Boolean deleted;

    /** 角色对应的权限列表 */
    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "sys_role_permission", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private Set<Permission> permissions = new HashSet<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "sys_role_org", joinColumns = {@JoinColumn(name = "role_id",referencedColumnName = "id")}, inverseJoinColumns = {@JoinColumn(name = "org_id",referencedColumnName = "id")})
    private Set<Org> orgs;

    /* 以下属性为非数据库映射字段 */

    // 用于回显用户已拥有角色
    @Transient
    private Boolean checked;
    @Transient
    private String permIds;
}