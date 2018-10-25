package org.springmvcshoppingcart.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springmvcshoppingcart.dao.RegularCustomerDiscountDAO;
import org.springmvcshoppingcart.entity.RegularCustomerDiscount;
import org.springmvcshoppingcart.model.RegularCustomerDiscountInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional
public class RegularCustomerDiscountDAOImpl implements RegularCustomerDiscountDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<RegularCustomerDiscountInfo> getListDiscounts() {
        String sql = "Select new " + RegularCustomerDiscountInfo.class.getName()
                + "(dis.id, dis.total, dis.value) " + " from "
                + RegularCustomerDiscount.class.getName() + " dis ";
        Session session = this.sessionFactory.getCurrentSession();
        Query query = session.createQuery(sql);
        return query.list();
    }

    @Override
    public void save(String total, String value) {
        RegularCustomerDiscount discount = new RegularCustomerDiscount();
        discount.setTotal(Integer.parseInt(total));
        discount.setValue(Integer.parseInt(value));
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(discount);
    }

    @Override
    public void delete (String discountId) {
        Session session = this.sessionFactory.getCurrentSession();
        Criteria crit = session.createCriteria(RegularCustomerDiscount.class);
        crit.add(Restrictions.eq("id", Integer.parseInt(discountId)));
        session.delete(crit.uniqueResult());
    }

    @Override
    public void update (RegularCustomerDiscountInfo discountInfo) {
        RegularCustomerDiscount discount = new RegularCustomerDiscount();
        discount.setId(discountInfo.getId());
        discount.setTotal(discountInfo.getTotal());
        discount.setValue(discountInfo.getValue());
        Session session = this.sessionFactory.getCurrentSession();
        session.update(discount);
    }
}
