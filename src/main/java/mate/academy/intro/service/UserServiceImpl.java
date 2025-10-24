package mate.academy.intro.service;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.intro.dto.UserRegistrationRequestDto;
import mate.academy.intro.dto.UserResponseDto;
import mate.academy.intro.exception.EntityNotFoundException;
import mate.academy.intro.exception.RegistrationException;
import mate.academy.intro.mapper.UserMapper;
import mate.academy.intro.model.Role;
import mate.academy.intro.model.User;
import mate.academy.intro.repository.RoleRepository;
import mate.academy.intro.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    public static final String ROLE_NAME = Role.RoleName.USER.toString();
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public UserResponseDto register(UserRegistrationRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.email())) {
            throw new RegistrationException(
                    "Email has been taken"
            );
        }
        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.password()));
        Role userRole = roleRepository.findByRole(ROLE_NAME)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can not find role in database: " + ROLE_NAME));
        user.setRoles(Set.of(userRole));
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public Page<UserResponseDto> findAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toDto);
    }
}
