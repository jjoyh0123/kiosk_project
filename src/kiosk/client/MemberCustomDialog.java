package kiosk.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MemberCustomDialog extends JDialog {

  private int i = 0;

  public MemberCustomDialog(MainFrame mainFrame, String message, int i) {
    super(mainFrame, true); // Modal 적용

    this.i = i;

    setTitle("Message Dialog"); // 제목은 필요 시 변경 가능
    setLayout(new BorderLayout());
    setUndecorated(true); // 창 닫기 버튼 및 테두리 제거

    // 메시지 표시 패널
    JPanel messagePanel = new JPanel();
    messagePanel.setBackground(Color.WHITE);
    messagePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    messagePanel.setLayout(new BorderLayout());
    JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
    messageLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
    messagePanel.add(messageLabel, BorderLayout.CENTER);

    // 확인 버튼 패널
    JPanel buttonPanel = new JPanel();
    buttonPanel.setBackground(Color.WHITE);
    JButton confirmButton = new RoundedButton("확인");
    confirmButton.setPreferredSize(new Dimension(200, 40));
    confirmButton.setFont(new Font("맑은 고딕", Font.BOLD, 14));
    confirmButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        dispose(); // 다이얼로그 닫기
        if (MemberCustomDialog.this.i == 1)
          mainFrame.switchToOrderScreen();
      }
    });
    buttonPanel.add(confirmButton);

    // 다이얼로그 구성
    add(messagePanel, BorderLayout.CENTER);
    add(buttonPanel, BorderLayout.SOUTH);

    // 크기와 위치 설정
    setSize(350, 200); // 다이얼로그 크기 설정
    setLocationRelativeTo(mainFrame); // 화면 중앙에 배치
    setVisible(true);
  }
}
