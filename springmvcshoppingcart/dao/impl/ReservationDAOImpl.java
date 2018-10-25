package org.springmvcshoppingcart.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springmvcshoppingcart.dao.AccountDAO;
import org.springmvcshoppingcart.dao.ProductDAO;
import org.springmvcshoppingcart.dao.ReservationDAO;
import org.springmvcshoppingcart.entity.Product;
import org.springmvcshoppingcart.entity.Reservation;
import org.springmvcshoppingcart.model.ProductInfo;
import org.springmvcshoppingcart.model.ReservationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.Query;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Transactional
public class ReservationDAOImpl implements ReservationDAO {

    @Autowired
    private AccountDAO accountDAO;

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private SessionFactory sessionFactory;

    private static final Long TimeForWait = 60000L;

    @Override
    public Reservation save(Date date, String user, String code, int numbers) {
        Session session = sessionFactory.getCurrentSession();
        Reservation reservation = new Reservation();
        reservation.setId(UUID.randomUUID().toString());
        reservation.setCreateDate(date);
        reservation.setAccount(accountDAO.findAccount(user));
        reservation.setProduct(productDAO.findProduct(code));
        reservation.setNumbers(numbers);
        session.persist(reservation);
        return reservation;
    }

    @Override
    public Reservation findReservation(String id) {
        Session session = sessionFactory.getCurrentSession();
        Criteria crit = session.createCriteria(Reservation.class);
        crit.add(Restrictions.eq("id", id));
        return (Reservation) crit.uniqueResult();
    }

    @Override
    public void delete(String id) {
        Session session = sessionFactory.getCurrentSession();
        Criteria crit = session.createCriteria(Reservation.class);
        crit.add(Restrictions.eq("id", id));
        session.delete(crit.uniqueResult());
    }

    @Override
    public ReservationInfo findReservationInfo(String id) {
        Reservation reservation = findReservation(id);
        if (reservation == null) {
            return null;
        }
        return new ReservationInfo(id, reservation.getCreateDate(), reservation.getAccount().getUserName(), reservation.getProduct().getCode(), reservation.getNumbers());
    }

    @Override
    public void deleteReservation() {
        long currentTime = new Date().getTime();
        String sql = "Select p "
                 + "from " + Reservation.class.getName() + " p ";
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(sql);
        for (Reservation reservation : (List<Reservation>)(query.getResultList())) {
            if ((currentTime - reservation.getCreateDate().getTime()) > TimeForWait && reservation.getNotify() != null) {
                session.delete(reservation);
                Product product = productDAO.findProduct(reservation.getProduct().getCode());
                product.setSurplus(product.getSurplus() + reservation.getNumbers());
                session.update(product);
            }
        }
    }

    @Override
    public void sendingNotifications(ProductInfo productInfo) {
        Session session = sessionFactory.getCurrentSession();
        Criteria crit = session.createCriteria(Reservation.class);
        crit.add(Restrictions.eq("product", productDAO.findProduct(productInfo.getCode())));
        List<Reservation> reservationList = crit.list();
        if (reservationList != null) {
            int num = productInfo.getSurplus();
            String message;
            for (Reservation reservation : reservationList) {
                if (reservation.getNumbers() <= num) {
//                    String href = "<a href=" + "'${pageContext.request.contextPath}/buy?code='" + reservation.getId() + ">Buy reserved products</a>";
                    message = "Hello, " + reservation.getAccount().getUserName() + "! You have been reserved product " +
                            reservation.getProduct().getName() + "in the amount of " + reservation.getNumbers() + " units. " +
                            "Please make an order within " + TimeForWait/60000 +  " minutes, otherwise your reservation will be cancelled." +
                    "Code for reservation: " + "\n" + reservation.getId();
                    reservation.setNotify("YES");
                    session.update(reservation);
                    num = num - reservation.getNumbers();
                    send(message , reservation.getEmail());
                }
                else if ( num > 0){
                    message = "Hello, " + reservation.getAccount().getUserName() + "! Currently available " +
                            reservation.getProduct().getName() + "in the amount of " + num + " units. " +
                            "Please make an order within " + TimeForWait/60000 + " minutes, otherwise your reservation will be cancelled." +
                    "Code for reservation: " + "\n" + reservation.getId();
                    reservation.setNotify("YES");
                    session.update(reservation);
                    num = 0;
                    send(message, reservation.getEmail());
                }
            }
        }
    }

        public void send (String text, String toEmail) {

        final String username = "gomelya2017@gmail.com";
        final String password = "3589cska3589";

            Properties props = System.getProperties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            javax.mail.Session session = javax.mail.Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            try {
                Message message = new MimeMessage(session);
                //от кого
                message.setFrom(new InternetAddress(username));
                //кому
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
                //Заголовок письма
                message.setSubject("Sending Notifications");
                //Содержимое
                message.setText(text);
                //Отправляем сообщение
                Transport.send(message);
                System.out.println("Email Sent successfully....");
            } catch (MessagingException mex){ mex.printStackTrace(); }
        }
}
