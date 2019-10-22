package com.jyou.thinker.config.security;

import com.jyou.thinker.utils.EncryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.session.ConcurrentSessionFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.security.web.session.SimpleRedirectSessionInformationExpiredStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * TODO: Spring Security 配置
 * @author wgbing
 * @date 2018/7/10 18:00
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)//开启security注解
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * TODO: 定义系统安全策略，如哪些url路径需要经过授权才能访问，哪些不用
     * @author wgbing
     * @date 2018/7/12 15:52
     * @param http
     * @return
     * @throws
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable();             //解决SpringBoot不允许加载iframe问题
        http.exceptionHandling().accessDeniedPage("/denied");//没有权限访问时跳转路径
//        http.csrf().disable();
        http
            .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/fine/**").permitAll()
                .antMatchers("/fonts/**").permitAll()
                .antMatchers("/images/**").permitAll()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/libs/**").permitAll()
                .antMatchers("/plugins/**").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/register").permitAll()
                .antMatchers("/rest/**").permitAll()
                .antMatchers("/captcha/image").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(customUsernamePasswordAuthenticationFilter(),UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(customSecurityFilter(),FilterSecurityInterceptor.class)
                .addFilterAt(concurrencyFilter(), ConcurrentSessionFilter.class)
                .sessionManagement().sessionAuthenticationStrategy(sessionAuthenticationStrategy())
                .and()
            .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/auth")
                .failureUrl("/login?error")
                .and()
            .logout() //logout config
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .invalidateHttpSession(true)
                .logoutSuccessUrl("/login");

    }

    @Override
    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailService();
    }

    @Bean
    public CustomAuthenticationFilter customUsernamePasswordAuthenticationFilter()
            throws Exception {
        CustomAuthenticationFilter customUsernamePasswordAuthenticationFilter = new CustomAuthenticationFilter();
        customUsernamePasswordAuthenticationFilter
                .setAuthenticationManager(authenticationManagerBean());
        customUsernamePasswordAuthenticationFilter
                .setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/auth","POST"));
        //session并发控制,因为默认的并发控制方法是空方法.这里必须自己配置一个
//       customUsernamePasswordAuthenticationFilter.setSessionAuthenticationStrategy(new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry()));
        customUsernamePasswordAuthenticationFilter.setSessionAuthenticationStrategy(sessionAuthenticationStrategy());
        return customUsernamePasswordAuthenticationFilter;
    }

    @Bean
    SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public SessionInformationExpiredStrategy sessionInformationExpiredStrategy() {
        return new SimpleRedirectSessionInformationExpiredStrategy("/login");
    }

    @Bean
    public ConcurrentSessionControlAuthenticationStrategy sessionAuthenticationStrategy() {
        ConcurrentSessionControlAuthenticationStrategy sessionStrategy =new  ConcurrentSessionControlAuthenticationStrategy(sessionRegistry());
        sessionStrategy.setMaximumSessions(1);
        sessionStrategy.setExceptionIfMaximumExceeded(false);
        return sessionStrategy;
    }

    @Bean
    public ConcurrentSessionFilter concurrencyFilter() {
        ConcurrentSessionFilter concurrentSessionFilter = new ConcurrentSessionFilter(sessionRegistry(), sessionInformationExpiredStrategy());
        return concurrentSessionFilter;
    }

    @Bean("CustomSecurityFilter")
    public CustomSecurityFilter customSecurityFilter() throws Exception{
        CustomSecurityFilter customSecurityFilter=new CustomSecurityFilter();
        customSecurityFilter.setAuthenticationManager(authenticationManagerBean());
        return customSecurityFilter;
    }

    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(EncryptUtil.getPasswordEncoder());
        auth.eraseCredentials(false);
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

}
