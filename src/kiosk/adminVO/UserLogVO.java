package kiosk.adminVO;

import java.sql.Date;

public class UserLogVO {
  // userIdx로 연락처 가져오기
  // 고객 등급 가져오기
  // 테이블 선택시 고객별로 각 1,2,3 순위별로 가져오기
  // product 테이블이랑 JION 해보고 안되면 보류하기

  String userContact, logGrade;
  String top1ProductName, top2ProductName, top3ProductName;
  int userIdx, logIdx, logTop1ProductIdx, logTop2ProductIdx, logTop3ProductIdx;
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

  public Date getLogDate() {
    return logDate;
  }

  public void setLogDate(Date logDate) {
    this.logDate = logDate;
  }


}