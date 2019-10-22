package com.jyou.thinker.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.*;
import java.io.IOException;

/**
 * 功能描述：自定义过滤用户请求类
 */
@Service
public class CustomSecurityFilter extends AbstractSecurityInterceptor implements Filter {
    @Autowired
    private CustomSecurityMetadataSource securityMetadataSource;
    @Autowired
    private CustomAccessDecisionManager accessDecisionManager;

    @Override
    public Class<?> getSecureObjectClass() {
        return FilterInvocation.class;
    }

    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource() {
        return this.securityMetadataSource;
    }

    @PostConstruct
    public void init(){
           super.setAccessDecisionManager(accessDecisionManager);
    }
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        FilterInvocation fi = new FilterInvocation(request, response, chain);
        invoke(fi);
    }
    private void invoke(FilterInvocation fi) throws IOException, ServletException {
        // object为FilterInvocation对象
        //super.beforeInvocation(fi);源码
        //1.获取请求资源的权限
        //执行Collection<ConfigAttribute> attributes = SecurityMetadataSource.getAttributes(object);
        //2.是否拥有权限
        //this.accessDecisionManager.decide(authenticated, object, attributes);
      //  System.out.println(fi.getFullRequestUrl());
        InterceptorStatusToken token = super.beforeInvocation(fi);
        try {
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        } finally {
           super.afterInvocation(token, null);
        }
    }
    @Override
    public void destroy() {}
    public void init(FilterConfig filterConfig) throws ServletException {}
}
