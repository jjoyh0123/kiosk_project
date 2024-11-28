package kiosk.client;

import kiosk.clientVO.UserVO;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.awt.*;
import javax.swing.*;

public class MemberDialog extends JDialog {

    private JTextField displayField;
    private JButton cancelBtn, loginBtn;
    private SqlSessionFactory factory;

    private String phoneNumber; // 입력된 전화번호 저장
    private String rawPhoneNumber = ""; // 원본 전화번호를 저장

    private MainFrame mainFrame;

    public MemberDialog(MainFrame mainFrame, SqlSessionFactory factory) {
        super(mainFrame, "휴대폰 번호 입력", true);

        // 전달받은 factory 저장
        this.factory = factory;
        this.mainFrame = mainFrame;

        // 다이얼로그 기본 설정
        this.setSize(400, 700);
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
        clearAllButton.addActionListener(e -> {
            rawPhoneNumber = ""; // 원본 데이터 초기화
            displayField.setText(""); // 텍스트 필드 초기화
        });
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
            if (validateAndSetPhoneNumber()) {
                getUserContact();
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
        if (rawPhoneNumber.length() < 11) { // 최대 길이 제한
            rawPhoneNumber += text;
            displayField.setText(formatMaskedPhoneNumber(rawPhoneNumber)); // 마스킹된 번호로 표시
        }
    }

    // 마지막 문자 삭제
    private void backspaceDisplay() {
        if (!rawPhoneNumber.isEmpty()) {
            rawPhoneNumber = rawPhoneNumber.substring(0, rawPhoneNumber.length() - 1); // 마지막 문자 삭제
            displayField.setText(formatMaskedPhoneNumber(rawPhoneNumber)); // 마스킹된 번호로 표시
        }
    }

    // 전화번호 유효성 검사 및 설정
    private boolean validateAndSetPhoneNumber() {
        String rawNumber = rawPhoneNumber; // 원본 데이터로 검사
        if (rawNumber.matches("\\d{10,11}")) {
            this.phoneNumber = rawNumber; // 유효한 경우 전화번호 저장
            return true;
        } else {
            JOptionPane.showMessageDialog(this, "유효하지 않은 휴대폰 번호입니다. 다시 입력해주세요.", "오류", JOptionPane.ERROR_MESSAGE);
            rawPhoneNumber = ""; // 원본 데이터도 초기화
            displayField.setText(""); // 텍스트 필드도 초기화
            return false;
        }
    }

    // 전화번호 포맷팅 메서드 (마스킹)
    private String formatMaskedPhoneNumber(String phoneNumber) {
        if (phoneNumber.length() <= 3) {
            return phoneNumber; // 첫 세 자리는 그대로
        } else if (phoneNumber.length() <= 7) {
            return phoneNumber.substring(0, 3) + " - " + "****".substring(0, phoneNumber.length() - 3);
        } else {
            return phoneNumber.substring(0, 3) + " - **** - " + phoneNumber.substring(7);
        }
    }

    // 전화번호 반환 메서드
    public String getPhoneNumber() {
        return phoneNumber;
    }

    // DB에서 사용자 정보 조회
    private void getUserContact() {
        try (SqlSession ss = factory.openSession()) {
            mainFrame.userVO = ss.selectOne("client.getMatchedUserInfo", phoneNumber);
            if (mainFrame.userVO != null) {
                String maskedNumber = maskPhoneNumber(mainFrame.userVO.getUserContact());
                JOptionPane.showMessageDialog(this,
                        "로그인 성공! 주문 화면으로 이동합니다.\n휴대폰 번호: " + maskedNumber,
                        "성공",
                        JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
                mainFrame.switchToOrderScreen();
            } else {
                JOptionPane.showMessageDialog(this, "존재하지 않는 휴대전화 번호입니다. 다시 입력해주세요.", "오류", JOptionPane.ERROR_MESSAGE);
                rawPhoneNumber = ""; // 번호 초기화
                displayField.setText("");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("데이터베이스 조회 실패");
        }
    }

    // 휴대폰 번호 마스킹 처리 메서드
    private String maskPhoneNumber(String phoneNumber) {
        if (phoneNumber.length() == 10) {
            return phoneNumber.substring(0, 3) + " - **** - " + phoneNumber.substring(6);
        } else if (phoneNumber.length() == 11) {
            return phoneNumber.substring(0, 3) + " - **** - " + phoneNumber.substring(7);
        } else {
            return phoneNumber;
        }
    }
}
