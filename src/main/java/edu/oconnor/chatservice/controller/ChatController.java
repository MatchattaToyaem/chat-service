package edu.oconnor.chatservice.controller;

import edu.oconnor.chatservice.grpc.InferenceReply;
import edu.oconnor.chatservice.model.ChatHistoryEntry;
import edu.oconnor.chatservice.model.MessageRequest;
import edu.oconnor.chatservice.model.MessageResponse;
import edu.oconnor.chatservice.model.SourceDto;
import edu.oconnor.chatservice.service.ChatSessionService;
import edu.oconnor.chatservice.service.ChatbotIntegrationService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.time.OffsetDateTime;
import java.util.List;
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
    @SendToUser("/queue/message")
    public MessageResponse sendMessage(@Payload MessageRequest messageRequest) {
        long start = System.currentTimeMillis();
        InferenceReply reply = chatbotService.askChatbot(messageRequest.getMessage());
        long responseTimeMs = System.currentTimeMillis() - start;

        List<SourceDto> sources = reply.getSourcesList().stream().map(s -> {
            SourceDto dto = new SourceDto();
            dto.setFile(s.getFile());
            dto.setSubfolder(s.getSubfolder());
            dto.setScore(s.getScore());
            dto.setChunkId(s.getChunkId());
            return dto;
        }).toList();

        if (messageRequest.getSessionId() != null && !messageRequest.getSessionId().isBlank()) {
            ChatHistoryEntry entry = new ChatHistoryEntry();
            entry.setQuestion(messageRequest.getMessage());
            entry.setAnswer(reply.getResult());
            entry.setConfidence(reply.getConfidence());
            entry.setModel(reply.getModel());
            entry.setSources(sources);
            entry.setResponseTime(responseTimeMs + "ms");
            entry.setResponseDate(OffsetDateTime.now().toString());
            chatSessionService.appendChatHistory(UUID.fromString(messageRequest.getSessionId()), entry);
        }

        MessageResponse response = new MessageResponse();
        response.setSender(reply.getModel().isBlank() ? "AI" : reply.getModel());
        response.setMessage(reply.getResult());
        response.setConfidence(reply.getConfidence());
        response.setModel(reply.getModel());
        response.setSources(sources);
        return response;
    }
}
