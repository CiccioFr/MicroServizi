package it.cgmconsulting.msbackup.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class AuthorityBackupResponse {

    private long id;
    private String authorityName;
}
