package mate.academy.intro.service;

import mate.academy.intro.dto.internal.ShoppingCartDto;

public interface ShoppingCartService {
    ShoppingCartDto getOrCreateCart(Long userId);
}
