package kiosk.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.ibatis.session.SqlSession;

import kiosk.vo.AdminVO;

public class AdminLogin extends JDialog {
    MainFrame parent;
    JPanel idPasswordPanel, loginCancelPanel, numberPadPanel;
    JComboBox<String> adminIdSelect;
    JTextField adminPassword;
    JButton loginBtn, cancelBtn;
    String password = "";
    AdminVO loggedInAdmin;
    JButton button;
    

    public AdminLogin(MainFrame parent) {
        super(parent, "관리자 로그인", true); // 모달 다이얼로그 설정
        this.parent = parent;
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout());

        // GridBagLayout을 사용하는 중앙 레이아웃으로 변경
        JPanel containerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        containerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 아이디, 패스워드 패널
        idPasswordPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        idPasswordPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // 관리자 아이디 선택
        adminIdSelect = new JComboBox<>(new String[] { "선택하세요", "Senior", "Junior" });
        adminIdSelect.setSelectedIndex(0);
        idPasswordPanel.add(new JLabel("ID"));
        idPasswordPanel.add(adminIdSelect);

        // 패스워드 입력
        adminPassword = new JTextField(10);
        adminPassword.setEditable(false); // 넘버패드로만 입력 가능
        idPasswordPanel.add(new JLabel("PW"));
        idPasswordPanel.add(adminPassword);

        // 로그인, 취소 패널
        loginCancelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        loginBtn = new JButton("Login");
        cancelBtn = new JButton("Cancel");
        loginCancelPanel.add(loginBtn);
        loginCancelPanel.add(cancelBtn);

        // 넘버패드 패널
        numberPadPanel = new JPanel(new GridLayout(4, 3, 5, 5));
        numberPadPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        String[] buttons = { "7", "8", "9", "4", "5", "6", "1", "2", "3", "←", "0", "C" };

        for (String text : buttons) {
            JButton button = new JButton(text);
            button.setPreferredSize(new Dimension(50, 50)); // 크기 조정
            button.setFont(new Font("Arial", Font.BOLD, 18)); // 텍스트 크기 키우기
            button.setMargin(new Insets(0, 10, 0, 10)); // 버튼 내부 여백 제거
            button.addActionListener(e -> handleNumberPadInput(text));
            numberPadPanel.add(button);
        }

        // 각 패널을 GridBagLayout으로 배치
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5); // 아이디, 패스워드 패널 사이 여백 줄이기
        containerPanel.add(idPasswordPanel, gbc); // 아이디/패스워드 패널을 첫 번째 행에 추가

        gbc.gridy = 1;
        containerPanel.add(loginCancelPanel, gbc); // 로그인/취소 버튼 패널을 두 번째 행에 추가

        gbc.gridy = 2;
        containerPanel.add(numberPadPanel, gbc); // 넘버패드 패널을 세 번째 행에 추가

        // 컨테이너 패널을 JFrame에 추가
        add(containerPanel, BorderLayout.CENTER); // 중앙에 위치하게 배치

        loginBtn.addActionListener(e -> handleLogin());
        cancelBtn.addActionListener(e -> dispose());

        setBounds(300, 300, 350, 450);
        setUndecorated(false);
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
            new Admin(AdminLogin.this, parent);
        } else {
            JOptionPane.showMessageDialog(this, "로그인 실패! 아이디와 비밀번호를 확인하세요");
        }
    }

    public AdminVO getLoggedInAdmin() {
        return loggedInAdmin;

    }
}
