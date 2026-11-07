

package kg.course.library.web;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

    public record CreateBookRequest(
            @NotBlank String isbn,
            @NotBlank String title,
            @NotEmpty List<String> authors,
            @Min(1450) int year,
            @Min(1) int copies
    ) {
    }

