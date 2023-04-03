package it.cgmconsulting.mscomment.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import it.cgmconsulting.mscomment.entity.Comment;
import it.cgmconsulting.mscomment.paylaod.request.CommentRequest;
import it.cgmconsulting.mscomment.paylaod.response.CommentResponse;
import it.cgmconsulting.mscomment.paylaod.response.UserResponse;
import it.cgmconsulting.mscomment.repository.CommentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CommentService {

    @Autowired
    CommentRepository commentRepository;

    public void save(Comment c) {
        commentRepository.save(c);
    }

    /**
     * <p> Verifica l'esistenza del Post contattando il MicroServizio. </p>
     * Integrato @CircuitBreaker: Se il MS è down, restituisce false
     *
     * @param postId id del post da cercare
     * @return bool sull'esistenza del post
     */
    @CircuitBreaker(name = "a-tempo", fallbackMethod = "checkIfPostExistFallBack")
    public boolean checkIfPostExist(long postId) {
        RestTemplate restTemplate = new RestTemplate(); // ci permette di effettuare chiamate a EndPoint esterni e recuperare la risposta. è un client sincrono.

        // richiama il MicroServizio Post, passando dal GateWay
        String uri = "http://localhost:8090/post/verify-post/" + postId;
        ResponseEntity<Boolean> response;
        try {
            response = restTemplate.getForEntity(uri, Boolean.class); // getForEntity() restituisce oggetto risposta con HttpStatus.
        } catch (HttpClientErrorException e) {
            log.error(e.getMessage());
            return false;
        }
        return response.getBody();
    }

    public boolean checkIfPostExistFallBack(Exception e) {
        log.info("--- POST SERVICE UNAVAILABLE (" + e.getMessage() + ") ---");
        return false;
    }

    /**
     * @param id
     * @param authorityName
     * @return
     */
    @CircuitBreaker(name = "a-tempo", fallbackMethod = "checkUserAndAuthorityFallBack")
    public boolean checkUserAndAuthority(long id, String authorityName) {
        RestTemplate restTemplate = new RestTemplate(); // ci permette di effettuare chiamate a EndPoint esterni e recuperare la risposta. è  un client sincrono.
        // richiama il MicroServizio User, passando dal GateWay
//        String uri = "http://localhost:8090/user?id=" + id + "&authorityName=" + authorityName; // prima soluzione
//        String uri2 = "http://localhost:8090/user?id={id}&authorityName={authorityName}";    // seconda soluzione
//        // li passa cosi perche uno è long, l'altro è string - NO
//        // fossero stati tutti string si poteva creare una map
//        boolean existsUser = restTemplate.getForObject(uri, Boolean.class, id, authorityName);
//        return existsUser;
        // SOLUZIONE 1
        //String uri = "http://localhost:8090/user?id="+id+"&authorityName="+authorityName;
        // boolean existsUser = restTemplate.getForObject(uri, Boolean.class);
        // SOLUZIONE 2
        String uri = "http://localhost:8090/user?id={id}&authorityName={authorityName}";
        System.out.println(" Barbara è stata qua ");
        boolean existsUser = restTemplate.getForObject(uri, Boolean.class, id, authorityName);
        return existsUser;
    }

    public boolean checkUserAndAuthorityFallBack(Exception e) {
        log.info("--- USER SERVICE UNAVAILABLE (" + e.getMessage() + ") ---");
        return false;
    }

    /**
     * Converte Request in Entity di comment
     *
     * @param cr CommentRequest
     * @return Comment Entity
     */
    public Comment fromRequestToEntity(CommentRequest cr) {
        return new Comment(cr.getComment(), cr.getAuthor(), cr.getPost());
    }

    /**
     * <p> Recupera List dei commenti del post in oggetto, contattando i MicroServizi User. </p>
     * I commenti censurati saranno sostituiti con "asterischi"
     *
     * @param postId id del post da recuperare
     * @return List dei commenti del post in oggetto in forma Response
     */
    @CircuitBreaker(name = "a-tentativi", fallbackMethod = "getByPostFallBack")
    public List<CommentResponse> getByPost(long postId) {
        RestTemplate restTemplate = new RestTemplate(); // ci permette di effettuare chiamate a EndPoint esterni e recuperare la risposta. è un client sincrono.
        List<CommentResponse> listCommentResponse = commentRepository.getByPost(postId);
//        richiamare MS user, farsi restiture una List<UserResponse> relativa ad user con ROLE_READER
        String uri = "http://localhost:8090/user/ROLE_READER";
        ResponseEntity<List<UserResponse>> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                new HttpEntity<>(UserResponse.class),
                new ParameterizedTypeReference<List<UserResponse>>() {
                }
        );
        List<UserResponse> listUser = response.getBody();
//         con ciclo annidato come in post service trovare
/*        for (CommentResponse c : listCommentResponse) {
            for (UserResponse u : listUser.) {
                int i = 0;
                while (c.getUserId() == listUser.get(i).getId()) {
                    c.setUsername(u.getUsername());
                }
            }
            if (c.isCensored())
                c.setComment("***");
        }*/
        for (CommentResponse c : listCommentResponse) {
            boolean interruzione = true;
            int i = 0;
            while (interruzione) {
                // IDE CONSIGLIA assert
                assert listUser != null;
                if (!(i < listUser.size())) break;
                if (c.getUserId() == listUser.get(i).getId()) {
                    c.setUsername(listUser.get(i).getUsername());
                    interruzione = false;
                }
                i++;
            }
            if (c.isCensored())
                c.setComment("**********");
        }
//        recuperare lista dei commenti del post in oggetto -> commentRepository.getByPost(postId)
//        ciclo annidato delle 2 listee per valorizzare l'attributo username di ogni elemento presente nella List<CommentResponse>
        return listCommentResponse;
    }

    public List<CommentResponse> getByPostFallBack(Exception e) {
        log.info("--- USER SERVICE UNAVAILABLE (" + e.getMessage() + ") ---");
        return new ArrayList<>();
    }

    public List<Comment> getBackupComments() {
        // metodo DERIVATO
        return commentRepository.findAll();
    }
}
