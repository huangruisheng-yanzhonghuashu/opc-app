package com.opc.web.service.common.opecli;

import com.opc.web.config.opencli.OpenCliProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.opc.web.service.common.opecli.OpenCliConstants.*;

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
    private String proxyUrl;
    private final boolean isOpenCliCommand;

    /**
     * 创建命令构建器
     */
    public OpenCliCommandBuilder() {
        this(true, null);
    }

    /**
     * 创建命令构建器（使用配置）
     * <p>
     * Windows 系统不需要使用 executablePath 配置，直接使用默认命令名
     * </p>
     *
     * @param properties OpenCLI 配置属性
     */
    public OpenCliCommandBuilder(OpenCliProperties properties) {
        this(true, getEffectiveExecutablePath(properties));
    }

    /**
     * 获取有效的可执行路径
     * <p>
     * Windows 系统返回 null（使用默认命令名）
     * Mac/Linux 返回配置的路径
     * </p>
     *
     * @param properties OpenCLI 配置属性
     * @return Windows 返回 null，其他系统返回配置的路径
     */
    private static String getEffectiveExecutablePath(OpenCliProperties properties) {
        if (properties == null) {
            return null;
        }
        String osName = System.getProperty("os.name").toLowerCase();
        boolean isWindows = osName.contains("win");
        // Windows 不需要使用 executablePath，直接返回 null
        if (isWindows) {
            return null;
        }
        return properties.getExecutablePath();
    }

    /**
     * 创建命令构建器
     *
     * @param isOpenCliCommand 是否是 opencli 命令，false 表示直接执行其他命令（如 yt-dlp）
     */
    public OpenCliCommandBuilder(boolean isOpenCliCommand) {
        this(isOpenCliCommand, null);
    }

    /**
     * 创建命令构建器（支持配置可执行路径）
     *
     * @param isOpenCliCommand 是否是 opencli 命令，false 表示直接执行其他命令（如 yt-dlp）
     * @param executablePath   opencli 的绝对路径，如果为 null 则使用默认名称
     */
    public OpenCliCommandBuilder(boolean isOpenCliCommand, String executablePath) {
        this.command = new ArrayList<>();
        this.isOpenCliCommand = isOpenCliCommand;
        String osName = System.getProperty("os.name").toLowerCase();
        this.isWindows = osName.contains("win");

        // 添加系统前缀命令（仅 opencli 命令需要）
        if (isOpenCliCommand) {
            if (isWindows) {
                command.add(CMD_WINDOWS);
                command.add(CMD_WINDOWS_ARG);
                command.add(executablePath != null ? executablePath : OPENCLI);
            } else {
                // Mac/Linux: 使用配置的绝对路径或默认名称
                command.add(executablePath != null ? executablePath : CMD_UNIX);
            }
        }
    }

    /**
     * 设置代理地址
     *
     * @param proxyUrl 代理地址，如 http://127.0.0.1:7890
     * @return 当前构建器
     */
    public OpenCliCommandBuilder withProxy(String proxyUrl) {
        this.proxyUrl = proxyUrl;
        return this;
    }

    /**
     * 使用默认代理地址
     *
     * @return 当前构建器
     */
    public OpenCliCommandBuilder withDefaultProxy() {
        this.proxyUrl = DEFAULT_PROXY;
        return this;
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
     * 判断当前是否是 opencli 命令
     *
     * @return true 如果是 opencli 命令
     */
    public boolean isOpenCliCommand() {
        return isOpenCliCommand;
    }

    /**
     * 获取命令字符串（用于日志输出）
     *
     * @return 命令字符串
     */
    public String toCommandString() {
        StringBuilder cmdBuilder = new StringBuilder();
        for (int i = 0; i < command.size(); i++) {
            String arg = command.get(i);
            if (i > 0) {
                cmdBuilder.append(" ");
            }
            // 非 Windows 系统：路径、URL、包含空格或特殊字符的参数需要引号
            if (!isWindows && shouldQuoteArg(arg)) {
                cmdBuilder.append("\"").append(arg).append("\"");
            } else {
                cmdBuilder.append(arg);
            }
        }
        return cmdBuilder.toString();
    }

    /**
     * 判断参数是否需要引号包裹
     * 适用于非 Windows 系统的命令行显示
     */
    private boolean shouldQuoteArg(String arg) {
        // 路径（包含 / 或 \）
        if (arg.contains("/") || arg.contains("\\")) {
            return true;
        }
        // URL（包含 ://）
        if (arg.contains("://")) {
            return true;
        }
        // 包含空格或特殊字符
        if (arg.contains(" ") || arg.contains("&") || arg.contains("|") ||
            arg.contains("<") || arg.contains(">") || arg.contains("(") || arg.contains(")")) {
            return true;
        }
        return false;
    }

    /**
     * 获取实际执行的完整命令字符串（包含代理设置）
     *
     * @return 完整命令字符串
     */
    public String toFullCommandString() {
        // 如果有代理且是 Windows 系统，返回包含代理设置的完整命令
        if (proxyUrl != null && !proxyUrl.isEmpty() && isWindows) {
            StringBuilder cmdBuilder = new StringBuilder();
            cmdBuilder.append(CMD_WINDOWS).append(" ").append(CMD_WINDOWS_ARG).append(" \"");
            cmdBuilder.append("set ").append(ENV_HTTP_PROXY).append("=").append(proxyUrl).append(" && ");
            cmdBuilder.append("set ").append(ENV_HTTPS_PROXY).append("=").append(proxyUrl).append(" && ");
            cmdBuilder.append("set ").append(ENV_HTTP_PROXY.toLowerCase()).append("=").append(proxyUrl).append(" && ");
            cmdBuilder.append("set ").append(ENV_HTTPS_PROXY.toLowerCase()).append("=").append(proxyUrl).append(" && ");
            
            // 添加实际命令参数
            if (isOpenCliCommand) {
                cmdBuilder.append(OPENCLI);
                // opencli 命令：从第4个开始（跳过 cmd /c opencli）
                for (int i = 3; i < command.size(); i++) {
                    appendCommandArgument(cmdBuilder, command.get(i));
                }
            } else {
                // 非 opencli 命令（如 yt-dlp）：从第1个开始
                for (int i = 0; i < command.size(); i++) {
                    String arg = command.get(i);
                    if (i == 0) {
                        cmdBuilder.append(arg);
                    } else {
                        appendCommandArgument(cmdBuilder, arg);
                    }
                }
            }
            cmdBuilder.append("\"");
            return cmdBuilder.toString();
        }
        // 非 Windows 或没有代理，返回普通命令
        return toCommandString();
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
     * 获取当前设置的代理地址
     *
     * @return 代理地址，如果未设置则返回 null
     */
    public String getProxyUrl() {
        return proxyUrl;
    }

    /**
     * 创建 ProcessBuilder 并设置环境变量
     *
     * @return 配置好的 ProcessBuilder
     */
    public ProcessBuilder createProcessBuilder() {
        // 如果有代理且是 Windows 系统，需要特殊处理
        if (proxyUrl != null && !proxyUrl.isEmpty() && isWindows) {
            return createWindowsProcessBuilderWithProxy();
        }

        ProcessBuilder processBuilder = new ProcessBuilder(build());
        processBuilder.redirectErrorStream(true);

        // 设置环境变量，确保能找到 opencli
        Map<String, String> env = processBuilder.environment();
        String path = env.get("PATH");
        if (path != null) {
            if (isWindows) {
                String additionalPaths = "C:\\Users\\admin-1\\AppData\\Roaming\\npm;" +
                        System.getProperty("user.home") + "\\AppData\\Roaming\\npm;" +
                        "C:\\Program Files\\nodejs;" +
                        "C:\\Program Files (x86)\\nodejs";
                env.put("PATH", additionalPaths + ";" + path);
            } else {
                // Mac/Linux: 添加常见的 npm 全局安装路径
                String home = System.getProperty("user.home");
                String additionalPaths = "/usr/local/bin:" +
                        "/opt/homebrew/bin:" +
                        home + "/.npm-global/bin:" +
                        home + "/.nvm/versions/node/current/bin:" +
                        // 添加可能的 nvm 版本路径（如 v22.22.0）
                        getNvmVersionPaths(home);
                env.put("PATH", additionalPaths + ":" + path);
            }
        }

        // 设置代理环境变量（非 Windows 系统）
        if (proxyUrl != null && !proxyUrl.isEmpty()) {
            env.put(ENV_HTTP_PROXY, proxyUrl);
            env.put(ENV_HTTPS_PROXY, proxyUrl);
            env.put(ENV_HTTP_PROXY.toLowerCase(), proxyUrl);
            env.put(ENV_HTTPS_PROXY.toLowerCase(), proxyUrl);
        }

        return processBuilder;
    }

    /**
     * 创建 Windows 带代理的 ProcessBuilder
     * Windows 下使用 cmd /c 时，需要在命令行中设置代理环境变量
     *
     * @return 配置好的 ProcessBuilder
     */
    private ProcessBuilder createWindowsProcessBuilderWithProxy() {
        // 构建带代理设置的命令
        List<String> proxyCommand = new ArrayList<>();
        proxyCommand.add(CMD_WINDOWS);
        proxyCommand.add(CMD_WINDOWS_ARG);
        
        // 使用 && 连接代理设置和实际命令（确保顺序执行）
        StringBuilder cmdBuilder = new StringBuilder();
        cmdBuilder.append("\""); // 整个命令用引号包裹
        cmdBuilder.append("set ").append(ENV_HTTP_PROXY).append("=").append(proxyUrl).append(" && ");
        cmdBuilder.append("set ").append(ENV_HTTPS_PROXY).append("=").append(proxyUrl).append(" && ");
        cmdBuilder.append("set ").append(ENV_HTTP_PROXY.toLowerCase()).append("=").append(proxyUrl).append(" && ");
        cmdBuilder.append("set ").append(ENV_HTTPS_PROXY.toLowerCase()).append("=").append(proxyUrl).append(" && ");
        
        // 添加实际命令
        List<String> originalCommand = build();
        if (isOpenCliCommand) {
            // opencli 命令：从第4个开始（跳过 cmd /c opencli）
            cmdBuilder.append(OPENCLI);
            for (int i = 3; i < originalCommand.size(); i++) {
                String arg = originalCommand.get(i);
                appendCommandArgument(cmdBuilder, arg);
            }
        } else {
            // 非 opencli 命令（如 yt-dlp）：从第1个开始
            for (int i = 0; i < originalCommand.size(); i++) {
                String arg = originalCommand.get(i);
                if (i == 0) {
                    cmdBuilder.append(arg);
                } else {
                    appendCommandArgument(cmdBuilder, arg);
                }
            }
        }
        cmdBuilder.append("\""); // 结束引号
        
        String fullCommand = cmdBuilder.toString();
        proxyCommand.add(fullCommand);
        
        // 打印实际执行的命令（用于调试）
        String debugCommand = String.join(" ", proxyCommand);
        System.out.println("[OpenCliCommandBuilder] Windows代理命令: " + debugCommand);
        
        ProcessBuilder processBuilder = new ProcessBuilder(proxyCommand);
        processBuilder.redirectErrorStream(true);

        // 设置 PATH 环境变量
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

    /**
     * 添加命令参数，处理包含空格或特殊字符的情况
     */
    private void appendCommandArgument(StringBuilder cmdBuilder, String arg) {
        // 如果参数包含空格或特殊字符，需要用引号括起来
        if (arg.contains(" ") || arg.contains("&") || arg.contains("|") || arg.contains("<") || arg.contains(">") || arg.contains("(") || arg.contains(")")) {
            cmdBuilder.append(" \"").append(arg).append("\"");
        } else {
            cmdBuilder.append(" ").append(arg);
        }
    }

    /**
     * 获取 nvm 版本目录路径
     * 扫描 ~/.nvm/versions/node/ 下的所有版本目录
     */
    private static String getNvmVersionPaths(String homeDir) {
        StringBuilder paths = new StringBuilder();
        java.io.File nvmDir = new java.io.File(homeDir + "/.nvm/versions/node");
        if (nvmDir.exists() && nvmDir.isDirectory()) {
            java.io.File[] versionDirs = nvmDir.listFiles();
            if (versionDirs != null) {
                for (java.io.File dir : versionDirs) {
                    if (dir.isDirectory()) {
                        if (paths.length() > 0) {
                            paths.append(":");
                        }
                        paths.append(dir.getAbsolutePath()).append("/bin");
                    }
                }
            }
        }
        return paths.toString();
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

    /**
     * 构建 Twitter 下载命令（带代理）
     *
     * @param tweetUrl Twitter 推文 URL
     * @param proxyUrl 代理地址，如 http://127.0.0.1:7890
     * @return 命令构建器
     */
    public static OpenCliCommandBuilder buildTwitterDownloadWithProxy(String tweetUrl, String proxyUrl) {
        return new OpenCliCommandBuilder()
                .withModule(MODULE_TWITTER)
                .withSubCommand(SUBCOMMAND_DOWNLOAD)
                .withOption(PARAM_TWEET_URL, tweetUrl)
                .withProxy(proxyUrl);
    }

    /**
     * 构建 Twitter 下载命令（使用默认代理）
     *
     * @param tweetUrl Twitter 推文 URL
     * @return 命令构建器
     */
    public static OpenCliCommandBuilder buildTwitterDownloadWithDefaultProxy(String tweetUrl) {
        return new OpenCliCommandBuilder()
                .withModule(MODULE_TWITTER)
                .withSubCommand(SUBCOMMAND_DOWNLOAD)
                .withOption(PARAM_TWEET_URL, tweetUrl)
                .withDefaultProxy();
    }

    /**
     * 根据配置自动应用代理设置
     *
     * @param properties OpenCLI 配置属性
     * @return 当前构建器
     */
    public OpenCliCommandBuilder applyProxyFromConfig(OpenCliProperties properties) {
        if (properties != null && properties.getProxy() != null && properties.getProxy().isEnabled()) {
            String proxyUrl = properties.getProxyUrl();
            if (proxyUrl != null && !proxyUrl.isEmpty()) {
                this.proxyUrl = proxyUrl;
            }
        }
        return this;
    }

    /**
     * 创建带有配置代理的 ProcessBuilder
     *
     * @param properties OpenCLI 配置属性
     * @return 配置好的 ProcessBuilder
     */
    public ProcessBuilder createProcessBuilder(OpenCliProperties properties) {
        // 如果当前没有设置代理，但配置中启用了代理，则应用配置中的代理
        if ((this.proxyUrl == null || this.proxyUrl.isEmpty()) && properties != null) {
            applyProxyFromConfig(properties);
        }
        return createProcessBuilder();
    }

    // ==================== yt-dlp 方法 ====================

    /**
     * 获取有效的 yt-dlp 可执行路径
     * <p>
     * Windows 系统返回 null（使用默认命令名 yt-dlp，依赖 PATH）
     * Mac/Linux 返回配置的 yt-dlp 绝对路径
     * </p>
     *
     * @param properties OpenCLI 配置属性
     * @return Windows 返回 null，其他系统返回配置的路径
     */
    private static String getEffectiveYtDlpPath(OpenCliProperties properties) {
        if (properties == null) {
            return null;
        }
        String osName = System.getProperty("os.name").toLowerCase();
        boolean isWindows = osName.contains("win");
        // Windows 不需要使用 ytDlpPath，直接返回 null（使用 PATH 中的 yt-dlp）
        if (isWindows) {
            return null;
        }
        return properties.getYtDlpPath();
    }

    /**
     * 构建 yt-dlp 下载命令
     * <p>
     * Windows 系统：直接使用 yt-dlp
     * 其他系统（Mac/Linux）：使用 cookies-from-browser chrome 选项
     * </p>
     *
     * @param videoUrl 视频 URL
     * @return 命令构建器
     */
    public static OpenCliCommandBuilder buildYtDlpDownload(String videoUrl) {
        return buildYtDlpDownload(videoUrl, null, null);
    }

    /**
     * 构建 yt-dlp 下载命令（使用配置）
     *
     * @param videoUrl 视频 URL
     * @param properties OpenCLI 配置属性
     * @return 命令构建器
     */
    public static OpenCliCommandBuilder buildYtDlpDownload(String videoUrl, OpenCliProperties properties) {
        return buildYtDlpDownload(videoUrl, null, properties);
    }

    /**
     * 构建 yt-dlp 下载命令（带输出目录和配置）
     * <p>
     * Windows 系统：直接使用 yt-dlp（从 PATH 查找）
     * 其他系统（Mac/Linux）：使用配置的 yt-dlp 绝对路径
     * </p>
     * <p>
     * 强制下载为 MP4 格式，避免 webm 格式不被文件服务器支持
     * </p>
     *
     * @param videoUrl 视频 URL
     * @param outputPath 输出目录
     * @param properties OpenCLI 配置属性，用于获取 yt-dlp 路径
     * @return 命令构建器
     */
    public static OpenCliCommandBuilder buildYtDlpDownload(String videoUrl, String outputPath, OpenCliProperties properties) {
        String ytDlpCommand = getEffectiveYtDlpPath(properties);
        // 如果未配置路径（Windows 或未配置），使用默认命令名
        if (ytDlpCommand == null || ytDlpCommand.isEmpty()) {
            ytDlpCommand = CMD_YT_DLP;
        }
        OpenCliCommandBuilder builder = new OpenCliCommandBuilder(false)
                .withModule(ytDlpCommand);

        // 非 Windows 系统使用 cookies-from-browser chrome 选项
        String osName = System.getProperty("os.name").toLowerCase();
        if (!osName.contains("win")) {
            builder.withOption("--cookies-from-browser", "chrome");
        }

        // 强制指定格式为 MP4，避免 webm 格式不被文件服务器支持
        builder.withOption("--format", "bestvideo[ext=mp4]+bestaudio[ext=m4a]/best[ext=mp4]/best");
        builder.withOption("--merge-output-format", "mp4");
        builder.withOption("--remux-video", "mp4");

        // yt-dlp 使用 -o 指定输出模板，使用 youtube_时间戳.mp4 格式
        if (outputPath != null && !outputPath.isEmpty()) {
            // 确保路径以 / 或 \ 结尾
            String path = outputPath.endsWith("/") || outputPath.endsWith("\\")
                    ? outputPath
                    : outputPath + "/";
            // 生成时间戳格式的文件名：youtube_年月日_时分秒_毫秒.mp4
            String timestamp = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new java.util.Date());
            builder.withOption("-o", path + "youtube_" + timestamp + ".%(ext)s");
        }

        builder.withArg(videoUrl);
        return builder;
    }

    /**
     * 构建 yt-dlp 下载命令（带输出目录）
     * <p>
     * Windows 系统：直接使用 yt-dlp
     * 其他系统（Mac/Linux）：使用 cookies-from-browser chrome 选项
     * </p>
     * <p>
     * 强制下载为 MP4 格式，避免 webm 格式不被文件服务器支持
     * </p>
     *
     * @param videoUrl 视频 URL
     * @param outputPath 输出目录
     * @return 命令构建器
     * @deprecated 请使用 {@link #buildYtDlpDownload(String, String, OpenCliProperties)}
     */
    @Deprecated
    public static OpenCliCommandBuilder buildYtDlpDownload(String videoUrl, String outputPath) {
        return buildYtDlpDownload(videoUrl, outputPath, null);
    }

    /**
     * 构建 yt-dlp 下载命令（带代理和输出目录）
     *
     * @param videoUrl 视频 URL
     * @param outputPath 输出目录
     * @param proxyUrl 代理地址
     * @return 命令构建器
     */
    public static OpenCliCommandBuilder buildYtDlpDownloadWithProxy(String videoUrl, String outputPath, String proxyUrl) {
        return buildYtDlpDownloadWithProxy(videoUrl, outputPath, proxyUrl, null);
    }

    /**
     * 构建 yt-dlp 下载命令（带代理、输出目录和配置）
     *
     * @param videoUrl 视频 URL
     * @param outputPath 输出目录
     * @param proxyUrl 代理地址
     * @param properties OpenCLI 配置属性
     * @return 命令构建器
     */
    public static OpenCliCommandBuilder buildYtDlpDownloadWithProxy(String videoUrl, String outputPath, String proxyUrl, OpenCliProperties properties) {
        OpenCliCommandBuilder builder = buildYtDlpDownload(videoUrl, outputPath, properties);
        if (proxyUrl != null && !proxyUrl.isEmpty()) {
            builder.withProxy(proxyUrl);  // 使用环境变量方式设置代理
        }
        return builder;
    }

    /**
     * 构建 yt-dlp 下载命令（使用配置代理）
     *
     * @param videoUrl 视频 URL
     * @param outputPath 输出目录
     * @param properties OpenCLI 配置属性
     * @return 命令构建器
     */
    public static OpenCliCommandBuilder buildYtDlpDownloadWithConfigProxy(String videoUrl, String outputPath, OpenCliProperties properties) {
        OpenCliCommandBuilder builder = buildYtDlpDownload(videoUrl, outputPath, properties);
        builder.applyProxyFromConfig(properties);
        return builder;
    }
}
