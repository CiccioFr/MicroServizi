package it.cgmconsulting.msuser.repository;

import it.cgmconsulting.msuser.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsernameOrEmail(String username, String email);

    boolean existsByIdAndAuthorityAuthorityName(long id, String authorityName);

    }
