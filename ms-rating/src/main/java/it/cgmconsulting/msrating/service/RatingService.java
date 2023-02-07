package it.cgmconsulting.msrating.service;

import it.cgmconsulting.msrating.entity.Rating;
import it.cgmconsulting.msrating.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RatingService {

    @Autowired
    RatingRepository ratingRepository;

    public void save(Rating r) {
        ratingRepository.save(r);
    }

    public double getAverage(long postId) {
        return ratingRepository.getAverage(postId);
    }

    public boolean checkUserAndAuthority(long userId, String authorityName) {
        RestTemplate restTemplate = new RestTemplate();

        /*// non va bene perchè nel PostController vuole @RequestParam
        // la uri sotto è per un EndPoint è impostato con @PathVariable
        String uri = "http://localhost:8090/user/" + userId + "/" + authorityName;
        boolean existsUser = restTemplate.getForObject(uri, Boolean.class);*/

        // nel PostController l'EndPoint è impostato con @RequestParam
        // ovvero una uri http://localhost:8090/user?id=2&authorityName=ROLE_READER
        String uri = "http://localhost:8090/user?id={userId}&authorityName={authorityName}";
        boolean existsUser = restTemplate.getForObject(uri, Boolean.class, userId, authorityName);

        return existsUser;
    }

}
