package it.cgmconsulting.mspost.controller;

import it.cgmconsulting.mspost.entity.Post;
import it.cgmconsulting.mspost.service.FileService;
import it.cgmconsulting.mspost.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.Optional;

@RestController
public class PostImageController {

    @Autowired
    FileService fileService;
    @Autowired
    PostService postService;

    @Value("${post.size}")
    private long size;

    @Value("${post.width}")
    private int width;

    @Value("${post.height}")
    private int height;

    @Value("${post.extensions}")
    private String[] extensions;

    @PatchMapping("/add-image/{postId}")
    @Transactional
    public ResponseEntity<?> addImage(@PathVariable long postId, @RequestParam MultipartFile file) {

        if (!fileService.checkSize(file, size))
            return new ResponseEntity("File empty or size grater than " + size, HttpStatus.BAD_REQUEST);

        if (!fileService.checkDimensions(fileService.fromMutipartFileToBufferedImage(file), width, height))
            return new ResponseEntity("Wrong width or height image", HttpStatus.BAD_REQUEST);

        if (!fileService.checkExtension(file, extensions))
            return new ResponseEntity("File type not allowed", HttpStatus.BAD_REQUEST);

        Optional<Post> p = postService.findById(postId);
        if (p.isEmpty())
            return new ResponseEntity("Post not found", HttpStatus.NOT_FOUND);

        String imageToUpload = fileService.uploadPostImage(file, postId, p.get().getImage());
        if (imageToUpload == null)
            return new ResponseEntity("Something went wrong uploading image", HttpStatus.INTERNAL_SERVER_ERROR);

        p.get().setImage(imageToUpload);

        return new ResponseEntity("Image " + imageToUpload + " successfully uploaded", HttpStatus.OK);
    }
}
