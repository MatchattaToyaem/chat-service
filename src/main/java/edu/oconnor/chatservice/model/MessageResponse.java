package edu.oconnor.chatservice.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class MessageResponse {
    private String answerId;
    private String message;
    private String sender;
    private float confidence;
    private String model;
    private List<SourceDto> sources;
}
