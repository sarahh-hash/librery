package kg.course.library.web;

import kg.course.library.Library;
import kg.course.library.Reader;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/readers")
public class ReaderController {

    private final Library library;

    public ReaderController(Library library) {
        this.library = library;
    }

    // Получить всех читателей
    @GetMapping
    public List<ReaderResponse> getAllReaders() {
        return library.findAllReaders().stream()
                .map(ReaderResponse::from)
                .toList();
    }

    // Получить читателя по номеру карты
    @GetMapping("/{cardNumber}")
    public ReaderResponse getReader(@PathVariable String cardNumber) {
        Reader reader = library.getReader(cardNumber);
        return ReaderResponse.from(reader);
    }

    // Зарегистрировать читателя
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReaderResponse createReader(
            @Valid @RequestBody CreateReaderRequest request) {

        Reader reader = new Reader(
                request.cardNumber(),
                request.fullName(),
                request.category()
        );

        library.registerReader(reader);

        return ReaderResponse.from(reader);
    }
}