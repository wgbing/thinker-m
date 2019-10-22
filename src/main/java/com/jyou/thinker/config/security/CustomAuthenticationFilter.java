package com.jyou.thinker.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 功能描述：用户身份验证
 */

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    /**
     * 登录成功后跳转的地址
     */
    private String successUrl = "/index";
    /**
     * 登录失败后跳转的地址
     */
    private String errorUrl = "/login?error";

    @Resource
    private SessionRegistry sessionRegistry;

    @Autowired
    private CustomUserDetailService customUserDetailService;

    /**
     * 功能描述：自定义表单参数的name属性，默认是 j_username 和 j_password
     * 定义登录成功和失败的跳转地址
     **/
    public CustomAuthenticationFilter() {
        super();
        SimpleUrlAuthenticationFailureHandler simpleUrlAuthenticationFailureHandler = new SimpleUrlAuthenticationFailureHandler(errorUrl);
        simpleUrlAuthenticationFailureHandler.setUseForward(true);
        this.setAuthenticationFailureHandler(simpleUrlAuthenticationFailureHandler);

        this.setUsernameParameter(USERNAME);
        this.setPasswordParameter(PASSWORD);
        // 验证成功，跳转的页面
        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setDefaultTargetUrl(successUrl);
        this.setAuthenticationSuccessHandler(successHandler);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        if (!request.getMethod().equals("POST")) {

            request.setAttribute("error","登录异常，请联系管理员！");
            throw new AuthenticationServiceException(
                    "Authentication method not supported: "
                            + request.getMethod());
        }

        //校验验证码是否正确
        String captchaToken = (String) request.getSession().getAttribute("captchaToken");
        String captcha=(String)request.getParameter("captcha");
        if (!captchaToken.toLowerCase().equals(captcha.trim().toLowerCase())) {
             request.setAttribute("error","输入验证码错误！");
           throw new AuthenticationServiceException("输入验证码错误！");

        }

        //获取登录名和密码
        String username = obtainUsername(request);
        String password = obtainPassword(request);
        if(username == null) {
            username = "";
        }
        if(password == null) {
            password = "";
        }
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            BadCredentialsException exception = new BadCredentialsException(
                    "账号或密码不能为空！");// 在界面输出自定义的信息！！
            request.setAttribute("error","账号或密码不能为空！");
            throw exception;
        }

        // 实现 Authentication
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);

        //用户名密码验证通过后,注册session
        sessionRegistry.registerNewSession(request.getSession().getId(), authRequest.getPrincipal());
        // 允许子类设置详细属性
        setDetails(request, authRequest);

        // 运行UserDetailsService的loadUserByUsername 再次封装Authentication
        try{
            Authentication authentication = this.getAuthenticationManager().authenticate(authRequest);

            //更新登录地址时间和登录IP
            //根据request获取登录IP
            String ip = request.getHeader("x-forwarded-for");
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
            ip = ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
            customUserDetailService.updateLoginInfo(username, ip);

            return authentication;
        } catch (AuthenticationException e){
            if(e instanceof InternalAuthenticationServiceException){
                request.setAttribute("error", "账号不存在");
            }
            else if(e instanceof DisabledException){
                request.setAttribute("error", "账号已被禁用");
            }
            else{
                request.setAttribute("error", "用户名密码错误");
            }
            throw new BadCredentialsException("登录认证异常");
        }
    }

    public String getSuccessUrl() {
        return successUrl;
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    public String getErrorUrl() {
        return errorUrl;
    }

    public void setErrorUrl(String errorUrl) {
        this.errorUrl = errorUrl;
    }
}
