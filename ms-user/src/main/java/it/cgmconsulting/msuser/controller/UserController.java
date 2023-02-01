package it.cgmconsulting.msuser.controller;

import it.cgmconsulting.msuser.entity.User;
import it.cgmconsulting.msuser.payload.request.SignupRequest;
import it.cgmconsulting.msuser.payload.response.UserResponse;
import it.cgmconsulting.msuser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    /**
     * Registrazione di un nuovo utente
     *
     * @param request User da salvare in formato Request
     * @return Response del server di salvataggio effettuato
     * o diniego per UserName o Email già usate
     */
    @PutMapping("/singup")
    public ResponseEntity<?> signup(@RequestBody @Valid SignupRequest request) {

        if (userService.existsByUsernameOrEmail(request.getUsername(), request.getEmail()))
            return new ResponseEntity("UserName or Email already in use", HttpStatus.BAD_REQUEST);

        User u = userService.fromRequestToUser(request);
        if (u.getUsername() == null)
            return new ResponseEntity("Authority not Found", HttpStatus.BAD_REQUEST);

        userService.save(u);
        return new ResponseEntity("User " + u.getUsername() + " succefully registred", HttpStatus.CREATED);
    }

    /**
     * Login utente
     *
     * @param id
     * @param authorityName
     * @return
     */
    @GetMapping("/{idAuthority}/{authorityName}")
    public ResponseEntity<?> existsByIdAndAuthority(@PathVariable long idAuthority, @PathVariable String authorityName) {
        return new ResponseEntity(userService.existsByIdAndAuthorityAuthorityName(idAuthority, authorityName), HttpStatus.OK);
    }

    /**
     * Ricerca di Users per Ruolo (passato come parametro)
     *
     * @param authorityName Ruolo da Ricercare
     * @return Response con List di User con Ruolo richiesto
     */
    @GetMapping("/{authorityName}")
    public ResponseEntity<?> getUsersByRole(@PathVariable String authorityName) {
        List<UserResponse> list = userService.getByRole(authorityName);
        return new ResponseEntity(list, HttpStatus.OK);
    }
}
