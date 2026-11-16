package kg.course.library.dto;

import java.util.List;

public record BatchLoanRequest(
        String cardNumber,
        List<String> isbns
) {}