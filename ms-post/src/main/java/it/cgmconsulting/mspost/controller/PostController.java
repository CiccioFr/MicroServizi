package it.cgmconsulting.mspost.controller;

import it.cgmconsulting.mspost.entity.Post;
import it.cgmconsulting.mspost.payload.request.PostRequest;
import it.cgmconsulting.mspost.payload.response.CommentResponse;
import it.cgmconsulting.mspost.payload.response.PostDetailResponse;
import it.cgmconsulting.mspost.payload.response.PostResponse;
import it.cgmconsulting.mspost.payload.response.UserResponse;
import it.cgmconsulting.mspost.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Optional;

@RestController
@Validated
public class PostController {

    @Autowired
    PostService postService;

    /**
     * Creazione e salvataggio di un Post
     *
     * @param request Post da salvare in formato Request
     * @return Response del server di salvataggio effettuato
     * o diniego per esistenza del titolo / autore non avente titolo ROLE_EDITOR
     */
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
     * Modifica Post Esistente.
     * Solo chi ha scritto il post può modificarlo
     *
     * @param request Post Modificato da salvare in formato Request
     * @param id      id del Post
     * @return Response del server di salvataggio effettuato
     * o diniego per esistenza del titolo in altro Post / autore non avente titolo
     */
    @PatchMapping("/{id}")
    @Transactional
    public ResponseEntity<?> update(@RequestBody @Valid PostRequest request, @PathVariable long id) {

        Optional<Post> postEsistente = postService.findById(id);

        if (postEsistente.isEmpty())
            return new ResponseEntity<>("Post not found", HttpStatus.NOT_FOUND);

        if (request.getAuthor() != postEsistente.get().getAuthor())
            return new ResponseEntity<>("Solo l'autore del post può modificarlo", HttpStatus.FORBIDDEN);

        if (postService.existsByTitleAndIdNot(request.getTitle(), postEsistente.get().getId()))
            return new ResponseEntity<>("Title already exists", HttpStatus.BAD_REQUEST);

        postEsistente.get().setTitle(request.getTitle());
        postEsistente.get().setOverview(request.getOverview());
        postEsistente.get().setContent(request.getContent());
        postEsistente.get().setPublished(false);

        return new ResponseEntity<String>("Post updated", HttpStatus.OK);
    }

    /**
     * Cambio dello stato "Pubblicato" a true del Post da parte di un ROLE_ADMIN
     *
     * @param postId id del Post da Pubblicare
     * @param userId id del ROLE_ADMIN
     * @return Response del serve di operazione effettuata
     */
    @PatchMapping
    @Transactional
    public ResponseEntity<?> publishPost(@RequestParam long postId, @RequestParam long userId) {
        if (!postService.checkUserAndAuthority(userId, "ROLE_ADMIN"))
            return new ResponseEntity<>("You are not the administrator", HttpStatus.NOT_FOUND);

        Optional<Post> p = postService.findById(postId);
        if (p.isEmpty())
            return new ResponseEntity<>("Post not found", HttpStatus.NOT_FOUND);

        p.get().setPublished(true);
        return new ResponseEntity<String>("Post published", HttpStatus.OK);
    }

    /**
     * Ricerca di tutti Post Pubblici (Publushed = True)
     *
     * @return Response con List di Post Pubblici
     */
    @GetMapping
    public ResponseEntity<?> getPosts() {
        List<PostResponse> list = postService.getPosts();
        return new ResponseEntity(list, HttpStatus.OK);
    }

    /**
     * verifica se il post esiste ed è pubblicato
     *
     * @param postId id del post da verificare
     * @return ResponseEntity con true/false in body
     */
    @GetMapping("verify-post/{postId}")
    public ResponseEntity<?> verifyPost(@PathVariable @Min(1) long postId) {
        if (!postService.existsByIdAndPublishedTrue(postId))
            return new ResponseEntity(false, HttpStatus.NOT_FOUND);

        return new ResponseEntity(true, HttpStatus.OK);
    }

    /**
     * Ricerca i commenti di un post e li accorpa al post
     *
     * @param postId id del post in oggetto
     * @return ResponseEntity di un post arricchito dei suoi commenti
     */
    @GetMapping("/{postId}")
    public ResponseEntity getPostDetail(@PathVariable long postId) {

        // recuperare il dettaglio del post
        PostResponse post = postService.getPost(postId);
        if (post == null)
            return new ResponseEntity("Post not found", HttpStatus.NOT_FOUND);

        // recuperare username dell'author del post
        UserResponse userAutoreDelPost = postService.getUser(post.getAuthor());
        if (userAutoreDelPost == null)
            return new ResponseEntity("Author of Post not found", HttpStatus.NOT_FOUND);
        post.setAuthorUsername(userAutoreDelPost.getUsername());

        // recuperare i commenti del post e per ognuno di essi trovare lo username di chi l'ha scritto.
        List<CommentResponse> comments = postService.getCommentsByPost(post.getId());

        // recupero la media dei voti del post
        double average = postService.getAvgRatePost(postId);

        PostDetailResponse pdr = postService.fromPostResponseToPostDetailResponse(post, comments, average);
        return new ResponseEntity(pdr, HttpStatus.OK);
    }

}
