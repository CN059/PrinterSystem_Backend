package com.powercess.printersystem.printersystem.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class SaTokenConfig implements WebMvcConfigurer {
    // 注册 Sa-Token 拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor(handler -> {
            // 定义需要鉴权的路径和放行的路径
            SaRouter.match("/**")              // 匹配所有路径
                    .notMatch("/api/user/register")  // 放行注册接口
                    .notMatch("/api/user/login")     // 放行登录接口
                    .check(r -> StpUtil.checkLogin()); // 其他路径需要登录才能访问
        })).addPathPatterns("/**");
    }
}