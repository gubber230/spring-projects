package mate.academy.intro.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Email
    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;
    @Length(min = 8, max = 20)
    @Column(nullable = false)
    private String password;
    @NotBlank
    @Column(nullable = false)
    private String firstName;
    @NotBlank
    @Column(nullable = false)
    private String lastName;
    private String shippingAddress;
}
