package kg.course.library;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookCsvImporterTest {

    @TempDir
    Path directory;

    @Test
    void importsValidBooksAndReportsLineNumbers() throws IOException {
        Path file = directory.resolve("books.csv");

        Files.writeString(file, """
                isbn;title;authors;year;copies
                978-5-4461-0914-9;Java. Эффективное программирование;Джошуа Блох;2019;2
                978-5-17-090630-2;Война и мир;Л. Н. Толстой;2015;1
                978-5-4461-0960-6;Чистый код;Роберт Мартин;2019;1
                978-5-4461-1101-2;Java. Библиотека профессионала;Кей Хорстманн, Гари Корнелл;2022;3
                123-456;Книга с неверным ISBN;Автор;2020;1
                978-0-306-40615-7;Книга без автора;;2020;1
                978-0-262-03384-8;Алгоритмы;Кормен, Лейзерсон, Ривест;год;1
                978-0-13-468599-1;Неполная строка;Автор
                """);

        ImportResult result = new BookCsvImporter().importBooks(file);

        assertEquals(4, result.imported().size());
        assertEquals(4, result.errors().size());

        for (int i = 0; i < 4; i++) {
            assertTrue(result.errors().get(i)
                    .startsWith("Строка " + (i + 6) + ":"));
        }

        assertEquals(2, result.imported().get(0).copies());
        assertEquals(
                List.of("Кей Хорстманн", "Гари Корнелл"),
                result.imported().get(3).book().authors()
        );
    }

    @Test
    void skipsBlankLinesAndChecksCopies() throws IOException {
        Path file = directory.resolve("copies.csv");

        Files.writeString(file, """
                isbn;title;authors;year;copies

                9785446109149;Java;Автор;2020;abc
                9785446109149;Java;Автор;2020;0
                9785446109149;Java;Автор;2020;2
                """);

        ImportResult result = new BookCsvImporter().importBooks(file);

        assertEquals(1, result.imported().size());
        assertEquals(2, result.errors().size());
        assertTrue(result.errors().get(0).startsWith("Строка 3:"));
        assertTrue(result.errors().get(1).startsWith("Строка 4:"));
    }
}