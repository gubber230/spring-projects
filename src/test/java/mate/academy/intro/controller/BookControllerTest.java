package mate.academy.intro.controller;


import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Set;
import mate.academy.intro.dto.external.BookCreateRequestDto;
import mate.academy.intro.dto.internal.BookDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class BookControllerTest {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Sql(scripts = {"classpath:database/book/clear-books.sql",
            "classpath:database/category/clear-categories.sql",
            "classpath:database/category/add-two-categories.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/book/clear-books.sql",
            "classpath:database/category/clear-categories.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Create new book")
    void createBook_ValidRequestDto_Created() throws Exception {
        BookCreateRequestDto requestDto = new BookCreateRequestDto(
                "title", "author", "isbn", new BigDecimal("11.99"),
                null, null, Set.of(1L, 2L)
        );

        BookDto expected = new BookDto()
                .setAuthor(requestDto.author())
                .setTitle(requestDto.title())
                .setIsbn(requestDto.isbn())
                .setPrice(requestDto.price())
                .setCategoryIds(requestDto.categoryIds());

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                        post("/books")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);


        assertTrue(reflectionEquals(expected, actual, "id"));
    }

    @Sql(scripts = {"classpath:database/book/clear-books.sql",
            "classpath:database/category/clear-categories.sql",
            "classpath:database/category/add-two-categories.sql",
            "classpath:database/book/add-three-books.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/book/clear-books.sql",
            "classpath:database/category/clear-categories.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    @DisplayName("Get list of all books")
    @WithMockUser
    void getAll_Ok() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/books")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        int expectedLength = 3;
        String expectedTitle = "Book1";

        JsonNode treePage = objectMapper.readTree(result.getResponse().getContentAsString());
        BookDto[] actualBooks = objectMapper.treeToValue(treePage.get("content"), BookDto[].class);

        assertEquals(expectedLength, actualBooks.length);
        assertEquals(expectedTitle, actualBooks[0].getTitle());
    }

    @Sql(scripts = {"classpath:database/book/clear-books.sql",
            "classpath:database/category/clear-categories.sql",
            "classpath:database/category/add-two-categories.sql",
            "classpath:database/book/add-three-books.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/book/clear-books.sql",
            "classpath:database/category/clear-categories.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    @DisplayName("Get book by id")
    @WithMockUser
    void getBookById_BookExist_Ok() throws Exception {
        Long bookId = 1L;
        MvcResult result = mockMvc.perform(
                        get("/books/{id}", bookId)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto expected = new BookDto()
                .setId(1L)
                .setTitle("Book1")
                .setAuthor("Author1")
                .setIsbn("1")
                .setPrice(new BigDecimal("10.00"))
                .setCategoryIds(Set.of(1L));

        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);

        assertTrue(reflectionEquals(expected, actual));
    }

    @Sql(scripts = {"classpath:database/book/clear-books.sql",
            "classpath:database/category/clear-categories.sql",
            "classpath:database/category/add-two-categories.sql",
            "classpath:database/book/add-three-books.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/book/clear-books.sql",
            "classpath:database/category/clear-categories.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    @DisplayName("Delete book by id")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteById_BookExist_NoContent() throws Exception {
        Long bookId = 1L;
        mockMvc.perform(delete("/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
