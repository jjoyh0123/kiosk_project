package kiosk.clientVO;

public class CartItem {

    int orderIdx, userIdx, productIdx, couponIdx, option1Idx, option2Idx, orderNumber, orderProductCategory, orderCount;
    boolean orderStatus;
    int orderCalorie, orderPrice, orderCouponApplyPrice;

    // UI에 상품이름 보여주기 위해 추가
    String productName;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

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

    public int getOrderProductCategory() {
        return orderProductCategory;
    }

    public void setOrderProductCategory(int orderProductCategory) {
        this.orderProductCategory = orderProductCategory;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }

    public boolean isOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(boolean orderStatus) {
        this.orderStatus = orderStatus;
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

    public int getOrderCouponApplyPrice() {
        return orderCouponApplyPrice;
    }

    public void setOrderCouponApplyPrice(int orderCouponApplyPrice) {
        this.orderCouponApplyPrice = orderCouponApplyPrice;
    }
}
