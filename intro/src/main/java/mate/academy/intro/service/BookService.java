package mate.academy.intro.service;

import mate.academy.intro.model.Book;
import java.util.List;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();
}
