
package kg.course.library.config;

import kg.course.library.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;

@Configuration
public class LibraryConfig {

    private static final Logger log =
            LoggerFactory.getLogger(LibraryConfig.class);
    //@Bean — это аннотация, которая регистрирует объект в контейнере Spring.
    // После этого Spring может автоматически внедрять
    // его в другие классы с помощью Dependency Injection.
    @Bean
    public Library library() {
        return new Library();
    }

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
    @Bean
    public LibraryReports libraryReports(Library library) {
        return new LibraryReports(library);
    }

    @Bean
    public BookCsvImporter bookCsvImporter() {
        return new BookCsvImporter();
    }

    @Bean
    public ApplicationRunner initializeLibrary(
            Library library,
            BookCsvImporter importer,
            @Value("${library.import-file:books.csv}") String importFile
    ) {
        return args -> {

            // Импорт книг
            Path path = Path.of(importFile);

            if (Files.exists(path)) {

                try {
                    ImportResult result = importer.importBooks(path);

                    for (ImportedBook imported : result.imported()) {
                        library.addBook(
                                imported.book(),
                                imported.copies()
                        );
                    }

                    log.info("Импортировано книг: {}",
                            result.imported().size());

                    for (String error : result.errors()) {
                        log.warn("Ошибка импорта: {}", error);
                    }

                } catch (Exception e) {
                    log.error("Ошибка чтения файла книг", e);
                }

            } else {
                log.warn("Файл {} не найден. Каталог пуст.", importFile);
            }

            // Регистрация трёх читателей
            library.registerReader(
                    new Reader("R-00001", "Иван Иванов",
                            ReaderCategory.STUDENT)
            );

            library.registerReader(
                    new Reader("R-00002", "Петр Петров",
                            ReaderCategory.TEACHER)
            );

            library.registerReader(
                    new Reader("R-00003", "Анна Сидорова",
                            ReaderCategory.GUEST)
            );

            log.info("Три читателя зарегистрированы");
        };
    }
}
