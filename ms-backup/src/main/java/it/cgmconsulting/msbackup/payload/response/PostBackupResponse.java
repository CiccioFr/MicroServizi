package it.cgmconsulting.msbackup.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PostBackupResponse {

    private long id;
    private String title;
    private String overview;
    private String content;
    private boolean published;
    private String image;
    private long author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
