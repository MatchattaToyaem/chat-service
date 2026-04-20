package edu.oconnor.chatservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatHistoryEntry {

    private String question;
    private String answer;

    @JsonProperty("document_reference_id")
    private String documentReferenceId;

    @JsonProperty("response_time")
    private String responseTime;

    @JsonProperty("response_date")
    private String responseDate;
}
