package org.springmvcshoppingcart.dao;

import org.springmvcshoppingcart.entity.Courier;
import org.springmvcshoppingcart.model.CourierInfo;
import org.springmvcshoppingcart.entity.Order;
import org.springmvcshoppingcart.model.PaginationResult;

import java.util.List;

public interface CourierDAO {

    public Courier choiceOfCourier(Order order);

    public List<CourierInfo> listCourier();

    public Courier findCourier(String courierId);

    public Courier findCourier2 (String name_user);

    public CourierInfo getCourierInfo(Courier courier);

    public void save(CourierInfo courierInfo);

    public void delete(CourierInfo courierInfo);

    public PaginationResult<CourierInfo> queryCouriers(int page,
                                                       int maxResult, int maxNavigationPage  );

    public PaginationResult<CourierInfo> queryCouriers(int page, int maxResult,
                                                       int maxNavigationPage, String likeName);

}
