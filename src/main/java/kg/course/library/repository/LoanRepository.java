package kg.course.library.repository;

import kg.course.library.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    boolean existsByCardNumberAndReturnedOnIsNullAndDueOnBefore(
            String cardNumber,
            LocalDate date
    );
}