package edu.oconnor.chatservice.repository;

import edu.oconnor.chatservice.model.ChatSession;
import edu.oconnor.chatservice.model.ChatSessionRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ChatSessionRepository {

    private final JdbcTemplate jdbcTemplate;

    public ChatSessionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<ChatSession> rowMapper = (rs, rowNum) -> {
        ChatSession session = new ChatSession();
        session.setId(UUID.fromString(rs.getString("id")));
        session.setChatName(rs.getString("chat_name"));
        session.setUserName(rs.getString("user_name"));
        session.setChatHistory(rs.getString("chat_history"));
        session.setCreatedBy(rs.getString("created_by"));
        session.setLastAccess(rs.getObject("last_access", OffsetDateTime.class));
        session.setStatus(rs.getString("status"));
        return session;
    };

    public List<ChatSession> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM chat_sessions ORDER BY last_access DESC",
                rowMapper);
    }

    public Optional<ChatSession> findById(UUID id) {
        List<ChatSession> results = jdbcTemplate.query(
                "SELECT * FROM chat_sessions WHERE id = ?",
                rowMapper, id);
        return results.stream().findFirst();
    }

    public List<ChatSession> findByUserName(String userName) {
        return jdbcTemplate.query(
                "SELECT * FROM chat_sessions WHERE user_name = ? ORDER BY last_access DESC",
                rowMapper, userName);
    }

    public ChatSession create(ChatSessionRequest request) {
        String chatHistory = request.getChatHistory() != null ? request.getChatHistory() : "[]";
        String status = request.getStatus() != null ? request.getStatus() : "active";

        UUID id = jdbcTemplate.queryForObject(
                "INSERT INTO chat_sessions (chat_name, user_name, chat_history, created_by, status) " +
                "VALUES (?, ?, ?::jsonb, ?, ?) RETURNING id",
                UUID.class,
                request.getChatName(),
                request.getUserName(),
                chatHistory,
                request.getCreatedBy(),
                status);

        return findById(id).orElseThrow();
    }

    public Optional<ChatSession> updateChatHistory(UUID id, String chatHistory) {
        int rows = jdbcTemplate.update("UPDATE chat_sessions SET chat_history = ?::jsonb WHERE id = ?", chatHistory, id);
        return rows > 0 ? findById(id) : Optional.empty();
    }
    public Optional<ChatSession> update(UUID id, ChatSessionRequest request) {
        String chatHistory = request.getChatHistory() != null ? request.getChatHistory() : "[]";
        String status = request.getStatus() != null ? request.getStatus() : "active";

        int rows = jdbcTemplate.update(
                "UPDATE chat_sessions SET chat_name = ?, user_name = ?, chat_history = ?::jsonb, " +
                "created_by = ?, status = ?, last_access = CURRENT_TIMESTAMP WHERE id = ?",
                request.getChatName(),
                request.getUserName(),
                chatHistory,
                request.getCreatedBy(),
                status,
                id);

        return rows > 0 ? findById(id) : Optional.empty();
    }

    public boolean delete(UUID id) {
        int rows = jdbcTemplate.update("DELETE FROM chat_sessions WHERE id = ?", id);
        return rows > 0;
    }

    public void appendChatHistory(UUID sessionId, String entryJson) {
        jdbcTemplate.update(
                "UPDATE chat_sessions SET chat_history = chat_history || ?::jsonb, " +
                "last_access = CURRENT_TIMESTAMP WHERE id = ?",
                "[" + entryJson + "]",
                sessionId);
    }
}
