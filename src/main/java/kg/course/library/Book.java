package kg.course.library;

import java.time.Year;
import java.util.List;

public record Book(Isbn isbn, String title, List<String> authors, int year) {

    public Book {
        if (isbn == null) {
            throw new IllegalArgumentException("ISBN не задан");
        }

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Название не должно быть пустым");
        }

        if (authors == null || authors.isEmpty()) {
            throw new IllegalArgumentException("Список авторов не должен быть пустым");
        }

        for (String author : authors) {
            if (author == null || author.isBlank()) {
                throw new IllegalArgumentException("Имя автора не должно быть пустым");
            }
        }

        if (year > Year.now().getValue()) {
            throw new IllegalArgumentException("Год не должен превышать текущий");
        }

        title = title.strip();
        authors = List.copyOf(authors);
    }
}

