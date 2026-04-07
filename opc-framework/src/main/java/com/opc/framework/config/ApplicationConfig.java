package com.opc.framework.config;

import java.util.TimeZone;
import jakarta.annotation.PostConstruct;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 程序注解配置
 *
 * @author opc
 */
@Configuration
// 表示通过aop框架暴露该代理对象,AopContext能够访问
@EnableAspectJAutoProxy(exposeProxy = true)
// 指定要扫描的Mapper类的包的路径
@MapperScan("com.opc.**.mapper")
public class ApplicationConfig
{
    @Value("${spring.jackson.time-zone:Asia/Shanghai}")
    private String timeZone;

    /**
     * 系统启动时设置默认时区
     */
    @PostConstruct
    public void setDefaultTimeZone()
    {
        TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
    }

    /**
     * Jackson 时区配置
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonObjectMapperCustomization()
    {
        return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder.timeZone(TimeZone.getTimeZone(timeZone));
    }
}
