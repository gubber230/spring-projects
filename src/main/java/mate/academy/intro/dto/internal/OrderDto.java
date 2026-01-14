package mate.academy.intro.dto.internal;

import java.math.BigDecimal;
import java.util.Set;

public record OrderDto(
        Long id,
        Long userId,
        Set<OrderItemDto> orderItems,
        String orderDate,
        BigDecimal total,
        String status
) {
}
