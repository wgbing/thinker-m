package com.jyou.thinker.service.sys;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.jyou.thinker.common.ApiResult;
import com.jyou.thinker.common.datatable.DataTableOutput;
import com.jyou.thinker.common.param.SearchParams;
import com.jyou.thinker.domain.User;
import com.jyou.thinker.domain.UserRole;
import com.jyou.thinker.mapper.UserMapper;
import com.jyou.thinker.utils.EncryptUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * TODO: 用户业务
 * @author wgbing
 * @date 2019-09-30 16:34
 */
@Slf4j
@Service
@Transactional
public class UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleService userRoleService;

    public UserMapper getUserMapper(){
        return userMapper;
    }

    /**
     * 分页查询
     * @param params 分页查询
     * @return
     */
    @Transactional(readOnly = true)
    public DataTableOutput findByPaging(SearchParams params) {
        PageHelper.startPage(params.getPage(), params.getLimit());
        List<User> list = this.userMapper.findByPaging(params);
        return new DataTableOutput(list);
    }

    public List<User> findBySuper(){
        return userMapper.findBySuper();
    }

    /**
     * 按用户名查找用户信息
     * @param userName 用户名
     * @return
     */
    @Transactional(readOnly = true)
    public User findByUserName(String userName) {
        Example example =  new Example(User.class);
        example.createCriteria()
                .andEqualTo("userName", userName)
                .andEqualTo("enable", 1)
                .andEqualTo("deleted", 0);
        return this.userMapper.selectOneByExample(example);
    }

    /**
     * 更新用户登录信息
     * @param userName 用户名
     * @param ip 客户端IP
     */
    public void updateLoginInfo(String userName, String ip) {
        User user = this.findByUserName(userName);
        if (null != user) {
            user.setLastLoginTime(new Date());
            user.setLastLoginIp(ip);
            this.userMapper.updateByPrimaryKeySelective(user);
        }
    }

    /**
     * 删除用户（伪删除）
     * @param ids JSON ID Array
     * @return
     */
    public ApiResult delHandler(List<Long> ids) {
        User record = new User();
        record.setDeleted(true);

        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", ids);
        criteria.andNotEqualTo("superAdmin", 1);  // 排除超级管理员

        this.userMapper.updateByExampleSelective(record, example);
        return ApiResult.success();
    }

    public ApiResult changePwdHandler(Long userId, String oldPwd, String newPwd) {
        User user = this.userMapper.selectByPrimaryKey(userId);
        if (!EncryptUtil.match(oldPwd, user.getPassword())) {
            log.warn("修改密码 => 旧密码不正确");
            return ApiResult.failure("旧密码不正确");
        }

        user.setPassword(EncryptUtil.encode(newPwd));
        user.setUpdateTime(new Date());
        this.userMapper.updateByPrimaryKeySelective(user);

        return ApiResult.success();
    }

    public ApiResult save(User user) {
        if(user == null || StrUtil.isEmpty(user.getUserName()) || StrUtil.isEmpty(user.getPassword())
            || StrUtil.isEmpty(user.getNickName())){
            return ApiResult.failure("参数校验失败，请重试！");
        }
        Date currDate = new Date();
        if (StrUtil.isNotEmpty(user.getPassword())) {
            user.setPassword(EncryptUtil.encode(user.getPassword()));
        }

        if (user.getId() == null) {
            // 新增用户时校验用户名唯一校验
            User find = findByUserName(user.getUserName());
            if (find != null) {
                return ApiResult.failure("用户名已存在");
            }
            user.setSuperAdmin(false);
            user.setDeleted(false);
            user.setCreateTime(currDate);
            this.userMapper.insertSelective(user);
        } else {
            user.setUpdateTime(currDate);
            this.userMapper.updateByPrimaryKeySelective(user);

            // 删除原有 用户~角色 关联关系
            this.userRoleService.deleteByUserId(user.getId());
        }

        // 保存新的 用户~角色 关联信息
        if (StrUtil.isNotEmpty(user.getRoleIds())) {
            List<UserRole> userRoleList = new ArrayList<>();
            for (String roleId : user.getRoleIds().split(",")) {
                userRoleList.add(new UserRole(user.getId(), Long.parseLong(roleId)));
            }
            this.userRoleService.getUserRoleMapper().insertList(userRoleList);
        }

        return ApiResult.success();
    }
}
