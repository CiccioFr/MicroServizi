package it.cgmconsulting.mspost.controller;

import it.cgmconsulting.mspost.entity.Post;
import it.cgmconsulting.mspost.service.PdfService;
import it.cgmconsulting.mspost.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.util.Optional;

@RestController
@RequestMapping("pdf")
public class PdfController {

    @Autowired PostService postService;
    @Autowired PdfService pdfService;

    @GetMapping("/{id}")
    public ResponseEntity<?> createPdf(@PathVariable long id) {

        Optional<Post> p = postService.findByIdAndPublishedTrue(id);
        if (!p.isPresent())
            return new ResponseEntity<String>("Post not found", HttpStatus.NOT_FOUND);

        InputStream pdfFile = null;
        ResponseEntity<InputStreamResource> responseEntity = null;

        try {
            pdfFile = pdfService.createPdfFromPost(p.get());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/pdf"));
            headers.add("Access-Control-Allow-Origin", "*");
            headers.add("Access-Control-Allow-Method", "GET");
            headers.add("Access-Control-Allow-Header", "Content-Type");
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
            headers.add("Content-disposition", "inline; filename=" + p.get().getId() + ".pdf");

            responseEntity = new ResponseEntity<InputStreamResource>(
                    new InputStreamResource(pdfFile),
                    headers,
                    HttpStatus.OK
            );

        } catch (Exception ex) {
            responseEntity = new ResponseEntity<InputStreamResource>(
                    new InputStreamResource(null, "Error creating PDF"),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
        return responseEntity;
    }
}
