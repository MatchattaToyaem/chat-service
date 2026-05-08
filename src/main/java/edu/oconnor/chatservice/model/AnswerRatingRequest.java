package edu.oconnor.chatservice.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AnswerRatingRequest {
    private UUID answerId;
    private Integer rating;
}
