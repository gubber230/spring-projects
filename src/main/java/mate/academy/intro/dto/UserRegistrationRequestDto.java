package mate.academy.intro.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UserRegistrationRequestDto(
        @Email
        @NotBlank
        String email,
        @NotBlank
        @Length(min = 8, max = 20)
        String password,
        @NotBlank
        @Length(min = 8, max = 20)
        String repeatPassword,
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        String shippingAddress
) {
}
