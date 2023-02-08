package it.cgmconsulting.msrating.repository;

import it.cgmconsulting.msrating.entity.Rating;
import it.cgmconsulting.msrating.entity.RatingId;
import it.cgmconsulting.msrating.payload.response.RatingBackupResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, RatingId> {

    @Query(value = "SELECT COALESCE(ROUND(AVG(r.rate), 2), 0.0) FROM Rating r WHERE r.ratingId.postId = :postId")
    double getAverage(@Param("postId") long postId);

    @Query(value="SELECT new it.cgmconsulting.msrating.payload.response.RatingBackupResponse(" +
            "r.ratingId.userId, " +
            "r.ratingId.postId, " +
            "r.rate, " +
            "r.createdAt, " +
            "r.updatedAt) " +
            "FROM Rating r" )
    List<RatingBackupResponse> getBackupRatings();
}
