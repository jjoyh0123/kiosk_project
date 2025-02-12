package kiosk.adminLogin;

import kiosk.Create.RoundedButton;
import kiosk.admin.Admin;
import kiosk.adminVO.AdminVO;
import kiosk.client.MainFrame;
import org.apache.ibatis.session.SqlSession;

import javax.swing.*;
import java.awt.*;

public class AdminLoginJDialog extends JDialog {
    MainFrame parent;
    JPanel titlePanel, idPasswordPanel, loginCancelPanel, numberPadPanel;
    JLabel titleLabel, idPasswordLabel, idLabel, pwLabel;
    JComboBox<String> adminIdSelect;
    JTextField adminPassword;
    RoundedButton loginBtn, cancelBtn, button;
    String password = "";
    public AdminVO loggedInAdmin;

    public AdminLoginJDialog(MainFrame parent) {
        super(parent, "Admin Login", true); // 모달 다이얼로그 설정
        this.parent = parent;
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        //this.setLayout(new BorderLayout());

        // 창 상단 타이틀
        titlePanel = new JPanel();
        JLabel title = new JLabel("관리자 로그인");
        title.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 0, 50));
        titlePanel.add(title);


// 아이디와 패스워드 입력 패널
        idPasswordPanel = new JPanel(new GridLayout(2, 2, 5, 5)); // 행/열 간 간격 10 설정
        idPasswordPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 0, 50)); // 외부 여백 설정

// 아이디 선택
        idLabel = new JLabel("아이디");
        idLabel.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        adminIdSelect = new JComboBox<>(new String[]{"선택하세요", "Senior", "Junior"});
        adminIdSelect.setFont(new Font("맑은 고딕", Font.BOLD, 10));
        adminIdSelect.setPreferredSize(new Dimension(200, 10)); // 크기 설정
        idPasswordPanel.add(idLabel);
        idPasswordPanel.add(adminIdSelect);

// 패스워드 입력
        pwLabel = new JLabel("패스워드");
        pwLabel.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        adminPassword = new JPasswordField(7);
        adminPassword.setEditable(false);
        adminPassword.setFont(new Font("맑은 고딕", Font.BOLD, 10));
        adminPassword.setPreferredSize(new Dimension(200, 10)); // 크기 설정
        idPasswordPanel.add(pwLabel);
        idPasswordPanel.add(adminPassword);


// 로그인, 취소 패널
        loginCancelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        loginBtn = new RoundedButton("로그인");
        cancelBtn = new RoundedButton("취소");

        loginBtn.setPreferredSize(new Dimension(150, 40)); // 버튼 크기 조정
        cancelBtn.setPreferredSize(new Dimension(150, 40)); // 버튼 크기 조정

        loginCancelPanel.setPreferredSize(new Dimension(300, 60));
        loginCancelPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 50));
        loginCancelPanel.add(loginBtn);
        loginCancelPanel.add(cancelBtn);

// 아이디, 패스워드 + 로그인/취소 패널을 담을 centerPanel
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(idPasswordPanel, BorderLayout.NORTH);  // ID, PW 입력 패널
        centerPanel.add(loginCancelPanel, BorderLayout.SOUTH); // 로그인, 취소 버튼

// 넘버패드 패널
        numberPadPanel = new JPanel(new GridLayout(4, 3, 5, 5));
        numberPadPanel.setBorder(BorderFactory.createEmptyBorder(50, 20, 10, 20));
        numberPadPanel.setPreferredSize(new Dimension(100, 200));

        String[] buttons = {"7", "8", "9", "4", "5", "6", "1", "2", "3", "←", "0", "C"};

        for (String text : buttons) {
            JButton button = new RoundedButton(text);
            //int buttonSize = 60;
            button.setPreferredSize(new Dimension(25, 30));
            button.addActionListener(e -> handleNumberPadInput(text));
            numberPadPanel.add(button);
        }

        loginBtn.addActionListener(e -> handleLogin());
        cancelBtn.addActionListener(e -> dispose());

        // 패널 추가
        this.add(titlePanel, BorderLayout.NORTH);      // 타이틀
        this.add(centerPanel, BorderLayout.CENTER);    // ID, PW + 로그인/취소 버튼
        this.add(numberPadPanel, BorderLayout.SOUTH);  // 하단 넘버패드

        setSize(450, 450);
        setLocation(730, 200);
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
