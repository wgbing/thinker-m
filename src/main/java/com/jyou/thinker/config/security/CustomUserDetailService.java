package com.jyou.thinker.config.security;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.jyou.thinker.domain.Permission;
import com.jyou.thinker.domain.User;
import com.jyou.thinker.mapper.PermissionMapper;
import com.jyou.thinker.mapper.UserMapper;
import com.jyou.thinker.service.sys.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 功能描述：获取登录账号的权限信息
 */
@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private PermissionService permissionService;

    /**
     * 功能描述：封装用户的权限信息为spring security的user
     **/
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userdetail = null;
        if(StrUtil.isNotEmpty(username)){
            User user = userMapper.findOneByUserName(username);
            //密码设置为null
            if(null != user){
                String password = user.getPassword();
                user.setPassword(null);
                //取得用户的权限
                Collection<GrantedAuthority> grantedAuths = obtionGrantedAuthorities(user);
                //取得用户菜单
                List<Permission> menuList = getMenu(user);
                // 封装成spring security的user
                userdetail = new CustomUserDetail(
                        user.getUserName(),
                        password,
                        user.getEnable(),  //账号状态  0 表示停用  1表示启用
                        true,
                        true,
                        true,
                        grantedAuths,	//用户的权限
                        user,
                        menuList
                );
            }
        }
        return userdetail;
    }

    /**
     * 功能描述：获取登录账号的权限信息
     **/
    private Set<GrantedAuthority> obtionGrantedAuthorities(User user) {
        List<String> permissionKey=new ArrayList<>();
        if(user.getSuperAdmin()) {
            List<Permission> permissionList =  permissionMapper.selectAll();
            if (CollUtil.isNotEmpty(permissionList)) {
                for(Permission permission:permissionList){
                    if(StrUtil.isNotEmpty(permission.getResKey())){
                        permissionKey.add(permission.getResKey());
                    }
                }
            }
        } else {
            permissionKey=permissionMapper.findResKeysByUserId(user.getId());
        }

        Set<GrantedAuthority> authSet = new HashSet<GrantedAuthority>();
        if(CollUtil.isNotEmpty(permissionKey)) {
            for (String key : permissionKey) {
                // TODO:ZZQ 用户可以访问的资源名称（或者说用户所拥有的权限） 注意：必须"ROLE_"开头
                if(StrUtil.isNotEmpty(key)){
                    authSet.add(new SimpleGrantedAuthority("ROLE_" + key));
                }
            }
        }
        return authSet;
    }

    /**
     * 功能描述：取得用户菜单
     **/
    private List<Permission> getMenu(User user){
        return permissionService.getMenu(user);
    }

    /**
     * 更新登录信息
     * @param username
     * @param ip
     */
    public void updateLoginInfo(String username, String ip) {
        if(StrUtil.isNotEmpty(username)){
            User u = userMapper.findOneByUserName(username);
            if(null != u) {
                u.setLastLoginTime(new Date());
                u.setLastLoginIp(ip);
                userMapper.updateByPrimaryKeySelective(u);
            }
        }

    }
}
