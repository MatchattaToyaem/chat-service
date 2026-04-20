package edu.oconnor.chatservice.model;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
public class ChatSession {

    private UUID id;
    private String chatName;
    private String userName;
    private String chatHistory;
    private String createdBy;
    private OffsetDateTime lastAccess;
    private String status;
}
