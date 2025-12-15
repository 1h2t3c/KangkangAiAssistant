package com.atguigu.java.ai.langchain4j.assistant;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;
import reactor.core.publisher.Flux;

import static dev.langchain4j.service.spring.AiServiceWiringMode.EXPLICIT;

@AiService(
        //! 设置wiringMode为EXPLICIT，表示使用自定义的配置
        wiringMode = EXPLICIT,
        //! 配置调用的大模型
        streamingChatModel = "qwenStreamingChatModel",
        //! 配置自定义记忆存储方式
        chatMemoryProvider = "chatMemoryProviderKangkang",
        //! ai大模型配置预约挂号工具
        tools = "appointmentTools",
        //! ai大模型配置检索外部知识库的能力
        contentRetriever = "contentRetrieverKangkang")
public interface KangkangAgent {
    @SystemMessage(fromResource = "promptTemplate/kangkang-prompt-template.txt")
    Flux<String> chat(@MemoryId Long memoryId, @UserMessage String userMessage);
} 
