package kiosk.adminLogin;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.ibatis.session.SqlSession;

import kiosk.admin.Admin;
import kiosk.adminVO.AdminVO;
import kiosk.client.MainFrame;

public class AdminLoginJDialog extends JDialog {
  MainFrame parent;
  JPanel idPasswordPanel, loginCancelPanel, numberPadPanel;
  JComboBox<String> adminIdSelect;
  JTextField adminPassword;
  JButton loginBtn, cancelBtn, button;
  String password = "";
  public AdminVO loggedInAdmin;

  public AdminLoginJDialog(MainFrame parent) {
    super(parent, "Admin Login", true); // 모달 다이얼로그 설정
    this.parent = parent;
    setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    this.setLayout(new BorderLayout());

    // 아이디, 패스워드 패널
    idPasswordPanel = new JPanel(new GridLayout(2, 2, 10, 10));
    idPasswordPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    // 관리자 아이디 선택
    adminIdSelect = new JComboBox<>(new String[]{"선택하세요", "Senior", "Junior"});
    adminIdSelect.setSelectedIndex(0);
    idPasswordPanel.add(new JLabel("ID"));
    idPasswordPanel.add(adminIdSelect);

    // 패스워드 입력
    adminPassword = new JTextField(10);
    adminPassword.setEditable(false); // 넘버패드로만 입력 가능
    idPasswordPanel.add(new JLabel("PW"));
    idPasswordPanel.add(adminPassword);

    adminIdSelect.setPreferredSize(new Dimension(60, 60));
    adminPassword.setPreferredSize(new Dimension(60, 60));


    // 로그인, 취소 패널
    loginCancelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
    loginBtn = new JButton("Login");
    cancelBtn = new JButton("Cancel");
    loginCancelPanel.add(loginBtn);
    loginCancelPanel.add(cancelBtn);

    // 넘버패드 패널
    numberPadPanel = new JPanel(new GridLayout(4, 3, 5, 5));
    numberPadPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    String[] buttons = {"7", "8", "9", "4", "5", "6", "1", "2", "3", "←", "0", "C"};

    for (String text : buttons) {
      JButton button = new JButton(text);
      button.setPreferredSize(new Dimension(40, 40));  // 너비와 높이를 동일하게 설정하여 정사각형 모양으로 만듦
      button.addActionListener(e -> handleNumberPadInput(text));
      numberPadPanel.add(button);
    }

    loginBtn.addActionListener(e -> handleLogin());
    cancelBtn.addActionListener(e -> dispose());

    // JFrame 에 패널 추가
    add(idPasswordPanel, BorderLayout.NORTH); // ID, PW 입력 패널
    add(numberPadPanel, BorderLayout.SOUTH); // 넘버패드
    add(loginCancelPanel, BorderLayout.CENTER); // 로그인, 취소 버튼

    setSize(460, 450);
    setLocation(730, 230);
    setUndecorated(true);
    setLocationRelativeTo(parent); // 부모창 기준으로 중앙 정렬
    setVisible(true);
  }

  // 넘버패드 버튼 동작 처리
  private void handleNumberPadInput(String input) {
    if ("C".equals(input)) {
      password = ""; // 전부 지우기
    } else if ("←".equals(input)) {
      if (!password.isEmpty()) {
        password = password.substring(0, password.length() - 1); // 한 글자 지우기
      }
    } else if (password.length() < 10) {
      password += input; // 숫자 추가
    }
    adminPassword.setText(password); // 텍스트 필드 업데이트
  }

  private void handleLogin() {
    System.out.println(adminIdSelect.getSelectedItem());
    SqlSession ss = parent.factory.openSession();
    loggedInAdmin = ss.selectOne("adminLogin.idpw", adminIdSelect.getSelectedItem());
    if (loggedInAdmin != null && loggedInAdmin.getAdminPassWord().equals(adminPassword.getText())) {
      JOptionPane.showMessageDialog(this, "로그인 성공");
      dispose(); // 창 닫기

      // 로그인 성공, 관리자 창 호출
      new Admin(parent, AdminLoginJDialog.this);
    } else {
      JOptionPane.showMessageDialog(this, "로그인 실패! 아이디와 비밀번호를 확인하세요");
    }
  }
}
