package com.jyou.thinker.mapper;

import com.jyou.thinker.common.param.SearchParams;
import com.jyou.thinker.domain.Org;
import com.jyou.thinker.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface OrgMapper extends MyMapper<Org> {
    List<Org> listOrg(SearchParams params);

    @Select("SELECT count(*) FROM sys_org o WHERE o.parent_id = #{parentId}")
    int countOrgByParentId(@Param("parentId") Long parentId);

    List<Org> listOrgByParentId(@Param("parentId") Long parentId);

}