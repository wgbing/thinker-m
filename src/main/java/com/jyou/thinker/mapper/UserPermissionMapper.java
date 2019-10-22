package com.jyou.thinker.mapper;

import com.jyou.thinker.domain.UserPermission;
import com.jyou.thinker.utils.MyMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface UserPermissionMapper extends MyMapper<UserPermission> {

    @Delete("delete from sys_user_permission where permission_id = ${permId}")
    int deleteByPermId(@Param("permId") Long permId);

}
