package org.springmvcshoppingcart.dao;

import org.springmvcshoppingcart.model.DiscountInfo;

import java.util.*;

public interface DiscountDAO {

    public List<DiscountInfo> getListDiscounts(String product_code);

    public void save(String quantity, String value, String code);

    public void delete (String discountId);

    public void update (DiscountInfo discountInfo);
}
