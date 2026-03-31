package com.opc.common.annotation;

import java.lang.annotation.*;

/**
 * 移动端会员匿名访问注解
 * 标记在 Controller 方法上，表示该接口不需要会员登录即可访问
 * 优先级高于类上的 @MemberLogin 注解
 * 
 * @author opc
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MemberAnonymous
{
}
