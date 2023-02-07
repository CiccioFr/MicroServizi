package it.cgmconsulting.mspost.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Struttura Response di un User
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private long id;
    private String username;
}
