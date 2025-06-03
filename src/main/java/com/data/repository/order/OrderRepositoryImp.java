package com.data.repository.order;

import com.data.model.Cart;
import com.data.model.Customer;
import com.data.model.Order;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class OrderRepositoryImp implements OrderRepository {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void save(Order order, int customerId) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            List<Cart> carts = session.createQuery("from Cart c where c.customer.id = :customerId", Cart.class)
                    .setParameter("customerId", customerId)
                    .getResultList();

            List<Integer> productIds = carts.stream()
                    .map(cart -> cart.getProduct().getProductId())
                    .collect(Collectors.toList());

            Customer customer = session.get(Customer.class, customerId);

            order.setCustomer(customer);
            order.setProducts(productIds);
            session.save(order);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Order> findByCustomerId(int customerId) {
        Session session = sessionFactory.openSession();

        Query<Order> query = session.createQuery("from Order o where o.customer.id = :customerId", Order.class);

        query.setParameter("customerId", customerId);
        return query.getResultList();
    }
}
