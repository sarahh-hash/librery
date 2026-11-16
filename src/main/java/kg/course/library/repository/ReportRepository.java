package kg.course.library.repository;

import kg.course.library.BookStat;
import kg.course.library.BookSummary;
import kg.course.library.ReaderCategory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Repository
public class ReportRepository {

    private final JdbcClient jdbc;

    public ReportRepository(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    /**
     * Популярные книги.
     */
    public List<BookStat> topBooks(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("n должно быть >= 1");
        }

        return jdbc.sql("""
                SELECT b.title AS title, COUNT(l.id) AS loans
                FROM books b
                LEFT JOIN loans l ON l.book_isbn = b.isbn
                GROUP BY b.isbn, b.title
                ORDER BY COUNT(l.id) DESC, b.title
                LIMIT :n
                """)
                .param("n", n)
                .query((rs, rowNum) ->
                        new BookStat(
                                rs.getString("title"),
                                rs.getLong("loans")
                        ))
                .list();
    }

    /**
     * Количество выдач по категориям читателей.
     */
    public Map<ReaderCategory, Long> loansByCategory() {

        Map<ReaderCategory, Long> result =
                new EnumMap<>(ReaderCategory.class);

        jdbc.sql("""
                SELECT r.category AS category, COUNT(l.id) AS cnt
                FROM loans l
                JOIN readers r ON r.card_number = l.card_number
                GROUP BY r.category
                """)
                .query((rs, rowNum) ->
                        Map.entry(
                                ReaderCategory.valueOf(rs.getString("category")),
                                rs.getLong("cnt")
                        ))
                .list()
                .forEach(e -> result.put(e.getKey(), e.getValue()));

        return result;
    }

    /**
     * Авторы существующих книг в алфавитном порядке.
     */
    public List<String> allAuthors() {

        return jdbc.sql("""
                SELECT a.name AS name
                FROM authors a
                WHERE EXISTS (
                    SELECT 1
                    FROM book_authors ba
                    WHERE ba.author_id = a.id
                )
                ORDER BY a.name COLLATE "C"
                """)
                .query(String.class)
                .list();
    }

    /**
     * Задание 7: безопасный поиск книг по фрагменту названия.
     */
    public List<BookSummary> searchByTitle(String title) {

        return jdbc.sql("""
                SELECT isbn, title, year
                FROM books
                WHERE LOWER(title) LIKE LOWER(:pattern)
                ORDER BY title
                """)
                .param("pattern", "%" + title + "%")
                .query((rs, rowNum) ->
                        new BookSummary(
                                rs.getString("isbn"),
                                rs.getString("title"),
                                rs.getInt("year")
                        ))
                .list();
    }
}