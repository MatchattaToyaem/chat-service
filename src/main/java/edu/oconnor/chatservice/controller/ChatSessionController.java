package edu.oconnor.chatservice.controller;

import edu.oconnor.chatservice.model.AnswerRatingRequest;
import edu.oconnor.chatservice.model.ChatSession;
import edu.oconnor.chatservice.model.ChatSessionRequest;
import edu.oconnor.chatservice.service.ChatSessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/chat-sessions")
public class ChatSessionController {

    private final ChatSessionService chatSessionService;

    public ChatSessionController(ChatSessionService chatSessionService) {
        this.chatSessionService = chatSessionService;
    }

    @GetMapping
    public List<ChatSession> getAllSessions() {
        return chatSessionService.getAllSessions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChatSession> getSessionById(@PathVariable UUID id) {
        return chatSessionService.getSessionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userName}")
    public List<ChatSession> getSessionsByUser(@PathVariable String userName) {
        return chatSessionService.getSessionsByUserName(userName);
    }

    @PostMapping
    public ResponseEntity<ChatSession> createSession(@RequestBody ChatSessionRequest request) {
        ChatSession created = chatSessionService.createSession(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChatSession> updateSession(
            @PathVariable UUID id,
            @RequestBody ChatSessionRequest request) {
        return chatSessionService.updateSession(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable UUID id) {
        boolean deleted = chatSessionService.deleteSession(id);
        return deleted
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @PutMapping("/answer-rating/{id}")
    public ResponseEntity<ChatSession> rateAnswer (@PathVariable UUID id, @RequestBody
    AnswerRatingRequest request) {
        return chatSessionService.rateChatAnswer(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
