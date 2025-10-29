package mate.academy.intro.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CreateBookRequestDto(
        @NotBlank
        String title,
        @NotBlank
        String author,
        @NotBlank
        String isbn,
        @NotNull
        @Positive
        BigDecimal price,
        String description,
        String coverImage,
        Set<Long> categoryIds
) {
}
