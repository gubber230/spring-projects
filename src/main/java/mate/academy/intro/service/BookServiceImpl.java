package mate.academy.intro.service;

import java.util.List;
import mate.academy.intro.model.Book;
import mate.academy.intro.repository.BookRepositoryImpl;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepositoryImpl bookRepositoryImpl;

    public BookServiceImpl(BookRepositoryImpl bookRepositoryImpl) {
        this.bookRepositoryImpl = bookRepositoryImpl;
    }

    @Override
    public Book save(Book book) {
        return bookRepositoryImpl.save(book);
    }

    @Override
    public List<Book> findAll() {
        return bookRepositoryImpl.findAll();
    }
}
