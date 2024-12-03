package kiosk.adminCouponManagement;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.ibatis.session.SqlSession;

import kiosk.adminVO.CouponSettingVO;
import kiosk.client.MainFrame;

public class AdminCouponDialog extends JDialog {

  MainFrame mainFrame;
  JCheckBox checkBox;
  JPanel numberPadPanel;
  JLabel label1, label2, label3, label4, label5;
  JTextField field1, field2, field3, field4, field5;
  private String input = "";

  JTextField lastFocued = null;

  public AdminCouponDialog(MainFrame mainFrame, Window parent, AdminCouponSetting adminCouponSet,
                           CouponSettingVO coupon) {
    super(parent, "쿠폰 상세", ModalityType.APPLICATION_MODAL);

    this.mainFrame = mainFrame;

    // 창 초기화
    setLayout(new BorderLayout());

    label1 = new JLabel("이름 :");
    label1.setFont(new Font("맑은 고딕", Font.BOLD, 15));

    field1 = new JTextField(coupon.getCouponSettingName());
    field1.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
    field1.setBorder(BorderFactory.createEmptyBorder());
    field1.setEditable(false);

    label2 = new JLabel("적용상품 :");
    label2.setFont(new Font("맑은 고딕", Font.BOLD, 15));

    field2 = new JTextField(coupon.getProductName());
    field2.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
    field2.setBorder(BorderFactory.createEmptyBorder());
    field2.setEditable(false);

    label3 = new JLabel("등급 :");
    label3.setFont(new Font("맑은 고딕", Font.BOLD, 15));

    field3 = new JTextField(coupon.getCouponSettingGrade());
    field3.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
    field3.setBorder(BorderFactory.createEmptyBorder());
    field3.setEditable(false);

    label4 = new JLabel("할인율 :");
    label4.setFont(new Font("맑은 고딕", Font.BOLD, 15));

    field4 = new JTextField();
    field4.setText(coupon.getCouponSettingRate() + "");
    field4.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
    field4.setBorder(BorderFactory.createEmptyBorder());
    field4.addFocusListener(new FocusAdapter() {
      @Override
      public void focusGained(FocusEvent e) {
        lastFocued = field4;
      }
    });

    label5 = new JLabel("고정할인금액 :");
    label5.setFont(new Font("맑은 고딕", Font.BOLD, 15));

    field5 = new JTextField();
    field5.setText(coupon.getCouponSettingFixed() + "");
    field5.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
    field5.setBorder(BorderFactory.createEmptyBorder());
    field5.addFocusListener(new FocusAdapter() {
      @Override
      public void focusGained(FocusEvent e) {
        lastFocued = field5;
      }
    });

    checkBox = new JCheckBox("사용 상태", true);
    checkBox.setFont(new Font("맑은 고딕", Font.BOLD, 15));

    // 저장 및 취소 버튼
    JButton saveButton = new JButton("저장");
    JButton cancelButton = new JButton("취소");

    // 넘버패드
    numberPadPanel = new JPanel(new GridLayout(4, 3, 5, 5));
    numberPadPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    String[] buttons = {"7", "8", "9", "4", "5", "6", "1", "2", "3", "←", "0", "C"};

    for (String text : buttons) {
      JButton button = new JButton(text);
      button.addActionListener(e -> handleNumberPadInput(text));
      numberPadPanel.add(button);
    }

    saveButton.addActionListener(e -> {
      try (SqlSession session = mainFrame.factory.openSession()) {
        kiosk.adminVO.CouponSettingVO updateCoupon = coupon;
        updateCoupon.setCouponSettingRate(Integer.parseInt(field4.getText()));
        updateCoupon.setCouponSettingFixed(Integer.parseInt(field5.getText()));
        updateCoupon.setCouponSettingStatus(checkBox.isSelected());

        session.update("couponSetting.updateCouponSet", updateCoupon); // 상태 업데이트
        session.commit();

        List<CouponSettingVO> updatedCouponList = session.selectList("couponSetting.couponSet");

        // 테이블 갱신
        adminCouponSet.loadCouponData(); // 갱신된 목록으로 테이블을 다시 로드

        JOptionPane.showMessageDialog(AdminCouponDialog.this, "저장되었습니다.");
        dispose();
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    });

    cancelButton.addActionListener(e -> dispose());

    // 입력 필드 패널
    JPanel inputPanel = new JPanel(new GridLayout(7, 2, 10, 10));
    inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    inputPanel.add(label1);
    inputPanel.add(field1);
    inputPanel.add(label2);
    inputPanel.add(field2);
    inputPanel.add(label3);
    inputPanel.add(field3);
    inputPanel.add(label4);
    inputPanel.add(field4);
    inputPanel.add(label5);
    inputPanel.add(field5);
    inputPanel.add(checkBox);

    // 버튼 패널
    JPanel buttonPanel = new JPanel();
    buttonPanel.add(saveButton);
    buttonPanel.add(cancelButton);

    // 전체 레이아웃
    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.add(inputPanel, BorderLayout.NORTH);
    mainPanel.add(buttonPanel, BorderLayout.CENTER);
    mainPanel.add(numberPadPanel, BorderLayout.SOUTH);

    add(mainPanel, BorderLayout.CENTER);

    setSize(460, 600);
    setUndecorated(false);
    setLocationRelativeTo(parent);
  }

  // 넘버패드 버튼 동작 처리
  private void handleNumberPadInput(String input) {
    if (lastFocued == null) {
      JOptionPane.showMessageDialog(AdminCouponDialog.this, "입력할 필드를 선택하세요.");
      return;
    }

    String text = lastFocued.getText();

    if ("C".equals(input)) {
      lastFocued.setText("0"); // 전부 지우기
    } else if ("←".equals(input)) {
      if (text.length() == 1) {
        lastFocued.setText("0");
      } else {
        lastFocued.setText(text.substring(0, text.length() - 1)); // 한 글자 지우기
      }
    } else if (text.length() < 10) {
      if (text.length() == 1 && text.equals("0")) {
        lastFocued.setText(input);
      } else {
        lastFocued.setText(lastFocued.getText() + input);
      }
    }
  }
}