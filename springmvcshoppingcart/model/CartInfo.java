package org.springmvcshoppingcart.model;

import org.springmvcshoppingcart.dao.ProductDAO;

import java.util.ArrayList;
import java.util.List;

public class CartInfo {

    private int orderNum;

    private CustomerInfo customerInfo;

    private boolean deliveryYes;

    private List<CartLineInfo> cartLines = new ArrayList<CartLineInfo>();

    public static final int CostOfDelivery = 10;

    public CartInfo() {

    }

    public void setCartLines(List<CartLineInfo> cartLines) {
        this.cartLines = cartLines;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public CustomerInfo getCustomerInfo() {
        return customerInfo;
    }

    public void setCustomerInfo(CustomerInfo customerInfo) {
        this.customerInfo = customerInfo;
    }

    public List<CartLineInfo> getCartLines() {
        return this.cartLines;
    }

    private CartLineInfo findLineByCode(String code) {
        for (CartLineInfo line : this.cartLines) {
            if (line.getProductInfo().getCode().equals(code)) {
                return line;
            }
        }
        return null;
    }

    public void addProduct(ProductInfo productInfo, int quantity) {
        CartLineInfo line = this.findLineByCode(productInfo.getCode());

        if (line == null) {
            line = new CartLineInfo();
            line.setQuantity(0);
            line.setProductInfo(productInfo);
            this.cartLines.add(line);
        }
        int newQuantity = line.getQuantity() + quantity;
        if (newQuantity <= 0) {
            this.cartLines.remove(line);
        } else {
            line.setQuantity(newQuantity);
        }
    }

    public void validate() {

    }

    public String updateProduct(String code, int quantity, ProductDAO productDAO) {
        CartLineInfo line = this.findLineByCode(code);
        if (line != null) {
            if (quantity <= 0) {
                this.cartLines.remove(line);
            } else {
                line.setQuantity(quantity);
            }
        }
        if (quantity <= productDAO.findProductInfo(code).getSurplus()) {
            return "success";
        }
        else {
            return code;
        }
    }

    public void removeProduct(ProductInfo productInfo) {
        CartLineInfo line = this.findLineByCode(productInfo.getCode());
        if (line != null) {
            this.cartLines.remove(line);
        }
    }

    public boolean isEmpty() {
        return this.cartLines.isEmpty();
    }

    public boolean isValidCustomer() {
        return this.customerInfo != null && this.customerInfo.isValid();
    }

    public int getQuantityTotal() {
        int quantity = 0;
        for (CartLineInfo line : this.cartLines) {
            quantity += line.getQuantity();
        }
        return quantity;
    }


    public double getAmountTotal() {
        double total = 0;
        for (CartLineInfo line : this.cartLines) {
            total += line.getAmount();
        }
        if (deliveryYes)
            return total + CostOfDelivery;
        else return total;
    }

    public double getAmountSaving() {
        double total = 0;
        for (CartLineInfo line : this.cartLines) {
            total += line.getSaving();
        }
        return total;
    }

    public List<String> updateQuantity(CartInfo cartForm, ProductDAO productDAO) {
        List<String> surplus = new ArrayList<>();
        if (cartForm != null) {
            List<CartLineInfo> lines = cartForm.getCartLines();
            for (CartLineInfo line : lines) {
                String result = this.updateProduct(line.getProductInfo().getCode(), line.getQuantity(), productDAO);
                if (!result.equals("success"))
                    surplus.add(result);
            }
        }
        return surplus;
    }

    public void setDeliveryYes(boolean deliveryYes) {
        this.deliveryYes = deliveryYes;
    }

    public boolean isDeliveryYes() {
        return deliveryYes;
    }

}