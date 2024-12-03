package kiosk.adminVO;

public class CouponVO {
    private int couponIdx;
    private int userIdx;
    private int productIdx;
    private String couponName;
    private int couponRate;
    private int couponFixed;
    private String couponRegDate;
    private String couponExpDate; 
    private boolean couponStatus; //수정해야함

    public int getCouponIdx() {
        return couponIdx;
    }

    public void setCouponIdx(int couponIdx) {
        this.couponIdx = couponIdx;
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

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public int getCouponRate() {
        return couponRate;
    }

    public void setCouponRate(int couponRate) {
        this.couponRate = couponRate;
    }

    public int getCouponFixed() {
        return couponFixed;
    }

    public void setCouponFixed(int couponFixed) {
        this.couponFixed = couponFixed;
    }

    public String getCouponRegDate() {
        return couponRegDate;
    }

    public void setCouponRegDate(String couponRegDate) {
        this.couponRegDate = couponRegDate;
    }

    public String getCouponExpDate() {
        return couponExpDate;
    }

    public void setCouponExpDate(String couponExpDate) {
        this.couponExpDate = couponExpDate;
    }

    public boolean getCouponStatus() {
        return couponStatus;
    }

    public void setCouponStatus(Boolean couponStatus) {
        this.couponStatus = couponStatus;
    }
}
