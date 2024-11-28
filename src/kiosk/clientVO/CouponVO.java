package kiosk.clientVO;

import java.time.LocalDate;

public class CouponVO {

    int couponIdx;               // 쿠폰 ID
    int userIdx;                 // 사용자 ID
    int productIdx;
    String couponName;           // 쿠폰 이름
    int couponRate;              // 할인율
    int couponFixed;             // 고정 할인 금액
    LocalDate couponRegDate; // 쿠폰 등록일
    LocalDate couponExpDate; // 쿠폰 만료일
    boolean couponStatus;        // 쿠폰 상태

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

    public boolean isCouponStatus() {
        return couponStatus;
    }

    public void setCouponStatus(boolean couponStatus) {
        this.couponStatus = couponStatus;
    }

    public LocalDate getCouponRegDate() {
        return couponRegDate;
    }

    public void setCouponRegDate(LocalDate couponRegDate) {
        this.couponRegDate = couponRegDate;
    }

    public LocalDate getCouponExpDate() {
        return couponExpDate;
    }

    public void setCouponExpDate(LocalDate couponExpDate) {
        this.couponExpDate = couponExpDate;
    }

    public int getProductIdx() {
        return productIdx;
    }

    public void setProductIdx(int productIdx) {
        this.productIdx = productIdx;
    }
}