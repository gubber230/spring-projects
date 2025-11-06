package mate.academy.intro.service;

import mate.academy.intro.dto.external.CartItemCreateRequestDto;
import mate.academy.intro.dto.external.CartItemUpdateRequestDto;
import mate.academy.intro.dto.internal.CartItemDto;

public interface CartItemService {
    CartItemDto addCartItem(CartItemCreateRequestDto requestDto, Long userId);

    void updateQuantityById(Long bookId, CartItemUpdateRequestDto requestDto, Long userId);

    void deleteById(Long bookId, Long useId);
}
