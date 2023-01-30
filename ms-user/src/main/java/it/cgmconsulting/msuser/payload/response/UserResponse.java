package it.cgmconsulting.msuser.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Struttura di un User per una Response
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private long id;
    private String username;
}
