package kg.course.library.web;

import kg.course.library.Isbn;
import kg.course.library.Library;
import kg.course.library.Loan;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final Library library;
    private final Clock clock;

    public LoanController(Library library, Clock clock) {
        this.library = library;
        this.clock = clock;
    }

    // Получить все выдачи
    @GetMapping
    public List<LoanResponse> getAllLoans() {
        return library.findAllLoans().stream()
                .map(LoanResponse::from)
                .toList();
    }

    // Получить активные выдачи читателя
    @GetMapping("/reader/{cardNumber}")
    public List<LoanResponse> getReaderLoans(
            @PathVariable String cardNumber) {
        return library.activeLoans(cardNumber).stream()
                .map(LoanResponse::from)
                .toList();
    }

    // Выдать книгу
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LoanResponse issueBook(
            @Valid @RequestBody IssueRequest request) {

        Loan loan = library.issueBook(
                request.cardNumber(),
                new Isbn(request.isbn()),
                LocalDate.now(clock)
        );

        return LoanResponse.from(loan);
    }

    // Вернуть книгу
    @PostMapping("/{id}/return")
    public LoanResponse returnBook(@PathVariable long id) {
        Loan loan = library.returnBook(id, LocalDate.now(clock));
        return LoanResponse.from(loan);
    }
}