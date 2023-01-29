package it.cgmconsulting.mspost.payload.request;

import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class PostRequest {

    @NotBlank @Size(min=3, max=100)
    private String title;

    @NotBlank @Size(min=20, max=255)
    private String overview;

    @NotBlank @Size(min=100, max=65536)
    private String content;

    @Min(1)
    private long author;
}
