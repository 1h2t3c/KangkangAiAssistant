package com.atguigu.java.ai.langchain4j;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.community.model.dashscope.WanxImageModel;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.output.Response;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LLMTest {

    @Test
    void contextLoads() {

    }
    @Test
    void testGPTDemo(){
        //引入大语言模型
        OpenAiChatModel model = OpenAiChatModel.builder()
                .baseUrl("http://langchain4j.dev/demo/openai/v1")
                .apiKey("demo")
                .modelName("gpt-4o-mini")
                .build();
        String answer = model.chat("您好,你是谁啊");
        System.out.println(answer);
    }
    //都是langchain4j来选择调用哪些大模型
    @Resource
    /*
     *TODO 1.测试openai的远程大模型
     * */
    private OpenAiChatModel openAiChatModel;
    //private ChatLanguageModel chatLanguageModel;
    @Test
    void testSpringBoot(){
        String answer = openAiChatModel.chat("您好,你是谁啊");
        System.out.println(answer);
    }
    @Resource
    /*
    * TODO 2.测试本地大模型
    * */
    private OllamaChatModel ollamaChatModel;
    @Test
    void testOllama(){
        String answer = ollamaChatModel.chat("您好,我是谁啊");
        System.out.println(answer);
    }
    @Resource
    /*
     *TODO 3.测试远程阿里云大模型
     * */
    private QwenChatModel qwenChatModel;
    @Test
    void testQwen(){
        String answer = qwenChatModel.chat("您好,我是谁啊");
        System.out.println(answer);
    }
    /*
    * TODO 4.测试通义万象来生成图片，没有像其他那样有配置文件，直接在这里去生成
    * */
    @Test
    public void testDashScopeWanx(){
        WanxImageModel wanxImageModel = WanxImageModel.builder()
                .modelName("wanx2.1-t2i-plus")
                .apiKey(System.getenv("DASH_SCOPE_API_KEY"))
                .build();
        Response<Image> response = wanxImageModel.generate("给我生成一只野猪");
                System.out.println(response.content().url());
    }

}
