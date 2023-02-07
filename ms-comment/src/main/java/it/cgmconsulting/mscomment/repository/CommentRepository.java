package it.cgmconsulting.mscomment.repository;

import it.cgmconsulting.mscomment.entity.Comment;
import it.cgmconsulting.mscomment.paylaod.response.CommentResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value="SELECT new it.cgmconsulting.mscomment.paylaod.response.CommentResponse(" +
            "c.id, " +
            "c.comment, " +
            "c.createdAt, " +
            "c.author, " +
            "c.censored) " +
            "FROM Comment c " +
            "WHERE c.post = :postId " +
            "ORDER BY c.createdAt DESC")
    List<CommentResponse> getByPost(@Param("postId") long postId);
}
