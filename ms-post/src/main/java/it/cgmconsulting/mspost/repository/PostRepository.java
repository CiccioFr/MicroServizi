package it.cgmconsulting.mspost.repository;

import it.cgmconsulting.mspost.entity.Post;
import it.cgmconsulting.mspost.payload.response.PostResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    /**
     * check se il titolo esiste
     *
     * @param title Titolo di cui verificare l'esistenza
     * @return true/false se titolo esiste
     */
    boolean existsByTitle(String title);

    boolean existsByTitleAndIdNot(String title, long postId);

    // TODO - Il docente non ha questo metodo
    // perché usa getPost( .. )
    Optional<Post> findById(long id);

    /**
     * Ricerca in DB di Post Pubblici
     *
     * @return List di Response di Post Pubblici
     */
    @Query(value = "SELECT new it.cgmconsulting.mspost.payload.response.PostResponse(" +
            "p.id," +
            "p.title," +
            "p.overview," +
            "p.author) " +
            "FROM Post p " +
            "WHERE p.published = true")
    List<PostResponse> getPosts();

    @Query(value = "SELECT new it.cgmconsulting.mspost.payload.response.PostResponse(" +
            "p.id," +
            "p.title," +
            "p.content," +
            "p.author) " +
            "FROM Post p " +
            "WHERE p.published = true " +
            "AND p.id = :postId")
    PostResponse getPost(@Param("postId") long postId);

    boolean existsByIdAndPublishedTrue(long id);

    Optional<Post> findByIdAndPublishedTrue(long postId);
}
