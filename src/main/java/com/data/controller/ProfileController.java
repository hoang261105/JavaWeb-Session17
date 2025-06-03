package com.data.controller;

import com.data.model.Customer;
import com.data.model.Order;
import com.data.service.customer.CustomerService;
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
import java.util.List;
import java.util.regex.Pattern;

@Controller
public class ProfileController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderService orderService;

    @GetMapping("profile/{id}")
    public String profile(@PathVariable int id, Model model) {
        Customer customer = customerService.findById(id);
        model.addAttribute("customer", customer);
        model.addAttribute("customerId", id);
        return "profile";
    }

    @GetMapping("history/{id}")
    public String history(@PathVariable int id, Model model) {
        List<Order> orders = orderService.findByCustomerId(id);
        model.addAttribute("orders", orders);
        model.addAttribute("customerId", id);
        return "history";
    }

    @PostMapping("change-profile")
    public String changeProfile(@Valid @ModelAttribute("customer") Customer customer, BindingResult result) {
        String regexEmail = "^[a-zA-Z0-9._]+@[a-zA-Z0-9]+\\.[a-zA-Z]{2,6}$";
        String regexPhone = "^(0)\\d{9}$";

        if (!Pattern.matches(regexEmail, customer.getEmail()) && !customer.getEmail().isEmpty()) {
            result.rejectValue("email", "email.invalid", "Email không đúng định dạng!");
        }

        if (!Pattern.matches(regexPhone, customer.getPhone()) && !customer.getPhone().isEmpty()) {
            result.rejectValue("phone", "phone.invalid", "Số điện thoại không hợp lệ!");
        }

        if (result.hasErrors()) {
            return "profile";
        }

        customerService.update(customer);
        return "redirect:/home";
    }
}
