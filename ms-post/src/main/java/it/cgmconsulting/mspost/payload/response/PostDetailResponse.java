package it.cgmconsulting.mspost.payload.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Struttura Response di un Post arricchito di List di commenti
 */
@Getter
@Setter
@NoArgsConstructor
public class PostDetailResponse extends PostResponse {

    private List<CommentResponse> comments;
    private double average;

    public PostDetailResponse(long id, String title, String overview, long author, String authorUsername, List<CommentResponse> comments, double average) {
        super(id, title, overview, author, authorUsername);
        this.comments = comments;
        this.average = average;
    }
}
