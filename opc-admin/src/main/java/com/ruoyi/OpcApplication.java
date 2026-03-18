package com.ruoyi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 * 
 * @author ruoyi
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class OpcApplication
{
    public static void main(String[] args)
    {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(OpcApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  OPC启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                "  OOOOO   PPPPP   CCCCC \n" +
                " O     O  P    P C     \n" +
                " O     O  PPPPP  C     \n" +
                " O     O  P      C     \n" +
                "  OOOOO   P       CCCCC \n");
    }
}
