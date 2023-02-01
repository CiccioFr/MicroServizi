package it.cgmconsulting.mspost.service;

import it.cgmconsulting.mspost.entity.Post;
import it.cgmconsulting.mspost.payload.request.PostRequest;
import it.cgmconsulting.mspost.payload.response.PostResponse;
import it.cgmconsulting.mspost.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
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

    // un metodo che converta automaticamente una request in un Post
    // ci sono diverse .. 9.39
    public Post fromRequestToEntity(PostRequest pr) {
        Post p = new Post(
                pr.getTitle(),
                pr.getOverview(),
                pr.getContent(),
                pr.getAuthor()
        );
        return p;
    }

    public boolean checkTitle(String title) {
        // return true se titolo esiste già
        return postRepository.existsByTitle(title);
    }

    public boolean checkUserAndAuthority(long id, String authorityName) {
        RestTemplate restTemplate = new RestTemplate();
        // richiama il microServizio di User, devo interrogare il GateWay
        String uri = "http://localhost:8090/user/" + id + "/" + authorityName;
        System.out.println("Stampo l'URI: " + uri);
        boolean existsUser = restTemplate.getForObject(uri, Boolean.class);
        // suggerito dall'iDE.. Unboxing of 'restTemplate.getForObject(uri, Boolean.class)' may produce 'NullPointerException'
        // boolean existsUser = Boolean.TRUE.equals(restTemplate.getForObject(uri, Boolean.class));
        return existsUser;
//        return true;
    }

    /**
     * Chiede a Repository una List di Post Pubblici
     *
     * @return List di Post Pubblici strutturati secondo PostResponse
     */
    public List<PostResponse> getPosts() {
        List<PostResponse> list = postRepository.getPosts();
        // richiamare il microservizio msUser e farmi restituire una lista (UserResponse) di utenti che siano ROLE_EDITOR
        // Ciclo list e per ogni match tra id dello user e author di PostResponse, setto lo username
        return list;
    }
}
