package kg.course.library.web;

import jakarta.validation.constraints.NotBlank;

public record IssueRequest(
        @NotBlank String isbn,
        @NotBlank String cardNumber
) {
}