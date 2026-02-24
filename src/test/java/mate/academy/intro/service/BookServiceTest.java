package mate.academy.intro.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import mate.academy.intro.dto.external.BookCreateRequestDto;
import mate.academy.intro.dto.internal.BookDto;
import mate.academy.intro.mapper.BookMapper;
import mate.academy.intro.model.Book;
import mate.academy.intro.repository.BookRepository;
import mate.academy.intro.repository.CategoryRepository;
import mate.academy.intro.service.impl.BookServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("Verify save() method works")
    public void save_ValidRequestDto_SavesEntityAndReturnsDto() {
        BookCreateRequestDto requestDto = new BookCreateRequestDto(
                "Title",
                "Author",
                "ISBN-123",
                new BigDecimal("19.99"),
                "Description",
                "image.jpg",
                Set.of(1L, 2L)
        );

        Book book = new Book()
                .setTitle(requestDto.title())
                .setAuthor(requestDto.author());

        BookDto bookDto = new BookDto()
                .setId(1L)
                .setTitle(book.getTitle())
                .setAuthor(book.getAuthor());

        when(bookMapper.toEntity(requestDto, categoryRepository)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);
        when(bookRepository.save(book)).thenReturn(book);

        BookDto savedBookDto = bookService.save(requestDto);

        assertThat(savedBookDto).isEqualTo(bookDto);
        verify(bookRepository, times(1)).save(book);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Verify getById() method works")
    public void getById_ValidId_ReturnsCategoryById() {
        Book book = new Book()
                .setTitle("Title")
                .setAuthor("Author");

        BookDto actual = new BookDto()
                .setId(1L)
                .setTitle(book.getTitle())
                .setAuthor(book.getAuthor());

        when(bookRepository.findById(actual.getId())).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(actual);

        BookDto expected = bookService.findById(actual.getId());

        Assertions.assertEquals(actual, expected);
    }

    @Test
    @DisplayName("Verify findAll() method works")
    public void findAll_ValidPageable_ReturnsAllCategories() {
        Book book = new Book()
                .setTitle("Title")
                .setAuthor("Author");

        BookDto bookDto = new BookDto()
                .setId(1L)
                .setTitle(book.getTitle())
                .setAuthor(book.getAuthor());


        Pageable pageable = PageRequest.of(0, 10);
        List<Book> books = List.of(book);
        Page<Book> categoryPage = new PageImpl<>(books, pageable, books.size());

        when(bookRepository.findAll(pageable)).thenReturn(categoryPage);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        List<BookDto> categoryDtos = bookService.findAll(pageable).stream().toList();

        assertThat(categoryDtos).hasSize(1);
        assertThat(categoryDtos.get(0)).isEqualTo(bookDto);

        verify(bookRepository, times(1)).findAll(pageable);
        verifyNoMoreInteractions(bookMapper, bookRepository);
    }
}
