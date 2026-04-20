package edu.oconnor.chatservice.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageResponse {
    private String message;
    private String sender;
}
