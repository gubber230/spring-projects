package mate.academy.intro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import mate.academy.intro.dto.internal.BookDto;
import mate.academy.intro.dto.internal.CategoryDto;
import mate.academy.intro.dto.external.CreateCategoryRequestDto;
import mate.academy.intro.service.BookService;
import mate.academy.intro.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Category management", description = "Endpoints for managing categories")
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final BookService bookService;

    @GetMapping
    @Operation(summary = "Retrieve all categories")
    public Page<CategoryDto> getAllCategories(Pageable pageable) {
        return categoryService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a specific category")
    public BookDto getBooksByCategory(@PathVariable @Positive Long id) {
        return bookService.findById(id);
    }

    @GetMapping("/{id}/books")
    @Operation(summary = "Retrieve books by a specific category")
    public Page<BookDto> getBooksByCategory(@PathVariable @Positive Long id, Pageable pageable) {
        return bookService.findByCategory(id, pageable);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new category")
    public CategoryDto createCategory(@RequestBody @Valid CreateCategoryRequestDto requestDto) {
        return categoryService.save(requestDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update the details of a category")
    @ResponseStatus(HttpStatus.CREATED)
    public void updateCategoryById(@PathVariable @Positive Long id,
                                   @RequestBody @Valid CreateCategoryRequestDto requestDto) {
        categoryService.updateById(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Remove a category")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable @Positive Long id) {
        categoryService.deleteById(id);
    }
}
