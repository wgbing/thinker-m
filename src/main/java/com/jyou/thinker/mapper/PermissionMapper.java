package com.jyou.thinker.mapper;

import com.jyou.thinker.common.param.SearchParams;
import com.jyou.thinker.domain.Permission;
import com.jyou.thinker.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PermissionMapper extends MyMapper<Permission> {
    List<Permission> list(SearchParams params);
    List<String> findResKeysByUserId(Long userId);
    List<Permission> listByParentId(@Param("parentId") Long parentId,@Param("type") Integer type);
    List<Permission> listByUserId(@Param("parentId") Long parentId, @Param("type") Integer type, @Param("userId") Long userId);
    List<Permission> findTreeNodes(Long roleId);
}