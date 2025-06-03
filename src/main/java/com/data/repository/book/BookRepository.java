package com.data.repository.book;

import com.data.model.Book;

import java.util.List;

public interface BookRepository {
    List<Book> findAll();

    void delete(int id);
}
