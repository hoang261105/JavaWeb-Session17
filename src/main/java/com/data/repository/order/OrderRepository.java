package com.data.repository.order;

import com.data.model.Order;

import java.util.List;

public interface OrderRepository {
    void save(Order order, int customerId);

    List<Order> findByCustomerId(int customerId);
}
