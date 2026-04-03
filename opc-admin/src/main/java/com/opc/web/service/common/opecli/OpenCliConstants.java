package com.opc.web.service.common.opecli;

import com.opc.web.controller.core.RedditImportController;
import com.opc.web.controller.core.TwitterImportController;

/**
 * OpenCLI 命令行常量定义
 * <p>
 * 集中管理所有 opencli 命令相关的常量，包括命令名称、参数和选项。
 * 用于 TwitterImportController 和 RedditImportController 中执行命令行操作。
 * </p>
 *
 * @author opc
 * @since 3.9.1
 * @see TwitterImportController
 * @see RedditImportController
 */
public final class OpenCliConstants {

    private OpenCliConstants() {
        // 私有构造器，防止实例化
    }

    // ==================== 系统命令 ====================

    /** Windows 命令行程序 */
    public static final String CMD_WINDOWS = "cmd";

    /** Windows 命令行参数 */
    public static final String CMD_WINDOWS_ARG = "/c";

    /** Linux/Mac 命令行程序 */
    public static final String CMD_UNIX = "opencli";

    // ==================== 命令主体 ====================

    /** opencli 命令 */
    public static final String OPENCLI = "opencli";

    /** Twitter 模块 */
    public static final String MODULE_TWITTER = "twitter";

    /** Reddit 模块 */
    public static final String MODULE_REDDIT = "reddit";

    /** yt-dlp 命令 */
    public static final String CMD_YT_DLP = "yt-dlp";

    // ==================== 子命令 ====================

    /** 搜索子命令 */
    public static final String SUBCOMMAND_SEARCH = "search";

    /** 下载子命令 */
    public static final String SUBCOMMAND_DOWNLOAD = "download";

    /** 读取详情子命令 */
    public static final String SUBCOMMAND_READ = "read";

    // ==================== 参数选项 ====================

    /** JSON 格式输出选项 */
    public static final String OPTION_FORMAT_JSON = "-f";

    /** JSON 格式值 */
    public static final String VALUE_JSON = "json";

    /** 排序选项 */
    public static final String OPTION_SORT = "--sort";

    /** 热门排序值 */
    public static final String SORT_HOT = "hot";

    /** Twitter 推文 URL 参数 */
    public static final String PARAM_TWEET_URL = "--tweet-url";

    /** 输出目录参数 */
    public static final String PARAM_OUTPUT = "--output";


    // ==================== 来源类型 ====================

    /** Twitter 来源类型 */
    public static final String SOURCE_TWITTER = "twitter";

    /** Reddit 来源类型 */
    public static final String SOURCE_REDDIT = "reddit";

    // ==================== 内容类型 ====================

    /** 文本内容类型 */
    public static final String CONTENT_TYPE_TEXT = "text";

    /** 图片内容类型 */
    public static final String CONTENT_TYPE_IMAGE = "image";

    /** 视频内容类型 */
    public static final String CONTENT_TYPE_VIDEO = "video";

    // ==================== 状态值 ====================

    /** 状态下线 */
    public static final String STATUS_OFFLINE = "1";

    /** 套餐类型 - 普通会员 */
    public static final int PACKAGE_TYPE_NORMAL = 1;

    // ==================== 代理设置 ====================

    /** HTTP 代理环境变量名 */
    public static final String ENV_HTTP_PROXY = "HTTP_PROXY";

    /** HTTPS 代理环境变量名 */
    public static final String ENV_HTTPS_PROXY = "HTTPS_PROXY";

    /** 默认代理地址 */
    public static final String DEFAULT_PROXY = "http://127.0.0.1:7890";

}
