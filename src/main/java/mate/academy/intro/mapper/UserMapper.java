package mate.academy.intro.mapper;

import mate.academy.intro.config.MapperConfig;
import mate.academy.intro.dto.external.UserRegistrationRequestDto;
import mate.academy.intro.dto.external.UserResponseDto;
import mate.academy.intro.model.User;
import mate.academy.intro.repository.UserRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    User toEntity(UserRegistrationRequestDto requestDto);

    @Named("getUserById")
    default User getUserById(Long userId, @Context UserRepository userRepository) {
        return userRepository.getReferenceById(userId);
    }
}
