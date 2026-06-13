package com.spring_ai.learn_spring_ai.service;

import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
public class AIServiceTests {
    @Autowired
    private AIService aiService;
    @Autowired
    private RAGService ragService;


    @Test
    void store()
    {
        ragService.storeDataToVectorStore();
    }

    @Test
    void search()
    {
        System.out.println(ragService.askAI("Not able to connect to discord"));
    }



}
