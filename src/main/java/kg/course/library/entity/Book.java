package kg.course.library.entity;

import jakarta.persistence.*;
import kg.course.library.Isbn;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @Column(length = 13)
    private String isbn;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    private int copies;

    @ManyToMany
    @JoinTable(name = "book_authors",
            joinColumns = @JoinColumn(name = "book_isbn"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    private Set<Author> authors = new LinkedHashSet<>();

    protected Book() { }

    public Book(Isbn isbn, String title, Collection<Author> authors, int year, int copies) {
        if (isbn == null) throw new IllegalArgumentException("ISBN не задан");
        if (title == null || title.isBlank()) throw new IllegalArgumentException("Название книги пустое");
        if (authors == null || authors.isEmpty() || authors.stream().anyMatch(a -> a == null))
            throw new IllegalArgumentException("Список авторов пуст или содержит пустого автора");
        if (year < 1450 || year > LocalDate.now().getYear())
            throw new IllegalArgumentException("Некорректный год издания: " + year);
        if (copies < 1) throw new IllegalArgumentException("Количество экземпляров должно быть не меньше 1");
        this.isbn = isbn.value();
        this.title = title.trim();
        this.authors = new LinkedHashSet<>(authors);
        this.year = year;
        this.copies = copies;
    }

    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public int getYear() { return year; }
    public int getCopies() { return copies; }
    public Set<Author> getAuthors() { return Collections.unmodifiableSet(authors); }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book other)) return false;
        return getIsbn() != null && getIsbn().equals(other.getIsbn());
    }
    @Override public int hashCode() { return Book.class.hashCode(); }
    @Override public String toString() { return "Book[" + isbn + ", " + title + "]"; }
}