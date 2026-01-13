package mate.academy.intro.dto.external;

import jakarta.validation.constraints.NotNull;

public record OrderCreateRequestDto(
        @NotNull
        String shippingAddress
) {
}
