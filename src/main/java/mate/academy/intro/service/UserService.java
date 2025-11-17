package mate.academy.intro.service;

import mate.academy.intro.dto.external.UserRegistrationRequestDto;
import mate.academy.intro.dto.external.UserResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto);

    Page<UserResponseDto> findAll(Pageable pageable);
}
