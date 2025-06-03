package com.data.service.product;

import com.data.model.Product;

import java.util.List;

public interface ProductService {
    List<Product> paginate(int pageNumber, int size);

    Long count();

    Product findById(int id);
}
