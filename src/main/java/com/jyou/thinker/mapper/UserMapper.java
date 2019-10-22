package com.jyou.thinker.mapper;

import com.jyou.thinker.common.param.SearchParams;
import com.jyou.thinker.domain.User;
import com.jyou.thinker.utils.MyMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserMapper extends MyMapper<User> {
    List<User> findByPaging(SearchParams params);

    @Select("SELECT * FROM dp_user t WHERE t.super_admin = 1")
    List<User> findBySuper();
    @Select("select * from sys_user a where a.enable = 1 and a.deleted = 0 and a.user_name = #{userName}")
    User findOneByUserName(String userName);

    @Select("SELECT COUNT(*) FROM sys_user u WHERE u.org_id = #{orgId}")
    int countUserByOrgId(Long orgId);


}