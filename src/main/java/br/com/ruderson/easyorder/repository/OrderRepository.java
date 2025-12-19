package br.com.ruderson.easyorder.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.ruderson.easyorder.domain.Order;
import br.com.ruderson.easyorder.domain.enums.OrderStatus;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("""
                SELECT o
                FROM Order o
                LEFT JOIN o.customer c
                WHERE o.store.id = :storeId
                  AND (:status IS NULL OR o.status = :status)
                  AND (:customerName IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :customerName, '%')))
            """)
    Page<Order> searchOrders(
            UUID storeId,
            OrderStatus status,
            String customerName,
            Pageable pageable);

}
