package mate.academy.intro.dto.internal;

public record OrderItemDto(
        Long id,
        Long bookId,
        Integer quantity
) {
}
