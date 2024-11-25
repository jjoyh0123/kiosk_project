package kiosk.client;

import kiosk.vo.UserVO;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.awt.*;
import javax.swing.*;

public class OrderDialog extends JDialog {

    private JTextField displayField;
    private JButton cancelBtn, loginBtn;
    private SqlSessionFactory factory;

    private String phoneNumber; // 입력된 전화번호 저장

    public OrderDialog(MainFrame mainFrame, SqlSessionFactory factory) {
        super(mainFrame, "휴대폰 번호 입력", true);

        // 전달받은 factory 저장
        this.factory = factory;

        // 다이얼로그 기본 설정
        this.setSize(500, 900);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setUndecorated(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // 상단 안내 텍스트와 텍스트 필드
        JPanel northPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("휴대폰 번호를 입력해주세요!", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        northPanel.add(titleLabel, BorderLayout.NORTH);

        displayField = new JTextField();
        displayField.setFont(new Font("Arial", Font.PLAIN, 24));
        displayField.setHorizontalAlignment(JTextField.CENTER);
        displayField.setEditable(false);
        displayField.setPreferredSize(new Dimension(400, 50));
        northPanel.add(displayField, BorderLayout.SOUTH);

        // 키패드 버튼
        JPanel buttonPanel = new JPanel(new GridLayout(4, 3, 10, 20));
        for (int i = 1; i <= 9; i++) {
            String number = String.valueOf(i);
            JButton numberButton = new JButton(number);
            numberButton.setFont(new Font("Arial", Font.BOLD, 24));
            numberButton.addActionListener(e -> appendToDisplay(number));
            buttonPanel.add(numberButton);
        }

        JButton clearAllButton = new JButton("전체 지우기");
        clearAllButton.setFont(new Font("Arial", Font.BOLD, 18));
        clearAllButton.addActionListener(e -> displayField.setText(""));
        buttonPanel.add(clearAllButton);

        JButton zeroButton = new JButton("0");
        zeroButton.setFont(new Font("Arial", Font.BOLD, 24));
        zeroButton.addActionListener(e -> appendToDisplay("0"));
        buttonPanel.add(zeroButton);

        JButton backspaceButton = new JButton("하나만 지우기");
        backspaceButton.setFont(new Font("Arial", Font.BOLD, 18));
        backspaceButton.addActionListener(e -> backspaceDisplay());
        buttonPanel.add(backspaceButton);

        // 하단 버튼
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        cancelBtn = new JButton("취소");
        loginBtn = new JButton("확인");

        cancelBtn.setFont(new Font("Arial", Font.BOLD, 18));
        cancelBtn.addActionListener(e -> dispose()); // 다이얼로그 닫기

        loginBtn.setFont(new Font("Arial", Font.BOLD, 18));
        loginBtn.addActionListener(e -> {
            if (validateAndSetPhoneNumber()) { // 유효성 검사
                getUserContact(); // DB 조회

            }
        });

        southPanel.add(cancelBtn);
        southPanel.add(loginBtn);

        // 패널 추가
        setLayout(new BorderLayout());
        add(northPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // 입력된 전화번호 추가
    private void appendToDisplay(String text) {
        String currentText = displayField.getText().replace(" - ", "");
        if (currentText.length() < 11) {
            currentText += text;
            displayField.setText(formatPhoneNumber(currentText));
        }
    }

    // 마지막 문자 삭제
    private void backspaceDisplay() {
        String currentText = displayField.getText().replace(" - ", "");
        if (!currentText.isEmpty()) {
            displayField.setText(formatPhoneNumber(currentText.substring(0, currentText.length() - 1)));
        }
    }

    // 전화번호 유효성 검사 및 설정
    private boolean validateAndSetPhoneNumber() {
        String rawNumber = displayField.getText().replace(" - ", "");
        if (rawNumber.matches("\\d{10,11}")) {
            this.phoneNumber = rawNumber;
            return true;
        } else {
            JOptionPane.showMessageDialog(this, "유효하지 않은 휴대폰 번호입니다. 다시 입력해주세요.", "오류", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // 전화번호 포맷팅
    private String formatPhoneNumber(String phoneNumber) {
        if (phoneNumber.length() <= 3) {
            return phoneNumber;
        } else if (phoneNumber.length() <= 7) {
            return phoneNumber.substring(0, 3) + " - " + phoneNumber.substring(3);
        } else {
            return phoneNumber.substring(0, 3) + " - " + phoneNumber.substring(3, 7) + " - " + phoneNumber.substring(7);
        }
    }

    // 전화번호 반환 메서드
    public String getPhoneNumber() {
        return phoneNumber;
    }

    // DB에서 사용자 정보 조회
    private void getUserContact() {
        try (SqlSession ss = factory.openSession()) {
            UserVO userVO = ss.selectOne("client.getMatchedUserInfo", phoneNumber);
            if (userVO != null) {
                System.out.println("유저 연락처: " + userVO.getUserContact());
            } else {
                System.out.println("해당 유저 정보를 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("데이터베이스 조회 실패");
        }
    }
}
