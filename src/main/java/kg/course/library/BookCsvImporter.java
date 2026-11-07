package kg.course.library;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class BookCsvImporter {

    public ImportResult importBooks(Path file) throws IOException {
        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);

        List<ImportedBook> imported = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        // Первая строка — заголовок.
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);

            if (line.isBlank()) {
                continue;
            }

            try {
                String[] fields = line.split(";", -1);

                if (fields.length != 5) {
                    throw new IllegalArgumentException("Ожидается 5 полей");
                }

                Isbn isbn = new Isbn(fields[0].strip());
                String title = fields[1].strip();

                List<String> authors = new ArrayList<>();
                for (String author : fields[2].split(",", -1)) {
                    if (author.isBlank()) {
                        throw new IllegalArgumentException("Отсутствует автор");
                    }
                    authors.add(author.strip());
                }

                int year = parseNumber(fields[3], "Год должен быть числом");
                int copies = parseNumber(fields[4], "Количество должно быть числом");

                if (copies < 1) {
                    throw new IllegalArgumentException("Количество меньше 1");
                }

                Book book = new Book(isbn, title, authors, year);
                imported.add(new ImportedBook(book, copies));

            } catch (IllegalArgumentException e) {
                errors.add("Строка " + (i + 1) + ": " + e.getMessage());
            }
        }

        return new ImportResult(imported, errors);
    }

    private int parseNumber(String value, String message) {
        try {
            return Integer.parseInt(value.strip());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(message);
        }
    }
}