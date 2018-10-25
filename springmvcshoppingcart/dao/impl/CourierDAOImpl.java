package org.springmvcshoppingcart.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springmvcshoppingcart.dao.CourierDAO;
import org.springmvcshoppingcart.entity.Courier;
import org.springmvcshoppingcart.model.CourierInfo;
import org.springmvcshoppingcart.entity.Order;
import org.springmvcshoppingcart.model.PaginationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
public class CourierDAOImpl implements CourierDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<CourierInfo> listCourier() {
        String sql = "SELECT new " + CourierInfo.class.getName() + "(d.id, d.name, d.phone, d.user_name) " + " from " + Courier.class.getName() + " d ";
        Session session = this.sessionFactory.getCurrentSession();

        Query query = session.createQuery(sql);

        List<CourierInfo> courierInfoList = query.list();
        List<CourierInfo> courierInfoList_tmp = new ArrayList<>();

        for (CourierInfo courierInfo : courierInfoList) {
            Courier courier = this.findCourier(courierInfo.getId());
            if (courier.getOrderList() == null) {
                courierInfo.setOrders(new ArrayList<Order>());
            }
            else {
                courierInfo.setOrders(courier.getOrderList());
            }
            courierInfoList_tmp.add(courierInfo);
        }
        return courierInfoList_tmp;
    }

    @Override

    public Courier choiceOfCourier(Order order) {
        Courier courier = new Courier();
        boolean freedom = true;
        int size = 10;
        int i = 0;
        int num = 0;
        boolean freeCourier = false;
        for (CourierInfo courierInfo : this.listCourier()) {
            if (courierInfo.getOrders().size() == 0) {
                courierInfo.getOrders().add(order);
                return this.findCourier(courierInfo.getId());
            }
        }
        List<CourierInfo> courierInfoList = this.listCourier();
        for (CourierInfo courierInfo : this.listCourier()) {
            for (Order order_tmp : courierInfo.getOrders()) {
                if (order_tmp.getOrderDelivery().getDate().equals(order.getOrderDelivery().getDate())
                        || order_tmp.getOrderDelivery().getTime().equals(order.getOrderDelivery().getTime())) {
                    freedom = false;
                    break;
                }
            }
            if (freedom)  {
                int size_tmp = courierInfo.getOrders().size();
                if (size_tmp <= size) {
                    freeCourier = true;
                    size = size_tmp;
                    num = i;
                    courier = this.findCourier(courierInfo.getId());
                }
            }
            i++;
        }
        if (freeCourier) {
            courierInfoList.get(num).getOrders().add(order);
            return courier;
        }
        num = 0;
        i = 0;
        int min = this.listCourier().get(0).getOrders().size();
        for (CourierInfo courierInfo : this.listCourier()) {
            if (courierInfo.getOrders().size() <= min) {
                min = courierInfo.getOrders().size();
                num = i;
            }
            i++;
        }
        courierInfoList.get(num).getOrders().add(order);
        return this.findCourier(this.listCourier().get(num).getId());
    }

    @Override
    public Courier findCourier(String courierId) {
        Session session = sessionFactory.getCurrentSession();
        Criteria crit = session.createCriteria(Courier.class);
        crit.add(Restrictions.eq("id", courierId));
        return (Courier) crit.uniqueResult();
    }

    @Override
    public Courier findCourier2(String user_name) {
        Session session = sessionFactory.getCurrentSession();
        Criteria crit = session.createCriteria(Courier.class);
        crit.add(Restrictions.eq("user_name", user_name));
        return (Courier) crit.uniqueResult();
    }

    @Override
    public CourierInfo getCourierInfo(Courier courier) {
        return new CourierInfo(courier.getId(), courier.getName(), courier.getPhone(), courier.getUser_name());
    }

    @Override
    public void save(CourierInfo courierInfo) {
        String id = courierInfo.getId();

        Courier courier = null;

        boolean isNew = false;
        if (id != null) {
            courier = this.findCourier(id);
        }
        if (courier == null) {
            isNew = true;
            courier = new Courier();
        }
        courier.setId(id);
        courier.setName(courierInfo.getName());
        courier.setPhone(courierInfo.getPhone());
        courier.setUser_name(courierInfo.getUser_name());

        if (isNew) {
            this.sessionFactory.getCurrentSession().persist(courier);
        }
        // If error in DB, Exceptions will be thrown out immediately
        // Nếu có lỗi tại DB, ngoại lệ sẽ ném ra ngay lập tức
        this.sessionFactory.getCurrentSession().flush();
    }

    @Override
    public PaginationResult<CourierInfo> queryCouriers(int page, int maxResult, int maxNavigationPage,
                                                       String likeName) {
        String sql = "Select new " + CourierInfo.class.getName() //
                + "(p.id, p.name, p.phone, p.user_name) " + " from "//
                + Courier.class.getName() + " p ";
        if (likeName != null && likeName.length() > 0) {
            sql += " Where lower(p.name) like :likeName ";
        }
        //
        Session session = sessionFactory.getCurrentSession();

        Query query = session.createQuery(sql);
        if (likeName != null && likeName.length() > 0) {
            query.setParameter("likeName", "%" + likeName.toLowerCase() + "%");
        }
        return new PaginationResult<>(query, page, maxResult, maxNavigationPage);
    }

    @Override
    public PaginationResult<CourierInfo> queryCouriers(int page, int maxResult, int maxNavigationPage) {
        return queryCouriers(page, maxResult, maxNavigationPage, null);
    }

    @Override
    public void delete(CourierInfo courierInfo) {
        Session session = this.sessionFactory.getCurrentSession();
        Criteria crit = null;
        crit = session.createCriteria(Courier.class);
        crit.add(Restrictions.eq("id", courierInfo.getId()));
        session.delete(crit.uniqueResult());
    }
}
