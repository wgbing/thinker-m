package com.jyou.thinker.service.sys;

import cn.hutool.core.collection.CollUtil;
import com.github.pagehelper.PageHelper;
import com.jyou.thinker.common.ApiResult;
import com.jyou.thinker.common.datatable.DataTableOutput;
import com.jyou.thinker.common.param.SearchParams;
import com.jyou.thinker.domain.Permission;
import com.jyou.thinker.domain.User;
import com.jyou.thinker.mapper.PermissionMapper;
import com.jyou.thinker.mapper.RolePermissionMapper;
import com.jyou.thinker.mapper.UserPermissionMapper;
import com.jyou.thinker.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@Transactional
public class PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private RolePermissionMapper rolePermissionMapper;
    @Autowired
    private UserPermissionMapper userPermissionMapper;

    public PermissionMapper getPermissionMapper() {
        return permissionMapper;
    }

    public List<Permission> listByParentId(Long parentId) {
        List<Permission>  list = permissionMapper.listByParentId(parentId,null);
        if(list != null && !list.isEmpty()){
            for(Permission perm:list){
                perm.checkParent();
            }
        }
        return list;
    }

    public DataTableOutput list(SearchParams params) {
        PageHelper.startPage(params.getPage(),params.getLimit());
        List<Permission> list = permissionMapper.list(params);

        return new DataTableOutput(list);
    }

    public Permission getById(Long id) {
        Permission perm = new Permission();
        if(id != null){
            perm = permissionMapper.selectByPrimaryKey(id);
        }
        return perm;
    }

    public ApiResult save(Permission permission) {
        int count = 0;
        Date currDate = new Date();
        int countByResKey = 0;
        if(permission.getParentId() != null && permission.getParentId() == 0){
            permission.setParentId(null);
        }

        if(!StringUtils.isEmpty(permission.getResKey())){
            Example example = new Example(Permission.class);
            example.createCriteria()
                    .andEqualTo("resKey",permission.getResKey());
            countByResKey = permissionMapper.selectCountByExample(example);
        }
        if(permission.getId() != null){
            Permission oldPerm = permissionMapper.selectByPrimaryKey(permission.getId());
            if(!oldPerm.getResKey().equals(oldPerm.getResKey())){
                if(permission.getType() != null && permission.getType() == 3){//按钮时用的
                    if(countByResKey >= 1){
                        return ApiResult.failure("授权标识唯一，不能有重复！");
                    }
                }
            }
            permission.setUpdateTime(currDate);
            count = permissionMapper.updateByPrimaryKeySelective(permission);
        }else {
            if(permission.getType() != null && permission.getType() == 3){//按钮时用的
                if(countByResKey >= 1){
                    return ApiResult.failure("授权标识唯一，不能有重复！");
                }
            }
            permission.setVisible(true);
            permission.setCreateTime(currDate);
            count = permissionMapper.insertSelective(permission);
        }
        return CommonUtils.msg(count);
    }

    public ApiResult batchRemove(Long[] ids) {
        boolean children = false;
        for(Long parentId : ids) {
            Example example = new Example(Permission.class);
            example.createCriteria()
                    .andEqualTo("parentId",parentId);
            int count = permissionMapper.selectCountByExample(example);
            if(CommonUtils.isIntThanZero(count)) {
                children = true;
            }
        }
        if(children) {
            return ApiResult.failure("操作失败，当前所选数据有子节点数据！");
        }
        for(Long id : ids){
            // 1.删除权限与角色的关系
            rolePermissionMapper.deleteByPermId(id);
            // 2.删除权限与用户的关系
            userPermissionMapper.deleteByPermId(id);
        }

        int count = permissionMapper.batchRemove(ids);
        return CommonUtils.msg(ids, count);
    }

    public List<Permission> getMenu(User user) {
        List<Permission> list = new ArrayList<>();
        if(user != null){
            if(user.getSuperAdmin() != null && user.getSuperAdmin()){
                list = permissionMapper.listByParentId(null,1);
                if(CollUtil.isNotEmpty(list)){
                    for(Permission perm:list){
                        perm.setChild(permissionMapper.listByParentId(perm.getId(),2));
                    }
                }
            }else {
                list = permissionMapper.listByUserId(null,1,user.getId());
                if(CollUtil.isNotEmpty(list)){
                    for(Permission perm:list){
                        perm.setChild(permissionMapper.listByUserId(perm.getId(),2,user.getId()));
                    }
                }
            }

        }

        return list;
    }


    public Object getTreeNodesHandler(Long roleId) {
        List<Permission> list =  this.permissionMapper.findTreeNodes(roleId);
        for (Permission item : list) {
            item.setOpen(item.getSize() > 0);
            item.setChecked(item.getHasPermCount() > 0);
        }
        return list;
    }
}
