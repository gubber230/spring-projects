package mate.academy.intro.repository;

import java.util.Optional;
import mate.academy.intro.model.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @EntityGraph(attributePaths = {"book"})
    Optional<Page<OrderItem>> findByOrderId(Pageable pageable, Long orderId);

    @EntityGraph(attributePaths = {"book"})
    Optional<OrderItem> findByIdAndOrderId(Long itemId, Long orderId);
}
