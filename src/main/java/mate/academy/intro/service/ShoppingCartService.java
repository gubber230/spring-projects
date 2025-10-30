package mate.academy.intro.service;

import mate.academy.intro.dto.external.CreateCartItemRequestDto;
import mate.academy.intro.dto.external.ShoppingCartResponseDto;
import mate.academy.intro.dto.external.UpdateCartItemRequestDto;
import mate.academy.intro.dto.internal.CartItemDto;

public interface ShoppingCartService {
    ShoppingCartResponseDto get();

    CartItemDto save(CreateCartItemRequestDto requestDto);

    CartItemDto updateQuantity(UpdateCartItemRequestDto requestDto);

    void deleteById(Long id);
}
