package com.data.controller;

import com.data.model.Cart;
import com.data.model.Customer;
import com.data.model.Product;
import com.data.service.cart.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;

@Controller
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping("carts/{id}")
    public String carts(@PathVariable int id, Model model, HttpSession session) {
        List<Cart> cartList = cartService.findAll(id);
        model.addAttribute("customerId", id);
        session.setAttribute("customerId", id);
        model.addAttribute("cartList", cartList);

        BigDecimal totalPrice = cartList.stream()
                .map(item -> {
                    Product product = item.getProduct();
                    return product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        model.addAttribute("totalPrice", totalPrice);
        session.setAttribute("totalPrice", totalPrice);
        return "carts";
    }

    @PostMapping("add-to-cart")
    public String addToCart(@RequestParam("productId") int productId, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return "redirect:/login";
        }

        cartService.addToCart(customer.getId(), productId);
        return "redirect:/product-detail/" + productId;
    }

    @PostMapping("/update-cart")
    public String updateCart(
            @RequestParam("cartId") int cartId,
            @RequestParam("productId") int productId,
            @RequestParam("quantity") int quantity,
            @RequestParam("action") String action,
            HttpSession session
    ) {
        int updatedQuantity = quantity;
        int customerId = (int) session.getAttribute("customerId");

        if ("increase".equals(action)) {
            updatedQuantity++;
        } else if ("decrease".equals(action) && quantity > 1) {
            updatedQuantity--;
        }

        cartService.updateItemQuantity(cartId, productId, updatedQuantity);
        return "redirect:/carts/" + customerId;
    }

    @PostMapping("delete-from-cart/{id}")
    public String deleteFromCart(@PathVariable int id, HttpSession session) {
        int customerId = (int) session.getAttribute("customerId");
        cartService.deleteFromCart(id);
        return "redirect:/carts/" + customerId;
    }
}
