package edu.oconnor.chatservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ChatHistoryEntry {

    private String answerId;
    private String question;
    private String answer;
    private float confidence;
    private String model;
    private List<SourceDto> sources;

    @JsonProperty("response_time")
    private String responseTime;

    @JsonProperty("response_date")
    private String responseDate;
}
