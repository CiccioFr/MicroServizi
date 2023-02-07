package it.cgmconsulting.msrating.payload.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class RatingRequest {

    @Min(1)
    private long postId;

    @Min(1)
    private long userId;

    @Min(1) @Max(5)
    private byte rate;
}
