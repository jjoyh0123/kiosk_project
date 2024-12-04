package kiosk.adminVO;

import java.util.Date;

public class UserLogVO {
  String userContact, logGrade;
  String top1ProductName, top2ProductName, top3ProductName;
  int userIdx, logIdx,
          logTop1ProductIdx, logTop2ProductIdx, logTop3ProductIdx,
          logTop1ProductOrderCount, logTop2ProductOrderCount, logTop3ProductOrderCount,
          logTop1ProductExpense, logTop2ProductExpense, logTop3ProductExpense,
          logTop1CouponUsage, logTop2CouponUsage, logTop3CouponUsage;
  Date logDate;

  public String getUserContact() {
    return userContact;
  }

  public void setUserContact(String userContact) {
    this.userContact = userContact;
  }

  public String getLogGrade() {
    return logGrade;
  }

  public void setLogGrade(String logGrade) {
    this.logGrade = logGrade;
  }

  public String getTop1ProductName() {
    return top1ProductName;
  }

  public void setTop1ProductName(String top1ProductName) {
    this.top1ProductName = top1ProductName;
  }

  public String getTop2ProductName() {
    return top2ProductName;
  }

  public void setTop2ProductName(String top2ProductName) {
    this.top2ProductName = top2ProductName;
  }

  public String getTop3ProductName() {
    return top3ProductName;
  }

  public void setTop3ProductName(String top3ProductName) {
    this.top3ProductName = top3ProductName;
  }

  public int getUserIdx() {
    return userIdx;
  }

  public void setUserIdx(int userIdx) {
    this.userIdx = userIdx;
  }

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

  public int getLogTop1ProductOrderCount() {
    return logTop1ProductOrderCount;
  }

  public void setLogTop1ProductOrderCount(int logTop1ProductOrderCount) {
    this.logTop1ProductOrderCount = logTop1ProductOrderCount;
  }

  public int getLogTop2ProductOrderCount() {
    return logTop2ProductOrderCount;
  }

  public void setLogTop2ProductOrderCount(int logTop2ProductOrderCount) {
    this.logTop2ProductOrderCount = logTop2ProductOrderCount;
  }

  public int getLogTop3ProductOrderCount() {
    return logTop3ProductOrderCount;
  }

  public void setLogTop3ProductOrderCount(int logTop3ProductOrderCount) {
    this.logTop3ProductOrderCount = logTop3ProductOrderCount;
  }

  public int getLogTop1ProductExpense() {
    return logTop1ProductExpense;
  }

  public void setLogTop1ProductExpense(int logTop1ProductExpense) {
    this.logTop1ProductExpense = logTop1ProductExpense;
  }

  public int getLogTop2ProductExpense() {
    return logTop2ProductExpense;
  }

  public void setLogTop2ProductExpense(int logTop2ProductExpense) {
    this.logTop2ProductExpense = logTop2ProductExpense;
  }

  public int getLogTop3ProductExpense() {
    return logTop3ProductExpense;
  }

  public void setLogTop3ProductExpense(int logTop3ProductExpense) {
    this.logTop3ProductExpense = logTop3ProductExpense;
  }

  public int getLogTop1CouponUsage() {
    return logTop1CouponUsage;
  }

  public void setLogTop1CouponUsage(int logTop1CouponUsage) {
    this.logTop1CouponUsage = logTop1CouponUsage;
  }

  public int getLogTop2CouponUsage() {
    return logTop2CouponUsage;
  }

  public void setLogTop2CouponUsage(int logTop2CouponUsage) {
    this.logTop2CouponUsage = logTop2CouponUsage;
  }

  public int getLogTop3CouponUsage() {
    return logTop3CouponUsage;
  }

  public void setLogTop3CouponUsage(int logTop3CouponUsage) {
    this.logTop3CouponUsage = logTop3CouponUsage;
  }

  public Date getLogDate() {
    return logDate;
  }

  public void setLogDate(Date logDate) {
    this.logDate = logDate;
  }


}