package com.opc.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取项目相关配置
 * 
 * @author opc
 */
@Component
@ConfigurationProperties(prefix = "sop")
public class SopConfig
{
    /** 项目名称 */
    private String name;

    /** 版本 */
    private String version;

    /** 版权年份 */
    private String copyrightYear;

    /** 上传路径 */
    private static String profile;

    /** 获取地址开关 */
    private static boolean addressEnabled;

    /** 验证码类型 */
    private static String captchaType;

    /** 服务器域名 */
    private static String serverUrl;

    /** 文件服务器上传地址 */
    private static String fileServer;

    /** 最大文件上传大小（MB） */
    private static int maxFileSize = 500;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public String getCopyrightYear()
    {
        return copyrightYear;
    }

    public void setCopyrightYear(String copyrightYear)
    {
        this.copyrightYear = copyrightYear;
    }

    public static String getProfile()
    {
        return profile;
    }

    public void setProfile(String profile)
    {
        SopConfig.profile = profile;
    }

    public static boolean isAddressEnabled()
    {
        return addressEnabled;
    }

    public void setAddressEnabled(boolean addressEnabled)
    {
        SopConfig.addressEnabled = addressEnabled;
    }

    public static String getCaptchaType() {
        return captchaType;
    }

    public void setCaptchaType(String captchaType) {
        SopConfig.captchaType = captchaType;
    }

    public static String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        SopConfig.serverUrl = serverUrl;
    }

    public static String getFileServer() {
        return fileServer;
    }

    public void setFileServer(String fileServer) {
        SopConfig.fileServer = fileServer;
    }

    public static int getMaxFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(int maxFileSize) {
        SopConfig.maxFileSize = maxFileSize;
    }

    /**
     * 获取导入上传路径
     */
    public static String getImportPath()
    {
        return getProfile() + "/import";
    }

    /**
     * 获取头像上传路径
     */
    public static String getAvatarPath()
    {
        return getProfile() + "/avatar";
    }

    /**
     * 获取下载路径
     */
    public static String getDownloadPath()
    {
        return getProfile() + "/download/";
    }

    /**
     * 获取上传路径
     */
    public static String getUploadPath()
    {
        return getProfile() + "/upload";
    }
}
