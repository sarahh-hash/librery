package kg.course.library.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import kg.course.library.ReaderCategory;

public record CreateReaderRequest(
        @NotBlank String cardNumber,
        @NotBlank String fullName,
        @NotNull ReaderCategory category
) {
}