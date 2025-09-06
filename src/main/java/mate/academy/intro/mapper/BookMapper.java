package mate.academy.intro.mapper;

import mate.academy.intro.config.MapperConfig;
import mate.academy.intro.dto.BookDto;
import mate.academy.intro.dto.CreateBookRequestDto;
import mate.academy.intro.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Book toModel(CreateBookRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateBookFromDto(CreateBookRequestDto requestDto, @MappingTarget Book book);
}
