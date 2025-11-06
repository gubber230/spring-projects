package mate.academy.intro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import mate.academy.intro.dto.external.CartItemCreateRequestDto;
import mate.academy.intro.dto.external.CartItemUpdateRequestDto;
import mate.academy.intro.dto.internal.CartItemDto;
import mate.academy.intro.dto.internal.ShoppingCartDto;
import mate.academy.intro.model.User;
import mate.academy.intro.service.CartItemService;
import mate.academy.intro.service.ShoppingCartService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping Cart management", description = "Endpoints for managing Shopping Cart")
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;
    private final CartItemService cartItemService;

    @GetMapping
    public ShoppingCartDto getShoppingCart(@AuthenticationPrincipal User user) {
        if (user == null) {
            throw new AccessDeniedException("User must be logged in to view cart.");
        }
        return shoppingCartService.getOrCreateCart(user.getId());
    }

    @PostMapping
    public CartItemDto addBookToCart(@RequestBody @Valid CartItemCreateRequestDto requestDto,
                                     @AuthenticationPrincipal User user) {
        return cartItemService.addCartItem(requestDto, user.getId());
    }

    @PutMapping("/items/{cartItemId}")
    @Operation(summary = "Update book quantity")
    @ResponseStatus(HttpStatus.CREATED)
    public void updateBookQuantity(@PathVariable @Positive Long bookId,
                                   @RequestBody @Valid CartItemUpdateRequestDto requestDto,
                                   @AuthenticationPrincipal User user) {
        cartItemService.updateQuantityById(bookId, requestDto, user.getId());
    }

    @DeleteMapping("/items/{cartItemId}")
    @Operation(summary = "Remove book from cart")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeBookFromCart(@PathVariable @Positive Long id,
                                   @AuthenticationPrincipal User user) {
        cartItemService.deleteById(id, user.getId());
    }
}
