package com.atguigu.java.ai.langchain4j;

import com.atguigu.java.ai.langchain4j.assistant.MemoryChatAssistant;
import com.atguigu.java.ai.langchain4j.assistant.SeparateChatAssistant;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ChatMemoryTest {
    @Autowired
    private MemoryChatAssistant memoryChatAssistant;
    @Test
    public void testChatMemory() {
        String answer1 = memoryChatAssistant.chat("我是李白");
        System.out.println(answer1);
        String answer2 = memoryChatAssistant.chat("那你告诉我，我是谁");
        System.out.println(answer2);
    }
    @Resource
    private SeparateChatAssistant separateChatAssistant;
    @Test
    public void testSeparateChatMemory() {
        String answer1 = separateChatAssistant.chat(1, "我是李白");
        System.out.println(answer1);
        String answer2 = separateChatAssistant.chat(1, "那你告诉我，我是谁");
        System.out.println(answer2);
        String answer3 = separateChatAssistant.chat(2, "我是谁");
        System.out.println(answer3);
    }
}