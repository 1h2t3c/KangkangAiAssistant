package com.atguigu.java.ai.langchain4j;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;

/**
 * @author 黄天赐
 * @project com.xzit
 */
@SpringBootTest
public class TestGenerator {
    @Test
    public void testGenerator() {
        String projectPath = System.getProperty("user.dir");

        FastAutoGenerator.create("jdbc:mysql://localhost:3306/yiliaoxiaozhi?serverTimezone=GMT%2B8",
                        "root", "123456")
                .globalConfig(builder -> {
                    builder.outputDir(projectPath + "/src/main/java")  // 设置代码输出目录
                            .fileOverride();  // 覆盖已有文件
                })
                .packageConfig(builder -> {
                    builder.parent("com.atguigu.java.ai.langchain4j")  // 设置父包名
                            .pathInfo(Collections.singletonMap(
                                    OutputFile.xml, projectPath + "/src/main/resources/mapper"
                            ));  // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("appointment")
                            .entityBuilder().enableLombok().enableChainModel()
                            .controllerBuilder().enableRestStyle();
                })
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
