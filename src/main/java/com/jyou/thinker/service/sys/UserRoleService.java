package com.jyou.thinker.service.sys;

import com.jyou.thinker.domain.UserRole;
import com.jyou.thinker.mapper.UserRoleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: 用户角色关联
 * @author wgbing
 * @date 2019-09-30 16:45
 */
@Slf4j
@Service
@Transactional
public class UserRoleService {
    @Autowired
    private UserRoleMapper userRoleMapper;

    public UserRoleMapper getUserRoleMapper() {
        return userRoleMapper;
    }

    public int deleteByUserId(Long userId) {
        Example example = new Example(UserRole.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        return this.userRoleMapper.deleteByExample(example);
    }

    public List<UserRole> findByUserId(Long userId) {
        Example example = new Example(UserRole.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        return this.userRoleMapper.selectByExample(example);
    }

    public List<Long> findRoleIdListByUserId(Long userId) {
        List<UserRole> userRoleList = findByUserId(userId);
        List<Long> roleIdList = new ArrayList<>();
        if (userRoleList != null && !userRoleList.isEmpty()) {
            userRoleList.forEach(item -> roleIdList.add(item.getRoleId()));
        }
        return roleIdList;
    }

}
