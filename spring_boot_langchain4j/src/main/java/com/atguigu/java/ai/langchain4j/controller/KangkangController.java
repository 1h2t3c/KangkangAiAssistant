package com.atguigu.java.ai.langchain4j.controller;

import com.atguigu.java.ai.langchain4j.assistant.KangkangAgent;
import com.atguigu.java.ai.langchain4j.entity.ChatForm;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Tag(name = "协和康康")
@RestController
@RequestMapping("/kangkang")
public class KangkangController {
    @Autowired
    private KangkangAgent kangkangAgent;

    @Operation(summary = "对话")
    @PostMapping(value = "/chat", produces = "text/stream;charset=utf-8")
    public Flux<String> chat(@RequestBody ChatForm chatForm) {
        return kangkangAgent.chat(chatForm.getMemoryId(), chatForm.getMessage());
    }

}
