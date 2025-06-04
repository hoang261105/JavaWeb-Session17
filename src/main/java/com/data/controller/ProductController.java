package com.data.controller;

import com.data.model.Cart;
import com.data.model.Customer;
import com.data.model.Product;
import com.data.service.CloudinaryService;
import com.data.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private CloudinaryService cloudinaryService;

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

    @GetMapping("add-product")
    public String add(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        return "addProduct";
    }

    @PostMapping("create-product")
    public String create(@Valid @ModelAttribute("product") Product product, BindingResult result) throws IOException {
        if (product.getFile() == null || product.getFile().isEmpty()) {
            result.rejectValue("file", "error", "Vui lòng chọn hình ảnh!");
        }

        if (result.hasErrors()) {
            return "addProduct";
        }

        String URL = cloudinaryService.uploadFile(product.getFile());

        product.setImage(URL);
        productService.save(product);
        return "redirect:/adminProduct";
    }

    @GetMapping("editProduct/{id}")
    public String edit(@PathVariable int id, Model model) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        return "editProduct";
    }

    @PostMapping("update-product")
    public String update(@Valid @ModelAttribute("product") Product product, BindingResult result) throws IOException {
        if (result.hasErrors()) {
            return "editProduct";
        }

        if (product.getFile() != null && !product.getFile().isEmpty()) {
            String newURL = cloudinaryService.uploadFile(product.getFile());
            product.setImage(newURL);
        }else{
            Product product1 = productService.findById(product.getProductId());
            product.setImage(product1.getImage());
        }

        productService.update(product);
        return "redirect:/adminProduct";
    }

    @PostMapping("deleteProduct/{id}")
    public String delete(@PathVariable int id){
        productService.delete(id);
        return "redirect:/adminProduct";
    }

    @GetMapping("search")
    public String search(@RequestParam("productName") String name, @RequestParam(defaultValue = "1") int page, Model model){
        int size = 5;
        List<Product> products = productService.searchProductPaginate(name, page, size);
        int totalPages = (int) Math.ceil((double) productService.count() / size);

        model.addAttribute("products", products);
        model.addAttribute("productName", name);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("message", "Không có kết quả phù hợp");
        model.addAttribute("viewName", "adminProduct");
        model.addAttribute("fragmentName", "content");
        return "layout";
    }
}
