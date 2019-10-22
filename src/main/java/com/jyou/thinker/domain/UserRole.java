package com.jyou.thinker.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * TODO: 用户与角色关联
 * @author wgbing
 * @date 2019-08-15 20:09
 */
@Data
@Table(name = "sys_user_role")
@MappedSuperclass
@NoArgsConstructor
public class UserRole implements Serializable {
    private Long userId;
    private Long roleId;

    public UserRole(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }
}
