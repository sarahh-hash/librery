package kg.course.library;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Library library = new Library();

        Isbn javaIsbn = new Isbn("978-5-4461-0914-9");
        Isbn cleanIsbn = new Isbn("978-5-4461-0960-6");

        Book javaBook = new Book(
                javaIsbn, "Java. Эффективное программирование",
                List.of("Джошуа Блох"), 2019
        );

        Book cleanBook = new Book(
                cleanIsbn, "Чистый код",
                List.of("Роберт Мартин"), 2019
        );

        library.addBook(javaBook, 1);
        library.addBook(javaBook, 1);
        library.addBook(cleanBook, 1);

        library.registerReader(new Reader(
                "R-00001", "Сара Уланова", ReaderCategory.STUDENT
        ));
        library.registerReader(new Reader(
                "R-00002", "Алина Иванова", ReaderCategory.GUEST
        ));
        library.registerReader(new Reader(
                "R-00003", "Иван Петров", ReaderCategory.TEACHER
        ));

        LocalDate issuedOn = LocalDate.of(2026, 9, 1);
        LocalDate today = LocalDate.of(2026, 10, 10);

        System.out.println("=== Каталог и Optional ===");
        System.out.println("Всего экземпляров Java: "
                + library.totalCopies(javaIsbn));

        library.findBook(javaIsbn).ifPresent(
                book -> System.out.println("Найдена книга: " + book.title())
        );

        library.findReader("R-00001").ifPresent(
                reader -> System.out.println("Найден читатель: " + reader.fullName())
        );

        System.out.println("\n=== Успешная выдача и возврат ===");

        Loan first = library.issueBook("R-00001", javaIsbn, issuedOn);
        System.out.println("Выдано: " + first);

        Loan returned = library.returnBook(
                first.id(), issuedOn.plusDays(2)
        );
        System.out.println("Возвращено: " + returned);

        // Оставляем одну выдачу студента активной для отчёта о просрочке.
        library.issueBook("R-00001", javaIsbn, issuedOn);

        System.out.println("\n=== Пять ошибочных ситуаций ===");

        showError("1. Читатель не найден", () ->
                library.issueBook("R-99999", javaIsbn, issuedOn));

        showError("2. Книга не найдена", () ->
                library.issueBook(
                        "R-00001", new Isbn("9780306406157"), issuedOn));

        showError("3. Есть просроченная выдача", () ->
                library.issueBook("R-00001", cleanIsbn, today));

        // У гостя лимит — одна книга.
        library.issueBook("R-00002", javaIsbn, issuedOn);

        showError("4. Превышен лимит", () ->
                library.issueBook("R-00002", cleanIsbn, issuedOn));

        // Оба экземпляра Java уже находятся на руках.
        showError("5. Нет свободных экземпляров", () ->
                library.issueBook("R-00003", javaIsbn, issuedOn));

        System.out.println("\n=== Импорт books.csv ===");

        Path booksFile = Path.of("books.csv");
        System.out.println("Путь к файлу: " + booksFile.toAbsolutePath());

        try {
            ImportResult result = new BookCsvImporter().importBooks(booksFile);

            for (ImportedBook imported : result.imported()) {
                library.addBook(imported.book(), imported.copies());
            }

            System.out.println("Импортировано книг: " + result.imported().size());

            for (String error : result.errors()) {
                System.out.println(error);
            }
        } catch (NoSuchFileException e) {
            System.out.println("Файл books.csv не найден");
        } catch (IOException e) {
            System.out.println("Ошибка чтения: " + e.getMessage());
        }

        System.out.println("\n=== Отчёты Stream API ===");

        LibraryReports reports = new LibraryReports(library);

        System.out.println("Поиск по названию:");
        reports.searchByTitle("JAVA").forEach(
                book -> System.out.println(book.title())
        );

        System.out.println("Просроченные выдачи:");
        reports.overdueLoans(today).forEach(System.out::println);

        System.out.println("Выдачи по категориям: " + reports.loansByCategory());
        System.out.println("Все авторы: " + reports.allAuthors());

        System.out.println("Топ-3 книги:");
        reports.topBooks(3).forEach(System.out::println);

        System.out.println("\n=== Коллекции ===");
        System.out.println("Книг: " + library.findAllBooks().size());
        System.out.println("Читателей: " + library.findAllReaders().size());
        System.out.println("Всего выдач: " + library.findAllLoans().size());
        System.out.println("Активные выдачи Сары: "
                + library.activeLoans("R-00001"));

        System.out.println("\n=== Запись overdue.csv ===");

        Path reportFile = Path.of("overdue.csv");

        try {
            new ReportWriter().writeOverdueReport(library, today, reportFile);

            System.out.println("Отчёт сохранён: " + reportFile.toAbsolutePath());
            System.out.println(
                    Files.readString(reportFile, StandardCharsets.UTF_8)
            );
        } catch (IOException e) {
            System.out.println("Ошибка работы с отчётом: " + e.getMessage());
        }
    }

    private static void showError(String title, Runnable action) {
        System.out.println(title);

        action.run();
    }
}