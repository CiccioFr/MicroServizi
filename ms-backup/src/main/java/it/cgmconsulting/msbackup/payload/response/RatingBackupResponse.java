package it.cgmconsulting.msbackup.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class RatingBackupResponse {

    private long userId;
    private long postId;
    private byte rate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
