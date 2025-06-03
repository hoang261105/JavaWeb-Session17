package com.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_book")
public class Book {
    @Id // Đánh dấu là khóa chính
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tự tăng
    private int id;

    @Column(name = "title", length = 100, unique = true)
    private String bookTitle;

    private String author;

    private int price;

    private String ISBN;

    @Column(columnDefinition = "text")
    private String image;

    private LocalDate createdDate;
}
