package mate.academy.intro.security;

import lombok.RequiredArgsConstructor;
import mate.academy.intro.dto.UserLoginRequestDto;
import mate.academy.intro.dto.UserLoginResponseDto;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public UserLoginResponseDto authenticate(UserLoginRequestDto requestDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDto.email(), requestDto.password())
        );

        String token = jwtUtil.generateToken(requestDto.email());
        return new UserLoginResponseDto(token);
    }
}
