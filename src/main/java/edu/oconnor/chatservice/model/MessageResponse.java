package edu.oconnor.chatservice.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MessageResponse {
    private String message;
    private String sender;
    private float confidence;
    private String model;
    private List<SourceDto> sources;
}
