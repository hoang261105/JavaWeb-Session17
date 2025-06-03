package com.data.repository.customer;

import com.data.model.Customer;

public interface CustomerRepository {
    void register(Customer customer);

    Customer login(String email, String password);

    Customer findById(int id);

    void update(Customer customer);
}
