package mate.academy.intro.mapper;

import mate.academy.intro.config.MapperConfig;
import mate.academy.intro.dto.CategoryDto;
import mate.academy.intro.dto.CreateCategoryRequestDto;
import mate.academy.intro.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Category toEntity(CreateCategoryRequestDto categoryDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateEntityFromDto(@MappingTarget Category category,
                             CreateCategoryRequestDto categoryDto);
}
