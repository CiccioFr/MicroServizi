package it.cgmconsulting.mspost.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Struttura di un Post per una Response
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {

    private long id;
    private String title;
    private String overview; // per lista di Post 'overview' è valorizzato con overview, per il PostDetail con content
    private long author;
    private String authorUsername;

    public PostResponse(long id, String title, String overview, long author) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.author = author;
    }
}
