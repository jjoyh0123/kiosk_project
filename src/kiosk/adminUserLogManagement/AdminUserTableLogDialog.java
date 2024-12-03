package kiosk.adminUserLogManagement;

import kiosk.adminVO.UserLogVO;
import kiosk.client.MainFrame;

import javax.swing.*;
import java.awt.*;

public class AdminUserTableLogDialog extends JDialog {

    MainFrame mainFrame;
    JLabel titleLabel;
    JLabel label1, label2, label3, label4, label5, label6, label7, label8, label9, label10, label11, label12, label13;
    JLabel field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13;
    JButton exitBtn;

    public AdminUserTableLogDialog(MainFrame mainFrame, Window parent, AdminUserLog adminUserLogSet,
                                   UserLogVO log) {
        super(parent, "사용자 로그 상세", ModalityType.APPLICATION_MODAL);

        setUndecorated(true);

        this.mainFrame = mainFrame;

        // 창 초기화
        setLayout(new BorderLayout());

        // 상단 패널 추가 (타이틀 + 종료 버튼)
        JPanel topPanel = new JPanel(new BorderLayout());

        // 타이틀은 가운데 배치
        titleLabel = new JLabel("사용자 로그 상세", SwingConstants.CENTER);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));

        // 종료 버튼은 오른쪽에 배치
        exitBtn = new JButton("닫기");
        exitBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        exitBtn.addActionListener(e -> dispose());  // 닫기 버튼 클릭 시 다이얼로그 닫기

        topPanel.add(titleLabel, BorderLayout.CENTER);  // 타이틀을 상단 중앙에 배치
        topPanel.add(exitBtn, BorderLayout.EAST);  // 종료 버튼을 상단 오른쪽에 배치

        add(topPanel, BorderLayout.NORTH);  // 상단 패널을 다이얼로그의 북쪽에 배치


        label1 = new JLabel("고객연락처 :");
        label1.setFont(new Font("맑은 고딕", Font.BOLD, 15));

        field1 = new JLabel(log.getUserContact());
        field1.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
        field1.setBorder(BorderFactory.createEmptyBorder());

        label2 = new JLabel("로그날짜 :");
        label2.setFont(new Font("맑은 고딕", Font.BOLD, 15));

        field2 = new JLabel(log.getLogDate());
        field2.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
        field2.setBorder(BorderFactory.createEmptyBorder());

        // ProductName Top 1,2,3
        label3 = new JLabel("Top1ProductName :");
        label3.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        field3 = new JLabel(log.getTop1ProductName());
        field3.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
        field3.setBorder(BorderFactory.createEmptyBorder());

        label7 = new JLabel("Top2ProductName :");
        label7.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        field7 = new JLabel(log.getTop2ProductName());
        field7.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
        field7.setBorder(BorderFactory.createEmptyBorder());

        label10 = new JLabel("Top3ProductName :");
        label10.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        field10 = new JLabel(log.getTop3ProductName());
        field10.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
        field10.setBorder(BorderFactory.createEmptyBorder());

        // ProductOrderCount Top 1,2,3
        label4 = new JLabel("Top1ProductOrderCount :");
        label4.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        field4 = new JLabel(String.valueOf(log.getLogTop1ProductOrderCount()));
        field4.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
        field4.setBorder(BorderFactory.createEmptyBorder());

        label8 = new JLabel("Top2ProductOrderCount :");
        label8.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        field8 = new JLabel(String.valueOf(log.getLogTop2ProductOrderCount()));
        field8.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
        field8.setBorder(BorderFactory.createEmptyBorder());

        label9 = new JLabel("Top3ProductOrderCount :");
        label9.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        field9 = new JLabel(String.valueOf(log.getLogTop3ProductOrderCount()));
        field9.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
        field9.setBorder(BorderFactory.createEmptyBorder());


        // ProductExpense Top 1,2,3
        label5 = new JLabel("Top1ProductExpense :");
        label5.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        field5 = new JLabel(String.valueOf(log.getLogTop1ProductExpense()));
        field5.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
        field5.setBorder(BorderFactory.createEmptyBorder());

        label6 = new JLabel("Top2ProductExpense :");
        label6.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        field6 = new JLabel(String.valueOf(log.getLogTop2ProductExpense()));
        field6.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
        field6.setBorder(BorderFactory.createEmptyBorder());






        // 입력 필드 패널

        JPanel inputPanel = new JPanel(new GridLayout(0, 2, 10, 10));

        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputPanel.add(label1);
        inputPanel.add(field1);
        inputPanel.add(label2);
        inputPanel.add(field2);
        inputPanel.add(label3);
        inputPanel.add(field3);
        inputPanel.add(label7);
        inputPanel.add(field7);
        inputPanel.add(label4);
        inputPanel.add(field4);
        inputPanel.add(label5);
        inputPanel.add(field5);
        inputPanel.add(label6);
        inputPanel.add(field6);


        // 전체 레이아웃
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(inputPanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        setSize(460, 400);
        setLocationRelativeTo(parent);
    }
}
