package kg.course.library.web;

import kg.course.library.LibraryReports;
import kg.course.library.BookStat;
import kg.course.library.ReaderCategory;

import org.springframework.web.bind.annotation.*;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final LibraryReports reports;
    private final Clock clock;

    public ReportController(LibraryReports reports, Clock clock) {
        this.reports = reports;
        this.clock = clock;
    }

    // Просроченные выдачи
    @GetMapping("/overdue")
    public List<LoanResponse> overdueLoans() {
        return reports.overdueLoans(LocalDate.now(clock))
                .stream()
                .map(LoanResponse::from)
                .toList();
    }

    // Количество выдач по категориям читателей
    @GetMapping("/loans-by-category")
    public Map<ReaderCategory, Long> loansByCategory() {
        return reports.loansByCategory();
    }

    // Список всех авторов
    @GetMapping("/authors")
    public SortedSet<String> allAuthors() {
        return reports.allAuthors();
    }

    // Самые популярные книги
    @GetMapping("/top-books")
    public List<BookStat> topBooks(
            @RequestParam(defaultValue = "5") int limit) {
        return reports.topBooks(limit);
    }
}