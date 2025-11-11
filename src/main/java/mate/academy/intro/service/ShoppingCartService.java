package mate.academy.intro.service;

import mate.academy.intro.model.ShoppingCart;

public interface ShoppingCartService {
    ShoppingCart getOrCreateCart(Long userId);
}
