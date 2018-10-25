package org.springmvcshoppingcart.dao;

import java.util.List;

import org.springmvcshoppingcart.entity.Order;
import org.springmvcshoppingcart.model.*;
import org.springmvcshoppingcart.model.*;

public interface OrderDAO {

    public void saveOrder(CartInfo cartInfo, DeliveryInfo deliveryInfo, String user_name);

    public PaginationResult<OrderInfo> listOrderInfo(int page,
                                                     int maxResult, int maxNavigationPage, String userName);

    public OrderInfo getOrderInfo(String orderId);

    public OrderInfo getOrderInfoByUserName(String user_name);

    public List<OrderDetailInfo> listOrderDetailInfos(String orderId);

    public OrderInfo findByNum(int num);

    public void delete(OrderInfo orderInfo);

    public void saveStatusOfDelivery(String orderId);

    public Order findOrder(String orderId);

    public void saveOrderDetails(OrderDetailInfo orderDetailInfo, Order order);

    public void saveOrderWithReturn(OrderInfo orderInfo, List<OrderDetailInfo> details_tmp);
}