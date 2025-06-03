package com.data.service.customer;

import com.data.model.Customer;

public interface CustomerService {
    void register(Customer customer);

    Customer login(String email, String password);

    Customer findById(int id);

    void update(Customer customer);
}
