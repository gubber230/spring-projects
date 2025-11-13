package mate.academy.intro.service;

import mate.academy.intro.dto.external.CategoryCreateRequestDto;
import mate.academy.intro.dto.internal.CategoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    Page<CategoryDto> findAll(Pageable pageable);

    CategoryDto getById(Long id);

    CategoryDto save(CategoryCreateRequestDto categoryDto);

    void updateById(Long id, CategoryCreateRequestDto categoryDto);

    void deleteById(Long id);
}
