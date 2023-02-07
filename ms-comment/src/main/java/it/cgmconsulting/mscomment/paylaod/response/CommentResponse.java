package it.cgmconsulting.mscomment.paylaod.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
    private long id;
    private String comment;
    private LocalDateTime createdAt;
    private long userId;
    private String username;
    private boolean censored;

    public CommentResponse(long id, String comment, LocalDateTime createdAt, long userId, boolean censored) {
        this.id = id;
        this.comment = comment;
        this.createdAt = createdAt;
        this.userId = userId;
        this.censored = censored;
    }
}
