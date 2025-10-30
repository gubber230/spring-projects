package mate.academy.intro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import mate.academy.intro.dto.external.CreateCartItemRequestDto;
import mate.academy.intro.dto.external.ShoppingCartResponseDto;
import mate.academy.intro.dto.external.UpdateCartItemRequestDto;
import mate.academy.intro.dto.internal.CartItemDto;
import mate.academy.intro.model.User;
import mate.academy.intro.service.ShoppingCartService;
import org.springframework.http.HttpStatus;
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

    @GetMapping
    public ShoppingCartResponseDto getShoppingCart(@Valid User user) {
        return shoppingCartService.get();
    }

    @PostMapping
    public CartItemDto addBookToCart(@RequestBody @Valid CreateCartItemRequestDto requestDto) {
        return shoppingCartService.save(requestDto);
    }

    @PutMapping("/items/{cartItemId}")
    @Operation(summary = "Update book quantity")
    @ResponseStatus(HttpStatus.CREATED)
    public CartItemDto updateBookQuantity(@PathVariable @Positive Long bookId,
                                          @RequestBody @Valid UpdateCartItemRequestDto requestDto) {
        return shoppingCartService.updateQuantity(requestDto);
    }

    @DeleteMapping("/items/{cartItemId}")
    @Operation(summary = "Remove book from cart")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeBookFromCart(@PathVariable @Positive Long id) {
        shoppingCartService.deleteById(id);
    }
}
