package kiosk.vo;

import java.sql.Date;
import java.util.List;

public class OrderVO {

  int orderNumber, totalOrderPrice;
  boolean orderStatus;
  String products, appliedCoupon;
  Date orderDate;

  public int getOrderNumber() {
    return orderNumber;
  }

  public void setOrderNumber(int orderNumber) {
    this.orderNumber = orderNumber;
  }

  public int getTotalOrderPrice() {
    return totalOrderPrice;
  }

  public void setTotalOrderPrice(int totalOrderPrice) {
    this.totalOrderPrice = totalOrderPrice;
  }

  public boolean isOrderStatus() {
    return orderStatus;
  }

  public void setOrderStatus(boolean orderStatus) {
    this.orderStatus = orderStatus;
  }

  public String getProducts() {
    return products;
  }

  public void setProducts(String products) {
    this.products = products;
  }

  public String getAppliedCoupon() {
    return appliedCoupon;
  }

  public void setAppliedCoupon(String appliedCoupon) {
    this.appliedCoupon = appliedCoupon;
  }

  public Date getOrderDate() {
    return orderDate;
  }

  public void setOrderDate(Date orderDate) {
    this.orderDate = orderDate;
  }

}