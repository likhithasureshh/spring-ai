package com.spring_ai.learn_spring_ai.controller;

import com.spring_ai.learn_spring_ai.tools.FlightBookingTool;
import com.spring_ai.learn_spring_ai.tools.TravellingTools;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private final ChatClient chatClient;
    private final TravellingTools travellingTools;
    private final FlightBookingTool flightBookingTool;
    private final ChatMemory chatMemory;
    @PostMapping("/chat")
    public String chat(@RequestBody String prompt, @RequestParam String userId)
    {

        String systemPrompt = String.format("""
                                        Act as the Flight Booking assistant and help the
                                        user to book the flight with the userId:%s
                                        please use the same userId to save the same in the database
                """,userId);
        return chatClient.prompt()
                .system(systemPrompt)
                .user(prompt)
                .advisors(
                        MessageChatMemoryAdvisor.builder(chatMemory)
                                .conversationId(userId)
                                .build()
                )
                .tools(travellingTools,flightBookingTool)
                .call()
                .content();
    }
}
