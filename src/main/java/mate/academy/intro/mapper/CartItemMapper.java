package mate.academy.intro.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import mate.academy.intro.config.MapperConfig;
import mate.academy.intro.dto.external.CartItemCreateRequestDto;
import mate.academy.intro.dto.external.CartItemUpdateRequestDto;
import mate.academy.intro.dto.internal.CartItemDto;
import mate.academy.intro.model.CartItem;
import mate.academy.intro.repository.CartItemRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class, uses = {BookMapper.class})
public interface CartItemMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "shoppingCart", ignore = true)
    @Mapping(source = "bookId", target = "book", qualifiedByName = "bookFromId")
    CartItem toEntity(CartItemCreateRequestDto cartItemDto);

    @Mapping(target = "bookId", source = "book", qualifiedByName = "idFromBook")
    @Mapping(target = "bookTitle", source = "book", qualifiedByName = "titleFromBook")
    CartItemDto toDto(CartItem cartItem);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "shoppingCart", ignore = true)
    @Mapping(target = "book", ignore = true)
    void updateQuantity(@MappingTarget CartItem cartItem,
                        CartItemUpdateRequestDto updateRequestDto);

    @Named("toCartItemById")
    default Set<CartItem> toEntityById(Set<CartItemDto> cartItems,
                                       @Context CartItemRepository cartItemRepository) {
        return cartItems.stream()
                .map(CartItemDto::getId)
                .map(cartItemRepository::getReferenceById)
                .collect(Collectors.toSet());
    }
}
