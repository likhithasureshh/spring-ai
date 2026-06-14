package com.spring_ai.learn_spring_ai.service;

import com.spring_ai.learn_spring_ai.advisors.TokenUsageAdvisor;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.VectorStoreChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RAGService {

    private final ChatClient chatClient;
    private final EmbeddingModel embeddingModel;
    private final VectorStore vectorStore;
    private final ChatMemory chatMemory;
    @Value("classpath:faq.pdf")
    Resource resource;

    public String askAIWithAdvisors(String prompt,String userId)
    {
        return chatClient.prompt()
                .system("""
                        You are an AI assistant helping the users.
                        You're name is CODY and greet users with your name and user name if u know the name.
                        please reply in the friendly conversational tone.
                        """)
                .user(prompt)
                .advisors(
                        //new SafeGuardAdvisor(List.of("Politics","Gaming")),
                        MessageChatMemoryAdvisor.builder(chatMemory)
                                        .conversationId(userId)
                                                .build(),
                        VectorStoreChatMemoryAdvisor.builder(vectorStore)
                                .conversationId(userId)
                                .defaultTopK(4)
                                .build(),
                        QuestionAnswerAdvisor.builder(vectorStore)
                                .searchRequest(SearchRequest.builder()
                                        .filterExpression("file_name=='faq.pdf'")
                                        .build())
                                .build(),
                        new TokenUsageAdvisor()

                )
                .call()
                .content();
    }


    public String askAI(String prompt)
    {
        String systemPrompt = """
                You are an AI ASSISTANT helping a developer.
                please follow the below RULES
                RULES:
                You ONLY need to provide answers from the context
                You can rephrase,summarize and extract from the context
                If more than one context is relevant you can combine them
                Dont use your own facts to answer to the questions
                if the question is out of context,just say I dont know.
                please reply in the friendly and conversational tone.
                Context:
                {Context}
                """;

        List<Document> documents = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(prompt)
                        .topK(2)
                        .similarityThreshold(0.4)
                        .filterExpression("file_name=='faq.pdf'")
                        .build()
        );

        String context = documents.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n\n"));


        PromptTemplate promptTemplate = new PromptTemplate(systemPrompt);
        String sysPrompt =promptTemplate.render(Map.of("Context",context));

        return chatClient.prompt()
                .system(sysPrompt)
                .user(prompt)
                .advisors(
                        new SimpleLoggerAdvisor()
                )
                .call()
                .content();


    }
    public void storeDataToVectorStore()
    {
        PagePdfDocumentReader reader = new PagePdfDocumentReader(resource);
        List<Document> documents = reader.get();
        TokenTextSplitter tokenTextSplitter = TokenTextSplitter.builder()
                .withChunkSize(200)
                .build();
        List<Document> toBeSaved = tokenTextSplitter.apply(documents);
        vectorStore.add(toBeSaved);
    }
}
