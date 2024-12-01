package kiosk.adminVO;

import java.sql.Date;

public class UserLogVO {
	
	int productIdx, orderIdx, userIdx, couponIdx, orderNumber, orderCount, orderCouponApplyPrice,
	logIdx, logTop1ProductIdx, logTop2ProductIdx,logTop3ProductIdx, logOrderCount, logExpense, logCouponUsage ;
	
	public int getLogIdx() {
		return logIdx;
	}
	public void setLogIdx(int logIdx) {
		this.logIdx = logIdx;
	}
	public int getLogTop1ProductIdx() {
		return logTop1ProductIdx;
	}
	public void setLogTop1ProductIdx(int logTop1ProductIdx) {
		this.logTop1ProductIdx = logTop1ProductIdx;
	}
	public int getLogTop2ProductIdx() {
		return logTop2ProductIdx;
	}
	public void setLogTop2ProductIdx(int logTop2ProductIdx) {
		this.logTop2ProductIdx = logTop2ProductIdx;
	}
	public int getLogTop3ProductIdx() {
		return logTop3ProductIdx;
	}
	public void setLogTop3ProductIdx(int logTop3ProductIdx) {
		this.logTop3ProductIdx = logTop3ProductIdx;
	}
	public int getLogOrderCount() {
		return logOrderCount;
	}
	public void setLogOrderCount(int logOrderCount) {
		this.logOrderCount = logOrderCount;
	}
	public int getLogExpense() {
		return logExpense;
	}
	public void setLogExpense(int logExpense) {
		this.logExpense = logExpense;
	}
	public int getLogCouponUsage() {
		return logCouponUsage;
	}
	public void setLogCouponUsage(int logCouponUsage) {
		this.logCouponUsage = logCouponUsage;
	}
	public Date getLogDate() {
		return logDate;
	}
	public void setLogDate(Date logDate) {
		this.logDate = logDate;
	}
	public String getLogGrade() {
		return logGrade;
	}
	public void setLogGrade(String logGrade) {
		this.logGrade = logGrade;
	}
	Date orderDate, logDate;
	String productName, logGrade;
	
	public int getProductIdx() {
		return productIdx;
	}
	public void setProductIdx(int productIdx) {
		this.productIdx = productIdx;
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
	public int getCouponIdx() {
		return couponIdx;
	}
	public void setCouponIdx(int couponIdx) {
		this.couponIdx = couponIdx;
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
	public int getOrderCouponApplyPrice() {
		return orderCouponApplyPrice;
	}
	public void setOrderCouponApplyPrice(int orderCouponApplyPrice) {
		this.orderCouponApplyPrice = orderCouponApplyPrice;
	}
	public Date getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}

}
