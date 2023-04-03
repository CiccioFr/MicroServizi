package it.cgmconsulting.msbackup.model;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class User {

    @Id
    @CsvBindByName
    private Long id;
    @CsvBindByName
    private String username;
    @CsvBindByName
    private String email;
    @CsvBindByName
    private LocalDateTime createdAt;
    @CsvBindByName
    private LocalDateTime updatedAt;
    @CsvBindByName
    private boolean enabled;
    @CsvBindByName
    private long authority;

}
