package com.data.controller;

import com.data.model.Cart;
import com.data.model.Order;
import com.data.service.cart.CartService;
import com.data.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Pattern;

@Controller
public class OrderController {
    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @GetMapping("payment/{id}")
    public String payment(@PathVariable int id, Model model, HttpSession session) {
        List<Cart> carts = cartService.findAll(id);
        Order order = new Order();
        BigDecimal totalPrice = (BigDecimal) session.getAttribute("totalPrice");
        model.addAttribute("cartList", carts);
        session.setAttribute("cartList", carts);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("order", order);
        session.setAttribute("customerId", id);
        return "payment";
    }

    @PostMapping("checkout")
    public String checkout(@Valid @ModelAttribute("order") Order order, BindingResult result, Model model, HttpSession session) {
        String regexPhone = "^(0)\\d{9}$";
        BigDecimal totalPrice = (BigDecimal) session.getAttribute("totalPrice");
        int customerId = (int) session.getAttribute("customerId");

        if (!Pattern.matches(regexPhone, order.getPhoneNumber()) && !order.getPhoneNumber().isEmpty()){
            result.rejectValue("phoneNumber", "phoneNumber.invalid", "Số điện thoại không hợp lệ!");
        }

        if (result.hasErrors()) {
            List<Cart> carts = (List<Cart>) session.getAttribute("cartList");
            model.addAttribute("totalPrice", totalPrice);
            model.addAttribute("cartList", carts);
            return "payment";
        }

        order.setTotalPrice(totalPrice);
        orderService.save(order, customerId);
        return "redirect:/home";
    }
}
