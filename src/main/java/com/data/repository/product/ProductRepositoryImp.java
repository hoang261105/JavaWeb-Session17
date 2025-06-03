package com.data.repository.product;

import com.data.model.Product;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepositoryImp implements ProductRepository {
    @Autowired
    private SessionFactory sessionFactory;

    public List<Product> paginate(int pageNumber, int size) {
        Session session = sessionFactory.openSession();

        Query<Product> query = session.createQuery("from Product", Product.class);
        int firstResult = (pageNumber - 1) * size;
        query.setFirstResult(firstResult);
        query.setMaxResults(size);

        return query.getResultList();
    }

    @Override
    public Long count() {
        Session session = sessionFactory.openSession();

        try {
            Query<Long> query = session.createQuery("select count(p) from Product p", Long.class);
            return query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            session.close();
        }

        return null;
    }

    @Override
    public Product findById(int id) {
        Session session = sessionFactory.openSession();

        try {
            Query<Product> query = session.createQuery("from Product p where p.productId = :id", Product.class);
            query.setParameter("id", id);

            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            session.close();
        }
        return null;
    }

}
