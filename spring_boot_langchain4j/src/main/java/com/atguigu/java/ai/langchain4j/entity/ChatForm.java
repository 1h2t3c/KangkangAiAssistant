package com.atguigu.java.ai.langchain4j.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "接收用户消息体")
public class ChatForm {
    @Schema(description = "对话id")
    private Long memoryId;
    @Schema(description = "用户问题")
    private String message;
}