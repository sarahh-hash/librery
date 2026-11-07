package kg.course.library.web;

import kg.course.library.Book;
import kg.course.library.Isbn;
import kg.course.library.Library;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final Library library;

    public BookController(Library library) {
        this.library = library;
    }

    // Получить все книги
    @GetMapping
    public List<BookResponse> getAllBooks(
            @RequestParam(required = false) String search) {

        return library.findAllBooks().stream()
                .filter(book -> search == null || search.isBlank()
                        || book.title().toLowerCase().contains(search.toLowerCase())
                        || book.authors().stream()
                        .anyMatch(a -> a.toLowerCase().contains(search.toLowerCase())))
                .map(book -> BookResponse.from(
                        book,
                        library.totalCopies(book.isbn()),
                        library.availableCopies(book.isbn())
                ))
                .toList();
    }

    // Получить книгу по ISBN
    @GetMapping("/{isbn}")
    public BookResponse getBook(@PathVariable String isbn) {
        Isbn bookIsbn = new Isbn(isbn);
        Book book = library.getBook(bookIsbn);

        return BookResponse.from(
                book,
                library.totalCopies(bookIsbn),
                library.availableCopies(bookIsbn)
        );
    }

    // Добавить книгу
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponse createBook(
            @Valid @RequestBody CreateBookRequest request) {

        Isbn isbn = new Isbn(request.isbn());

        Book book = new Book(
                isbn,
                request.title(),
                request.authors(),
                request.year()
        );

        library.addBook(book, request.copies());

        return BookResponse.from(
                book,
                library.totalCopies(isbn),
                library.availableCopies(isbn)
        );
    }

    // Удалить книгу
    @DeleteMapping("/{isbn}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable String isbn) {
        library.removeBook(new Isbn(isbn));
    }
}