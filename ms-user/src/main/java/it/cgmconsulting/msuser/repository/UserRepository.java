package it.cgmconsulting.msuser.repository;

import it.cgmconsulting.msuser.entity.User;
import it.cgmconsulting.msuser.payload.response.UserResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsernameOrEmail(String username, String email);

    boolean existsByIdAndAuthorityAuthorityName(long idAuthority, String authorityName);

    /**
     * Ricerca in DB di User con Ruolo passato come parametro
     *
     * @return List di User con ruolo richiesto
     */
    @Query(value = "SELECT new it.cgmconsulting.msuser.payload.response.UserResponse(" +
            "u.id," +
            "u.username) " +
            "FROM User u " +
            "WHERE u.authority.authorityName = :authorityName")
    List<UserResponse> getByRole(@Param("authorityName") String authorityName);

    /**
     * Ricerca in DB di User con ID passato come parametro
     *
     * @return User
     */
    @Query(value="SELECT new it.cgmconsulting.msuser.payload.response.UserResponse(" +
            "u.id," +
            "u.username) " +
            "FROM User u " +
            "WHERE u.id = :userId")
    UserResponse getUser(@Param("userId") long userId);
}
