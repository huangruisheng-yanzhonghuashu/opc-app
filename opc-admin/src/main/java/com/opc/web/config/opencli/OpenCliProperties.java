package com.opc.web.config.opencli;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * OpenCLI 配置属性
 * <p>
 * 从 application-opencli.yml 读取 OpenCLI 相关配置，
 * 包括代理设置等。
 * </p>
 *
 * @author opc
 * @since 3.9.1
 */
@Component
@ConfigurationProperties(prefix = "opencli")
public class OpenCliProperties {

    /**
     * opencli 可执行文件的绝对路径
     * 例如: /Users/sevilinma/.nvm/versions/node/v22.22.0/bin/opencli
     * 如果未设置，则使用 PATH 环境变量查找
     */
    private String executablePath;

    /**
     * yt-dlp 可执行文件的绝对路径
     * 例如: /usr/local/bin/yt-dlp (Mac/Linux) 或 C:\Tools\yt-dlp.exe (Windows)
     * 如果未设置，则使用 PATH 环境变量查找
     */
    private String ytDlpPath;

    /**
     * 命令行代理配置（用于 opencli、yt-dlp 等命令行工具）
     */
    private ProxyConfig commandProxy = new ProxyConfig();

    /**
     * HTTP 代理配置（用于 HTTP 下载、API 请求等）
     */
    private ProxyConfig httpProxy = new ProxyConfig();

    public String getExecutablePath() {
        return executablePath;
    }

    public void setExecutablePath(String executablePath) {
        this.executablePath = executablePath;
    }

    public String getYtDlpPath() {
        return ytDlpPath;
    }

    public void setYtDlpPath(String ytDlpPath) {
        this.ytDlpPath = ytDlpPath;
    }

    public ProxyConfig getCommandProxy() {
        return commandProxy;
    }

    public void setCommandProxy(ProxyConfig commandProxy) {
        this.commandProxy = commandProxy;
    }

    public ProxyConfig getHttpProxy() {
        return httpProxy;
    }

    public void setHttpProxy(ProxyConfig httpProxy) {
        this.httpProxy = httpProxy;
    }

    /**
     * 获取命令行代理 URL
     *
     * @return 代理地址，如 http://127.0.0.1:7890，如果未启用则返回 null
     */
    public String getCommandProxyUrl() {
        return getProxyUrlFromConfig(commandProxy);
    }

    /**
     * 获取 HTTP 代理 URL
     *
     * @return 代理地址，如 http://127.0.0.1:7890，如果未启用则返回 null
     */
    public String getHttpProxyUrl() {
        return getProxyUrlFromConfig(httpProxy);
    }

    /**
     * 从配置中获取代理 URL
     */
    private String getProxyUrlFromConfig(ProxyConfig config) {
        if (config == null || !config.isEnabled()) {
            return null;
        }
        // 如果配置了完整的 url，优先使用
        if (config.getUrl() != null && !config.getUrl().isEmpty()) {
            return config.getUrl();
        }
        // 否则根据 host/port/protocol 构建
        if (config.getHost() == null || config.getHost().isEmpty()) {
            return null;
        }
        String protocol = config.getProtocol() != null ? config.getProtocol() : "http";
        int port = config.getPort() > 0 ? config.getPort() : 7890;
        return String.format("%s://%s:%d", protocol, config.getHost(), port);
    }

    /**
     * 代理配置内部类
     */
    public static class ProxyConfig {
        /**
         * 是否启用代理
         */
        private boolean enabled = false;

        /**
         * 代理主机地址
         */
        private String host = "127.0.0.1";

        /**
         * 代理端口
         */
        private int port = 7890;

        /**
         * 代理协议 (http, https, socks5)
         */
        private String protocol = "http";

        /**
         * 代理完整地址（优先使用）
         */
        private String url;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getProtocol() {
            return protocol;
        }

        public void setProtocol(String protocol) {
            this.protocol = protocol;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
