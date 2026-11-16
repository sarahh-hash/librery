package kg.course.library.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "authors")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    protected Author() { }

    public Author(String name) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Имя автора не может быть пустым");
        this.name = name.trim();
    }

    public Long getId() { return id; }
    public String getName() { return name; }

    // Равенство по первичному ключу; у ещё не сохранённых сущностей (id == null) равны только они сами.
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Author other)) return false;
        return getId() != null && getId().equals(other.getId());
    }
    @Override public int hashCode() { return Author.class.hashCode(); }
    @Override public String toString() { return "Author[" + id + ", " + name + "]"; }
}