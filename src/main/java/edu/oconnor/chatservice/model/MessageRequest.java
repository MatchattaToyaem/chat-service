package edu.oconnor.chatservice.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageRequest {
    private String sessionId;
    private String sender;
    private String message;
}
