package mate.academy.intro.service;

import mate.academy.intro.dto.external.CartItemCreateRequestDto;
import mate.academy.intro.dto.external.CartItemUpdateRequestDto;
import mate.academy.intro.dto.internal.ShoppingCartDto;
import mate.academy.intro.model.ShoppingCart;
import mate.academy.intro.model.User;

public interface ShoppingCartService {
    ShoppingCart getOrCreateCart(User user);

    ShoppingCartDto addCartItem(CartItemCreateRequestDto requestDto, User user);

    ShoppingCartDto updateQuantityById(Long bookId, CartItemUpdateRequestDto requestDto, User user);

    void deleteById(Long bookId, User user);
}
