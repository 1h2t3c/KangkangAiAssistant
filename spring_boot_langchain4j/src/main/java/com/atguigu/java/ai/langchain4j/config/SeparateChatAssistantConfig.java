package com.atguigu.java.ai.langchain4j.config;

import com.atguigu.java.ai.langchain4j.store.MongoChatMemoryStore;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SeparateChatAssistantConfig {
    @Resource
    private MongoChatMemoryStore mongoChatMemoryStore;
    @Bean
    ChatMemoryProvider chatMemoryProvider() {
        //函数式编程,有参有返
        return memoryId -> MessageWindowChatMemory.builder()
                .id(memoryId)
                .maxMessages(10)
                //选择聊天记忆底层存储
                //.chatMemoryStore(new InMemoryChatMemoryStore())
                .chatMemoryStore(mongoChatMemoryStore)
                .build();
    }
}