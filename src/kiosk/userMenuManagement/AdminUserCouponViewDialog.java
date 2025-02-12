package kiosk.userMenuManagement;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.ibatis.session.SqlSession;

import kiosk.Create.RoundedButton;
import kiosk.admin.Admin;
import kiosk.adminVO.CouponVO;
import kiosk.client.MainFrame;

public class AdminUserCouponViewDialog extends JDialog {
  int useridx;
  MainFrame mainframe;

  public AdminUserCouponViewDialog(Admin owner, int useridx, String userContact, String userGrade,
                                   MainFrame mainframe) {
    super(owner, "사용자쿠폰보기", true);
    this.useridx = useridx;
    this.mainframe = mainframe;

    // 다이어로그 사이즈 세팅
    setLayout(new BorderLayout());
    setSize(460, 450);
    ;

    // "AdminUserCouponViewDialog" 클래스에서 다이얼로그 위치 설정
    setLocation(730, 230);
    // setLocationRelativeTo(owner);//이거하면 가운데로 고정

    setUndecorated(true);// 위에 윈도우창없애기
    
    JPanel contentPanel = new JPanel(new BorderLayout());
    contentPanel.setBorder(BorderFactory.createLineBorder(Color.gray, 1)); // 검정색 2픽셀 테두리

    JPanel topPanel = new JPanel(new BorderLayout());

    // 상단레이블설정
    JLabel titleLabel = new JLabel("쿠폰 보기", JLabel.CENTER);
    titleLabel.setFont(new Font("맑은고딕", Font.PLAIN, 25));
    titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 여백 추가
    topPanel.setPreferredSize(new Dimension(460,40));
    topPanel.add(titleLabel, BorderLayout.NORTH);

    JPanel secondpanel = new JPanel(new BorderLayout());
    JLabel userInfoLabel = new JLabel("회원 번호: " + userContact + ", 등급: " + userGrade);
    secondpanel.add(userInfoLabel, BorderLayout.WEST);

    // 나가기 버튼
    JButton closeButton = new RoundedButton("나가기");
    closeButton.addActionListener(e -> dispose());
    secondpanel.add(closeButton, BorderLayout.EAST);

    //add(topPanel, BorderLayout.NORTH);
    contentPanel.add(topPanel,BorderLayout.NORTH);
    contentPanel.add(secondpanel,BorderLayout.CENTER);
    JPanel couponListPanel = new JPanel();
    couponListPanel.setLayout(new BoxLayout(couponListPanel, BoxLayout.Y_AXIS));
    JScrollPane scrollPane = new JScrollPane(couponListPanel);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    //add(scrollPane, BorderLayout.CENTER);
    scrollPane.setPreferredSize(new Dimension(460,380));
    contentPanel.add(scrollPane,BorderLayout.SOUTH);
    add(contentPanel);
    
    loadUserCoupons(couponListPanel);

    setVisible(true);
  }

  private void loadUserCoupons(JPanel couponListPanel) {
    try (SqlSession session = mainframe.factory.openSession()) {
      List<CouponVO> couponList = session.selectList("adminUserManagement.selectCouponsByUserIdx", useridx);

      if (!couponList.isEmpty()) {
        for (CouponVO coupon : couponList) {
          JPanel couponPanel = new JPanel();
          couponPanel.setLayout(new BoxLayout(couponPanel, BoxLayout.Y_AXIS));
          couponPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

          JLabel nameLabel = new JLabel(coupon.getCouponName() + "쿠폰");
          nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
          JLabel startDateLabel = new JLabel("발급시기:" + coupon.getCouponRegDate());
          startDateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
          JLabel dateLabel = new JLabel("유효기간: " + coupon.getCouponExpDate());
          dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

          // 쿠폰 사용 여부 status 확인
          String statusText = coupon.getCouponStatus() == false ? "미사용" : "사용됨";
          JLabel statusLabel = new JLabel("사용 여부: " + statusText);
          statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

          couponPanel.add(nameLabel);
          couponPanel.add(Box.createVerticalStrut(10));
          couponPanel.add(startDateLabel);
          couponPanel.add(dateLabel);
          couponPanel.add(statusLabel);

          couponListPanel.add(couponPanel);
          couponListPanel.add(Box.createVerticalStrut(10));
        }
      } else {
        couponListPanel.add(new JLabel("보유한 쿠폰이 없습니다."));
      }
    } catch (Exception e) {
      e.printStackTrace();
      couponListPanel.add(new JLabel("쿠폰 정보를 불러오는 데 실패했습니다."));
    }
  }
}