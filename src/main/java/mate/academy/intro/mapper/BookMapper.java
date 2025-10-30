package mate.academy.intro.mapper;

import java.util.stream.Collectors;
import mate.academy.intro.config.MapperConfig;
import mate.academy.intro.dto.internal.BookDto;
import mate.academy.intro.dto.internal.BookDtoWithoutCategoryIds;
import mate.academy.intro.dto.external.CreateBookRequestDto;
import mate.academy.intro.model.Book;
import mate.academy.intro.model.Category;
import mate.academy.intro.repository.BookRepository;
import mate.academy.intro.repository.CategoryRepository;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Book toEntity(CreateBookRequestDto requestDto, @Context CategoryRepository categoryRepository);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateBookFromDto(@MappingTarget Book book,
                           CreateBookRequestDto requestDto,
                           @Context CategoryRepository categoryRepository);

    @Named("bookFromId")
    default Book bookFromId(Long id, @Context BookRepository bookRepository) {
        if (id == null) {
            return null;
        }
        return bookRepository.getReferenceById(id);
    }

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

    @AfterMapping
    default void setCategories(CreateBookRequestDto bookDto,
                               @MappingTarget Book book,
                               @Context CategoryRepository categoryRepository
                               ) {
        if (bookDto.categoryIds() != null) {
            book.setCategories(
                    bookDto.categoryIds().stream()
                            .map(categoryRepository::getReferenceById)
                            .collect(Collectors.toSet())
            );
        }
    }
}
