package kiosk.adminVO;

public class CouponSettingVO {

	int couponSettingIdx, productIdx, couponSettingRate, couponSettingFixed;
	String couponSettingName, couponSettingGrade, productName;
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	boolean couponSettingStatus;

	public int getCouponSettingIdx() {
		return couponSettingIdx;
	}

	public void setCouponSettingIdx(int couponSettingIdx) {
		this.couponSettingIdx = couponSettingIdx;
	}

	public int getProductIdx() {
		return productIdx;
	}

	public void setProductIdx(int productIdx) {
		this.productIdx = productIdx;
	}

	public int getCouponSettingRate() {
		return couponSettingRate;
	}

	public void setCouponSettingRate(int couponSettingRate) {
		this.couponSettingRate = couponSettingRate;
	}

	public int getCouponSettingFixed() {
		return couponSettingFixed;
	}

	public void setCouponSettingFixed(int couponSettingFixed) {
		this.couponSettingFixed = couponSettingFixed;
	}

	public String getCouponSettingName() {
		return couponSettingName;
	}

	public void setCouponSettingName(String couponSettingName) {
		this.couponSettingName = couponSettingName;
	}

	public String getCouponSettingGrade() {
		return couponSettingGrade;
	}

	public void setCouponSettingGrade(String couponSettingGrade) {
		this.couponSettingGrade = couponSettingGrade;
	}

	public boolean isCouponSettingStatus() {
		return couponSettingStatus;
	}

	public void setCouponSettingStatus(boolean couponSettingStatus) {
		this.couponSettingStatus = couponSettingStatus;
	}

}
