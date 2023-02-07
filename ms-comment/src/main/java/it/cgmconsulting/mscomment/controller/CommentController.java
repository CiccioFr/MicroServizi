package it.cgmconsulting.mscomment.controller;

import it.cgmconsulting.mscomment.entity.Comment;
import it.cgmconsulting.mscomment.paylaod.request.CommentRequest;
import it.cgmconsulting.mscomment.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class CommentController {

    @Autowired
    CommentService commentService;

    /**
     * Salvataggio di un Commento
     *
     * @return
     */
    @PutMapping
    // @Valid fa im modo
    public ResponseEntity<?> save(@RequestBody @Valid CommentRequest request) {

        // verificare esistenza utente e con ruolo di READER
        if (!commentService.checkUserAndAuthority(request.getAuthor(), "ROLE_READER"))
            //      andiamo a richiamare l'endPoint existsByIdAndAuthority dello UserController
            return new ResponseEntity<>("Author not found", HttpStatus.NOT_FOUND);

        // verificare esistenza post
        if (!commentService.checkIfPostExist(request.getPost()))
            return new ResponseEntity<>("Post not found", HttpStatus.NOT_FOUND);
        Comment comment = commentService.fromRequestToEntity(request);

        // salvare il commento
        commentService.save(comment);
        return new ResponseEntity<>("The comment saved", HttpStatus.CREATED);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getComments(@PathVariable long postId) {
        return new ResponseEntity(commentService.getByPost(postId), HttpStatus.OK);
    }
}
