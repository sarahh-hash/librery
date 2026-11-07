package kg.course.library.web;

import kg.course.library.Book;

import java.util.List;

public record BookResponse(
        String isbn,
        String title,
        List<String> authors,
        int year,
        int totalCopies,
        int availableCopies
) {
    public static BookResponse from(
            Book book,
            int totalCopies,
            int availableCopies
    ) {
        return new BookResponse(
                book.isbn().value(),
                book.title(),
                book.authors(),
                book.year(),
                totalCopies,
                availableCopies
        );
    }
}