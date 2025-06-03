package com.data.service.customer;

import com.data.model.Customer;
import com.data.repository.customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
