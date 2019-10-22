package com.jyou.thinker.service.sys;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.jyou.thinker.common.ApiResult;
import com.jyou.thinker.common.datatable.DataTableOutput;
import com.jyou.thinker.common.param.SearchParams;
import com.jyou.thinker.domain.Role;
import com.jyou.thinker.domain.RolePermission;
import com.jyou.thinker.domain.UserRole;
import com.jyou.thinker.mapper.PermissionMapper;
import com.jyou.thinker.mapper.RoleMapper;
import com.jyou.thinker.mapper.RolePermissionMapper;
import com.jyou.thinker.mapper.UserRoleMapper;
import com.jyou.thinker.utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@Transactional
public class RoleService {

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RolePermissionMapper rolePermissionMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;

    public RoleMapper getRoleMapper() {
        return roleMapper;
    }

    /**
     * 分页查询
     */
    @Transactional(readOnly = true)
    public DataTableOutput findByPaging(SearchParams params) {
        PageHelper.startPage(params.getPage(), params.getLimit());
        List<Role> list = this.roleMapper.findByPaging(params);
        return new DataTableOutput(list);
    }


    /**
     * 删除角色（伪删除）
     * @param ids JSON ID Array
     * @return
     */
    public ApiResult delHandler(List<Long> ids) {
        Role record = new Role();
        record.setDeleted(true);

        Example example = new Example(Role.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", ids);

        this.roleMapper.updateByExampleSelective(record, example);
        return ApiResult.success();
    }

    /**
     * 获取所有角色
     * @return
     */
    @Transactional(readOnly = true)
    public List<Role> findAll() {
        Example example = new Example(Role.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("deleted", 0);
        return this.roleMapper.selectByExample(example);
    }

    /**
     * 获取所有角色，并标记用户所拥有的权限选中状态
     * @param userId
     * @return
     */
//    @Transactional(readOnly = true)
//    public List<Role> findAllWithMarkChecked(Long userId) {
//        List<Role> roleList = findAll();
//        if (userId != null) {
//            // 获取拥有角色列表
//            List<Long> roleIds = userRoleService.findRoleIdListByUserId(userId);
//            // 设置角色选中
//            if (roleList != null && !roleList.isEmpty()) {
//                roleList.forEach(item -> item.setChecked(roleIds.contains(item.getId())) );
//            }
//        }
//        return roleList;
//    }

    /**
     * 判断当前登录用户是否拥有某个角色，拥有=true 没有=false
     */
    public Boolean hasRoleByCurrentUser(String roleName){
        if(SecurityUtil.getUser() != null
                && SecurityUtil.getUser().getId() != null
                && StrUtil.isNotEmpty(roleName)){
            Example example = new Example(Role.class);
            example.createCriteria()
                    .andEqualTo("name",roleName)
                    .andEqualTo("deleted",false);
            List<Role> roles = roleMapper.selectByExample(example);
            if(CollUtil.isNotEmpty(roles)){
                List<Long> roleIds = new ArrayList<>();
                for(Role role:roles){
                    roleIds.add(role.getId());
                }
                Example e = new Example(UserRole.class);
                e.createCriteria()
                        .andEqualTo("userId",SecurityUtil.getUser().getId())
                        .andIn("roleId",roleIds);
                int count = userRoleMapper.selectCountByExample(e);
                if(count >0){
                    return true;
                }
            }

        }
        return false;
    }

    public ApiResult save(Role role) {
        if(role == null || role.getName() == null){
            return ApiResult.failure("参数校验失败，请重试！");
        }

        Date currDate = new Date();
        if (role.getId() == null) {
            role.setDeleted(false);
            role.setCreateTime(currDate);
            this.roleMapper.insertSelective(role);
        } else {
            role.setUpdateTime(currDate);
            this.roleMapper.updateByPrimaryKeySelective(role);
            // 删除角色原有权限
            this.rolePermissionMapper.deleteByRoleId(role.getId());
        }

        // 重新添加角色权限
        List<RolePermission> rpList = new ArrayList<>();
        if (StrUtil.isNotEmpty(role.getPermIds())) {
            String[] idStrArr = role.getPermIds().split(",");
            for (String item : idStrArr) {
                RolePermission rp = new RolePermission();
                rp.setRoleId(role.getId());
                rp.setPermissionId(Long.parseLong(item));
                rpList.add(rp);
            }
            this.rolePermissionMapper.insertList(rpList);
        }

        return ApiResult.success();
    }

    public ApiResult batchRemove(Long[] ids) {
        Role record = new Role();
        record.setDeleted(true);

        Example example = new Example(Role.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", Arrays.asList(ids));

        this.roleMapper.updateByExampleSelective(record, example);
        return ApiResult.success();
    }

    /**
     * 获取所有角色，并标记用户所拥有的权限选中状态
     * @param userId
     * @return
     */
    @Transactional(readOnly = true)
    public List<Role> findAllWithMarkChecked(Long userId) {
        List<Role> roleList = findAll();
        if (userId != null) {
            // 获取拥有角色列表
            List<Long> roleIds = userRoleService.findRoleIdListByUserId(userId);
            // 设置角色选中
            if (roleList != null && !roleList.isEmpty()) {
                roleList.forEach(item -> item.setChecked(roleIds.contains(item.getId())) );
            }
        }
        return roleList;
    }
}
