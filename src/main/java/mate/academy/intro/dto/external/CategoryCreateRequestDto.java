package mate.academy.intro.dto.external;

import jakarta.validation.constraints.NotBlank;

public record CategoryCreateRequestDto(
        @NotBlank
        String name,
        String description
) {
}
