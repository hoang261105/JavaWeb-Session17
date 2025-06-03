package com.data.controller;

import com.data.model.Customer;
import com.data.model.Role;
import com.data.service.customer.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.regex.Pattern;

@Controller
public class AuthController {
    @Autowired
    private CustomerService customerService;

    @GetMapping("register")
    public String register(Model model) {
        Customer customer = new Customer();
        model.addAttribute("customer", customer);
        return "register";
    }

    @GetMapping("login")
    public String login(Model model) {
        Customer customer = new Customer();
        model.addAttribute("customer", customer);
        return "login";
    }

    @GetMapping("admin")
    public String admin(Model model, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        model.addAttribute("customer", customer);
        return "admin";
    }

    @PostMapping("register-user")
    public String registerUser(@Valid @ModelAttribute("customer") Customer customer, BindingResult result){
        String regexEmail = "^[a-zA-Z0-9._]+@[a-zA-Z0-9]+\\.[a-zA-Z]{2,6}$";
        String regexPhone = "^(0)\\d{9}$";

        if (!Pattern.matches(regexEmail, customer.getEmail()) && !customer.getEmail().isEmpty()) {
            result.rejectValue("email", "email.invalid", "Email không đúng định dạng!");
        }

        if (!Pattern.matches(regexPhone, customer.getPhone()) && !customer.getPhone().isEmpty()) {
            result.rejectValue("phone", "phone.invalid", "Số điện thoại không hợp lệ!");
        }

        if (result.hasErrors()) {
            return "register";
        }

        customerService.register(customer);
        return "redirect:/login";
    }

    @PostMapping("login-user")
    public String loginUser(@ModelAttribute("customer") Customer customer, BindingResult result, HttpSession session){
        Customer customerLogined = customerService.login(customer.getEmail(), customer.getPassword());

        if (customerLogined != null) {
            session.setAttribute("customer", customerLogined);
            if (customerLogined.getRole() == Role.ADMIN){
                return "redirect:/admin";
            }else if (customerLogined.getRole() == Role.USER){
                return "redirect:/home";
            }
        }

        result.rejectValue("password", "password.invalid", "Email hoặc mật khẩu không đúng!");
        return "login";
    }
}
