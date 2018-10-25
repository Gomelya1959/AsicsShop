package org.springmvcshoppingcart.dao;

import org.springmvcshoppingcart.entity.Reservation;
import org.springmvcshoppingcart.model.ProductInfo;
import org.springmvcshoppingcart.model.ReservationInfo;

import java.util.Date;

public interface ReservationDAO {

    public Reservation save (Date date, String user, String code, int numbers);

    public void sendingNotifications(ProductInfo productInfo);

    public Reservation findReservation(String id);

    public ReservationInfo findReservationInfo(String id);

    public void delete(String id);

    public void deleteReservation();
}
