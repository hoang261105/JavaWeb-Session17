package com.data.service.product;

import com.data.model.Product;
import com.data.repository.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImp implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> paginate(int pageNumber, int size) {
        return productRepository.paginate(pageNumber, size);
    }

    @Override
    public Long count() {
        return productRepository.count();
    }

    @Override
    public Product findById(int id) {
        return productRepository.findById(id);
    }
}
