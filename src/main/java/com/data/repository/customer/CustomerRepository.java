package com.data.repository.customer;

import com.data.model.Customer;
import com.data.model.Status;

import java.util.List;

public interface CustomerRepository {
    void register(Customer customer);

    Customer login(String email, String password);

    Customer findById(int id);

    void update(Customer customer);

    long count();

    List<Customer> getAll(int page, int size);

    List<Customer> searchUserPaginate(String username, int page, int size);

    void updateStatus(int id);
}
