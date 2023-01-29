package it.cgmconsulting.msuser.service;

import it.cgmconsulting.msuser.entity.Authority;
import it.cgmconsulting.msuser.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorityService {

    @Autowired
    AuthorityRepository authorityRepository;

    public Optional<Authority> findByAuthorityName(String authorityName){
        return authorityRepository.findByAuthorityName(authorityName);
    }

}
