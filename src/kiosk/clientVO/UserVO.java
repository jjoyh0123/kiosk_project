package kiosk.clientVO;

public class UserVO {

  Integer userIdx;

  String userContact, userJoinDate, userGrade;

  public String getUserContact() {
    return userContact;
  }

  public void setUserContact(String userContact) {
    this.userContact = userContact;
  }

  public String getUserJoinDate() {
    return userJoinDate;
  }

  public void setUserJoinDate(String userJoinDate) {
    this.userJoinDate = userJoinDate;
  }

  public String getUserGrade() {
    return userGrade;
  }

  public void setUserGrade(String userGrade) {
    this.userGrade = userGrade;
  }

  public Integer getUserIdx() {
    return userIdx;
  }

  public void setUserIdx(Integer userIdx) {
    this.userIdx = userIdx;
  }
}
