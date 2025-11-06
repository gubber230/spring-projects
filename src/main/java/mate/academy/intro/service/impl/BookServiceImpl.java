package mate.academy.intro.service.impl;

import lombok.RequiredArgsConstructor;
import mate.academy.intro.dto.external.BookCreateRequestDto;
import mate.academy.intro.dto.internal.BookDto;
import mate.academy.intro.exception.EntityNotFoundException;
import mate.academy.intro.mapper.BookMapper;
import mate.academy.intro.model.Book;
import mate.academy.intro.repository.BookRepository;
import mate.academy.intro.repository.CategoryRepository;
import mate.academy.intro.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public BookDto save(BookCreateRequestDto requestDto) {
        Book book = bookMapper.toEntity(requestDto, categoryRepository);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    @Transactional(readOnly = true)
    public BookDto findById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find book with Id: " + id));
        return bookMapper.toDto(book);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(bookMapper::toDto);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateById(Long id, BookCreateRequestDto requestDto) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find book with Id: " + id));
        bookMapper.updateBookFromDto(book, requestDto, categoryRepository);
        bookRepository.save(book);
    }

    @Override
    public Page<BookDto> findByCategory(Long categoryId, Pageable pageable) {
        return bookRepository.findAllByCategoryId(categoryId, pageable)
                .map(bookMapper::toDto);
    }
}
