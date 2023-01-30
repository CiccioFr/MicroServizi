package it.cgmconsulting.msuser.service;

import it.cgmconsulting.msuser.entity.Authority;
import it.cgmconsulting.msuser.entity.User;
import it.cgmconsulting.msuser.payload.request.SignupRequest;
import it.cgmconsulting.msuser.payload.response.UserResponse;
import it.cgmconsulting.msuser.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthorityService authorityService;
    @Autowired
    PasswordEncoder passwordEncoder;

    public boolean existsByUsernameOrEmail(String username, String email) {
        return userRepository.existsByUsernameOrEmail(username, email);
    }

    public void save(User u) {
        userRepository.save(u);
    }

    public User fromRequestToUser(SignupRequest sr) {
        User user = new User();
        Optional<Authority> authority = authorityService.findByAuthorityName(sr.getAuthorityName());

        if (authority.isEmpty())
            return user;

        user.setAuthority(authority.get());
        user.setEmail(sr.getEmail());
        user.setUsername((sr.getUsername()));
        user.setPassword(passwordEncoder.encode(sr.getPassword()));
        return user;
    }

    public boolean existsByIdAndAuthorityAuthorityName(long id, String authorityName) {
        return userRepository.existsByIdAndAuthorityAuthorityName(id, authorityName);
    }

    /**
     * Chiede a Repository una List di User con Ruolo passato come parametro
     *
     * @param authorityName Ruolo da Ricercare
     * @return List di User con Ruolo richiesto strutturati secondo UserResponse
     */
    public List<UserResponse> getByRole(String authorityName) {
        return userRepository.getByRole(authorityName);
    }
}
