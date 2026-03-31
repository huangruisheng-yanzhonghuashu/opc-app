package com.opc.framework.interceptor;

import java.lang.reflect.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import com.alibaba.fastjson2.JSON;
import com.opc.common.annotation.MemberAnonymous;
import com.opc.common.annotation.MemberLogin;
import com.opc.common.constant.HttpStatus;
import com.opc.common.core.domain.AjaxResult;
import com.opc.common.utils.ServletUtils;
import com.opc.core.domain.vo.MemberLoginVO;
import com.opc.core.service.MemberTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 移动端会员登录拦截器
 * 
 * 验证标注了 @MemberLogin 注解的接口是否已登录
 * 
 * @author opc
 */
@Component
public class MemberLoginInterceptor implements HandlerInterceptor
{
    @Autowired
    private MemberTokenService memberTokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        if (handler instanceof HandlerMethod)
        {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            
            // 优先检查方法上的 @MemberAnonymous 注解，如果存在则跳过登录验证
            MemberAnonymous anonymousAnnotation = method.getAnnotation(MemberAnonymous.class);
            if (anonymousAnnotation != null)
            {
                return true;
            }
            
            // 检查方法上的 @MemberLogin 注解
            MemberLogin annotation = method.getAnnotation(MemberLogin.class);
            
            // 如果方法上没有，检查类上的注解
            if (annotation == null)
            {
                annotation = handlerMethod.getBeanType().getAnnotation(MemberLogin.class);
            }
            
            if (annotation != null)
            {
                // 验证会员登录状态
                MemberLoginVO loginUser = memberTokenService.getLoginUser(request);
                if (loginUser == null)
                {
                    // 未登录，返回 401 错误
                    AjaxResult ajaxResult = AjaxResult.error(HttpStatus.UNAUTHORIZED, "请先登录");
                    ServletUtils.renderString(response, JSON.toJSONString(ajaxResult));
                    return false;
                }
                // 已登录，将用户信息存入请求属性，便于后续使用
                request.setAttribute("memberLoginUser", loginUser);
            }
        }
        return true;
    }
}
