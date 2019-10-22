package com.jyou.thinker.domain;

import lombok.Data;

import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * TODO: 用户与权限关联
 * @author wgbing
 * @date 2019-08-15 20:09
 */
@Data
@Table(name = "sys_user_permission")
@MappedSuperclass
public class UserPermission implements Serializable {
    private Long userId;
    private Long permissionId;
}
