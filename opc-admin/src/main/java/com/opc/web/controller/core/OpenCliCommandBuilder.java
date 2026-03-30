package com.opc.web.controller.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.opc.web.controller.core.OpenCliConstants.*;

/**
 * OpenCLI 命令构建器
 * <p>
 * 用于构建 opencli 命令行命令，支持 Windows 和 Linux/Mac 系统。
 * 封装了命令构建的复杂逻辑，提供链式调用方式简化命令创建。
 * </p>
 *
 * <h3>使用示例：</h3>
 * <pre>
 * List<String> command = new OpenCliCommandBuilder()
 *     .withModule(MODULE_REDDIT)
 *     .withSubCommand(SUBCOMMAND_SEARCH)
 *     .withArg(keyword)
 *     .withOption(OPTION_SORT, SORT_HOT)
 *     .withOption(OPTION_FORMAT_JSON, VALUE_JSON)
 *     .build();
 * </pre>
 *
 * @author opc
 * @since 3.9.1
 * @see OpenCliConstants
 */
public class OpenCliCommandBuilder {

    private final List<String> command;
    private final boolean isWindows;

    /**
     * 创建命令构建器
     */
    public OpenCliCommandBuilder() {
        this.command = new ArrayList<>();
        String osName = System.getProperty("os.name").toLowerCase();
        this.isWindows = osName.contains("win");

        // 添加系统前缀命令
        if (isWindows) {
            command.add(CMD_WINDOWS);
            command.add(CMD_WINDOWS_ARG);
            command.add(OPENCLI);
        } else {
            command.add(CMD_UNIX);
        }
    }

    /**
     * 设置模块（twitter/reddit）
     *
     * @param module 模块名称
     * @return 当前构建器
     */
    public OpenCliCommandBuilder withModule(String module) {
        command.add(module);
        return this;
    }

    /**
     * 设置子命令（search/download/read）
     *
     * @param subCommand 子命令名称
     * @return 当前构建器
     */
    public OpenCliCommandBuilder withSubCommand(String subCommand) {
        command.add(subCommand);
        return this;
    }

    /**
     * 添加普通参数
     *
     * @param arg 参数值
     * @return 当前构建器
     */
    public OpenCliCommandBuilder withArg(String arg) {
        command.add(arg);
        return this;
    }

    /**
     * 添加选项参数（--option value）
     *
     * @param option 选项名
     * @param value  选项值
     * @return 当前构建器
     */
    public OpenCliCommandBuilder withOption(String option, String value) {
        command.add(option);
        command.add(value);
        return this;
    }

    /**
     * 添加单个选项（如 -f json）
     *
     * @param option 选项
     * @return 当前构建器
     */
    public OpenCliCommandBuilder withFlag(String option) {
        command.add(option);
        return this;
    }

    /**
     * 构建命令列表
     *
     * @return 命令字符串列表
     */
    public List<String> build() {
        return new ArrayList<>(command);
    }

    /**
     * 获取命令字符串（用于日志输出）
     *
     * @return 命令字符串
     */
    public String toCommandString() {
        return String.join(" ", command);
    }

    /**
     * 判断当前是否为 Windows 系统
     *
     * @return true 如果是 Windows
     */
    public boolean isWindows() {
        return isWindows;
    }

    /**
     * 创建 ProcessBuilder 并设置环境变量
     *
     * @return 配置好的 ProcessBuilder
     */
    public ProcessBuilder createProcessBuilder() {
        ProcessBuilder processBuilder = new ProcessBuilder(build());
        processBuilder.redirectErrorStream(true);

        // 设置环境变量，确保能找到 opencli
        Map<String, String> env = processBuilder.environment();
        String path = env.get("PATH");
        if (path != null) {
            String additionalPaths = "C:\\Users\\admin-1\\AppData\\Roaming\\npm;" +
                    System.getProperty("user.home") + "\\AppData\\Roaming\\npm;" +
                    "C:\\Program Files\\nodejs;" +
                    "C:\\Program Files (x86)\\nodejs";
            env.put("PATH", additionalPaths + ";" + path);
        }

        return processBuilder;
    }

    // ==================== 便捷静态方法 ====================

    /**
     * 构建 Reddit 搜索命令
     *
     * @param keyword 搜索关键词
     * @return 命令列表
     */
    public static List<String> buildRedditSearch(String keyword) {
        return new OpenCliCommandBuilder()
                .withModule(MODULE_REDDIT)
                .withSubCommand(SUBCOMMAND_SEARCH)
                .withArg(keyword)
                .withOption(OPTION_SORT, SORT_HOT)
                .withOption(OPTION_FORMAT_JSON, VALUE_JSON)
                .build();
    }

    /**
     * 构建 Reddit 读取详情命令
     *
     * @param postUrl Reddit 帖子 URL
     * @return 命令列表
     */
    public static List<String> buildRedditRead(String postUrl) {
        return new OpenCliCommandBuilder()
                .withModule(MODULE_REDDIT)
                .withSubCommand(SUBCOMMAND_READ)
                .withArg(postUrl)
                .withOption(OPTION_FORMAT_JSON, VALUE_JSON)
                .build();
    }

    /**
     * 构建 Twitter 搜索命令
     *
     * @param keyword 搜索关键词
     * @return 命令列表
     */
    public static List<String> buildTwitterSearch(String keyword) {
        return new OpenCliCommandBuilder()
                .withModule(MODULE_TWITTER)
                .withSubCommand(SUBCOMMAND_SEARCH)
                .withArg(keyword)
                .withOption(OPTION_FORMAT_JSON, VALUE_JSON)
                .build();
    }

    /**
     * 构建 Twitter 下载命令
     *
     * @param tweetUrl Twitter 推文 URL
     * @return 命令列表
     */
    public static List<String> buildTwitterDownload(String tweetUrl) {
        return new OpenCliCommandBuilder()
                .withModule(MODULE_TWITTER)
                .withSubCommand(SUBCOMMAND_DOWNLOAD)
                .withOption(PARAM_TWEET_URL, tweetUrl)
                .build();
    }
}
