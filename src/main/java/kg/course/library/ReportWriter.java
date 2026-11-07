package kg.course.library;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ReportWriter {

    public void writeOverdueReport(
            Library library,
            LocalDate today,
            Path file
    ) throws IOException {

        LibraryReports reports = new LibraryReports(library);

        try (BufferedWriter writer =
                     Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {

            writer.write("cardNumber;fullName;title;dueOn;daysOverdue");
            writer.newLine();

            for (Loan loan : reports.overdueLoans(today)) {
                Reader reader = library.getReader(loan.cardNumber());
                Book book = library.getBook(loan.isbn());

                long days = ChronoUnit.DAYS.between(loan.dueOn(), today);

                writer.write(
                        csv(reader.cardNumber()) + ";"
                                + csv(reader.fullName()) + ";"
                                + csv(book.title()) + ";"
                                + loan.dueOn() + ";"
                                + days
                );
                writer.newLine();
            }
        }
    }

    private String csv(String value) {
        if (value.contains(";") || value.contains("\"")
                || value.contains("\n") || value.contains("\r")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}