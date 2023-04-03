package it.cgmconsulting.msbackup.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import it.cgmconsulting.msbackup.payload.response.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Slf4j
public class BackupApiService {

    /* COMMENT */

    /**
     * Contatta Ms-Comment, che restituisce una List di Entity comment
     * @return List di CommentBackupResponse, trasposizione di List di comment del MS
     */
    @CircuitBreaker(name="a-tempo-comment", fallbackMethod = "getCommentsFallBack")
    List<CommentBackupResponse> getComments(){
        String uri = "http://localhost:8090/comment/backup";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<CommentBackupResponse>> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                new HttpEntity<>(CommentBackupResponse.class),
                new ParameterizedTypeReference<List<CommentBackupResponse>>(){}
        );
        List<CommentBackupResponse> comments = response.getBody();
        return comments;
    }
    List<CommentBackupResponse> getCommentsFallBack(Exception e){
        return null;
    }

    /* POST */
    @CircuitBreaker(name="a-tempo-post", fallbackMethod = "getPostsFallBack")
    List<PostBackupResponse> getPosts(){
        String uri = "http://localhost:8090/post/backup";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<PostBackupResponse>> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                new HttpEntity<>(PostBackupResponse.class),
                new ParameterizedTypeReference<List<PostBackupResponse>>(){}
        );
        List<PostBackupResponse> posts = response.getBody();
        return posts;
    }
    List<PostBackupResponse> getPostsFallBack(Exception e){
        return null;
    }

    /* RATING */
    @CircuitBreaker(name="a-tempo-rating", fallbackMethod = "getRatingsFallBack")
    List<RatingBackupResponse> getRatings(){
        String uri = "http://localhost:8090/rating/backup";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<RatingBackupResponse>> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                new HttpEntity<>(RatingBackupResponse.class),
                new ParameterizedTypeReference<List<RatingBackupResponse>>(){}
        );
        List<RatingBackupResponse> ratings = response.getBody();
        return ratings;
    }
    List<PostBackupResponse> getRatingsFallBack(Exception e){
        return null;
    }

    /* USER */
    @CircuitBreaker(name="a-tempo-user", fallbackMethod = "getUsersFallBack")
    List<UserBackupResponse> getUsers(){
        String uri = "http://localhost:8090/user/backup";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<UserBackupResponse>> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                new HttpEntity<>(UserBackupResponse.class),
                new ParameterizedTypeReference<List<UserBackupResponse>>(){}
        );
        List<UserBackupResponse> users = response.getBody();
        return users;
    }
    List<PostBackupResponse> getUsersFallBack(Exception e){
        return null;
    }

    /* AUTHORITY */
    @CircuitBreaker(name="a-tempo-authority", fallbackMethod = "getAuthoritiesFallBack")
    List<AuthorityBackupResponse> getAuthorities(){
        String uri = "http://localhost:8090/user/authority/backup";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<AuthorityBackupResponse>> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                new HttpEntity<>(AuthorityBackupResponse.class),
                new ParameterizedTypeReference<List<AuthorityBackupResponse>>(){}
        );
        List<AuthorityBackupResponse> authorities = response.getBody();
        return authorities;
    }
    List<AuthorityBackupResponse> getAuthoritiesFallBack(Exception e){
        return null;
    }
}
