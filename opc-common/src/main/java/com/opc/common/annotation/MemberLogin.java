package com.opc.common.annotation;

import java.lang.annotation.*;

/**
 * 移动端会员登录校验注解
 * 标记在 Controller 方法或类上，表示该接口需要会员登录才能访问
 * 
 * @author opc
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MemberLogin
{
}
