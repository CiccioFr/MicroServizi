package it.cgmconsulting.mspost.repository;

import it.cgmconsulting.mspost.entity.Post;
import it.cgmconsulting.mspost.payload.response.PostResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    boolean existsByTitle(String title);

    Optional<Post> findById(long id);

    boolean existsByTitleAndIdNot(String title, long postId);

    /**
     * Ricerca in DB di Post Pubblici
     * @return List di Response di Post Pubblici
     */
    @Query(value = "SELECT new it.cgmconsulting.mspost.payload.response.PostResponse(" +
            "p.id, " +
            "p.title, " +
            "p.overview, " +
            "p.author) " +
            "FROM Post p " +
            "WHERE p.published = true")
    List<PostResponse> getPosts();
}
