package com.jyou.thinker.mapper;

import com.jyou.thinker.common.param.SearchParams;
import com.jyou.thinker.domain.Role;
import com.jyou.thinker.utils.MyMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface RoleMapper extends MyMapper<Role> {

    List<Role> findByPaging(SearchParams params);

    List<Role> listByUserId(Long userId);
}
