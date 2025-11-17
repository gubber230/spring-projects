package mate.academy.intro.service;

import mate.academy.intro.dto.external.CartItemCreateRequestDto;
import mate.academy.intro.dto.external.CartItemUpdateRequestDto;
import mate.academy.intro.dto.internal.ShoppingCartDto;
import mate.academy.intro.model.User;

public interface ShoppingCartService {
    void createShoppingCart(User user);

    ShoppingCartDto getShoppingCart(Long userId);

    ShoppingCartDto addCartItem(CartItemCreateRequestDto requestDto, Long userId);

    ShoppingCartDto updateQuantityById(Long bookId, CartItemUpdateRequestDto dto, Long userId);

    void deleteById(Long bookId, Long userId);
}
