package edu.oconnor.chatservice.service;

import com.nimbusds.jose.shaded.gson.JsonObject;
import edu.oconnor.chatservice.model.AnswerRatingRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import edu.oconnor.chatservice.model.ChatHistoryEntry;
import edu.oconnor.chatservice.model.ChatSession;
import edu.oconnor.chatservice.model.ChatSessionRequest;
import edu.oconnor.chatservice.repository.ChatSessionRepository;
import org.springframework.stereotype.Service;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
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

    public Optional<ChatSession> rateChatAnswer(UUID sessionId, AnswerRatingRequest request) {
        UUID answerId = request.getAnswerId();
        Integer rating = request.getRating();
        Optional<ChatSession> chatSession = chatSessionRepository.findById(sessionId);
        boolean updated = false;
        if(chatSession.isPresent()) {
            if (!ObjectUtils.isEmpty(answerId)) {
                String chatHistory = chatSession.get().getChatHistory();
                ArrayNode chatList = (ArrayNode) objectMapper.readTree(chatHistory);
                for (JsonNode chatBox : chatList) {
                    if(chatBox.has("answerId") && answerId.toString().
                            equals(chatBox.get("answerId").asString())) {
                        ObjectNode objectNode = (ObjectNode) chatBox;
                        objectNode.put("rating", rating);
                        updated = true;
                    }
                }
                String updatedChatHistory = objectMapper.writeValueAsString(chatList);
                if (updated){
                    return chatSessionRepository.updateChatHistory(sessionId, updatedChatHistory);
                }

            }
        }
        return chatSession;
    }
}
