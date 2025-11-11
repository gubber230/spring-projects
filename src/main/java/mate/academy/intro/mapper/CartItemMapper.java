package mate.academy.intro.mapper;

import mate.academy.intro.config.MapperConfig;
import mate.academy.intro.dto.external.CartItemCreateRequestDto;
import mate.academy.intro.dto.external.CartItemUpdateRequestDto;
import mate.academy.intro.dto.internal.CartItemDto;
import mate.academy.intro.model.Book;
import mate.academy.intro.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = {BookMapper.class})
public interface CartItemMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "shoppingCart", ignore = true)
    CartItem toEntity(CartItemCreateRequestDto cartItemDto, Book book);

    @Mapping(target = "bookId", source = "book", qualifiedByName = "idFromBook")
    @Mapping(target = "bookTitle", source = "book", qualifiedByName = "titleFromBook")
    CartItemDto toDto(CartItem cartItem);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "shoppingCart", ignore = true)
    @Mapping(target = "book", ignore = true)
    void updateQuantity(@MappingTarget CartItem cartItem,
                        CartItemUpdateRequestDto updateRequestDto);
}
