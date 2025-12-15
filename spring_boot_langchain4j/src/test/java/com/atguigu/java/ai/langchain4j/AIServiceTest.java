package com.atguigu.java.ai.langchain4j;

import com.atguigu.java.ai.langchain4j.assistant.MemoryChatAssistant;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author 黄天赐
 * @project com.atguigu.java.ai.langchain4j
 */
@SpringBootTest
public class AIServiceTest {
    @Resource
    private MemoryChatAssistant memoryChatAssistant;
    @Test
    public void testChat() {
    //调用service的接口
        String answer = memoryChatAssistant.chat("你是谁");
        System.out.println(answer);
    }
}
