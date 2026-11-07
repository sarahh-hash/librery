package kg.course.library;

public class PaperBook extends Item {

    private final String author;
    private final int pages;

    public PaperBook(String title, int year, String author, int pages) {
        super(title, year);

        if (author == null || author.isBlank()) {
            throw new IllegalArgumentException("Автор не должен быть пустым");
        }

        if (pages < 1) {
            throw new IllegalArgumentException(
                    "Количество страниц должно быть положительным"
            );
        }

        this.author = author.strip();
        this.pages = pages;
    }

    public String getAuthor() {
        return author;
    }

    public int getPages() {
        return pages;
    }

    @Override
    public int loanDays() {
        return pages > 500 ? 21 : 14;
    }

    @Override
    public String describe() {
        return super.describe() + " Автор: " + author;
    }
}

