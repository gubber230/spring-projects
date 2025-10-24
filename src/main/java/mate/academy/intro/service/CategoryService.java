package mate.academy.intro.service;

import mate.academy.intro.dto.CategoryDto;
import mate.academy.intro.dto.CreateCategoryRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    Page<CategoryDto> findAll(Pageable pageable);

    CategoryDto getById(Long id);

    CategoryDto save(CreateCategoryRequestDto categoryDto);

    void updateById(Long id, CreateCategoryRequestDto categoryDto);

    void deleteById(Long id);
}
