package it.cgmconsulting.mspost.entity;

import it.cgmconsulting.mspost.entity.common.CreationUpdate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Check(constraints = "author > 0")
public class Post extends CreationUpdate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true, length = 100)
    private String title;

    @Column(nullable = false)
    private String overview;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    private boolean published = false;

    private String image;

    private long author;

    public Post(String title, String overview, String content, long author) {
        this.title = title;
        this.overview = overview;
        this.content = content;
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return id == post.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
