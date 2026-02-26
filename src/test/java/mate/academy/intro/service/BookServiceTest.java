package mate.academy.intro.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import mate.academy.intro.dto.external.BookCreateRequestDto;
import mate.academy.intro.dto.internal.BookDto;
import mate.academy.intro.exception.EntityNotFoundException;
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
    @DisplayName("Verify save() method saves and returns book")
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
        verify(bookRepository).save(book);
        verifyNoMoreInteractions(bookRepository, bookMapper, categoryRepository);
    }

    @Test
    @DisplayName("Verify findById() returns book by id")
    public void findById_ValidId_ReturnsBookById() {
        Book book = new Book()
                .setId(1L)
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
    @DisplayName("Verify findById() throws exception when book not found")
    public void findById_InvalidId_ThrowsEntityNotFoundException() {
        Long invalidId = 100L;
        when(bookRepository.findById(invalidId)).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> bookService.findById(invalidId)
        );

        assertThat(exception.getMessage()).isEqualTo("Can't find book with Id: "
                + invalidId);
        verify(bookRepository).findById(invalidId);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Verify findAll() returns all books")
    public void findAll_ValidPageable_ReturnsAllBooks() {
        Book book = new Book()
                .setTitle("Title")
                .setAuthor("Author");

        BookDto bookDto = new BookDto()
                .setId(1L)
                .setTitle(book.getTitle())
                .setAuthor(book.getAuthor());

        Pageable pageable = PageRequest.of(0, 10);
        List<Book> books = List.of(book);
        Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        List<BookDto> bookDtos = bookService.findAll(pageable).stream().toList();

        assertThat(bookDtos).hasSize(1);
        assertThat(bookDtos.get(0)).isEqualTo(bookDto);

        verify(bookRepository).findAll(pageable);
        verify(bookMapper).toDto(book);
        verifyNoMoreInteractions(bookMapper, bookRepository);
    }

    @Test
    @DisplayName("Verify deleteById() calls repository delete method")
    public void deleteById_ValidId_DeletesBook() {
        Long id = 1L;
        bookService.deleteById(id);
        verify(bookRepository).deleteById(id);
        verifyNoMoreInteractions(bookRepository, bookMapper, categoryRepository);
    }

    @Test
    @DisplayName("Verify updateById() updates existing book")
    public void updateById_ValidIdAndDto_UpdatesBook() {
        Long id = 1L;
        BookCreateRequestDto requestDto = new BookCreateRequestDto(
                "Updated Title", "Updated Author",
                "ISBN-123", new BigDecimal("19.99"),
                "Description", "image.jpg", Set.of(1L)
        );

        Book existingBook = new Book().setId(id).setTitle("Old Title").setAuthor("Old Author");

        when(bookRepository.findById(id)).thenReturn(Optional.of(existingBook));

        bookService.updateById(id, requestDto);

        verify(bookRepository).findById(id);
        verify(bookMapper).updateBookFromDto(existingBook, requestDto, categoryRepository);
        verify(bookRepository).save(existingBook);
        verifyNoMoreInteractions(bookRepository, bookMapper, categoryRepository);
    }

    @Test
    @DisplayName("Verify updateById() throws exception when book not found")
    public void updateById_InvalidId_ThrowsEntityNotFoundException() {
        Long invalidId = 100L;
        BookCreateRequestDto requestDto = new BookCreateRequestDto(
                "Updated Title",
                "Updated Author",
                "ISBN-123",
                new BigDecimal("19.99"),
                "Description",
                "image.jpg",
                Set.of(1L)
        );

        when(bookRepository.findById(invalidId)).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> bookService.updateById(invalidId, requestDto)
        );

        assertThat(exception.getMessage()).isEqualTo("Can't find book with Id: "
                + invalidId);
        verify(bookRepository).findById(invalidId);
        verifyNoMoreInteractions(bookRepository, bookMapper, categoryRepository);
    }

    @Test
    @DisplayName("Verify findByCategory() returns books mapped to a category")
    public void findByCategory_ValidCategoryIdAndPageable_ReturnsBooks() {
        Long categoryId = 1L;
        Book book = new Book()
                .setTitle("Title")
                .setAuthor("Author");

        BookDto bookDto = new BookDto()
                .setId(1L)
                .setTitle(book.getTitle())
                .setAuthor(book.getAuthor());

        Pageable pageable = PageRequest.of(0, 10);
        List<Book> books = List.of(book);
        Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());

        when(bookRepository.findAllByCategoryId(categoryId, pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        List<BookDto> bookDtos = bookService.findByCategory(categoryId, pageable).stream().toList();

        assertThat(bookDtos).hasSize(1);
        assertThat(bookDtos.get(0)).isEqualTo(bookDto);

        verify(bookRepository).findAllByCategoryId(categoryId, pageable);
        verify(bookMapper).toDto(book);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }
}
