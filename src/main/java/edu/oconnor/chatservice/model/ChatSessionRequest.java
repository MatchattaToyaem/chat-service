package edu.oconnor.chatservice.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatSessionRequest {

    private String chatName;
    private String userName;
    private String chatHistory;
    private String createdBy;
    private String status;
}
