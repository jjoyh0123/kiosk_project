package kiosk.adminVO;

import java.sql.Timestamp;

public class OrderVO {
  int orderIdx, userIdx, productIdx, couponIdx,
      option1Idx, option2Idx, orderNumber, orderProductCategory,
      orderCount, orderCalorie, orderPrice, totalOrderPrice;

  String products, appliedCoupon, userContact;

  public String getUserContact() {
    return userContact;
  }

  public void setUserContact(String userContact) {
    this.userContact = userContact;
  }

  public int getTotalOrderPrice() {
    return totalOrderPrice;
  }

  public void setTotalOrderPrice(int totalOrderPrice) {
    this.totalOrderPrice = totalOrderPrice;
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

  boolean orderStatus;
  String couponName, orderDate;

  public int getOrderIdx() {
    return orderIdx;
  }

  public void setOrderIdx(int orderIdx) {
    this.orderIdx = orderIdx;
  }

  public int getUserIdx() {
    return userIdx;
  }

  public void setUserIdx(int userIdx) {
    this.userIdx = userIdx;
  }

  public int getProductIdx() {
    return productIdx;
  }

  public void setProductIdx(int productIdx) {
    this.productIdx = productIdx;
  }

  public int getCouponIdx() {
    return couponIdx;
  }

  public void setCouponIdx(int couponIdx) {
    this.couponIdx = couponIdx;
  }

  public int getOption1Idx() {
    return option1Idx;
  }

  public void setOption1Idx(int option1Idx) {
    this.option1Idx = option1Idx;
  }

  public int getOption2Idx() {
    return option2Idx;
  }

  public void setOption2Idx(int option2Idx) {
    this.option2Idx = option2Idx;
  }

  public int getOrderNumber() {
    return orderNumber;
  }

  public void setOrderNumber(int orderNumber) {
    this.orderNumber = orderNumber;
  }

  public int getOrderProductCategory() {
    return orderProductCategory;
  }

  public void setOrderProductCategory(int orderProductCategory) {
    this.orderProductCategory = orderProductCategory;
  }

  public int getOrderCount() {
    return orderCount;
  }

  public void setOrderCount(int orderCount) {
    this.orderCount = orderCount;
  }

  public int getOrderCalorie() {
    return orderCalorie;
  }

  public void setOrderCalorie(int orderCalorie) {
    this.orderCalorie = orderCalorie;
  }

  public int getOrderPrice() {
    return orderPrice;
  }

  public void setOrderPrice(int orderPrice) {
    this.orderPrice = orderPrice;
  }

  public boolean isOrderStatus() {
    return orderStatus;
  }

  public void setOrderStatus(boolean orderStatus) {
    this.orderStatus = orderStatus;
  }

  public String getCouponName() {
    return couponName;
  }

  public void setCouponName(String couponName) {
    this.couponName = couponName;
  }

  public String getOrderDate() {
    return orderDate;
  }

  public void setOrderDate(String orderDate) {
    this.orderDate = orderDate;
  }


}
