package kg.course.library;

public record ImportedBook(Book book, int copies) {
    public ImportedBook {
        if (book == null || copies < 1) {
            throw new IllegalArgumentException("Неверные данные импортированной книги");
        }
    }
}