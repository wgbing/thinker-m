package com.jyou.thinker.domain;

import lombok.Data;

import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * TODO: 角色与部门关联
 * @author wgbing
 * @date 2019-08-15 20:09
 */
@Data
@Table(name = "sys_role_org")
@MappedSuperclass
public class RoleOrg implements Serializable {
    private Long roleId;
    private Long orgId;
}
