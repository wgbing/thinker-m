package com.jyou.thinker.config.security;

import com.jyou.thinker.domain.Permission;
import com.jyou.thinker.service.sys.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 功能描述：加载资源与权限的对应关系
 */
@Service
public class CustomSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    @Autowired
    private PermissionService permissionService;

    private static Map<String,Collection<ConfigAttribute>> permissionMap = null;

    /**
     * 功能描述：加载所有的权限信息
     **/
    @PostConstruct
    private void loadPermissionDefine() {
        if (permissionMap == null) {
            permissionMap = new HashMap<String, Collection<ConfigAttribute>>();
            List<Permission> permissions = permissionService.getPermissionMapper().selectAll();
            for (Permission m : permissions) {
                Collection<ConfigAttribute> configAttributes =permissionMap.get(m.getResUrl());
                // TODO:ZZQ 通过资源名称来表示具体的权限 注意：必须"ROLE_"开头
               if(configAttributes==null) {
                   configAttributes=new ArrayList<ConfigAttribute>();
               }
                ConfigAttribute configAttribute = new org.springframework.security.access.SecurityConfig("ROLE_" + m.getResKey());
                configAttributes.add(configAttribute);
                permissionMap.put(m.getResUrl(), configAttributes);
            }
        }
    }
    /**
     * 功能描述：返回所请求资源所需要的权限
     **/
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        String requestUrl = ((FilterInvocation) object).getRequestUrl();
        //如果权限资源为null，重新加载
        if(permissionMap == null) {
            loadPermissionDefine();
        }
        Collection<ConfigAttribute> configAttributes = null;
        if(permissionMap != null){
            for(String regex:permissionMap.keySet()){
                if(Pattern.matches(regex, requestUrl)){
                    configAttributes = permissionMap.get(regex);
                    break;
                }
            }
        }
        return configAttributes;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
