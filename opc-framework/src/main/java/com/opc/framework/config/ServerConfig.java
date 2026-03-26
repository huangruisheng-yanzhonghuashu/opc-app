package com.opc.framework.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import com.opc.common.config.SopConfig;
import com.opc.common.utils.ServletUtils;
import com.opc.common.utils.StringUtils;

/**
 * 服务相关配置
 * 
 * @author opc
 */
@Component
public class ServerConfig
{
    /**
     * 获取完整的请求路径，包括：域名，端口，上下文访问路径
     * 
     * @return 服务地址
     */
    public String getUrl()
    {
        // 优先使用配置文件中的域名
        String serverUrl = SopConfig.getServerUrl();
        if (StringUtils.isNotEmpty(serverUrl))
        {
            return serverUrl;
        }
        // 未配置则自动获取请求域名
        HttpServletRequest request = ServletUtils.getRequest();
        return getDomain(request);
    }

    public static String getDomain(HttpServletRequest request)
    {
        StringBuffer url = request.getRequestURL();
        String contextPath = request.getServletContext().getContextPath();
        return url.delete(url.length() - request.getRequestURI().length(), url.length()).append(contextPath).toString();
    }
}
