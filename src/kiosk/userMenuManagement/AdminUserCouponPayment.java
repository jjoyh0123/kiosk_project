package kiosk.userMenuManagement;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

import org.apache.ibatis.session.SqlSession;

import Create.RoundedButton;
import kiosk.admin.Admin;
import kiosk.adminVO.CouponSettingVO;
import kiosk.adminVO.CouponVO;
import kiosk.client.MainFrame;

public class AdminUserCouponPayment extends JDialog {
  int userIdx;
  MainFrame mainFrame;
  String userGrade;

  public AdminUserCouponPayment(Admin owner, int userIdx, String userGrade, String userContact, MainFrame mainFrame) {
    super(owner, "회원 쿠폰 관리", true);
    this.userIdx = userIdx;
    this.mainFrame = mainFrame;
    this.userGrade = userGrade;
    // 다이얼로그 기본 설정
    setLayout(new BorderLayout());
    setSize(460, 450);
    // setLocationRelativeTo(owner);
    

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createLineBorder(Color.gray, 1)); // 검정색 2픽셀 테두리

    setUndecorated(true);
    setLocation(730, 230);
    // 탑패널 그 해당 회원의 번호와 등급이 나오고 나가기 버튼 만듬
    JPanel topPanel = new JPanel(new BorderLayout());
    topPanel.setPreferredSize(new Dimension(460,40));
    JLabel titleLabel = new JLabel("쿠폰 지급", JLabel.CENTER);
    titleLabel.setFont(new Font("맑은고딕", Font.PLAIN, 25));
    titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 여백 추가
    topPanel.add(titleLabel,BorderLayout.NORTH);
    
    JPanel secondpanel = new JPanel(new BorderLayout());

    JLabel userInfoLabel = new JLabel("고객 ID: " + userContact + ", 등급: " + userGrade);
    secondpanel.add(userInfoLabel, BorderLayout.WEST);

    JButton closeButton = new RoundedButton("나가기");
    closeButton.addActionListener(e -> dispose());
    secondpanel.add(closeButton, BorderLayout.EAST);

     contentPanel.add(topPanel, BorderLayout.NORTH);
     contentPanel.add(secondpanel,BorderLayout.CENTER);
    // 개별 쿠폰나오는 패널들 box layout설정
    JPanel couponListPanel = new JPanel();
    couponListPanel.setLayout(new BoxLayout(couponListPanel, BoxLayout.Y_AXIS));
    JScrollPane scrollPane = new JScrollPane(couponListPanel);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    scrollPane.setPreferredSize(new Dimension(460,380));
    contentPanel.add(scrollPane, BorderLayout.SOUTH);
    
    add(contentPanel);

    loadCouponData(couponListPanel);

    setVisible(true);
  }

  private void loadCouponData(JPanel couponListPanel) {
    try (SqlSession session = mainFrame.factory.openSession()) {
      List<CouponSettingVO> couponList = session.selectList("adminUserManagement.selectCouponByGrade", userGrade);
      if (!couponList.isEmpty()) {
        for (CouponSettingVO coupon : couponList) {
          JPanel couponPanel = new JPanel();
          couponPanel.setLayout(new BoxLayout(couponPanel, BoxLayout.Y_AXIS));
          couponPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

          JLabel nameLabel = new JLabel(coupon.getCouponSettingName() + " 쿠폰");
          nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
          // JLabel discountLabel = new JLabel("할인율: " + coupon.getCouponSettingFixed()+"원");
          // discountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

          JButton issueButton = new RoundedButton("쿠폰 지급");
          issueButton.setAlignmentX(Component.CENTER_ALIGNMENT);
          issueButton.addActionListener(e -> {
            issueCouponToUser(coupon);
            JOptionPane.showMessageDialog(this, coupon.getCouponSettingName() + " 쿠폰이 지급되었습니다!");
          });

          couponPanel.add(nameLabel);
          couponPanel.add(Box.createVerticalStrut(10));
          // couponPanel.add(discountLabel);
          // couponPanel.add(Box.createVerticalStrut(10));
          couponPanel.add(issueButton);

          couponListPanel.add(couponPanel);
          couponListPanel.add(Box.createVerticalStrut(10)); // 각 쿠폰 사이에 공간 추가
        }
      } else {
        couponListPanel.add(new JLabel("해당 등급에 대한 쿠폰 정보가 없습니다."));
      }
    } catch (Exception e) {
      e.printStackTrace();
      couponListPanel.add(new JLabel("쿠폰 정보를 불러오는 데 실패했습니다."));
    }
  }

  // 쿠폰 지급 로직
  private void issueCouponToUser(CouponSettingVO coupon) {
    try (SqlSession session = mainFrame.factory.openSession()) {
      // 현재 시각 (발행 시각)
      LocalDateTime now = LocalDateTime.now();
      String regDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
      // 만료 시각 (30일 후)
      String expDate = now.plusDays(30).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

      // 쿠폰 객체 생성
      CouponVO newCoupon = new CouponVO();
      newCoupon.setUserIdx(userIdx); // 사용자 ID
      newCoupon.setProductIdx(coupon.getProductIdx());
      newCoupon.setCouponName(coupon.getCouponSettingName());
      newCoupon.setCouponRate(coupon.getCouponSettingRate()); //
      newCoupon.setCouponFixed(coupon.getCouponSettingFixed());
      newCoupon.setCouponRegDate(regDate);
      newCoupon.setCouponExpDate(expDate);
      newCoupon.setCouponStatus(false); // 기본값: 사용 가능 (0)

      session.insert("adminUserManagement.insertCoupon", newCoupon);
      session.commit(); // DB 변경 사항 적용
      JOptionPane.showMessageDialog(this, "쿠폰이 성공적으로 지급되었습니다!");
      System.out.println("Inserting coupon with values: " + newCoupon);
    } catch (Exception e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(this, "쿠폰 지급에 실패했습니다.");
    }
  }
}
