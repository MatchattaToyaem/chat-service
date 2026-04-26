package edu.oconnor.chatservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SourceDto {
    private String file;
    private String subfolder;
    private float score;

    @JsonProperty("chunk_id")
    private String chunkId;

    @JsonProperty("document_path")
    private String documentPath;
}
