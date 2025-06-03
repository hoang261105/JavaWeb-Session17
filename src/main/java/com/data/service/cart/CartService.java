package com.data.service.cart;

import com.data.model.Cart;

import java.util.List;

public interface CartService {
    List<Cart> findAll(int customerId);

    void addToCart(int customerId, int productId);

    void deleteFromCart(int cartId);

    void updateItemQuantity(int cartId, int productId, int quantity);
}
