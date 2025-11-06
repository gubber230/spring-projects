package mate.academy.intro.mapper;

import mate.academy.intro.config.MapperConfig;
import mate.academy.intro.dto.internal.ShoppingCartDto;
import mate.academy.intro.model.ShoppingCart;
import mate.academy.intro.model.User;
import mate.academy.intro.repository.UserRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = {CartItemMapper.class, UserMapper.class})
public interface ShoppingCartMapper {
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "user", source = "userId", qualifiedByName = "getUserById")
    ShoppingCart toEntity(ShoppingCartDto dto, @Context UserRepository userRepository);

    @Mapping(target = "userId", source = "user.id")
    ShoppingCartDto toDto(ShoppingCart shoppingCart);

    @Mapping(target = "user", source = "user")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cartItems", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    ShoppingCart createNewCart(User user);
}
