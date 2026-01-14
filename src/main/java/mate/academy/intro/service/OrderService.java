package mate.academy.intro.service;

import mate.academy.intro.dto.external.OrderCreateRequestDto;
import mate.academy.intro.dto.external.OrderPatchRequestDto;
import mate.academy.intro.dto.internal.OrderDto;
import mate.academy.intro.dto.internal.OrderItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderDto createOrder(Long userid, OrderCreateRequestDto requestDto);

    Page<OrderDto> findAllOrders(Pageable pageable);

    Page<OrderItemDto> findAllOrderItems(Pageable pageable, Long orderId);

    OrderItemDto getOrderItemById(Long orderId, Long itemId);

    void updateStatus(Long orderId, OrderPatchRequestDto requestDto);
}
