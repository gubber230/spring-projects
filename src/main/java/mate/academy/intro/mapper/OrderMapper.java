package mate.academy.intro.mapper;

import mate.academy.intro.config.MapperConfig;
import mate.academy.intro.dto.external.OrderPatchRequestDto;
import mate.academy.intro.dto.internal.OrderDto;
import mate.academy.intro.model.Order;
import mate.academy.intro.model.ShoppingCart;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "orderItemSet", source = "cartItems")
    Order toOrder(ShoppingCart shoppingCart);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "orderItems", source = "orderItemSet")
    OrderDto toDto(Order order);

    void updateOrderFromDto(OrderPatchRequestDto dto, @MappingTarget Order order);

    @AfterMapping
    default void setOrderReference(@MappingTarget Order order) {
        if (order.getOrderItemSet() != null) {
            order.getOrderItemSet()
                    .forEach(item -> item.setOrder(order));
        }
    }
}
