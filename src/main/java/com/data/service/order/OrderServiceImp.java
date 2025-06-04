package com.data.service.order;

import com.data.model.Order;
import com.data.repository.order.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImp implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public void save(Order order, int customerId) {
        orderRepository.save(order, customerId);
    }

    @Override
    public List<Order> findByCustomerId(int customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    @Override
    public long count() {
        return orderRepository.count();
    }
}
