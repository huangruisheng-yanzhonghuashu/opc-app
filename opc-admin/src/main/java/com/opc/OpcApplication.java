package com.opc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 启动程序
 *
 * @author opc
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class OpcApplication
{
    // 颜色常量
    private static final String RESET = "\033[0m";
    private static final String GREEN = "\033[32m";
    private static final String CYAN = "\033[36m";
    private static final String BLUE = "\033[34m";
    private static final String YELLOW = "\033[33m";

    public static void main(String[] args) throws UnknownHostException
    {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        var app = SpringApplication.run(OpcApplication.class, args);
        Environment env = app.getEnvironment();

        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port", "8080");
        String contextPath = env.getProperty("server.servlet.context-path", "");
        String baseUrl = "http://" + ip + ":" + port + contextPath;

        System.out.println("(♥◠‿◠)ﾉﾞ  OPC启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                "  OOOOO   PPPPP   CCCCC \n" +
                " O     O  P    P C     \n" +
                " O     O  PPPPP  C     \n" +
                " O     O  P      C     \n" +
                "  OOOOO   P       CCCCC \n");

        // 打印接口文档信息
        System.out.println(GREEN + "╔══════════════════════════════════════════════════════════════════╗" + RESET);
        System.out.println(GREEN + "║  " + YELLOW + "接口文档访问地址" + GREEN + "                                                  ║" + RESET);
        System.out.println(GREEN + "╠══════════════════════════════════════════════════════════════════╣" + RESET);
        System.out.println(GREEN + "║  " + CYAN + "接口文档: " + BLUE + padRight(baseUrl + "doc.html", 54) + GREEN + " ║" + RESET);
        System.out.println(GREEN + "║  " + CYAN + "Swagger:  " + BLUE + padRight(baseUrl + "swagger-ui.html", 54) + GREEN + " ║" + RESET);
        System.out.println(GREEN + "╚══════════════════════════════════════════════════════════════════╝" + RESET);
    }

    private static String padRight(String str, int length) {
        if (str.length() >= length) {
            return str.substring(0, length);
        }
        StringBuilder sb = new StringBuilder(str);
        while (sb.length() < length) {
            sb.append(" ");
        }
        return sb.toString();
    }
}
