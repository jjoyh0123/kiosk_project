package kiosk.adminUserLogManagement;

import javax.swing.*;

import kiosk.Create.RoundedButton;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.function.Consumer;

public class AdminUserSeachDialog extends JDialog {
  private JTextField textField, currentText;
  private JPanel inputPanel, keypadPanel, actionPanel;
  private JButton backspaceButton, zeroButton, clearButton, searchButton;

  public AdminUserSeachDialog(Frame frame, Consumer<String> onSearch) {
    super(frame, "번호 검색", true);
    setUndecorated(true);// 위에 윈도우창없애기
    setLayout(new BorderLayout());

    setSize(300, 400);
    setLocation(730, 230);


    // 상단 번호 입력 필드
    JPanel contentPanel = new JPanel(new BorderLayout());
    contentPanel.setBorder(BorderFactory.createLineBorder(Color.gray, 1)); // 검정색 2픽셀 테두리
    JPanel inputPanel = new JPanel(new BorderLayout());
    inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
    JLabel label = new JLabel("번호: ");
    textField = new JTextField(15);

    textField.addKeyListener(new KeyAdapter() {
      @Override
      public void keyReleased(KeyEvent e) {
        textField.setText(textField.getText());
      }
    });
    inputPanel.add(label, BorderLayout.WEST);
    inputPanel.add(textField, BorderLayout.CENTER);

    // 버튼 키패드
    JPanel keypadPanel = new JPanel(new GridLayout(4, 3, 5, 5));
    for (int i = 1; i <= 9; i++) {
      JButton button = new RoundedButton(String.valueOf(i));
      button.setFont(new Font("맑은글씨", Font.PLAIN, 30));
      button.setPreferredSize(new Dimension(60, 60));
      button.addActionListener(e -> appendToTextField(button.getText()));
      keypadPanel.add(button);
    }

    JButton backspaceButton = new RoundedButton("←");
    backspaceButton.setFont(new Font("맑은글씨", Font.PLAIN, 30));
    backspaceButton.addActionListener(e -> {
      String currentText = textField.getText();
      if (currentText.length() > 0) {
        textField.setText((currentText.substring(0, currentText.length() - 1)));
      }
    });
    keypadPanel.add(backspaceButton);
    JButton zeroButton = new RoundedButton("0");
    zeroButton.setFont(new Font("맑은글씨", Font.PLAIN, 30));
    zeroButton.addActionListener(e -> appendToTextField("0"));
    keypadPanel.add(zeroButton);

    JButton clearButton = new RoundedButton("C");
    clearButton.setFont(new Font("맑은글씨", Font.PLAIN, 30));
    clearButton.addActionListener(e -> textField.setText(""));
    keypadPanel.add(clearButton);

    // 하단 버튼: 입력 및 취소
    JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
    JButton searchButton = new RoundedButton("입력");
    JButton cancelButton = new RoundedButton("취소");
    actionPanel.add(searchButton);
    actionPanel.add(cancelButton);

    // 입력 버튼
    searchButton.addActionListener(e -> {
      String input = textField.getText().replace("-", "");
      if (input.length() >= 4) {
        onSearch.accept(input); // 검색 로직 실행 (실제 입력값 전달)
        dispose();
      } else {
        JOptionPane.showMessageDialog(this, "번호를 정확히 입력해주세요.");
      }
    });

    // 취소 버튼 리스너
    cancelButton.addActionListener(e -> dispose());

    // 컴포넌트 추가
   // add(inputPanel, BorderLayout.NORTH);
   // add(keypadPanel, BorderLayout.SOUTH);
  //  add(actionPanel, BorderLayout.CENTER);

    contentPanel.add(inputPanel, BorderLayout.NORTH);
    contentPanel.add(actionPanel, BorderLayout.CENTER);
    contentPanel.add(keypadPanel, BorderLayout.SOUTH);

    setSize(300, 400);
    setUndecorated(true);
    setLocationRelativeTo(frame);
  }

  private void appendToTextField(String text) {
    String currentText = textField.getText().replace("-", "");
    if (currentText.length() < 11) { // 최대 11자리까지 입력 허용
      textField.setText((currentText + text));
    }
  }


}
