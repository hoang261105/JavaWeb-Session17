package com.data.service.customer;

import com.data.model.Customer;
import com.data.repository.customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImp implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public void register(Customer customer) {
        customerRepository.register(customer);
    }

    @Override
    public Customer login(String email, String password) {
        return customerRepository.login(email, password);
    }

    @Override
    public Customer findById(int id) {
        return customerRepository.findById(id);
    }

    @Override
    public void update(Customer customer) {
        customerRepository.update(customer);
    }

    @Override
    public long count() {
        return customerRepository.count();
    }

    @Override
    public List<Customer> getAll(int page, int size) {
        return customerRepository.getAll(page, size);
    }

    @Override
    public List<Customer> searchUserPaginate(String username, int page, int size) {
        return customerRepository.searchUserPaginate(username, page, size);
    }

    @Override
    public void updateStatus(int id) {
        customerRepository.updateStatus(id);
    }
}
