package mate.academy.intro.dto.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CartItemUpdateRequestDto(
        @NotNull
        Integer quantity
) {
}
