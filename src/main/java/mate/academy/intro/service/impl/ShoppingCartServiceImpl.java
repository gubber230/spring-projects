package mate.academy.intro.service.impl;

import lombok.RequiredArgsConstructor;
import mate.academy.intro.mapper.ShoppingCartMapper;
import mate.academy.intro.model.ShoppingCart;
import mate.academy.intro.model.User;
import mate.academy.intro.repository.ShoppingCartRepository;
import mate.academy.intro.repository.UserRepository;
import mate.academy.intro.service.ShoppingCartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ShoppingCart getOrCreateCart(Long userId) {
        return shoppingCartRepository.findByUserId(userId)
                .orElseGet(() -> createCart(userId));
    }

    private ShoppingCart createCart(Long userId) {
        User user = userRepository.getReferenceById(userId);
        ShoppingCart cart = shoppingCartMapper.createNewCart(user);
        return shoppingCartRepository.save(cart);
    }
}
