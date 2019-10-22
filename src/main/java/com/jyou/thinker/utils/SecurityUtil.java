package com.jyou.thinker.utils;

import com.jyou.thinker.config.security.CustomUserDetail;
import com.jyou.thinker.domain.Permission;
import com.jyou.thinker.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * TODO: 获取登录身份信息
 * @author wgbing
 * @date 2019-09-30 16:46
 */
public class SecurityUtil {

    /**
     * 获取登录用户名，如果没有通过登录认证返回null值
     * @return
     */
    public static String getLoginName() {
        if (!isAuthenticated()) {
            return null;
        }

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String loginName = null;
        if (principal instanceof UserDetails) {
            CustomUserDetail userDetails = (CustomUserDetail) principal;
            loginName = userDetails.getUsername();
        } else {
            loginName = (String) principal;
        }
        return loginName;
    }

    /**
     * 判断是否认证通过
     * @return
     */
    public static Boolean isAuthenticated() {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            return SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
        }
        return false;
    }

    /**
     * 获取登录用户信息，如果未登录返回null
     * @return
     */
    public static User getUser() {
        if (!isAuthenticated()) {
            return null;
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = null;
        if (principal instanceof UserDetails) {
            CustomUserDetail userDetails = (CustomUserDetail) principal;
            user = userDetails.getUser();
        }

        return user;
    }

    public static List<Permission> getMenu(){
        if (!isAuthenticated()) {
            return null;
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Permission> menuList = new ArrayList<>();
        if (principal instanceof UserDetails) {
            CustomUserDetail userDetails = (CustomUserDetail) principal;
            menuList = userDetails.getMenuList();
        }

        return menuList;
    }

    /**
     * 设置登录用户信息
     * @param user
     */
    public static void setUser(User user) {
        if (!isAuthenticated()) {
            return;
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            CustomUserDetail userDetails = (CustomUserDetail) principal;
            userDetails.setUser(user);
        }
    }

    /**
     * 获取登录用户权限信息，如果未登录返回null值
     * @return
     */
    public static List<GrantedAuthority> getAuthorities() {
        if (!isAuthenticated()) {
            return null;
        }

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<GrantedAuthority> authorities = null;
        if (principal instanceof UserDetails) {
            CustomUserDetail userDetails = (CustomUserDetail) principal;
            Collection<GrantedAuthority> tmpColl = (Collection<GrantedAuthority>) userDetails.getAuthorities();
            if (null != tmpColl && !tmpColl.isEmpty()) {
                authorities = new ArrayList<>();
                for (GrantedAuthority ga : tmpColl) {
                    authorities.add(ga);
                }
            }
        }

        return authorities;
    }

    /**
     * 判断当前登录用户是否包含某个权限
     * @param authority
     * @return
     */
    public static boolean hasAuthority(String authority) {
        List<GrantedAuthority> authList = SecurityUtil.getAuthorities();
        if (authList == null || authList.isEmpty()) {
            return false;
        }
        for (GrantedAuthority item : authList) {
            if (item.getAuthority().equals(authority)) {
                return true;
            }
        }
        return false;
    }

}
