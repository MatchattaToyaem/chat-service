package edu.oconnor.chatservice.service;

import tools.jackson.databind.ObjectMapper;
import edu.oconnor.chatservice.model.ChatHistoryEntry;
import edu.oconnor.chatservice.model.ChatSession;
import edu.oconnor.chatservice.model.ChatSessionRequest;
import edu.oconnor.chatservice.repository.ChatSessionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ChatSessionService {

    private final ChatSessionRepository chatSessionRepository;
    private final ObjectMapper objectMapper;

    public ChatSessionService(ChatSessionRepository chatSessionRepository, ObjectMapper objectMapper) {
        this.chatSessionRepository = chatSessionRepository;
        this.objectMapper = objectMapper;
    }

    public List<ChatSession> getAllSessions() {
        return chatSessionRepository.findAll();
    }

    public Optional<ChatSession> getSessionById(UUID id) {
        return chatSessionRepository.findById(id);
    }

    public List<ChatSession> getSessionsByUserName(String userName) {
        return chatSessionRepository.findByUserName(userName);
    }

    public ChatSession createSession(ChatSessionRequest request) {
        return chatSessionRepository.create(request);
    }

    public Optional<ChatSession> updateSession(UUID id, ChatSessionRequest request) {
        return chatSessionRepository.update(id, request);
    }

    public boolean deleteSession(UUID id) {
        return chatSessionRepository.delete(id);
    }

    public void appendChatHistory(UUID sessionId, ChatHistoryEntry entry) {
        String entryJson = objectMapper.writeValueAsString(entry);
        chatSessionRepository.appendChatHistory(sessionId, entryJson);
    }
}
