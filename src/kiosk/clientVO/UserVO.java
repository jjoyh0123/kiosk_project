package kiosk.clientVO;

public class UserVO {

    int userIdx;

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

    public int getUserIdx() {
        return userIdx;
    }

    public void setUserIdx(int userIdx) {
        this.userIdx = userIdx;
    }
}
