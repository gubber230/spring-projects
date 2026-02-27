package mate.academy.intro.repository;

import java.util.List;
import mate.academy.intro.model.Book;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {"classpath:database/book/clear-books.sql",
        "classpath:database/category/clear-categories.sql",
        "classpath:database/category/add-two-categories.sql",
        "classpath:database/book/add-three-books.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = {"classpath:database/book/clear-books.sql",
        "classpath:database/category/clear-categories.sql",},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
public class BookRepositoryTest {
    @Autowired
    public BookRepository bookRepository;

    @Test
    @DisplayName("""
            Find all by Category Id
            """)
    void findAllByCategoryId_ValidCategoryId_ReturnsPageOfBooks() {
        List<Book> actual = bookRepository.findAllByCategoryId(
                1L, PageRequest.of(0, 10)).stream().toList();

        Assertions.assertEquals(1, actual.size());
    }
}
