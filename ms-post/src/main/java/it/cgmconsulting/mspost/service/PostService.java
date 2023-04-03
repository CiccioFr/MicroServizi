package it.cgmconsulting.mspost.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import it.cgmconsulting.mspost.entity.Post;
import it.cgmconsulting.mspost.payload.request.PostRequest;
import it.cgmconsulting.mspost.payload.response.CommentResponse;
import it.cgmconsulting.mspost.payload.response.PostDetailResponse;
import it.cgmconsulting.mspost.payload.response.PostResponse;
import it.cgmconsulting.mspost.payload.response.UserResponse;
import it.cgmconsulting.mspost.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j // @Slf4j per i log
public class PostService {

    @Autowired
    PostRepository postRepository;

    public void save(Post p) {
        postRepository.save(p);
    }

    public Optional<Post> findById(long id) {
        return postRepository.findById(id);
    }

    public boolean existsByTitleAndIdNot(String title, long postId) {
        return postRepository.existsByTitleAndIdNot(title, postId);
    }

    public boolean existsByTitle(String title) {
        return postRepository.existsByTitle(title);
    }

    /**
     * Converte una request in un Post
     *
     * @param postRequest PostRequest da convertire in Post
     * @return Post derivato da una PostRequest
     */
    public Post fromRequestToEntity(PostRequest postRequest) {
        // ci sono diverse .. 9.39
        Post post = new Post(
                postRequest.getTitle(),
                postRequest.getOverview(),
                postRequest.getContent(),
                postRequest.getAuthor()
        );
        return post;
    }

    /**
     * verifica esistenza di un titolo
     *
     * @param title
     * @return
     */
    public boolean checkTitle(String title) {
        // return true se titolo esiste già
        return postRepository.existsByTitle(title);
    }

    // TODO rivedere javaDoc

    /**
     * verifica user e authority DA RIVEDERE
     *
     * @param id
     * @param authorityName
     * @return
     */
    @CircuitBreaker(name = "a-tempo", fallbackMethod = "checkUserAndAuthorityFallBack")
    public boolean checkUserAndAuthority(long id, String authorityName) {
        RestTemplate restTemplate = new RestTemplate();
        // richiama il microServizio di User, devo interrogare il GateWay
        String uri = "http://localhost:8090/user/" + id + "/" + authorityName;
        System.out.println("Stampo l'URI: " + uri);
        boolean existsUser = restTemplate.getForObject(uri, Boolean.class);
        // suggerito dall'iDE.. Unboxing of 'restTemplate.getForObject(uri, Boolean.class)' may produce 'NullPointerException'
        // boolean existsUser = Boolean.TRUE.equals(restTemplate.getForObject(uri, Boolean.class));
        return existsUser;
    }

    public boolean checkUserAndAuthorityFallBack(Exception e) {
        log.info("--- User Microservice not avalaible (" + e.getMessage());
        return false;
    }

    /**
     * Chiede a Repository una List di Post Pubblici
     *
     * @return List di Post Pubblici strutturati secondo PostResponse
     */
/*
    public List<PostResponse> getPosts(){
        List<PostResponse> list = postRepository.getPosts();
        // richiamare il microservizio msuser e farmi restituire una lista(UserResponse) di utenti che siano ROLE_EDITOR
        RestTemplate restTemplate = new RestTemplate();
        String uri = "http://localhost:8090/user/ROLE_EDITOR";
        UserResponse[] editorList = restTemplate.getForObject(uri, UserResponse[].class);
        // Ciclo list e per ogni match tra id dello user e author di PostReposnse, setto lo username
        for (PostResponse post: list) {
            for (int i=0; i<editorList.length; i++){
                if (post.getAuthor() == editorList[i].getId())
                    post.setAuthorUsername(editorList[i].getUsername());
            }
        }
        return list;
    }
    */
    @CircuitBreaker(name = "a-tempo", fallbackMethod = "getPostsFallBack")
    public List<PostResponse> getPosts() {
        List<PostResponse> listPost = postRepository.getPosts();
        // richiamare il microservizio msUser per farmi restituire una lista (UserResponse) di utenti che siano ROLE_EDITOR
        RestTemplate restTemplate = new RestTemplate(); // Permette di effettuare chiamate a EndPoint esterni e recuperare la risposta. è un client sincrono.
        String uri = "http://localhost:8090/user/ROLE_EDITOR";

//        List<UserResponse> listUser = restTemplate.getForObject(uri,List.class);
        ResponseEntity<List<UserResponse>> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                new HttpEntity(UserResponse.class),
                new ParameterizedTypeReference<List<UserResponse>>() {
                }
        );
        // list di User con ruolo ROLE_EDITOR
        List<UserResponse> listUser = response.getBody();

        // Ciclo list e per ogni match tra id dello user e author di PostResponse, setto lo username
        // todo provare con Stream ed iterator
        for (PostResponse post : listPost) {
            // l'IDE consiglia assert
            // assert listUser != null;
            for (UserResponse u : listUser) {
                if (post.getId() == u.getId())
                    post.setAuthorUsername(u.getUsername());
            }
        }
        return listPost;
    }

    /**
     * <p> Metodo di fallback - Nel caso in cui la chiamata al MS User fallisse ( {@link #getPosts()} ) </p>
     * Restituisce comunque una lista di PostResponse ma senza lo username dell'autore del post (value = null)
     *
     * @param e Eccezione scaturita del metodo {@link #getPosts()}
     * @return List di Post Pubblici senza nameUser (valorizzato null)
     */
    public List<PostResponse> getPostsFallBack(Exception e) {
        log.info("RESILIENCE4J: ms-user not avalaible. " + e.getMessage());
        List<PostResponse> list = postRepository.getPosts();
        return list;
    }

    public boolean existsByIdAndPublishedTrue(long id) {
        return postRepository.existsByIdAndPublishedTrue(id);
    }

    /**
     * Ricerca del post per id
     *
     * @param postId id del post
     * @return post (se PUBBLICATO)
     */
    public PostResponse getPost(long postId) {
        return postRepository.getPost(postId);
    }

    /**
     * Ricerca l'user per id
     *
     * @param userId id dell'user
     * @return UserResponse
     */
    public UserResponse getUser(long userId) {
        RestTemplate restTemplate = new RestTemplate();
        String uri = "http://localhost:8090/user/id/" + userId;

        ResponseEntity<UserResponse> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                new HttpEntity<>(UserResponse.class),
                new ParameterizedTypeReference<UserResponse>() {
                }
        );
        UserResponse user = response.getBody();
        return user;
    }

    /**
     * <p> Recupera i commenti di un post </p>
     * Successivamente saranno aggiunti all'oggetto PostDetails
     *
     * @param postId id del post
     * @return List di commenti del post
     */
    @CircuitBreaker(name = "a-tentativi", fallbackMethod = "getCommentsByPostFallBack")
    public List<CommentResponse> getCommentsByPost(long postId) {
        RestTemplate restTemplate = new RestTemplate();
        String uri = "http://localhost:8090/comment/" + postId;
        ResponseEntity<List<CommentResponse>> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                new HttpEntity<>(CommentResponse.class),
                new ParameterizedTypeReference<List<CommentResponse>>() {
                }
        );
        List<CommentResponse> comments = response.getBody();
        return comments;
    }

    public List<CommentResponse> getCommentsByPostFallBack() {
        log.info("--- COMMENT MICROSERVICE NOT FOUND ---");
        return new ArrayList<>();
    }

    /**
     * <p> Recupera la media dei voti di un post </p>
     * Successivamente saranno aggiunti all'oggetto PostDetails
     *
     * @param postId id del post
     * @return Media dei commenti del post
     */
    @CircuitBreaker(name = "a-tentativi", fallbackMethod = "getAvgRatePostFallback")
    public double getAvgRatePost(long postId) {
        RestTemplate restTemplate = new RestTemplate();
        String uri = "http://localhost:8090/rating/" + postId;
        double rateAvg = restTemplate.getForObject(uri, Double.class);
        return rateAvg;
    }

    public double getAvgRatePostFallback(Exception e) {
        log.info(" ---- Rating Microservice not avalaible. " + e.getMessage());
        return 0d;
    }

    /**
     * Aggiunge commenti e media dei voti all'oggetto post
     *
     * @param post     Post base
     * @param comments List di Commenti da aggiungere al Post
     * @param average  Media dei commenti del post
     * @return Post arricchito da commenti e media dei voti
     */
    public PostDetailResponse fromPostResponseToPostDetailResponse(PostResponse post, List<CommentResponse> comments, double average) {
        return new PostDetailResponse(
                post.getId(),
                post.getTitle(),
                post.getOverview(),
                post.getAuthor(),
                post.getAuthorUsername(),
                comments,
                average
        );
    }

    public Optional<Post> findByIdAndPublishedTrue(long postId) {
        return postRepository.findByIdAndPublishedTrue(postId);
    }

    public List<Post> getBackupPosts() {
        return postRepository.findAll();
    }
}
