package kiosk.client;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class MainPanel extends javax.swing.JPanel {
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Image background = new ImageIcon(Objects.requireNonNull(getClass().getResource("/kiosk/static/main1.jpg"))).getImage();
    g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
  }

  public MainPanel(MainFrame mainFrame) {
    setLayout(null);

    // 버튼 추가
    RoundedButton userLoginBtn = new RoundedButton("회원 주문");
    userLoginBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 24));
    userLoginBtn.setBounds(50, 800, 200, 70);
    userLoginBtn.addActionListener(e -> new MemberDialog(mainFrame)); // OrderDialog 호출

    RoundedButton nonMemberOrderBtn = new RoundedButton("비회원 주문");
    nonMemberOrderBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 24));
    nonMemberOrderBtn.setBounds(290, 800, 200, 70);
    nonMemberOrderBtn.addActionListener(e -> mainFrame.switchToOrderScreen());

    add(userLoginBtn);
    add(nonMemberOrderBtn);
  }
}
