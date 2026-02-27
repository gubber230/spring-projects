package mate.academy.intro.controller;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import mate.academy.intro.dto.external.CategoryCreateRequestDto;
import mate.academy.intro.dto.internal.BookDto;
import mate.academy.intro.dto.internal.CategoryDto;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.Assertions;
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
public class CategoryControllerTest {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private int expectedLength;

    @Sql(scripts = {"classpath:database/category/clear-categories.sql",
            "classpath:database/category/add-two-categories.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/category/clear-categories.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Create new category")
    void createCategory_ValidRequestDto_Created() throws Exception {
        CategoryCreateRequestDto requestDto = new CategoryCreateRequestDto(
                "name", "description"
        );



        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                        post("/categories")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryDto.class);

        CategoryDto expected = new CategoryDto(
                anyLong(), requestDto.name(), requestDto.description()
        );

        assertTrue(reflectionEquals(expected, actual, "id"));
    }

    @Sql(scripts = {"classpath:database/category/clear-categories.sql",
            "classpath:database/category/add-two-categories.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/category/clear-categories.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    @DisplayName("Get list of all categories")
    @WithMockUser
    void getAll_Ok() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/categories")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        int expectedLength = 2;
        String expectedName = "Fantasy";

        JsonNode treePage = objectMapper.readTree(result.getResponse().getContentAsString());
        CategoryDto[] actualCategories = objectMapper.treeToValue(
                treePage.get("content"), CategoryDto[].class);

        assertEquals(expectedLength, actualCategories.length);
        assertEquals(expectedName, actualCategories[0].name());
    }

    @Sql(scripts = {"classpath:database/category/clear-categories.sql",
            "classpath:database/category/add-two-categories.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/category/clear-categories.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    @DisplayName("Delete category by id")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteById_CategoryExist_NoContent() throws Exception {
        Long categoryId = 1L;
        mockMvc.perform(delete("/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        MvcResult result = mockMvc.perform(get("/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        int expectedLength = 1;

        JsonNode readTree = objectMapper.readTree(result.getResponse().getContentAsString());
        CategoryDto[] actualDtos = objectMapper.treeToValue(
                readTree.get("content"), CategoryDto[].class);

        assertEquals(expectedLength, actualDtos.length);
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
    @DisplayName("Get books by category by id")
    @WithMockUser
    void getBooksByCategory_Valid_Ok() throws Exception {
        Long categoryId = 2L;
        MvcResult result = mockMvc.perform(get("/categories/{id}/books", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        int expectedLength = 2;

        JsonNode readTree = objectMapper.readTree(result.getResponse().getContentAsString());
        BookDto[] actualDtos = objectMapper.treeToValue(readTree.get("content"), BookDto[].class);

        assertEquals(expectedLength, actualDtos.length);
    }
}
