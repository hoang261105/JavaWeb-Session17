package com.data.service.order;

import com.data.model.Order;

import java.util.List;

public interface OrderService {
    void save(Order order, int customerId);

    List<Order> findByCustomerId(int customerId);

    long count();
}
