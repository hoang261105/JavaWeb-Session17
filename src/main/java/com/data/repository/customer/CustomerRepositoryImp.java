package com.data.repository.customer;

import com.data.model.Customer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerRepositoryImp implements CustomerRepository {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void register(Customer customer) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.save(customer);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    @Override
    public Customer login(String email, String password) {
        Session session = sessionFactory.openSession();

        try {
            Query<Customer> query = session.createQuery("from Customer where email = :email and password = :password", Customer.class);
            query.setParameter("email", email);
            query.setParameter("password", password);

            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }

    @Override
    public Customer findById(int id) {
        Session session = sessionFactory.openSession();

        Query<Customer> query = session.createQuery("from Customer where id = :id", Customer.class);

        query.setParameter("id", id);
        return query.uniqueResult();
    }

    @Override
    public void update(Customer customer) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.update(customer);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
