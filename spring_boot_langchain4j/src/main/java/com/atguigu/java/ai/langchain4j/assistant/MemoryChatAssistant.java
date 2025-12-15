package com.atguigu.java.ai.langchain4j.assistant;

import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;

import static dev.langchain4j.service.spring.AiServiceWiringMode.EXPLICIT;

/**
 * @author 黄天赐
 * @project com.atguigu.java.ai.langchain4j.assistant
 */
@AiService(wiringMode = EXPLICIT,
        chatModel = "qwenChatModel",
        chatMemory = "chatMemory")
/*
* TODO 创建assistant来调用大语言模型
*  智能体
* */
public interface MemoryChatAssistant {
    //接收用户的输入，返回大模型的输出结果
    @UserMessage("你是我的好朋友，请用上海话回答问题，并且添加一些表情符号。 {{m}}") //{{it}}表示这里
    String chat(@V("m")String userMessage);
}
