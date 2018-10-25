package org.springmvcshoppingcart.dao.impl;

import java.util.*;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;


import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springmvcshoppingcart.dao.AccountDAO;
import org.springmvcshoppingcart.dao.CourierDAO;
import org.springmvcshoppingcart.dao.OrderDAO;
import org.springmvcshoppingcart.dao.ProductDAO;
import org.springmvcshoppingcart.entity.*;
import org.springmvcshoppingcart.model.*;

//Transactional for Hibernate
@Transactional
public class OrderDAOImpl implements OrderDAO {

    @PersistenceContext
    private EntityManager entityManager;

//    @PersistenceUnit
//    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private AccountDAO accountDAO;

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private CourierDAO courierDAO;

    @Override
    public OrderInfo findByNum(int num) {
        String sql = "Select new " + OrderInfo.class.getName()
                + "(ord.id, ord.orderDate, ord.orderNum, ord.amount, ord.saving, "
                + " ord.customerName, ord.customerAddress, ord.customerEmail, ord.customerPhone, ord.delivery, "
                + " ord.orderDelivery.id, ord.orderDelivery.date, ord.orderDelivery.time) " + " from "
                + Order.class.getName() + " ord " +
                "where ord.orderNum = :num";
        Session session = this.sessionFactory.getCurrentSession();

        Query query = session.createQuery(sql);
        query.setParameter("num", num);

        OrderInfo orderInfo = (OrderInfo)query.getSingleResult();
        Order order = this.findOrder(orderInfo.getId());
        if (order.getCourier() != null) {
            orderInfo.setCourierName(courierDAO.findCourier(order.getCourier()).getName());
            orderInfo.setCourierPhone(courierDAO.findCourier(order.getCourier()).getPhone());
        }
        if (order.isBack()) {
            orderInfo.setBack(true);
        }
        if (order.isPartialReturn()) {
            orderInfo.setPartialReturn(true);
        }
        return orderInfo;
    }

    private int getMaxOrderNum() {
        String sql = "Select max(o.orderNum) from " + Order.class.getName() + " o ";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(sql);
        Integer value = (Integer) query.getSingleResult();
        if (value == null) {
            return 0;
        }
        return value;
    }

    @Override
    public void delete(OrderInfo orderInfo) {
        Session session = this.sessionFactory.getCurrentSession();
        Criteria crit;
        for (OrderDetailInfo orderDetailInfo : listOrderDetailInfos(orderInfo.getId())) {
            crit = session.createCriteria(OrderDetail.class);
            crit.add(Restrictions.eq("id", orderDetailInfo.getId()));
            session.delete(crit.uniqueResult());
        }
        crit = session.createCriteria(Order.class);
        crit.add(Restrictions.eq("id", orderInfo.getId()));
        session.delete(crit.uniqueResult());
        crit = session.createCriteria(Delivery.class);
        crit.add(Restrictions.eq("id", orderInfo.getDeliveryInfoId()));
        session.delete(crit.uniqueResult());
    }

    @Override
    public void saveOrder(CartInfo cartInfo, DeliveryInfo deliveryInfo, String user_name) {
        Session session = sessionFactory.getCurrentSession();
        int orderNum = this.getMaxOrderNum() + 1;
        Order order = new Order();

        if (deliveryInfo.getDate() != null) {
            Delivery delivery = new Delivery();
            delivery.setId(UUID.randomUUID().toString());
            delivery.setDate(deliveryInfo.getDate());
            delivery.setTime(deliveryInfo.getTime());
            session.persist(delivery);
            order.setOrderDelivery(delivery);
            order.setStatus(false);
        }
        else {
            Delivery delivery = new Delivery();
            delivery.setId(UUID.randomUUID().toString());
            delivery.setDate("NOT");
            delivery.setTime("NOT");
            session.persist(delivery);
            order.setOrderDelivery(delivery);
        }
        order.setId(UUID.randomUUID().toString());
        order.setOrderNum(orderNum);
        order.setOrderDate(new Date());
        double discount;
        if (accountDAO.findAccount(user_name).getDiscount() != null) {
            discount = accountDAO.findAccount(user_name).getDiscount();
        }
        else {
            discount = 0.0;
        }
        order.setAmount(cartInfo.getAmountTotal() * (1 - discount/100));
        order.setSaving(cartInfo.getAmountSaving() * (1 - discount/100));
        if (cartInfo.isDeliveryYes()) {
            order.setDelivery("YES");
            Courier courier = courierDAO.choiceOfCourier(order);
            order.setCourier(courier.getId());
        }
        else {
            order.setDelivery("NO");
        }
        CustomerInfo customerInfo = cartInfo.getCustomerInfo();
        order.setCustomerName(customerInfo.getName());
        order.setCustomerEmail(customerInfo.getEmail());
        order.setCustomerPhone(customerInfo.getPhone());
        order.setCustomerAddress(customerInfo.getAddress());
        order.setBack(false);
        order.setPartialReturn(false);
        order.setAccount(accountDAO.findAccount(user_name));

        session.persist(order);

        List<CartLineInfo> lines = cartInfo.getCartLines();

        for (CartLineInfo line : lines) {
            OrderDetail detail = new OrderDetail();
            detail.setId(UUID.randomUUID().toString());
            detail.setOrder(order);
            detail.setAmount(line.getAmount() * (1 - discount/100));
            detail.setSaving(line.getSaving() * (1 - discount/100));
            detail.setPrice(line.getProductInfo().getPrice());
            detail.setQuanity(line.getQuantity());

            String code = line.getProductInfo().getCode();
            Product product = this.productDAO.findProduct(code);
            detail.setProduct(product);

            session.persist(detail);
        }

        cartInfo.setOrderNum(orderNum);
    }

    @Override
    public PaginationResult<OrderInfo> listOrderInfo(int page, int maxResult, int maxNavigationPage, String userName) {
        org.hibernate.Query query;
        Courier courier = courierDAO.findCourier2(userName);
        String string1 = " where ord.courier = :courierId";
        String string2 = " where ord.account = :account";
        String string3 = " order by ord.id";
        String sql = "Select new " + OrderInfo.class.getName()
                + "(ord.id, ord.orderDate, ord.orderNum, ord.amount, ord.saving, "
                + " ord.customerName, ord.customerAddress, ord.customerEmail, ord.customerPhone, ord.delivery, "
                + " ord.orderDelivery.id, ord.orderDelivery.date, ord.orderDelivery.time, ord.status, ord.back, ord.partialReturn) " + " from "
                + Order.class.getName() + " ord ";
        Session session = this.sessionFactory.getCurrentSession();
        if (accountDAO.findAccount(userName).getUserRole().equals("EMPLOYEE")) {
            sql += string2;
            query = session.createQuery(sql);
            query.setParameter("account", accountDAO.findAccount(userName));
            return new PaginationResult<>(query, page, maxResult, maxNavigationPage);
        }
        if (courier != null) {
            String courierId = courier.getId();
            sql += string1;
            query = session.createQuery(sql);
            query.setParameter("courierId", courierId);
            return new PaginationResult<>(query, page, maxResult, maxNavigationPage);
        }
        sql +=string3;
        query = session.createQuery(sql);
        return new PaginationResult<>(query, page, maxResult, maxNavigationPage);
    }

    public Order findOrder(String orderId) {
        Session session = sessionFactory.getCurrentSession();
        Criteria crit = session.createCriteria(Order.class);
        crit.add(Restrictions.eq("id", orderId));
        return (Order) crit.uniqueResult();
    }

    public Order findOrderByUserName(String user_name) {
        Session session = sessionFactory.getCurrentSession();
//        Transaction transaction = session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> orderRoot = criteriaQuery.from(Order.class);
        ParameterExpression<Account> account = criteriaBuilder.parameter(Account.class);
        criteriaQuery.select(orderRoot).where(criteriaBuilder.equal(orderRoot.get("account"), account));
        TypedQuery<Order> query = entityManager.createQuery(criteriaQuery);
        query.setParameter("account", accountDAO.findAccount(user_name));
//        transaction.commit();
        return query.getSingleResult();
    }

    @Override
    public OrderInfo getOrderInfo(String orderId) {
        Order order = this.findOrder(orderId);
        if (order == null) {
            return null;
        }
        OrderInfo orderInfo = new OrderInfo(order.getId(), order.getOrderDate(), //
                order.getOrderNum(), order.getAmount(), order.getSaving(),
                order.getCustomerName(),
                order.getCustomerAddress(), order.getCustomerEmail(), order.getCustomerPhone(), order.getDelivery(),
                order.getOrderDelivery().getId(), order.getOrderDelivery().getDate(), order.getOrderDelivery().getTime());
        if (order.getCourier() != null) {
            orderInfo.setCourierName(courierDAO.findCourier(order.getCourier()).getName());
            orderInfo.setCourierPhone(courierDAO.findCourier(order.getCourier()).getPhone());
        }
        if (order.isBack()) {
            orderInfo.setBack(true);
        }
        if (order.isPartialReturn()) {
            orderInfo.setPartialReturn(true);
        }
        orderInfo.setUser_name(order.getAccount().getUserName());
        return orderInfo;
    }

    @Override
    public List<OrderDetailInfo> listOrderDetailInfos(String orderId) {
        String sql = "Select new " + OrderDetailInfo.class.getName() //
                + "(d.id, d.product.code, d.product.name , d.quanity,d.price,d.amount,d.saving) "//
                + " from " + OrderDetail.class.getName() + " d "//
                + " where d.order.id = :orderId ";

        Session session = this.sessionFactory.getCurrentSession();

        Query query = session.createQuery(sql);


//        Query query  = entityManager.createQuery(sql);
        query.setParameter("orderId", orderId);

        return query.getResultList();
    }

    public void saveStatusOfDelivery(String orderId) {
        Session session = sessionFactory.getCurrentSession();
        Criteria crit = session.createCriteria(Order.class);
        crit.add(Restrictions.eq("id", orderId));
        Order order = (Order) crit.uniqueResult();
        if (order != null) {
            order.setStatus(true);
            session.update(order);
        }
    }

    @Override
    public void saveOrderDetails(OrderDetailInfo orderDetailInfo, Order order) {
        Session session = sessionFactory.getCurrentSession();
        Criteria crit = session.createCriteria(OrderDetail.class);
        crit.add(Restrictions.eq("id", orderDetailInfo.getId()));
        OrderDetail orderDetail = (OrderDetail) crit.uniqueResult();
        orderDetail.setQuanity(orderDetailInfo.getQuanity());
        orderDetail.setAmount(orderDetailInfo.getAmount());
        orderDetail.setSaving(orderDetailInfo.getSaving());
        orderDetail.setOrder(order);
        session.update(orderDetail);
    }

    @Override
    public void saveOrderWithReturn(OrderInfo orderInfo, List<OrderDetailInfo> details_tmp) {
        Session session = sessionFactory.getCurrentSession();
        Criteria crit = session.createCriteria(Order.class);
        crit.add(Restrictions.eq("id", orderInfo.getId()));
        Order order = (Order) crit.uniqueResult();
        int i = 0;
        System.out.println(details_tmp.size());
        for (OrderDetailInfo orderDetailInfo : details_tmp) {
            if (orderDetailInfo.getAmount() != 0) {
                order.setPartialReturn(true);
                break;
            }
            else {
                i++;
            }
        }
        for (OrderDetailInfo orderDetailInfo : details_tmp) {
            saveOrderDetails(orderDetailInfo, order);
        }
        if (details_tmp.size() == i) {
            order.setBack(true);
        }
        order.setAmount(orderInfo.getAmount());
        order.setSaving(orderInfo.getSaving());
        session.update(order);
    }

    @Override
    public OrderInfo getOrderInfoByUserName(String user_name) {
        Order order = this.findOrderByUserName(user_name);
        if (order == null) {
            return null;
        }
        OrderInfo orderInfo = new OrderInfo(order.getId(), order.getOrderDate(), //
                order.getOrderNum(), order.getAmount(), order.getSaving(),
                order.getCustomerName(),
                order.getCustomerAddress(), order.getCustomerEmail(), order.getCustomerPhone(), order.getDelivery(),
                order.getOrderDelivery().getId(), order.getOrderDelivery().getDate(), order.getOrderDelivery().getTime());
        if (order.getCourier() != null) {
            orderInfo.setCourierName(courierDAO.findCourier(order.getCourier()).getName());
            orderInfo.setCourierPhone(courierDAO.findCourier(order.getCourier()).getPhone());
        }
        if (order.isBack()) {
            orderInfo.setBack(true);
        }
        if (order.isPartialReturn()) {
            orderInfo.setPartialReturn(true);
        }
        return orderInfo;
    }
}
