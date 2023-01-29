package it.cgmconsulting.msuser.controller;

import it.cgmconsulting.msuser.entity.User;
import it.cgmconsulting.msuser.payload.request.SignupRequest;
import it.cgmconsulting.msuser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserController {

    @Autowired
    UserService userService;

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

    @GetMapping("/{id}/{authority}")
    public ResponseEntity<?> existsByIdAndAuthority(@PathVariable long id, @PathVariable String authorityName){
        return new ResponseEntity(userService.existsByIdAndAuthorityAuthorityName(id, authorityName), HttpStatus.OK);
    }
}
