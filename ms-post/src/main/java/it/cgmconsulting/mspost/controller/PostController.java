package it.cgmconsulting.mspost.controller;

import it.cgmconsulting.mspost.entity.Post;
import it.cgmconsulting.mspost.payload.request.PostRequest;
import it.cgmconsulting.mspost.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Optional;

@RestController
public class PostController {

    @Autowired
    PostService postService;

    @PutMapping
    public ResponseEntity<?> save(@RequestBody @Valid PostRequest request) {

        if (!postService.checkUserAndAuthority(request.getAuthor(), "ROLE_EDITOR"))
            return new ResponseEntity("No Author found", HttpStatus.NOT_FOUND);

        /** Verifica esistenza del titolo del Post */
        if (postService.checkTitle(request.getTitle()))
            return new ResponseEntity("The post with " + request.getTitle() + " already exist", HttpStatus.BAD_REQUEST);

        Post p = postService.fromRequestToEntity(request);
        postService.save(p);
        return new ResponseEntity("The Post " + p.getTitle() + " has been saved with id " + p.getId(), HttpStatus.CREATED);
    }

    /**
     * Modifica Post Esistente
     *
     * @param request
     * @return
     */
    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody @Valid PostRequest request, @PathVariable long id) {
        // solo chi ha scritto il post può modificarlo

    /*  PROVA MIA
            Post postDaModificare = postService.findById(id).get();

            */
        /** Verifica esistenza del titolo del Post e Verifica dell'autore *//*

            if (postDaModificare.getTitle().equals(postService.checkTitle(request.getTitle()))
                    && request.getAuthor() != postDaModificare.getAuthor())
                return new ResponseEntity("The post with " + request.getTitle() + " already exist", HttpStatus.BAD_REQUEST);

            */
        /** Verifica dell'autore *//*

            if (request.getAuthor() != postDaModificare.getAuthor())
                return new ResponseEntity("Non sei l'autore del Post", HttpStatus.BAD_REQUEST);

            postService.save(postDaModificare);
            return new ResponseEntity("The Post " + postDaModificare.getTitle() + " has been updated", HttpStatus.UPGRADE_REQUIRED);
    */

        Optional<Post> p = postService.findById(id);

        if (p.isEmpty())
            return new ResponseEntity<>("Post not found", HttpStatus.NOT_FOUND);

        if (request.getAuthor() != p.get().getAuthor())
            return new ResponseEntity<>("Solo l'autore del post può modificarlo", HttpStatus.FORBIDDEN);

        if (postService.existsByTitleAndIdNot(request.getTitle(), p.get().getId()))
            return new ResponseEntity<>("Title already exists", HttpStatus.BAD_REQUEST);

        p.get().setTitle(request.getTitle());
        p.get().setOverview(request.getOverview());
        p.get().setContent(request.getContent());
        p.get().setPublished(false);

        return new ResponseEntity<String>("Post updated", HttpStatus.OK);
    }

    @PatchMapping
    @Transactional
    public ResponseEntity<?> publishPost(@RequestParam long postId, @RequestParam long userId) {

        if (!postService.checkUserAndAuthority(userId, "ROLE_ADMINISTRATOR"))
            return new ResponseEntity("You are not the administrator", HttpStatus.NOT_FOUND);

        Optional<Post> p = postService.findById(postId);
        if (p.isEmpty())
            return new ResponseEntity<>("Post not found", HttpStatus.NOT_FOUND);

        p.get().setPublished(true);
        return new ResponseEntity("Post published", HttpStatus.OK);
    }

}
