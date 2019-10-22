package com.jyou.thinker.mapper;

import com.jyou.thinker.domain.RolePermission;
import com.jyou.thinker.utils.MyMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface RolePermissionMapper extends MyMapper<RolePermission> {

    @Delete("delete from sys_role_permission where role_id = ${roleId}")
    void deleteByRoleId(@Param("roleId") Long roleId);
    @Delete("delete from sys_role_permission where permission_id = ${permId}")
    int deleteByPermId(@Param("permId") Long permId);

}
