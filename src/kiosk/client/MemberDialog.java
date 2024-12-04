package kiosk.client;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.swing.*;
import java.awt.*;

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
    JPanel titlepanel = new JPanel(new BorderLayout());
    JLabel titleLabel = new JLabel("휴대폰 번호를 입력해주세요!", JLabel.CENTER);
    titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 30));
    titleLabel.setBorder(BorderFactory.createEmptyBorder(55, 0, 0, 0));
    titlepanel.setBackground(Color.WHITE);
    titlepanel.add(titleLabel, BorderLayout.CENTER);

    // 텍스트 필드 패널
    JPanel fieldPanel = new JPanel(new BorderLayout());
    displayField = new JTextField();
    displayField.setFont(new Font("맑은 고딕", Font.PLAIN, 24));
    displayField.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
    displayField.setBackground(Color.WHITE);
    displayField.setHorizontalAlignment(JTextField.CENTER);
    displayField.setEditable(false);
    displayField.setPreferredSize(new Dimension(400, 50));
    fieldPanel.add(displayField, BorderLayout.CENTER);

    JPanel northPanel = new JPanel(new BorderLayout());
    northPanel.setPreferredSize(new Dimension(400, 200));
    northPanel.setBackground(Color.WHITE);
    northPanel.add(titlepanel, BorderLayout.NORTH);
    northPanel.add(fieldPanel, BorderLayout.SOUTH);

    // 키패드 버튼
    JPanel buttonPanel = new JPanel(new GridLayout(4, 3, 5, 5));
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(55, 0, 0, 0));
    buttonPanel.setBackground(Color.WHITE);
    buttonPanel.setPreferredSize(new Dimension(300, 400));

    // 버튼 크기 조정
    Dimension buttonSize = new Dimension(80, 80);

    for (int i = 1; i <= 9; i++) {
      String number = String.valueOf(i);
      RoundedButton numberButton = new RoundedButton(number);
      numberButton.setFont(new Font("맑은 고딕", Font.BOLD, 24));
      numberButton.setPreferredSize(buttonSize);
      numberButton.addActionListener(e -> appendToDisplay(number));
      buttonPanel.add(numberButton);
    }

    RoundedButton clearAllButton = new RoundedButton("clear");
    clearAllButton.setFont(new Font("맑은 고딕", Font.BOLD, 24));
    clearAllButton.addActionListener(e -> {
      rawPhoneNumber = ""; // 원본 데이터 초기화
      displayField.setText(""); // 텍스트 필드 초기화
    });
    buttonPanel.add(clearAllButton);

    RoundedButton zeroButton = new RoundedButton("0");
    zeroButton.setFont(new Font("맑은 고딕", Font.BOLD, 24));
    zeroButton.addActionListener(e -> appendToDisplay("0"));
    buttonPanel.add(zeroButton);

    RoundedButton backspaceButton = new RoundedButton("<");
    backspaceButton.setFont(new Font("맑은 고딕", Font.BOLD, 24));
    backspaceButton.addActionListener(e -> backspaceDisplay());
    buttonPanel.add(backspaceButton);

    // 키패드 패널을 담는 상위 패널
    JPanel buttonContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0)); // 중앙 정렬
    buttonContainer.setBackground(Color.WHITE);
    buttonContainer.add(buttonPanel);

    // 하단 버튼
    JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
    southPanel.setBackground(Color.WHITE);
    cancelBtn = new RoundedButton("취소");
    cancelBtn.setPreferredSize(new Dimension(140, 50));
    loginBtn = new RoundedButton("확인");
    loginBtn.setPreferredSize(new Dimension(140, 50));

    cancelBtn.setFont(new Font("맑은 고딕", Font.BOLD, 18));
    cancelBtn.addActionListener(e -> dispose()); // 다이얼로그 닫기

    loginBtn.setFont(new Font("맑은 고딕", Font.BOLD, 18));
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
    add(buttonContainer, BorderLayout.CENTER);
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
        MemberCustomDialog memberCustomDialog = new MemberCustomDialog(mainFrame, "로그인 성공! 휴대폰 번호: " + maskedNumber, 1);
        this.dispose();
      } else {
        MemberCustomDialog memberCustomDialog = new MemberCustomDialog(mainFrame, "존재하지 않는 휴대전화 번호입니다.", 2);
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
