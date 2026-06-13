package com.spring_ai.learn_spring_ai.service;

import com.spring_ai.learn_spring_ai.dto.Joke;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor

public class AIService {
    private final ChatClient chatClient;
    private final EmbeddingModel embeddingModel;
    private final VectorStore vectorStore;


    public float[] getEmbedding(String text)
    {
        return embeddingModel.embed(text);
    }

    public void storeInVectorDb()
    {
        Document doc1 = new Document(
                "Spring Boot is a Java framework used to build standalone, production-ready applications...",
                Map.of(
                        "id", "doc_001",
                        "title", "Introduction to Spring Boot",
                        "category", "Java"
                )
        );

        Document doc2 = new Document(
                "A vector database stores data as numerical embeddings...",
                Map.of(
                        "id", "doc_002",
                        "title", "Understanding Vector Databases",
                        "category", "AI"
                )
        );

        Document doc3 = new Document(
                "Retrieval-Augmented Generation combines information retrieval with large language models...",
                Map.of(
                        "id", "doc_003",
                        "title", "Introduction to RAG",
                        "category", "Generative AI"
                )
        );
        vectorStore.add(List.of(doc1,doc2,doc3));

    }

    public List<Document> similaritySearch(String text)
    {
        return vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(text)
                        .similarityThreshold(0.7)
                        .topK(1)
                        .build()
        );
    }




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
