package kg.course.library.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "loans")
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_isbn", nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "card_number", nullable = false)
    private Reader reader;

    @Column(name = "issued_on", nullable = false)
    private LocalDate issuedOn;

    @Column(name = "due_on", nullable = false)
    private LocalDate dueOn;

    @Column(name = "returned_on")
    private LocalDate returnedOn;

    protected Loan() { }

    public Loan(Book book, Reader reader, LocalDate issuedOn, LocalDate dueOn) {
        Objects.requireNonNull(book, "Книга");
        Objects.requireNonNull(reader, "Читатель");
        Objects.requireNonNull(issuedOn, "Дата выдачи");
        Objects.requireNonNull(dueOn, "Срок возврата");
        if (dueOn.isBefore(issuedOn))
            throw new IllegalArgumentException("Срок возврата раньше выдачи");
        this.book = book;
        this.reader = reader;
        this.issuedOn = issuedOn;
        this.dueOn = dueOn;
    }

    public Long getId() { return id; }
    public Book getBook() { return book; }
    public Reader getReader() { return reader; }
    public LocalDate getIssuedOn() { return issuedOn; }
    public LocalDate getDueOn() { return dueOn; }
    public LocalDate getReturnedOn() { return returnedOn; }

    public boolean isActive() { return returnedOn == null; }

    public boolean isOverdue(LocalDate today) { return isActive() && today.isAfter(dueOn); }

    /** Оформляет возврат книги. */
    public void markReturned(LocalDate date) {
        Objects.requireNonNull(date, "Дата возврата");
        if (!isActive()) throw new IllegalStateException("Книга уже возвращена");
        if (date.isBefore(issuedOn))
            throw new IllegalArgumentException("Дата возврата раньше выдачи");
        this.returnedOn = date;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Loan other)) return false;
        return getId() != null && getId().equals(other.getId());
    }
    @Override public int hashCode() { return Loan.class.hashCode(); }
}