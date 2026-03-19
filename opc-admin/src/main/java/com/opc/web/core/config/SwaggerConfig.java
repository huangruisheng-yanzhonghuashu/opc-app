package com.opc.web.core.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.opc.common.config.RuoYiConfig;

import java.util.List;

/**
 * Swagger / Knife4j 接口文档配置
 *
 * @author opc
 */
@Configuration
public class SwaggerConfig
{
    /** 系统基础配置 */
    @Autowired
    private RuoYiConfig ruoyiConfig;

    /**
     * 自定义的 OpenAPI 对象
     */
    @Bean
    public OpenAPI customOpenApi()
    {
        return new OpenAPI()
            // 添加服务器信息
            .servers(List.of(new Server().url("/").description("默认服务器地址")))
            // 配置接口文档基本信息
            .info(getApiInfo())
            // 配置安全策略
            .addSecurityItem(new SecurityRequirement().addList("Authorization"))
            // 配置安全策略的具体实现
            .components(new io.swagger.v3.oas.models.Components()
                .addSecuritySchemes("Authorization", securityScheme()));
    }

    /**
     * 配置安全策略
     */
    private SecurityScheme securityScheme()
    {
        return new SecurityScheme()
            // 认证类型：APIKEY
            .type(SecurityScheme.Type.APIKEY)
            // 令牌名称
            .name("Authorization")
            // 令牌存放位置：请求头
            .in(SecurityScheme.In.HEADER)
            // 认证方案
            .scheme("Bearer")
            // 描述
            .description("请输入Bearer token进行认证");
    }

    /**
     * 添加摘要信息
     */
    private Info getApiInfo()
    {
        return new Info()
            // 设置标题
            .title("OPC 接口文档")
            // 描述
            .description("用于管理集团旗下公司的人员信息，提供用户管理、角色管理、权限管理等模块的接口")
            // 作者信息
            .contact(new Contact()
                .name(ruoyiConfig.getName())
                .url("https://www.opc.com")
                .email("support@opc.com"))
            // 版本
            .version("v" + ruoyiConfig.getVersion())
            // 许可证
            .license(new License()
                .name("Apache 2.0")
                .url("https://www.apache.org/licenses/LICENSE-2.0"));
    }
}



