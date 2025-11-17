package mate.academy.intro.service.impl;

import jakarta.persistence.EntityNotFoundException;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final BookRepository bookRepository;
    private final CartItemMapper cartItemMapper;
    private final CartItemRepository cartItemRepository;

    @Override
    public void createShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCartDto getShoppingCart(Long userId) {
        return shoppingCartMapper.toDto(shoppingCartRepository.getReferenceById(userId));
    }

    @Override
    public ShoppingCartDto addCartItem(CartItemCreateRequestDto requestDto, Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository.getReferenceById(userId);
        Book book = bookRepository.findById(requestDto.bookId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Book with this id not exist or was deleted. Id: " + requestDto.bookId()));
        CartItem cartItem = cartItemMapper.toEntity(requestDto, book);
        var optionalCartItem = cartItemRepository.findByBookIdAndShoppingCartId(
                book.getId(), shoppingCart.getId());
        if (optionalCartItem.isPresent()) {
            updateQuantityById(
                    optionalCartItem.get().getId(),
                    new CartItemUpdateRequestDto(
                            optionalCartItem.get().getQuantity() + requestDto.quantity()),
                    shoppingCart.getId());
            return shoppingCartMapper.toDto(shoppingCartRepository.save(shoppingCart));
        }
        addCartItemToCart(requestDto, book, shoppingCart);
        return shoppingCartMapper.toDto(shoppingCartRepository.save(shoppingCart));
    }

    @Override
    public ShoppingCartDto updateQuantityById(Long cartItemId,
                                              CartItemUpdateRequestDto dto,
                                              Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository.getReferenceById(userId);
        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(
                cartItemId, shoppingCart.getId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Item not found with id: " + cartItemId));
        cartItemMapper.updateQuantity(cartItem, dto);
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toDto(shoppingCartRepository.save(shoppingCart));
    }

    @Override
    public void deleteById(Long cartItemId, Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository.getReferenceById(userId);
        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(
                cartItemId, shoppingCart.getId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Item not found with id: " + cartItemId));
        cartItemRepository.delete(cartItem);
        shoppingCartRepository.save(shoppingCart);
    }

    private void addCartItemToCart(
            CartItemCreateRequestDto requestDto,
            Book book,
            ShoppingCart cart) {
        CartItem cartItem = cartItemMapper.toEntity(requestDto, book);
        cartItem.setBook(book);
        cart.addCartItem(cartItem);
    }
}
