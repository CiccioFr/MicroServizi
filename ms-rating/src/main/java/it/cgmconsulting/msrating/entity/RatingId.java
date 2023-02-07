package it.cgmconsulting.msrating.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RatingId implements Serializable {

    private long userId;
    private long postId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RatingId ratingId = (RatingId) o;
        return userId == ratingId.userId && postId == ratingId.postId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, postId);
    }
}
