package com.jyou.thinker.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * TODO: 用户
 * @author wgbing
 * @date 2019-08-15 19:55
 */
@Data
@Entity
@Table(name = "sys_user")
public class User extends BaseEntity implements Serializable {

    /** 用户名 */
    @Column(nullable = false,unique = true)
    private String userName;

    /** 登录密码 */
    @Column(nullable = false)
    private String password;

    /** 是否超级管理员 */
    @Column
    private Boolean superAdmin;

    /** 昵称 */
    @Column
    private String nickName;

    /** 性别 0=女；1=男 */
    @Column
    private Integer sex;

    /** 手机号 */
    @Column
    private String phone;

    /** 邮箱 */
    @Column
    private String email;

    /** 头像 */
    @Column
    private String avatar;

    /** 备注 */
    @Column
    private String remark;

    /** 上次登录时间 */
    @Column
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastLoginTime;

    /** 上次登录所用IP */
    @Column
    private String lastLoginIp;

    /** 是否启用 */
    @Column
    private Boolean enable;

    /** 是否删除 */
    @Column
    private Boolean deleted;

    /*
     * 以下为多对一关联外键创建
     * 因为不用使用 Hibernate，目前把 @ManyToOne、@JoinColumn 注解在一个非实体属性上，既可以保证列和外键正常创建，也不影响 通用Mapper 对JPA注解的依赖问题
     */
    /** 所属部门ID */
    @OneToOne(targetEntity = Org.class)
    @JoinColumn(name = "org_id")
    private Long orgId;

    /* 以下为多对多关联中间表及外键创建 */

    /** 所属角色 */
    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "sys_user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    /** 拥有权限 */
    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "sys_user_permission", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private Set<Permission> permissions = new HashSet<>();

    @Transient
    private String roleIds;

}
