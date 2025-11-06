package mate.academy.intro.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import mate.academy.intro.dto.external.CartItemCreateRequestDto;
import mate.academy.intro.dto.external.CartItemUpdateRequestDto;
import mate.academy.intro.dto.internal.CartItemDto;
import mate.academy.intro.mapper.CartItemMapper;
import mate.academy.intro.mapper.ShoppingCartMapper;
import mate.academy.intro.model.CartItem;
import mate.academy.intro.model.ShoppingCart;
import mate.academy.intro.repository.CartItemRepository;
import mate.academy.intro.repository.UserRepository;
import mate.academy.intro.service.CartItemService;
import mate.academy.intro.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final ShoppingCartMapper shoppingCartMapper;
    private final ShoppingCartService shoppingCartService;
    private final UserRepository userRepository;

    @Override
    public CartItemDto addCartItem(CartItemCreateRequestDto requestDto, Long userId) {
        ShoppingCart cart = shoppingCartMapper.toEntity(
                shoppingCartService.getOrCreateCart(userId), userRepository);
        CartItem item = cartItemMapper.toEntity(requestDto);
        cart.addCartItem(item);
        return cartItemMapper.toDto(cartItemRepository.save(item));
    }

    @Override
    public void updateQuantityById(Long bookId, CartItemUpdateRequestDto requestDto, Long userId) {
        CartItem item = cartItemRepository.findById(bookId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find item with Id: " + bookId));
        cartItemMapper.updateQuantity(item, requestDto);
        cartItemRepository.save(item);
    }

    @Override
    public void deleteById(Long bookId, Long useId) {
        cartItemRepository.deleteById(bookId);
    }
}
