package com.atguigu.java.ai.langchain4j.assistant;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;

import static dev.langchain4j.service.spring.AiServiceWiringMode.EXPLICIT;

@AiService(
        wiringMode = EXPLICIT,
        chatModel = "qwenChatModel",
        //* 设置记忆存储方式
        chatMemoryProvider = "chatMemoryProvider",
        tools = "calculatorTools" //配置tools
)
public interface SeparateChatAssistant {
    /**
     * 分离聊天记录
     *
     * @param memoryId    聊天id
     * @param userMessage 用户消息
     * @return
     */
    //! 给ai设置身份
    @SystemMessage(fromResource = "promptTemplate/my-prompt-template.txt")
    String chat(@MemoryId int memoryId, @UserMessage String userMessage);

    //! 用户传入参数让系统给ai设置身份
    @SystemMessage(fromResource = "my-prompt-template3.txt")
    String chat3(
            @MemoryId int memoryId,
            @UserMessage String userMessage,
            @V("username") String username,
            @V("age") int age
    );
}