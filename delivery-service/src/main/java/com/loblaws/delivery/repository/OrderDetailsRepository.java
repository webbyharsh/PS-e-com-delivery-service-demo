package com.loblaws.delivery.repository;

import com.loblaws.delivery.entity.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {
    OrderDetails findByOrderId(Long orderId);
}
