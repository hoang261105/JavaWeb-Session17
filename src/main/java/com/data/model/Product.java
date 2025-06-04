package com.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;

    @NotBlank(message = "Tên sản phẩm không được để trống!")
    private String productName;

    @NotBlank(message = "Vui lòng nhập mô tả!")
    private String description;
    private BigDecimal price;
    private int stock;

    private transient MultipartFile file;
    private String image;
}
