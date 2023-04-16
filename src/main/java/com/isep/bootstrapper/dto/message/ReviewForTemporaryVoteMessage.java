package com.isep.bootstrapper.dto.message;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReviewForTemporaryVoteMessage {
    
    private UUID reviewId;
    private UUID temporaryVoteId;
    
}