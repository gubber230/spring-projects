package mate.academy.intro.repository;

import java.util.Optional;
import mate.academy.intro.model.ShoppingCart;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    @EntityGraph(attributePaths = "cartItems.book")
    Optional<ShoppingCart> findById(Long id);

    @Query("SELECT sc FROM ShoppingCart sc "
            + "LEFT JOIN FETCH sc.cartItems ci "
            + "LEFT JOIN FETCH ci.book "
            + "WHERE sc.id = :id")
    Optional<ShoppingCart> findByIdFullyLoaded(@Param("id") Long id);
}
