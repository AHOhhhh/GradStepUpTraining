package com.thoughtworks.mall_order.repository;

import com.thoughtworks.mall_order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
   Optional<OrderItem> findByOrderIdAndProductId(long orderId, long productId);
}
