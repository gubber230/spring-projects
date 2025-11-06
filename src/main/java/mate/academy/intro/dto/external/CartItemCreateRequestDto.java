package mate.academy.intro.dto.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CartItemCreateRequestDto(
        @NotNull
        Long bookId,
        @Positive
        Integer quantity
) {
}
