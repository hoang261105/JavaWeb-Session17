package com.data.repository.cart;

import com.data.model.Cart;
import com.data.model.Customer;
import com.data.model.Product;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CartRepositoryimp implements CartRepository {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Cart> findAll(int customerId) {
        Session session = sessionFactory.openSession();

        Query<Cart> query = session.createQuery("from Cart c where c.customer.id = :customerId", Cart.class);

        query.setParameter("customerId", customerId);

        return query.getResultList();
    }

    @Override
    public void addToCart(int customerId, int productId) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            String hql = "from Cart c where c.customer.id = :customerId and c.product.productId = :productId";
            Cart cart = session.createQuery(hql, Cart.class)
                    .setParameter("customerId", customerId)
                    .setParameter("productId", productId)
                    .uniqueResult();

            if (cart != null) {
                cart.setQuantity(cart.getQuantity() + 1);
                session.update(cart);
            }else{
                Customer customer = session.get(Customer.class, customerId);
                Product product = session.get(Product.class, productId);

                Cart newCart = new Cart();
                newCart.setCustomer(customer);
                newCart.setProduct(product);
                newCart.setQuantity(1);
                session.save(newCart);
            }

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteFromCart(int cartId) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            Query query = session.createQuery("delete Cart c where c.id = :cartId");

            query.setParameter("cartId", cartId);

            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateItemQuantity(int cartId, int productId, int quantity) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            Cart cart = session.get(Cart.class, cartId);

            if (cart != null && cart.getProduct().getProductId() == productId) {
                cart.setQuantity(quantity);
                session.update(cart);
            }

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
