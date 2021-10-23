package com.management.order.repository;

import com.management.order.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    @Modifying
    @Query(
            value = "DELETE FROM order_details                " +
                    "WHERE order_id = :orderId                      "
            , nativeQuery = true
    )
    int deleteAllByOrderId(@Param("orderId") long orderId);

    List<OrderDetail> findAllByOrderId(long orderId);
}
