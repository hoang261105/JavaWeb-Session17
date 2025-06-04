package com.data.controller;

import com.data.model.Customer;
import com.data.model.Product;
import com.data.model.Role;
import com.data.service.customer.CustomerService;
import com.data.service.order.OrderService;
import com.data.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.regex.Pattern;

@Controller
public class AuthController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

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

    @GetMapping("dashboard")
    public String admin(Model model, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        model.addAttribute("customer", customer);

        int countCustomers = (int) customerService.count();
        model.addAttribute("totalUsers", countCustomers);

        long countProducts = productService.count();
        model.addAttribute("totalProducts", countProducts);

        long countOrders = orderService.count();
        model.addAttribute("totalOrders", countOrders);
        model.addAttribute("viewName", "dashboard");
        model.addAttribute("fragmentName", "content");
        return "layout";
    }

    @GetMapping("adminProduct")
    public String adminProducts(Model model, @RequestParam(defaultValue = "1") int page){
        int size = 5;
        List<Product> products = productService.paginate(page, size);
        int totalPages = (int) Math.ceil((double) productService.count() / size);
        model.addAttribute("pageSize", size);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("products", products);
        model.addAttribute("viewName", "adminProduct");
        model.addAttribute("fragmentName", "content");
        return "layout";
    }
    
    @GetMapping("adminUser")
    public String adminUser(Model model, @RequestParam(defaultValue = "1") int page) {
        int size = 5;
        List<Customer> customers = customerService.getAll(page, size);
        int totalPages = (int) Math.ceil((double) customerService.count() / size);
        model.addAttribute("pageSize", size);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("customers", customers);
        model.addAttribute("viewName", "adminUser");
        model.addAttribute("fragmentName", "content");
        return "layout";
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
    public String loginUser(@Valid @ModelAttribute("customer") Customer customer, BindingResult result, HttpSession session){
        Customer customerLogined = customerService.login(customer.getEmail(), customer.getPassword());

        if (customerLogined != null) {
            session.setAttribute("customer", customerLogined);
            if (customerLogined.getRole() == Role.ADMIN){
                return "redirect:/dashboard";
            }else if (customerLogined.getRole() == Role.USER){
                return "redirect:/home";
            }
        }

        result.rejectValue("password", "password.invalid", "Email hoặc mật khẩu không đúng!");
        return "login";
    }

    @GetMapping("searchUser")
    public String searchUser(
            @RequestParam("username") String name,
            @RequestParam(defaultValue = "1") int page,
            Model model) {
        int size = 5;
        List<Customer> customers = customerService.searchUserPaginate(name, page, size);
        int totalPages = (int) Math.ceil((double) customerService.count() / size);

        model.addAttribute("pageSize", size);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("customers", customers);
        model.addAttribute("username", name);
        model.addAttribute("message", "Không có kết quả phù hợp!");
        model.addAttribute("viewName", "adminUser");
        model.addAttribute("fragmentName", "content");
        return "layout";
    }

    @PostMapping("update-status/{id}")
    public String updateStatus(@PathVariable int id, @RequestParam(defaultValue = "1") int page){
        customerService.updateStatus(id);
        return "redirect:/adminUser?page=" + page;
    }
}
