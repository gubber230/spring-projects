package mate.academy.intro.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Data
public class Book {
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    Long id;
    String title;
    String author;
    String isbn;
    BigDecimal price;
    String description;
    String coverImage;
}
