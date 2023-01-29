package it.cgmconsulting.mspost.controller;

import it.cgmconsulting.mspost.entity.Post;
import it.cgmconsulting.mspost.payload.request.PostRequest;
import it.cgmconsulting.mspost.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class PostController {

    @Autowired
    PostService postService;

    @PutMapping
    public ResponseEntity<?> save(@RequestBody @Valid PostRequest request) {

        if (!postService.checkUserAndAuthority(request.getAuthor(), "ROLE_EDITOR"))
            return new ResponseEntity("No Author found", HttpStatus.NOT_FOUND);

        if (postService.checkTitle(request.getTitle()))
            return new ResponseEntity("The post with " + request.getTitle() + " already exist", HttpStatus.BAD_REQUEST);

        Post p = postService.fromRequestToEntity(request);
        postService.save(p);
        return new ResponseEntity("The Post " + p.getTitle() + " has been saved with id " + p.getId(), HttpStatus.CREATED);
    }
}
