package it.cgmconsulting.msuser.payload.request;

import it.cgmconsulting.msuser.entity.Authority;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class SignupRequest {

    @NotBlank @Size(min = 6, max = 20)
    private String username;
    @NotBlank @Size(min = 6)
    private String password;
    @Email @NotBlank
    private String email;
    @NotBlank @Size(min = 6, max = 20)
    private String authorityName;
}
