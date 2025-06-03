package com.data.repository.product;

import com.data.model.Product;

import java.util.List;

public interface ProductRepository {
    List<Product> paginate(int pageNumber, int size);

    Long count();

    Product findById(int id);
}
