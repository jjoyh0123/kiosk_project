package kiosk.adminVO;

public class CouponSettingVO {
    private String couponSettingName;
    private int couponSettingFixed;
    private int couponSettingRate; 
    private int productIdx;

    public String getCouponSettingName() {
        return couponSettingName;
    }

    public void setCouponSettingName(String couponSettingName) {
        this.couponSettingName = couponSettingName;
    }

    public int getCouponSettingFixed() {
        return couponSettingFixed;
    }

    public void setCouponSettingFixed(int couponSettingFixed) {
        this.couponSettingFixed = couponSettingFixed;
    }

    public int getCouponSettingRate() { // 할인 비율 Getter
        return couponSettingRate;
    }

    public void setCouponSettingRate(int couponSettingRate) { // 할인 비율 Setter
        this.couponSettingRate = couponSettingRate;
    }

    public int getProductIdx() {
        return productIdx;
    }

    public void setProductIdx(int productIdx) {
        this.productIdx = productIdx;
    }
}
