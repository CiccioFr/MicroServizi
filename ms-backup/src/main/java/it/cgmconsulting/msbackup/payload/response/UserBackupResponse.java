package it.cgmconsulting.msbackup.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserBackupResponse {

    private long id;
    private String username;
    private String password;
    private String email;
    private boolean enabled;
    private long authority;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
