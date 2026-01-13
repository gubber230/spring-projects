package mate.academy.intro.dto.external;

import jakarta.validation.constraints.NotNull;
import mate.academy.intro.model.Order;

public record OrderPatchRequestDto(
        @NotNull
        Order.Status status
) {
}
