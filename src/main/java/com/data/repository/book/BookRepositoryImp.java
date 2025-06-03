package com.data.repository.book;

import com.data.model.Book;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookRepositoryImp implements BookRepository{
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Book> findAll() {
        Session session = sessionFactory.openSession();

        Query<Book> query = session.createQuery("from Book", Book.class);
        // Phân trang
        int pageNumber = 1;
        int size = 5;

        int firstResult = (pageNumber - 1) * size;

        query.setFirstResult(0);
        query.setMaxResults(5);

        List<Book> books = query.getResultList();

        return books;
    }

    @Override
    public void delete(int id) {
        Session session = sessionFactory.openSession();

        Transaction transaction = session.beginTransaction();

        Query query = session.createQuery("delete from Book where id= " + id);

        query.executeUpdate();

        transaction.commit();
    }
}
