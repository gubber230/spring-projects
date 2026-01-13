package mate.academy.intro.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import mate.academy.intro.dto.external.OrderCreateRequestDto;
import mate.academy.intro.dto.external.OrderPatchRequestDto;
import mate.academy.intro.dto.internal.OrderDto;
import mate.academy.intro.dto.internal.OrderItemDto;
import mate.academy.intro.exception.EntityNotFoundException;
import mate.academy.intro.mapper.OrderItemMapper;
import mate.academy.intro.mapper.OrderMapper;
import mate.academy.intro.model.Order;
import mate.academy.intro.model.ShoppingCart;
import mate.academy.intro.repository.OrderItemRepository;
import mate.academy.intro.repository.OrderRepository;
import mate.academy.intro.repository.ShoppingCartRepository;
import mate.academy.intro.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Override
    public OrderDto createOrder(Long userId, OrderCreateRequestDto requestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("No User with id: " + userId));
        Order order = orderMapper.toOrder(shoppingCart);
        order.setStatus(Order.Status.PENDING);
        order.setShippingAddress(requestDto.shippingAddress());
        order.setOrderDate(LocalDateTime.now());
        order.getOrderItemSet().forEach(orderItem -> orderItem.setOrder(order));
        BigDecimal total = order.getOrderItemSet().stream()
                .map(orderItem
                        -> BigDecimal.valueOf(orderItem.getQuantity())
                        .multiply(orderItem.getPrice()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotal(total);
        Order savedOrder = orderRepository.save(order);
        shoppingCart.getCartItems().clear();
        shoppingCartRepository.save(shoppingCart);
        return orderMapper.toDto(savedOrder);
    }

    @Override
    public Page<OrderDto> findAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(orderMapper::toDto);
    }

    @Override
    public Page<OrderItemDto> findAllOrderItems(Pageable pageable, Long orderId) {
        return orderItemRepository.findByOrderId(pageable, orderId)
                .orElseThrow(() -> new EntityNotFoundException("No Order with id: " + orderId))
                .map(orderItemMapper::toDto);
    }

    @Override
    public OrderItemDto getOrderItemById(Long orderId, Long itemId) {
        return orderItemMapper.toDto(
                orderItemRepository.findByIdAndOrderId(itemId, orderId)
                        .orElseThrow(() -> new EntityNotFoundException("Wrong Order id: "
                                + orderId + "; or Item id: " + itemId)));
    }

    @Override
    public void updateStatus(Long orderId, OrderPatchRequestDto requestDto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("No Order with id: " + orderId));
        orderMapper.updateOrderFromDto(requestDto, order);
    }
}
