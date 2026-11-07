package kg.course.library;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class LibraryReports {

    private final Library library;

    public LibraryReports(Library library) {
        this.library = Objects.requireNonNull(library);
    }

    public List<Book> searchByTitle(String fragment) {
        if (fragment == null) {
            throw new IllegalArgumentException("Фрагмент не задан");
        }

        String text = fragment.toLowerCase(Locale.ROOT);

        return library.findAllBooks().stream()
                .filter(book -> book.title()
                        .toLowerCase(Locale.ROOT).contains(text))
                .sorted(Comparator.comparing(Book::title))
                .toList();
    }

    public List<Loan> overdueLoans(LocalDate today) {
        Objects.requireNonNull(today);

        return library.findAllLoans().stream()
                .filter(loan -> loan.isOverdue(today))
                .sorted(Comparator.comparing(Loan::dueOn))
                .toList();
    }

    public Map<ReaderCategory, Long> loansByCategory() {
        return library.findAllLoans().stream()
                .collect(Collectors.groupingBy(
                        loan -> library.getReader(loan.cardNumber()).category(),
                        Collectors.counting()
                ));
    }

    public SortedSet<String> allAuthors() {
        return library.findAllBooks().stream()
                .flatMap(book -> book.authors().stream())
                .collect(Collectors.toCollection(TreeSet::new));
    }

    public List<BookStat> topBooks(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Количество не должно быть отрицательным");
        }

        Map<Isbn, Long> counts = library.findAllLoans().stream()
                .collect(Collectors.groupingBy(
                        Loan::isbn,
                        Collectors.counting()
                ));

        return library.findAllBooks().stream()
                .map(book -> new BookStat(
                        book.title(),
                        counts.getOrDefault(book.isbn(), 0L)
                ))
                .sorted(Comparator.comparingLong(BookStat::loans)
                        .reversed()
                        .thenComparing(BookStat::title))
                .limit(n)
                .toList();
    }
}