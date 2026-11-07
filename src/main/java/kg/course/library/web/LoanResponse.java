package kg.course.library.web;

import kg.course.library.Loan;

import java.time.LocalDate;

public record LoanResponse(
        long id,
        String isbn,
        String cardNumber,
        LocalDate issuedOn,
        LocalDate dueOn,
        LocalDate returnedOn
) {
    public static LoanResponse from(Loan loan) {
        return new LoanResponse(
                loan.id(),
                loan.isbn().value(),
                loan.cardNumber(),
                loan.issuedOn(),
                loan.dueOn(),
                loan.returnedOn()
        );
    }
}