package com.jyou.thinker.config.security;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Iterator;

/**
 * 功能描述：用户是否拥有所请求资源的权限
 */
@Service
public class CustomAccessDecisionManager implements AccessDecisionManager {
    /**
     * 功能描述：判断是否有权限访问所请求的资源
     **/

    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        if(configAttributes == null) {
           return;
        }
        //所请求的资源拥有的权限(一个资源对多个权限)
        Iterator<ConfigAttribute> iterator = configAttributes.iterator();
        while(iterator.hasNext()) {
            ConfigAttribute configAttribute = iterator.next();
            //访问所请求资源所需要的权限
            String needPermission = configAttribute.getAttribute();
            //用户所拥有的权限authentication
            for(GrantedAuthority ga : authentication.getAuthorities()) {
                if(needPermission.equals(ga.getAuthority())) {
                    return;
                }
            }
        }
        //没有权限
        throw new AccessDeniedException("没有权限访问!");
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return false;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
