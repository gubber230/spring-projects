package mate.academy.intro.mapper;

import java.util.stream.Collectors;
import mate.academy.intro.config.MapperConfig;
import mate.academy.intro.dto.BookDto;
import mate.academy.intro.dto.BookDtoWithoutCategoryIds;
import mate.academy.intro.dto.CreateBookRequestDto;
import mate.academy.intro.model.Book;
import mate.academy.intro.model.Category;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Book toEntity(CreateBookRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateBookFromDto(@MappingTarget Book book, CreateBookRequestDto requestDto);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        if (book.getCategories() != null) {
            bookDto.setCategoryIds(
                    book.getCategories().stream()
                            .map(Category::getId)
                            .collect(Collectors.toSet())
            );
        }
    }
}
