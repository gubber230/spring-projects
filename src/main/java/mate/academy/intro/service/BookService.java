package mate.academy.intro.service;

import mate.academy.intro.dto.external.BookCreateRequestDto;
import mate.academy.intro.dto.internal.BookDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(BookCreateRequestDto requestDto);

    BookDto findById(Long id);

    Page<BookDto> findAll(Pageable pageable);

    void deleteById(Long id);

    void updateById(Long id, BookCreateRequestDto bookDto);

    Page<BookDto> findByCategory(Long categoryId, Pageable pageable);
}
