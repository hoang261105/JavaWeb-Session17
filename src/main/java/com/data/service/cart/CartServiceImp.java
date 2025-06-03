package com.data.service.cart;

import com.data.model.Cart;
import com.data.repository.cart.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImp implements CartService {
    @Autowired
    private CartRepository cartRepository;

    @Override
    public List<Cart> findAll(int customerId) {
        return cartRepository.findAll(customerId);
    }

    @Override
    public void addToCart(int customerId, int productId) {
        cartRepository.addToCart(customerId, productId);
    }

    @Override
    public void deleteFromCart(int cartId) {
        cartRepository.deleteFromCart(cartId);
    }

    @Override
    public void updateItemQuantity(int cartId, int productId, int quantity) {
        cartRepository.updateItemQuantity(cartId, productId, quantity);
    }
}
