package mate.academy.intro.repository;

import mate.academy.intro.model.Book;
import java.util.List;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();
}
