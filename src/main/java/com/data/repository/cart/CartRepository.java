package com.data.repository.cart;

import com.data.model.Cart;

import java.util.List;

public interface CartRepository {
    List<Cart> findAll(int customerId);

    void addToCart(int customerId, int productId);

    void deleteFromCart(int cartId);

    void updateItemQuantity(int cartId, int productId, int quantity);
}
