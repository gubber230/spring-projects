package mate.academy.intro.dto.external;

import java.util.Set;
import mate.academy.intro.dto.internal.CartItemDto;

public record ShoppingCartResponseDto(
        Long id,
        Long userId,
        Set<CartItemDto> cartItems
) {
}
