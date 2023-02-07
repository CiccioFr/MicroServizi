package it.cgmconsulting.mscomment.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author Fra
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // updatable=false Attributo non aggiornabile: No UPDATE, solo insert
    @Column(nullable = false, updatable = false)
    private String comment;

    private boolean censored = false;

    // si valorizzano in automatico
    @CreationTimestamp
    // la data di creazione non può essere aggiornata
    @Column(updatable = false)
    private LocalDateTime createdAt;

    // le ForegnKey (i loro equivalenti della monolitica)
    private long author;
    private long post;
    // verebbero valorizzati a 0
    // hibernate è così inteliggente che non mettendono i wrapper sul DB li mette NotNull

    public Comment(String comment, long author, long post) {
        this.comment = comment;
        this.author = author;
        this.post = post;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return id == comment.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
