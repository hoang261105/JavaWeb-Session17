package com.data.service.book;

import com.data.model.Book;

import java.util.List;

public interface BookService {
    List<Book> findAll();

    void delete(int id);
}
