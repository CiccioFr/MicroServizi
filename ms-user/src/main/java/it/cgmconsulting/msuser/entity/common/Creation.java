package it.cgmconsulting.msuser.entity.common;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

//nel monento in cui viene estesa
// senza la trasposizione sul DB non avverrebbe
@MappedSuperclass
@Getter
@Setter
public class Creation implements Serializable {

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
