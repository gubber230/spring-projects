package mate.academy.intro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import mate.academy.intro.dto.external.OrderCreateRequestDto;
import mate.academy.intro.dto.external.OrderPatchRequestDto;
import mate.academy.intro.dto.internal.OrderDto;
import mate.academy.intro.dto.internal.OrderItemDto;
import mate.academy.intro.model.User;
import mate.academy.intro.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Orders", description = "Endpoints for managing users orders")
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/orders")
public class OrdersController {
    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Place order and empty ShoppingCart")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto placeOrder(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid OrderCreateRequestDto requestDto) {
        return orderService.createOrder(user.getId(), requestDto);
    }

    @GetMapping
    @Operation(summary = "Retrieve order history")
    public Page<OrderDto> getOrderHistory(Pageable pageable) {
        return orderService.findAllOrders(pageable);
    }

    @GetMapping("/{orderId}/items")
    @Operation(summary = "Retrieve OrderItems by Id")
    public Page<OrderItemDto> getAllOrderItems(@PathVariable @Positive Long orderId,
                                               Pageable pageable) {
        return orderService.findAllOrderItems(pageable, orderId);
    }

    @GetMapping("/{orderId}/items/{id}")
    @Operation(summary = "Retrieve OrderItem by Id")
    public OrderItemDto getOrderItemById(
            @PathVariable(name = "orderId") @Positive Long orderId,
            @PathVariable(name = "id") @Positive Long itemId) {
        return orderService.getOrderItemById(orderId, itemId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{orderId}")
    @Operation(summary = "Update Order status")
    public void updateOrderStatus(@PathVariable @Positive Long orderId,
                                  @RequestBody @Valid OrderPatchRequestDto requestDto) {
        orderService.updateStatus(orderId, requestDto);
    }
}
