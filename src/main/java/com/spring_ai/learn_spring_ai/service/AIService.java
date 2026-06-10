package com.spring_ai.learn_spring_ai.service;

import com.spring_ai.learn_spring_ai.dto.Joke;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor

public class AIService {
    private final ChatClient chatClient;



    public String getResult(String topic)
    {
        String systemPrompt = """
            Act like a sarcastic joker.Give me the poetic joke in 4 lines.
            dont touch the topics on politics
            and always give me the output based on the topic:{topic}
            """;
        PromptTemplate promptTemplate = new PromptTemplate(systemPrompt);

         String renderedText =promptTemplate.render(Map.of("topic",topic));

        var response = chatClient.prompt()
                .system(renderedText)
                .advisors(
                        new SimpleLoggerAdvisor()
                )
                .user("Provide me the joke on topic: "+topic)
                .call()
                .entity(Joke.class);
        return response.text();
    }
}
