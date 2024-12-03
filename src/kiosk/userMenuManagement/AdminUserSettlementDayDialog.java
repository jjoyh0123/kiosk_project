package kiosk.userMenuManagement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AdminUserSettlementDayDialog extends JDialog {
  private JTextField startField;
  private JTextField endField;
  private boolean isApplied = false;
  private JTextField activeField; // 현재 활성화된 필드
  int fixedWidth = 270;

  public AdminUserSettlementDayDialog(JFrame parentFrame) {
    super(parentFrame, "기간 입력", true);
    setSize(fixedWidth, 400); // 다이얼로그 크기
    //setLayout(new BorderLayout(10, 10)); // 패널 간 간격
    //setUndecorated(true);
    setLocation(945, 230);
    setUndecorated(true);//위에 윈도우창없애기
    // 상단 패널: 제목
    JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    JLabel titleLabel = new JLabel("기간 입력");
    titleLabel.setFont(new Font("굴림", Font.BOLD, 25)); // 제목 크기 강조

    topPanel.add(titleLabel);
    add(topPanel, BorderLayout.NORTH);

    // 입력 패널: 시작일, 종료일
    JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10)); // 열 간격, 행 간격 설정
    inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 패널 여백

    JLabel startLabel = new JLabel("시작일:");
    startLabel.setHorizontalAlignment(SwingConstants.CENTER); // 텍스트 오른쪽 정렬
    startField = createPlaceholderField("yyyyMMdd"); // 시작일 필드 생성
    JPanel startLabelPanel = new JPanel(new BorderLayout());
    startLabelPanel.setPreferredSize(new Dimension(120, 30));
    startLabelPanel.add(startLabel);
    JPanel startFieldPanel = new JPanel(new BorderLayout());
    startFieldPanel.setPreferredSize(new Dimension(120, 30));
    startFieldPanel.add(startField);


    JLabel endLabel = new JLabel("종료일:");
    endLabel.setHorizontalAlignment(SwingConstants.CENTER); // 텍스트 오른쪽 정렬
    endField = createPlaceholderField("yyyyMMdd"); // 종료일 필드 생성
    JPanel endLabelPanel = new JPanel(new BorderLayout());
    endLabelPanel.setPreferredSize(new Dimension(130, 30));
    endLabelPanel.add(endLabel);
    JPanel endFieldPanel = new JPanel(new BorderLayout());
    endFieldPanel.setPreferredSize(new Dimension(130, 30));
    endFieldPanel.add(endField);

    inputPanel.add(startLabelPanel);
    inputPanel.add(startFieldPanel);
    inputPanel.add(endLabelPanel);
    inputPanel.add(endFieldPanel);
    add(inputPanel, BorderLayout.CENTER);

    // 버튼 패널: 적용 및 취소
    JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
    JButton applyButton = new JButton("적용");
    JButton cancelButton = new JButton("취소");

    applyButton.setPreferredSize(new Dimension(110, 40)); // 버튼 크기
    cancelButton.setPreferredSize(new Dimension(110, 40)); // 버튼 크기

    applyButton.addActionListener(e -> {
      if (validateDates()) {
        isApplied = true;
        dispose();
      }
    });

    cancelButton.addActionListener(e -> dispose());

    actionPanel.add(applyButton);
    actionPanel.add(cancelButton);

    // 키패드 패널
    JPanel keypadPanel = new JPanel(new GridLayout(4, 3, 0, 0)); // 키패드 간격 조정
    //keypadPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 키패드 여백

    String[] keys = {"7", "8", "9", "4", "5", "6", "1", "2", "3", "<", "0", "C"};
    for (String key : keys) {
      JPanel numBtnPanel;
      if (key.equals("1") || key.equals("4") || key.equals("7") || key.equals("<")) {
        numBtnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
      } else if (key.equals("3") || key.equals("6") || key.equals("9") || key.equals("C")) {
        numBtnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      } else {
        numBtnPanel = new JPanel(new FlowLayout());
      }
      numBtnPanel.setPreferredSize(new Dimension(90, 90));
      JButton button = new JButton(key);
      button.setPreferredSize(new Dimension(70, 70)); // 버튼 크기
      button.addActionListener(e -> handleKeyPress(key));
      numBtnPanel.add(button);
      keypadPanel.add(numBtnPanel);
    }

    // 버튼 패널과 키패드 추가
    JPanel bottomPanel = new JPanel(new BorderLayout());
    bottomPanel.add(actionPanel, BorderLayout.NORTH); // 버튼 패널을 키패드 위로
    bottomPanel.add(keypadPanel, BorderLayout.CENTER);
    add(bottomPanel, BorderLayout.SOUTH);

    pack();
    setSize(fixedWidth, this.getPreferredSize().height);
  }

  private JTextField createPlaceholderField(String placeholder) {
    JTextField field = new JTextField(placeholder);

    field.setPreferredSize(new Dimension(150, 40)); // 필드 크기
    field.setForeground(Color.GRAY); // 플레이스홀더 텍스트 색상

    field.addFocusListener(new FocusAdapter() {
      @Override
      public void focusGained(FocusEvent e) {
        if (field.getText().equals(placeholder)) {
          field.setText("");
          field.setForeground(Color.BLACK); // 입력 시 텍스트 색상 변경
        }
        activeField = field; // 현재 활성화된 필드 설정
      }

      @Override
      public void focusLost(FocusEvent e) {
        if (field.getText().isEmpty()) {
          field.setText(placeholder);
          field.setForeground(Color.GRAY); // 비어 있을 때 플레이스홀더 복원
        }
      }
    });

    field.addActionListener(e -> activeField = field); // 입력 중에도 활성 필드 업데이트

    return field;
  }

  private void handleKeyPress(String key) {
    if (activeField == null) return; // 활성화된 필드가 없으면 입력 무시

    // 입력 시 기본값 지우기
    if (activeField.getText().equals("yyyyMMdd")) {
      activeField.setText("");
    }

    if (key.equals("C")) {
      activeField.setText("");
    } else if (key.equals("<")) {
      String text = activeField.getText();
      if (!text.isEmpty()) {
        activeField.setText(text.substring(0, text.length() - 1));
      }
    } else {
      activeField.setText(activeField.getText() + key);
    }
  }

  private boolean validateDates() {
    try {
      String startDate = startField.getText();
      String endDate = endField.getText();
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
      LocalDate.parse(startDate, formatter);
      LocalDate.parse(endDate, formatter);
      return true;
    } catch (Exception e) {
      JOptionPane.showMessageDialog(this, "날짜 형식이 올바르지 않습니다. yyyyMMdd 형식으로 입력해주세요.");
      return false;
    }
  }

  public String getStartDate() {
    return startField.getText();
  }

  public String getEndDate() {
    return endField.getText();
  }

  public boolean isApplied() {
    return isApplied;
  }
}
