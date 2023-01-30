package it.cgmconsulting.mspost.repository;

import it.cgmconsulting.mspost.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    boolean existsByTitle(String title);
}
