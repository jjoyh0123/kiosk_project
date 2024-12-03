package kiosk.userMenuManagement;

import javax.swing.*;

import kiosk.admin.Admin;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.function.Consumer;

public class AdminUserContactSearchJdialog extends JDialog {
    private JTextField textField;

    public AdminUserContactSearchJdialog(Admin owner, Consumer<String> onSearch) {
        super(owner, "번호 검색", true);
        setLayout(new BorderLayout());

        // 상단 번호 입력 필드
        JPanel inputPanel = new JPanel(new BorderLayout());
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
            JButton button = new JButton(String.valueOf(i));
            button.setPreferredSize(new Dimension(60,60));
            button.addActionListener(e -> appendToTextField(button.getText()));
            keypadPanel.add(button);
            
        }
          
      
        JButton backspaceButton = new JButton("←");
        backspaceButton.addActionListener(e -> {
            String currentText = textField.getText();
            if (currentText.length() > 0) {
                textField.setText((currentText.substring(0, currentText.length() - 1)));
            }
        });
        keypadPanel.add(backspaceButton);
        JButton zeroButton = new JButton("0");
        zeroButton.addActionListener(e -> appendToTextField("0"));
        keypadPanel.add(zeroButton);
        
        
        JButton clearButton = new JButton("C");
        clearButton.addActionListener(e -> textField.setText(""));
        keypadPanel.add(clearButton);

        // 하단 버튼: 입력 및 취소
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        JButton searchButton = new JButton("입력");
        JButton cancelButton = new JButton("취소");
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
        add(inputPanel, BorderLayout.NORTH);
        add(keypadPanel, BorderLayout.SOUTH);
        add(actionPanel, BorderLayout.CENTER);

        setSize(300, 400);
        setLocationRelativeTo(owner);
    }

    private void appendToTextField(String text) {
        String currentText = textField.getText().replace("-", "");
        if (currentText.length() < 11) { // 최대 11자리까지 입력 허용
            textField.setText((currentText + text));
        }
    }

   

   
}
