package mate.academy.intro.service;

import mate.academy.intro.dto.internal.BookDto;
import mate.academy.intro.dto.external.CreateBookRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    BookDto findById(Long id);

    Page<BookDto> findAll(Pageable pageable);

    void deleteById(Long id);

    void updateById(Long id, CreateBookRequestDto bookDto);

    Page<BookDto> findByCategory(Long categoryId, Pageable pageable);
}
