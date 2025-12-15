package com.atguigu.java.ai.langchain4j;

import com.atguigu.java.ai.langchain4j.entity.ChatMessages;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

@SpringBootTest
public class MongoCrudTest {
    //类似于redis中的stringRedisTemplate,是用来操作数据库的
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 插入文档
     */
    /*@Test
    public void testInsert() {
        mongoTemplate.insert(new ChatMessages(1L, "聊天记录"));
    }*/

    /**
     * 插入文档
     */
    @Test
    public void testInsert2() {
        ChatMessages chatMessages = new ChatMessages();
        chatMessages.setContent("聊天记录列表");
        mongoTemplate.insert(chatMessages);
    }

    /**
     * 根据id查询文档
     */
    @Test
    public void testFindById() {
        //需要指定映射的实体类
        ChatMessages chatMessages = mongoTemplate.findById("686794613f68a268a1715056",
                ChatMessages.class);
        System.out.println(chatMessages);
    }

    /**
     * 修改文档
     */
    @Test
    public void testUpdate() {
        /*
        * TODO 1. 创建查询条件
        * */
        //MongoDB中使用Criteria来创建查询条件
        //mybatis-plus使用的是queryWrapper或者updateWrapper
        Criteria criteria = Criteria.where("_id").is("686794613f68a268a1715056");
        /*
        * TODO 2. 创建更新(查询)对象
        * */
        //根据条件创建更新对象或者查询对象
        Query query = new Query(criteria);
        Update update = new Update();
        update.set("content", "新的聊天记录列表");
        /*
        * TODO 3. 执行修改或新增
        * */
        mongoTemplate.upsert(query, update, ChatMessages.class);
    }

    /**
     * 新增或修改文档
     */
    @Test
    public void testUpdate2() {
        Criteria criteria = Criteria.where("_id").is("100");
        Query query = new Query(criteria);
        Update update = new Update();
        update.set("content", "新的聊天记录列表");
//修改或新增
        mongoTemplate.upsert(query, update, ChatMessages.class);
    }

    /**
     * 删除文档
     */
    @Test
    public void testDelete() {
        Criteria criteria = Criteria.where("_id").is("100");
        Query query = new Query(criteria);
        mongoTemplate.remove(query, ChatMessages.class);
    }

}