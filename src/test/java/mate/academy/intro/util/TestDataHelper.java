package mate.academy.intro.util;

import java.math.BigDecimal;
import java.util.Set;
import mate.academy.intro.dto.external.BookCreateRequestDto;
import mate.academy.intro.dto.internal.BookDto;
import mate.academy.intro.model.Book;

public class TestDataHelper {
    public static BookCreateRequestDto createValidBookRequestDto() {
        return new BookCreateRequestDto(
                "Title",
                "Author",
                "ISBN-123",
                new BigDecimal("19.99"),
                "Description",
                "image.jpg",
                Set.of(1L, 2L)
        );
    }

    public static Book createValidBook() {
        return new Book()
                .setId(1L)
                .setTitle("Title")
                .setAuthor("Author");
    }

    public static BookDto createValidBookDto() {
        return new BookDto()
                .setId(1L)
                .setTitle("Title")
                .setAuthor("Author");
    }

    public static BookCreateRequestDto createUpdatedBookRequestDto() {
        return new BookCreateRequestDto(
                "Updated Title",
                "Updated Author",
                "ISBN-123",
                new BigDecimal("19.99"),
                "Description",
                "image.jpg",
                Set.of(1L)
        );
    }

    public static Book createOldBookForUpdate() {
        return new Book()
                .setId(1L)
                .setTitle("Old Title")
                .setAuthor("Old Author");
    }
}
