package it.cgmconsulting.msrating.controller;

import it.cgmconsulting.msrating.entity.Rating;
import it.cgmconsulting.msrating.entity.RatingId;
import it.cgmconsulting.msrating.payload.request.RatingRequest;
import it.cgmconsulting.msrating.service.RatingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RatingController {
    @Autowired
    RatingService ratingService;

    /**
     * <p> Salva un voto fatto al Post, verificando che l'utente abbia ruolo di READER </p>
     * Se l'utente ha già votato il Post, sarà eseguita una Update del voto
     *
     * @param request RatingRequest
     * @return ResponseEntity
     */
    @PutMapping
    public ResponseEntity<?> save(@RequestBody @Valid RatingRequest request) {
        // verifica che l'user ha ROLE_RADER
        if (!ratingService.checkUserAndAuthority(request.getUserId(), "ROLE_READER"))
            return new ResponseEntity("You are not a reader", HttpStatus.FORBIDDEN);

        // salvare il voto se PrimaryKey rating non esiste, altrimenti andare un update
        Rating r = new Rating(new RatingId(request.getUserId(), request.getPostId()), request.getRate());
        ratingService.save(r);
        return new ResponseEntity("The rating is posted", HttpStatus.OK);
    }

    /**
     * Restituisce la media dei voti di un Post
     *
     * @param postId post di cui si richiede la media
     * @return
     */
    @GetMapping("/{postId}")
    public ResponseEntity<?> getAverage(@PathVariable long postId){
        return new ResponseEntity(ratingService.getAverage(postId), HttpStatus.OK);
    }

    @GetMapping("backup")
    public ResponseEntity<?> getBackupRatings(){
        return new ResponseEntity(ratingService.getBackupRatings(), HttpStatus.OK);
    }
}
