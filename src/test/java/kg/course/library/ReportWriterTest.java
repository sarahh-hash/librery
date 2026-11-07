package kg.course.library;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReportWriterTest {

    @TempDir
    Path directory;

    @Test
    void writesOnlyActiveOverdueLoansInDueDateOrder() throws IOException {
        Library library = new Library();
        Isbn isbn = new Isbn("9785446109149");

        library.addBook(new Book(
                isbn, "Java", List.of("Автор"), 2020
        ), 4);

        library.registerReader(new Reader(
                "R-00001", "Сара", ReaderCategory.STUDENT
        ));
        library.registerReader(new Reader(
                "R-00002", "Алина", ReaderCategory.GUEST
        ));
        library.registerReader(new Reader(
                "R-00003", "Иван", ReaderCategory.TEACHER
        ));

        LocalDate issuedOn = LocalDate.of(2026, 9, 1);
        LocalDate today = LocalDate.of(2026, 9, 20);

        // Студент: срок 15 сентября.
        library.issueBook("R-00001", isbn, issuedOn);

        // Гость: срок 8 сентября.
        library.issueBook("R-00002", isbn, issuedOn);

        // Возвращённая выдача не должна попасть в отчёт.
        Loan returned = library.issueBook("R-00001", isbn, issuedOn);
        library.returnBook(returned.id(), issuedOn.plusDays(1));

        // Срок преподавателя ещё не истёк.
        library.issueBook("R-00003", isbn, issuedOn);

        Path file = directory.resolve("overdue.csv");

        new ReportWriter().writeOverdueReport(library, today, file);

        assertEquals(
                List.of(
                        "cardNumber;fullName;title;dueOn;daysOverdue",
                        "R-00002;Алина;Java;2026-09-08;12",
                        "R-00001;Сара;Java;2026-09-15;5"
                ),
                Files.readAllLines(file)
        );
    }
}