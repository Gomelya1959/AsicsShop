package org.springmvcshoppingcart.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springmvcshoppingcart.dao.DiscountDAO;
import org.springmvcshoppingcart.dao.ProductDAO;
import org.springmvcshoppingcart.entity.Discount;
import org.springmvcshoppingcart.model.DiscountInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional
public class DiscountDAOImpl implements DiscountDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private ProductDAO productDAO;


    @Override
    public List<DiscountInfo> getListDiscounts(String product_code) {
        String sql = "Select new " + DiscountInfo.class.getName()
                + "(dis.id, dis.quantity, dis.value, dis.product.code) " + " from "
                + Discount.class.getName() + " dis "
                + " where dis.product.code = :code ";
        Session session = this.sessionFactory.getCurrentSession();

        Query query = session.createQuery(sql);
        query.setParameter("code", product_code);
        return query.list();
    }

    @Override
    public void save(String quantity, String value, String code) {
        Discount discount = new Discount();
        discount.setQuantity(Integer.parseInt(quantity));
        discount.setValue(Integer.parseInt(value));
        discount.setProduct(productDAO.findProduct(code));
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(discount);
    }

    @Override
    public void delete (String discountId) {
        Session session = this.sessionFactory.getCurrentSession();
        Criteria crit = null;
        crit = session.createCriteria(Discount.class);
        crit.add(Restrictions.eq("id", Integer.parseInt(discountId)));
        session.delete(crit.uniqueResult());
    }

    @Override
    public void update (DiscountInfo discountInfo) {
        Discount discount = new Discount();
        discount.setId(discountInfo.getId());
        discount.setProduct(productDAO.findProduct(discountInfo.getProduct_code()));
        discount.setQuantity(discountInfo.getQuantity());
        discount.setValue(discountInfo.getValue());
        Session session = this.sessionFactory.getCurrentSession();
        session.update(discount);
    }
}
