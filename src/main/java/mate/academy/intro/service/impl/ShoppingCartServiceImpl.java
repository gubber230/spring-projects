package mate.academy.intro.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import mate.academy.intro.dto.external.CartItemCreateRequestDto;
import mate.academy.intro.dto.external.CartItemUpdateRequestDto;
import mate.academy.intro.dto.internal.ShoppingCartDto;
import mate.academy.intro.mapper.CartItemMapper;
import mate.academy.intro.mapper.ShoppingCartMapper;
import mate.academy.intro.model.Book;
import mate.academy.intro.model.CartItem;
import mate.academy.intro.model.ShoppingCart;
import mate.academy.intro.model.User;
import mate.academy.intro.repository.BookRepository;
import mate.academy.intro.repository.CartItemRepository;
import mate.academy.intro.repository.ShoppingCartRepository;
import mate.academy.intro.service.ShoppingCartService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final BookRepository bookRepository;
    private final CartItemMapper cartItemMapper;
    private final CartItemRepository cartItemRepository;

    @Override
    public ShoppingCart getOrCreateCart(User user) {
        Optional<ShoppingCart> existingCart = shoppingCartRepository.findById(user.getId());
        if (existingCart.isPresent()) {
            return existingCart.get();
        }
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCart.setId(user.getId());
        shoppingCartRepository.save(shoppingCart);
        return shoppingCart;
    }

    @Transactional
    @Override
    public ShoppingCartDto addCartItem(CartItemCreateRequestDto requestDto, User user) {
        ShoppingCart cart = getOrCreateCart(user);
        Book book = bookRepository.findById(requestDto.bookId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Book with this id not exist or was deleted. Id: " + requestDto.bookId()));
        CartItem newItem = cartItemMapper.toEntity(requestDto, book);
        Optional<CartItem> checkForDuplicates =
                cartItemRepository.findByBookIdAndShoppingCartId(
                        newItem.getBook().getId(), cart.getId());
        if (checkForDuplicates.isPresent()) {
            CartItem existingItem = checkForDuplicates.get();
            existingItem.setQuantity(newItem.getQuantity() + existingItem.getQuantity());
            cartItemRepository.save(existingItem);
            return shoppingCartMapper.toDto(cart);
        }
        cart.addCartItem(newItem);
        cartItemRepository.save(newItem);
        return shoppingCartMapper.toDto(cart);
    }

    @Override
    public ShoppingCartDto updateQuantityById(Long cartItemId,
                                              CartItemUpdateRequestDto requestDto,
                                              User user) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Item not found with id: " + cartItemId));
        if (!cartItem.getShoppingCart().getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You are not authorized to update this cart item.");
        }
        cartItemMapper.updateQuantity(cartItem, requestDto);
        cartItemRepository.save(cartItem);
        ShoppingCart shoppingCartEager = shoppingCartRepository.findByIdFullyLoaded(user.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Shopping cart not found for user."));
        return shoppingCartMapper.toDto(shoppingCartEager);
    }

    @Override
    public void deleteById(Long cartItemId, User user) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Item not found with id: " + cartItemId));
        if (!cartItem.getShoppingCart().getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You are not authorized to update this cart item.");
        }
        cartItemRepository.delete(cartItem);
    }
}
