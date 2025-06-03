package com.data.controller;

import com.data.model.Cart;
import com.data.model.Customer;
import com.data.model.Product;
import com.data.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("home")
    public String home(Model model, HttpSession session, @RequestParam(defaultValue = "1") int page) {
        int size = 5;
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return "redirect:/login";
        }

        List<Product> productList = productService.paginate(page, size);

        int totalPages = (int) Math.ceil((double) productService.count() / size);
        model.addAttribute("customer", customer);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("productList", productList);
        return "home";
    }

    @GetMapping("product-detail/{id}")
    public String productDetail(@PathVariable int id, Model model) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        return "product-detail";
    }
}
