package mate.academy.intro.service;

import lombok.RequiredArgsConstructor;
import mate.academy.intro.dto.CategoryDto;
import mate.academy.intro.dto.CreateCategoryRequestDto;
import mate.academy.intro.exception.EntityNotFoundException;
import mate.academy.intro.mapper.CategoryMapper;
import mate.academy.intro.model.Category;
import mate.academy.intro.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(categoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getById(Long id) {
        return categoryRepository.findById(id)
                .map(categoryMapper::toDto)
                .orElseThrow(
                        () -> new EntityNotFoundException("Can't find category by id: " + id)
                );
    }

    @Override
    @Transactional
    public CategoryDto save(CreateCategoryRequestDto categoryDto) {
        Category entity = categoryMapper.toEntity(categoryDto);
        return categoryMapper.toDto(categoryRepository.save(entity));
    }

    @Override
    @Transactional
    public void updateById(Long id, CreateCategoryRequestDto categoryDto) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find category with Id: " + id)
        );
        categoryMapper.updateEntityFromDto(category, categoryDto);
        categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
}
