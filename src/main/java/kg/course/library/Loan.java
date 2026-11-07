package kg.course.library;

import java.time.LocalDate;
import java.util.Objects;

public record Loan(
        long id,
        Isbn isbn,
        String cardNumber,
        LocalDate issuedOn,
        LocalDate dueOn,
        LocalDate returnedOn
) {

    public Loan {

        if (id < 1) {
            throw new IllegalArgumentException(
                    "ID выдачи должен быть положительным"
            );
        }

        Objects.requireNonNull(isbn, "ISBN не задан");
        Objects.requireNonNull(cardNumber, "Номер билета не задан");
        Objects.requireNonNull(issuedOn, "Дата выдачи не задана");
        Objects.requireNonNull(dueOn, "Срок возврата не задан");

        if (dueOn.isBefore(issuedOn)) {

            throw new IllegalArgumentException(
                    "Срок возврата не может быть раньше даты выдачи"
            );
        }

        if (returnedOn != null &&
                returnedOn.isBefore(issuedOn)) {

            throw new IllegalArgumentException(
                    "Дата возврата не может быть раньше даты выдачи"
            );
        }
    }

    public boolean isActive() {
        return returnedOn == null;
    }

    public boolean isOverdue(LocalDate today) {

        Objects.requireNonNull(today, "Дата проверки не задана");

        return isActive() && today.isAfter(dueOn);
    }

    public Loan returned(LocalDate date) {

        if (!isActive()) {

            throw new IllegalStateException(
                    "Книга уже возвращена"
            );
        }

        Objects.requireNonNull(date, "Дата возврата не задана");

        return new Loan(
                id,
                isbn,
                cardNumber,
                issuedOn,
                dueOn,
                date
        );
    }
}






























































































































































































































































































































































































































































































