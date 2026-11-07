package kg.course.library;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LibraryTest {

    private Library library;
    private Isbn isbn;
    private LocalDate date;

    @BeforeEach
    void setUp() {
        library = new Library();
        isbn = new Isbn("9785446109149");
        date = LocalDate.of(2026, 9, 1);

        library.addBook(new Book(
                isbn, "Java", List.of("Автор"), 2020
        ), 1);

        library.registerReader(new Reader(
                "R-00001", "Сара", ReaderCategory.GUEST
        ));

        library.registerReader(new Reader(
                "R-00002", "Алина", ReaderCategory.STUDENT
        ));
    }

    @Test
    void issueRejectsUnknownReaderFirst() {
        // Одновременно отсутствуют читатель и книга.
        NotFoundException error = assertThrows(
                NotFoundException.class,
                () -> library.issueBook(
                        "R-99999", new Isbn("9780306406157"), date
                )
        );

        assertTrue(error.getMessage().contains("Читатель"));
        assertTrue(library.findAllLoans().isEmpty());
    }

    @Test
    void issueRejectsUnknownBookBeforeOverdueCheck() {
        library.issueBook("R-00001", isbn, date);

        NotFoundException error = assertThrows(
                NotFoundException.class,
                () -> library.issueBook(
                        "R-00001",
                        new Isbn("9780306406157"),
                        date.plusDays(8)
                )
        );

        assertTrue(error.getMessage().contains("Книга"));
        assertEquals(1, library.findAllLoans().size());
    }

    @Test
    void issueRejectsOverdueBeforeLimitAndAvailability() {
        library.issueBook("R-00001", isbn, date);

        assertThrows(
                OverdueException.class,
                () -> library.issueBook("R-00001", isbn, date.plusDays(8))
        );

        assertEquals(1, library.findAllLoans().size());
    }

    @Test
    void issueRejectsLimitBeforeAvailability() {
        library.issueBook("R-00001", isbn, date);

        assertThrows(
                LimitExceededException.class,
                () -> library.issueBook("R-00001", isbn, date)
        );

        assertEquals(1, library.findAllLoans().size());
    }

    @Test
    void issueRejectsUnavailableBook() {
        library.issueBook("R-00001", isbn, date);

        assertThrows(
                BookUnavailableException.class,
                () -> library.issueBook("R-00002", isbn, date)
        );

        assertEquals(1, library.findAllLoans().size());
    }

    @Test
    void issueSucceedsAndAssignsSequentialIds() {
        Loan first = library.issueBook("R-00001", isbn, date);

        assertEquals(1, first.id());
        assertEquals(isbn, first.isbn());
        assertEquals("R-00001", first.cardNumber());
        assertEquals(date, first.issuedOn());
        assertEquals(date.plusDays(7), first.dueOn());
        assertTrue(first.isActive());
        assertEquals(0, library.availableCopies(isbn));

        library.returnBook(first.id(), date.plusDays(1));

        Loan second = library.issueBook(
                "R-00002", isbn, date.plusDays(1)
        );

        assertEquals(2, second.id());
        assertEquals(date.plusDays(15), second.dueOn());
        assertFalse(library.findAllLoans().get(0).isActive());
    }
}