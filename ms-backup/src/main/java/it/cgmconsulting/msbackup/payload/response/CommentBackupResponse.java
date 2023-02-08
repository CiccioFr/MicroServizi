package it.cgmconsulting.msbackup.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentBackupResponse {

    private long id;
    private String comment;
    private boolean censored;
    private LocalDateTime createdAt;
    private long author;
    private long post;
}
