package mate.academy.intro.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import mate.academy.intro.dto.external.CartItemCreateRequestDto;
import mate.academy.intro.dto.external.CartItemUpdateRequestDto;
import mate.academy.intro.dto.internal.CartItemDto;
import mate.academy.intro.mapper.CartItemMapper;
import mate.academy.intro.model.Book;
import mate.academy.intro.model.CartItem;
import mate.academy.intro.model.ShoppingCart;
import mate.academy.intro.repository.BookRepository;
import mate.academy.intro.repository.CartItemRepository;
import mate.academy.intro.service.CartItemService;
import mate.academy.intro.service.ShoppingCartService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final ShoppingCartService shoppingCartService;
    private final BookRepository bookRepository;

    @Override
    public CartItemDto addCartItem(CartItemCreateRequestDto requestDto, Long userId) {
        ShoppingCart cart = shoppingCartService.getOrCreateCart(userId);
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
            return cartItemMapper.toDto(cartItemRepository.save(existingItem));
        }
        cart.addCartItem(newItem);
        return cartItemMapper.toDto(cartItemRepository.save(newItem));
    }

    @Override
    public void updateQuantityById(Long cartItemId,
                                   CartItemUpdateRequestDto requestDto,
                                   Long userId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Item not found with id: " + cartItemId));
        if (!cartItem.getShoppingCart().getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You are not authorized to update this cart item.");
        }
        cartItemMapper.updateQuantity(cartItem, requestDto);
        cartItemRepository.save(cartItem);
    }

    @Override
    public void deleteById(Long cartItemId, Long userId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Item not found with id: " + cartItemId));
        if (!cartItem.getShoppingCart().getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You are not authorized to delete this cart item.");
        }
        cartItemRepository.delete(cartItem);
    }
}
