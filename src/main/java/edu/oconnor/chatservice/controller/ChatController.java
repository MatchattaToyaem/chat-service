package edu.oconnor.chatservice.controller;

import edu.oconnor.chatservice.model.ChatHistoryEntry;
import edu.oconnor.chatservice.model.MessageRequest;
import edu.oconnor.chatservice.model.MessageResponse;
import edu.oconnor.chatservice.service.ChatSessionService;
import edu.oconnor.chatservice.service.ChatbotIntegrationService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.time.OffsetDateTime;
import java.util.UUID;

@Controller
public class ChatController {

    private final ChatbotIntegrationService chatbotService;
    private final ChatSessionService chatSessionService;

    public ChatController(ChatbotIntegrationService chatbotService, ChatSessionService chatSessionService) {
        this.chatbotService = chatbotService;
        this.chatSessionService = chatSessionService;
    }

    @MessageMapping("/chat")
    @PreAuthorize("isAuthenticated()")
    @SendToUser("/queue/message")   // sends only to the requesting user's private queue
    public MessageResponse sendMessage(@Payload MessageRequest messageRequest) {
        long start = System.currentTimeMillis();
        String answer = chatbotService.askChatbot(messageRequest.getMessage());
        long responseTimeMs = System.currentTimeMillis() - start;

        if (messageRequest.getSessionId() != null && !messageRequest.getSessionId().isBlank()) {
            ChatHistoryEntry entry = new ChatHistoryEntry();
            entry.setQuestion(messageRequest.getMessage());
            entry.setAnswer(answer);
            entry.setDocumentReferenceId("");
            entry.setResponseTime(responseTimeMs + "ms");
            entry.setResponseDate(OffsetDateTime.now().toString());
            chatSessionService.appendChatHistory(UUID.fromString(messageRequest.getSessionId()), entry);
        }

        MessageResponse response = new MessageResponse();
        response.setSender("Mistral");
        response.setMessage(answer);
        return response;
    }
}
