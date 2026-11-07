package kg.course.library;
import java.time.LocalDate;
public enum ReaderCategory {

    STUDENT(14, 3),
    TEACHER(30, 10),
    GUEST(7, 1);

    private final int loanDays;
    private final int maxItems;

    ReaderCategory(int loanDays, int maxItems) {
        this.loanDays = loanDays;
        this.maxItems = maxItems;
    }

    public int getLoanDays() {
        return loanDays;
    }

    public int getMaxItems() {
        return maxItems;
    }

    public LocalDate dueDate(LocalDate issuedOn) {
        if (issuedOn == null) {
            throw new IllegalArgumentException("Дата выдачи не задана");
        }

        return issuedOn.plusDays(loanDays);
    }

    public int maxBooks() {
        return maxItems;
    }
}
