package org.springmvcshoppingcart.dao;

import org.springmvcshoppingcart.model.RegularCustomerDiscountInfo;

import java.util.List;

public interface RegularCustomerDiscountDAO {

    public List<RegularCustomerDiscountInfo> getListDiscounts();

    public void save(String sum, String value);

    public void delete (String discountId);

    public void update (RegularCustomerDiscountInfo discountInfo);

}
