package kiosk.userMenuManagement;
import javax.swing.*;

import Create.RoundedButton;

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

        // 다이얼로그 기본 설정
        setSize(fixedWidth, 400);
        setLocation(810, 230);
        setUndecorated(true); // 창 상단 제거

        // 컨테이너 패널 생성 (전체 내용 포함)
        
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createLineBorder(Color.gray, 1)); // 검정색 2픽셀 테두리
        contentPanel.setBackground(Color.WHITE); // 배경색 설정 (선택 사항)

        // 상단 패널: 제목
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("기간 입력");
        titleLabel.setFont(new Font("맑은굴림", Font.BOLD, 25)); // 제목 크기 강조
        topPanel.add(titleLabel);
        contentPanel.add(topPanel, BorderLayout.NORTH);

        // 입력 패널: 시작일, 종료일
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10)); // 열 간격, 행 간격 설정
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 여백 설정

        JLabel startLabel = new JLabel("시작일:");
        startLabel.setHorizontalAlignment(SwingConstants.CENTER);
        startField = createPlaceholderField("yyyyMMdd");

        JLabel endLabel = new JLabel("종료일:");
        endLabel.setHorizontalAlignment(SwingConstants.CENTER);
        endField = createPlaceholderField("yyyyMMdd");

        inputPanel.add(new JLabel("시작일:"));
        inputPanel.add(startField);
        inputPanel.add(new JLabel("종료일:"));
        inputPanel.add(endField);

        contentPanel.add(inputPanel, BorderLayout.CENTER);

        // 버튼 패널: 적용 및 취소
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        JButton applyButton = new RoundedButton("적용");
        JButton cancelButton = new RoundedButton("취소");

        applyButton.setPreferredSize(new Dimension(110, 40));
        cancelButton.setPreferredSize(new Dimension(110, 40));

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
        JPanel keypadPanel = new JPanel(new GridLayout(4, 3, 0, 0));
        String[] keys = {"7", "8", "9", "4", "5", "6", "1", "2", "3", "<", "0", "C"};
        for (String key : keys) {
            JButton button = new RoundedButton(key);
            button.setPreferredSize(new Dimension(70, 70));
            button.setFont(new Font("맑은글씨", Font.PLAIN, 30));
            button.addActionListener(e -> handleKeyPress(key));
            keypadPanel.add(button);
        }

        // 버튼 패널과 키패드 추가
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(actionPanel, BorderLayout.NORTH);
        bottomPanel.add(keypadPanel, BorderLayout.CENTER);

        contentPanel.add(bottomPanel, BorderLayout.SOUTH);

        // 컨텐츠 패널을 다이얼로그에 추가
        setContentPane(contentPanel);

        pack();
        setSize(fixedWidth, getPreferredSize().height);
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
