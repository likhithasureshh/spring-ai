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

    @Test
    void runTests()
    {
        System.out.println(aiService.getResult("dogs"));
    }
    @Test
    void embedded()
    {
        var res=aiService.getEmbedding("Its a big text here");
        System.out.println(Arrays.toString(res));
    }
    @Test
    void store()
    {
        aiService.storeInVectorDb();
    }

    @Test
    void search()
    {
       var res= aiService.similaritySearch("what is spring boot");
       for(Document doc : res)
       {
           System.out.println(res);
       }
    }

}
