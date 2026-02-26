package mate.academy.intro.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import mate.academy.intro.dto.external.CategoryCreateRequestDto;
import mate.academy.intro.dto.internal.CategoryDto;
import mate.academy.intro.exception.EntityNotFoundException;
import mate.academy.intro.mapper.CategoryMapper;
import mate.academy.intro.model.Category;
import mate.academy.intro.repository.CategoryRepository;
import mate.academy.intro.service.impl.CategoryServiceImpl;
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
public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Verify findAll() returns all categories")
    public void findAll_ValidPageable_ReturnsAllCategories() {
        Category category = new Category();
        category.setName("Horror");
        category.setDescription("Scary movie");

        CategoryDto categoryDto = new CategoryDto(
                1L, category.getName(), category.getDescription()
        );

        Pageable pageable = PageRequest.of(0, 10);
        List<Category> categories = List.of(category);
        Page<Category> categoryPage = new PageImpl<>(categories, pageable, categories.size());

        when(categoryMapper.toDto(category)).thenReturn(categoryDto);
        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);

        List<CategoryDto> categoryDtos = categoryService.findAll(pageable).stream().toList();

        assertThat(categoryDtos).hasSize(1);
        assertThat(categoryDtos.get(0)).isEqualTo(categoryDto);

        verify(categoryRepository).findAll(pageable);
        verifyNoMoreInteractions(categoryMapper, categoryRepository);
    }

    @Test
    @DisplayName("Verify getById() returns category by id")
    public void getById_ValidId_ReturnsCategoryById() {
        Category category = new Category();
        category.setName("Horror");
        category.setDescription("Scary movie");

        CategoryDto actual = new CategoryDto(
                1L, category.getName(), category.getDescription()
        );

        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(actual);

        CategoryDto expected = categoryService.getById(category.getId());

        Assertions.assertEquals(actual, expected);
    }

    @Test
    @DisplayName("Verify save() saves and returns category")
    public void save_ValidRequestDto_SavesEntityAndReturnsDto() {
        CategoryCreateRequestDto requestDto =
                new CategoryCreateRequestDto("Horror", "Scary movie");

        Category category = new Category();
        category.setName(requestDto.name());
        category.setDescription(requestDto.description());

        CategoryDto categoryDto = new CategoryDto(
                1L, category.getName(), category.getDescription()
        );

        when(categoryMapper.toEntity(requestDto)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);
        when(categoryRepository.save(category)).thenReturn(category);

        CategoryDto savedCategoryDto = categoryService.save(requestDto);

        assertThat(savedCategoryDto).isEqualTo(categoryDto);
        verify(categoryRepository).save(category);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }
    @Test
    @DisplayName("Verify getById() throws exception when category not found")
    public void getById_InvalidId_ThrowsEntityNotFoundException() {
        Long invalidId = 100L;
        when(categoryRepository.findById(invalidId)).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> categoryService.getById(invalidId)
        );

        assertThat(exception.getMessage()).isEqualTo("Can't find category by id: "
                + invalidId);
        verify(categoryRepository).findById(invalidId);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Verify updateById() updates existing category")
    public void updateById_ValidIdAndDto_UpdatesCategory() {
        Long id = 1L;
        CategoryCreateRequestDto requestDto =
                new CategoryCreateRequestDto("Action", "Exciting movies");

        Category existingCategory = new Category();
        existingCategory.setId(id);
        existingCategory.setName("Old Name");
        existingCategory.setDescription("Old Description");

        when(categoryRepository.findById(id)).thenReturn(Optional.of(existingCategory));

        categoryService.updateById(id, requestDto);

        verify(categoryRepository).findById(id);
        verify(categoryMapper).updateEntityFromDto(existingCategory, requestDto);
        verify(categoryRepository).save(existingCategory);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Verify updateById() throws exception when category not found")
    public void updateById_InvalidId_ThrowsEntityNotFoundException() {
        Long invalidId = 100L;
        CategoryCreateRequestDto requestDto =
                new CategoryCreateRequestDto("Action", "Exciting movies");

        when(categoryRepository.findById(invalidId)).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> categoryService.updateById(invalidId, requestDto)
        );

        assertThat(exception.getMessage()).isEqualTo("Can't find category with Id: "
                + invalidId);

        verify(categoryRepository).findById(invalidId);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Verify deleteById() calls repository delete method")
    public void deleteById_ValidId_DeletesCategory() {
        Long id = 1L;

        categoryService.deleteById(id);

        verify(categoryRepository).deleteById(id);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }
}
