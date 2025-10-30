package mate.academy.intro.dto.external;

import jakarta.validation.constraints.NotBlank;

public record CreateCategoryRequestDto(
        @NotBlank
        String name,
        String description
) {
}
