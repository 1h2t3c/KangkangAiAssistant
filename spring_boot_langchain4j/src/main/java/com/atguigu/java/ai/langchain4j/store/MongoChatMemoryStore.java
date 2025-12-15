package com.atguigu.java.ai.langchain4j.store;

import com.atguigu.java.ai.langchain4j.entity.ChatMessages;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageDeserializer;
import dev.langchain4j.data.message.ChatMessageSerializer;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
/*
* TODO 自定义记忆存储方式(使用MongoDb来实现存储)，实现ChatMemoryStore接口
*  TODO 类似于mybatis中的mapper类
* */
@Component
public class MongoChatMemoryStore implements ChatMemoryStore {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    //! 首先会调用getMessages方法，获取当前memoryId对应的消息列表
    public List<ChatMessage> getMessages(Object memoryId) {
        Criteria criteria = Criteria.where("memoryId").is(memoryId);
        Query query = new Query(criteria);
        ChatMessages chatMessages = mongoTemplate.findOne(query, ChatMessages.class);
        if (chatMessages == null) return new LinkedList<>();
        //? 将查询到的历史记录进行反序列化，将json字符串转换成ChatMessage对象放入消息列表当中、
        return ChatMessageDeserializer.messagesFromJson(chatMessages.getContent());
    }

    /*
    * TODO 然后处理当前对话：并将查询到的历史记录和当前消息发给ai，然后ai生成一个结果,
    *  最终将历史记录+当前消息+ai生成的结果放入到List<ChatMessage>当中
    * */
    @Override
    //! 处理完对话之后，调用updateMessage方法，将历史记录+当前消息+ai生成的结果保存在MongoDb当中
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        //创建一个查询条件对象
        Criteria criteria = Criteria.where("memoryId").is(memoryId);
        //根据条件找到对应数据对象
        Query query = new Query(criteria);
        //设置更新对象
        Update update = new Update();
        update.set("content", ChatMessageSerializer.messagesToJson(messages));
        //根据query条件能查询出文档，则修改文档；否则新增文档
        mongoTemplate.upsert(query, update, ChatMessages.class);
    }

    @Override
    public void deleteMessages(Object memoryId) {
        Criteria criteria = Criteria.where("memoryId").is(memoryId);
        Query query = new Query(criteria);
        mongoTemplate.remove(query, ChatMessages.class);
    }
}