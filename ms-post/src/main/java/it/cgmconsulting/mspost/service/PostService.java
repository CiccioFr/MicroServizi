package it.cgmconsulting.mspost.service;

import it.cgmconsulting.mspost.entity.Post;
import it.cgmconsulting.mspost.payload.request.PostRequest;
import it.cgmconsulting.mspost.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

    // un metodo che converta automaticamente un request in un Post
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
        // devo richiamare il microServizio di User, devo interrogare il GateWay
        String uri = "http://localhost:8090/user/" + id + "/" + authorityName;
        System.out.println(uri);
        boolean existsUser = restTemplate.getForObject(uri, Boolean.class);
        //                  Boolean.TRUE.equals(restTemplate.getForObject
        return existsUser;
    }

    public boolean existsByTitle(String title) {
        return postRepository.existsByTitle(title);
    }

    public boolean existsByTitleAndIdNot(String title, long postId) {
        return postRepository.existsByTitleAndIdNot(title, postId);
    }
}


























