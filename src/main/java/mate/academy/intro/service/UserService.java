package mate.academy.intro.service;

import mate.academy.intro.dto.UserRegistrationRequestDto;
import mate.academy.intro.dto.UserResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto);

    Page<UserResponseDto> findAll(Pageable pageable);
}
