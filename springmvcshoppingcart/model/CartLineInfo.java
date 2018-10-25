package org.springmvcshoppingcart.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartLineInfo {

    private ProductInfo productInfo;
    private int quantity;
    private List<DiscountInfo> discountInfoList;


    public CartLineInfo() {
        this.quantity = 0;
    }

    public ProductInfo getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(ProductInfo productInfo) {
        this.productInfo = productInfo;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getAmount() {
        if (discountInfoList == null || discountInfoList.size() == 0) {
            return this.productInfo.getPrice() * this.quantity;
        }
        Map<Integer, Integer> discountMap = new HashMap<>();
        for (DiscountInfo discountInfo : discountInfoList) {
            discountMap.put(discountInfo.getQuantity(), discountInfo.getValue());
        }
        Map.Entry<Integer, Integer> discountEntry_tmp = null;
        for (Map.Entry<Integer, Integer> discountEntry : discountMap.entrySet()) {
            int first = discountEntry.getKey();
            if (this.quantity < first) {
                return this.productInfo.getPrice() * this.quantity;
            }
            if (this.quantity == discountEntry.getKey()) {
                return this.productInfo.getPrice() * this.quantity * (100 - discountEntry.getValue())/100;
            }
            discountEntry_tmp = discountEntry;
        }
        return this.productInfo.getPrice() * this.quantity * (100 - discountEntry_tmp.getValue())/100;
    }

    public double getSaving() {
        return this.productInfo.getPrice() * this.quantity - getAmount();
    }

    public List<DiscountInfo> getDiscountInfoList() { return discountInfoList; }

    public void setDiscountInfoList(List<DiscountInfo> discountInfoList) { this.discountInfoList = discountInfoList; }
}

