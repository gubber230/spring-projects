package mate.academy.intro.repository;

import mate.academy.intro.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long> {
    @EntityGraph(attributePaths = "categories")
    @Query("SELECT b FROM Book b "
            + "JOIN b.categories c "
            + "WHERE c.id = :categoryId")
    Page<Book> findAllByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);
}
