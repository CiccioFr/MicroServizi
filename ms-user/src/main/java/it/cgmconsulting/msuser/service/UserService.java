package it.cgmconsulting.msuser.service;

import it.cgmconsulting.msuser.entity.Authority;
import it.cgmconsulting.msuser.entity.User;
import it.cgmconsulting.msuser.payload.request.SignupRequest;
import it.cgmconsulting.msuser.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        User u = new User();
        Optional<Authority> a = authorityService.findByAuthorityName(sr.getAuthorityName());
        if (a.isEmpty())
            return u;

        u.setAuthority(a.get());
        u.setEmail(sr.getEmail());
        u.setUsername((sr.getUsername()));
        u.setPassword(passwordEncoder.encode(sr.getPassword()));
        return u;
    }

    public boolean existsByIdAndAuthorityAuthorityName(long id, String authorityName){
        return userRepository.existsByIdAndAuthorityAuthorityName(id, authorityName);
    }

}
