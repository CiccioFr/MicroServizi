package it.cgmconsulting.mspost.service;

import it.cgmconsulting.mspost.entity.Post;
import it.cgmconsulting.mspost.payload.request.PostRequest;
import it.cgmconsulting.mspost.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PostService {

    @Autowired
    PostRepository postRepository;

    public void save(Post p) {
        postRepository.save(p);
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
        return postRepository.findByTitle(title);
    }

    public boolean checkUserAndAuthority(long id, String authorityName) {
        RestTemplate restTemplate = new RestTemplate();
        // devo richiamare il microServizio di User, devo interrogare il GateWay
        String uri = "http://localhost:8090/user/" + id + "/" + authorityName;
        boolean existsUser = restTemplate.getForObject(uri, Boolean.class);
        //                  Boolean.TRUE.equals(restTemplate.getForObject
        return existsUser;
    }
}


























