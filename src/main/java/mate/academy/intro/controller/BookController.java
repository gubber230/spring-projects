package mate.academy.intro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import mate.academy.intro.dto.BookDto;
import mate.academy.intro.dto.CreateBookRequestDto;
import mate.academy.intro.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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

@Tag(name = "Book management", description = "Endpoints for managing books")
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    @GetMapping
    @Operation(summary = "Get all books with pagination and sorting")
    public Page<BookDto> getAll(
            @Parameter(example = "page=0&size=5&sort=price,desc") Pageable pageable
    ) {
        return bookService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get book by id")
    public BookDto getBookById(@PathVariable @Positive Long id) {
        return bookService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Create book")
    @ResponseStatus(HttpStatus.CREATED)
    public BookDto createBook(@RequestBody @Valid CreateBookRequestDto bookDto) {
        return bookService.save(bookDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete book by id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable @Positive Long id) {
        bookService.deleteById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update book by id")
    public void updateById(@PathVariable @Positive Long id,
                           @RequestBody @Valid CreateBookRequestDto bookDto) {
        bookService.updateById(id, bookDto);
    }
}
