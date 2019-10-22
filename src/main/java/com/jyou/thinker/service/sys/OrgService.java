package com.jyou.thinker.service.sys;

import com.jyou.thinker.common.ApiResult;
import com.jyou.thinker.common.datatable.DataTableOutput;
import com.jyou.thinker.common.param.SearchParams;
import com.jyou.thinker.domain.Org;
import com.jyou.thinker.mapper.OrgMapper;
import com.jyou.thinker.mapper.UserMapper;
import com.jyou.thinker.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@Transactional
public class OrgService {

    @Autowired
    private OrgMapper orgMapper;
    @Autowired
    private UserMapper userMapper;

    public DataTableOutput list(SearchParams params) {
        List<Org> list = orgMapper.listOrg(params);
        return new DataTableOutput((long) list.size(),list);
    }

    public Org getOrgById(Long id) {
        Org org = new Org();
        if(id != null){
            org = orgMapper.selectByPrimaryKey(id);
        }
        return org;
    }

    public ApiResult save(Org org) {
        int count = 0;
        Date currDate = new Date();
        if(org.getParentId() != null && org.getParentId() == 0){
            org.setParentId(null);
        }
        if(org.getEnable() == null){
            org.setEnable(false);
        }
        if(org.getId() != null){
            org.setUpdateTime(currDate);
            count = orgMapper.updateByPrimaryKeySelective(org);
        }else {
            org.setCreateTime(currDate);
            count = orgMapper.insertSelective(org);
        }
        return CommonUtils.msg(count);
    }

    public ApiResult batchRemoveOrg(Long[] ids) {
        boolean children = false;
        boolean hasUser = false;
        for(Long parentId : ids) {
            int count = orgMapper.countOrgByParentId(parentId);
            if(CommonUtils.isIntThanZero(count)) {
                children = true;
            }
            int userCount = userMapper.countUserByOrgId(parentId);
            if(CommonUtils.isIntThanZero(userCount)) {
                hasUser = true;
            }
        }
        if(children) {
            return ApiResult.failure("操作失败，当前所选数据有子节点数据！");
        }
        if(hasUser) {
            return ApiResult.failure("操作失败，当前所选组织中有用户数据！");
        }
        int count = orgMapper.batchRemove(ids);
        return CommonUtils.msg(ids, count);
    }

    public List<Org> listOrgByParentId(Long parentId) {
        List<Org>  list = orgMapper.listOrgByParentId(parentId);
        if(list != null && !list.isEmpty()){
            for(Org org:list){
                org.checkParent();
            }
        }
        return list;
    }

}
