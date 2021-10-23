package com.management.order.repository;

import com.management.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(
            value = "SELECT COUNT(*)                                                                    " +
                    "FROM orders                                                                        " +
                    "WHERE user_id = :userId                                                            " +
                    "AND order_date BETWEEN CONCAT(:today, ' 00:00:00') AND CONCAT(:today, ' 23:59:59') "
            , nativeQuery = true
    )
    int findCountByUserIdAndToday(@Param("userId") long userId, @Param("today") String today);

    List<Order> findAllByUserId(long userId);

    List<Order> findAllByUserIdAndDeletedTrue(long userId);
}
