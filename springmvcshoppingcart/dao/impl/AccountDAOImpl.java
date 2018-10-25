package org.springmvcshoppingcart.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springmvcshoppingcart.dao.AccountDAO;
import org.springmvcshoppingcart.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

// Transactional for Hibernate
@Transactional
public class AccountDAOImpl implements AccountDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Account findAccount(String userName) {
        Session session = sessionFactory.getCurrentSession();
        Criteria crit = session.createCriteria(Account.class);
        crit.add(Restrictions.eq("userName", userName));
        return (Account) crit.uniqueResult();
    }

    @Override
    public void saveTotal(Account account, double amount) {
        Session session = sessionFactory.getCurrentSession();
        if (account.getTotal() == null) {
            account.setTotal(amount);
            session.update(account);
        }
        else {
            account.setTotal(account.getTotal() + amount);
            session.update(account);
        }
    }

    @Override
    public void updateAccount(Account account) {
        Session session = sessionFactory.getCurrentSession();
        session.update(account);
    }

}
