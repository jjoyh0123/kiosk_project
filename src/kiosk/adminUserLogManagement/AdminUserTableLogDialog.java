package kiosk.adminUserLogManagement;

import kiosk.adminVO.UserLogVO;
import kiosk.client.MainFrame;

import javax.swing.*;

import Create.RoundedButton;

import java.awt.*;

public class AdminUserTableLogDialog extends JDialog {

    MainFrame mainFrame;
    JLabel titleLabel;
    JLabel label1, label2, label3, label4, label5, label6, label7, label8, label9;
    JLabel field1, field2, field3, field4, field5, field6, field7, field8, field9;
    JButton exitBtn;

    public AdminUserTableLogDialog(MainFrame mainFrame, Window parent, AdminUserLog adminUserLogSet,
                                   UserLogVO log) {
        super(parent, "사용자 로그 상세", ModalityType.APPLICATION_MODAL);

        this.mainFrame = mainFrame;
        setUndecorated(true); // 창 상단 제거

        // 창 초기화
        setLayout(new BorderLayout());
        
        //전체패널
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createLineBorder(Color.gray, 1)); // 검정색 2픽셀 테두리

        // 상단 패널 추가 (타이틀 + 종료 버튼)
        JPanel topPanel = new JPanel(new BorderLayout());

        // 타이틀은 가운데 배치
        titleLabel = new JLabel("사용자 로그 상세", SwingConstants.CENTER);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));

        // 종료 버튼은 오른쪽에 배치
        exitBtn = new RoundedButton("닫기");
        exitBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        exitBtn.addActionListener(e -> dispose());  // 닫기 버튼 클릭 시 다이얼로그 닫기

        topPanel.add(titleLabel, BorderLayout.CENTER);  // 타이틀을 상단 중앙에 배치
        topPanel.add(exitBtn, BorderLayout.EAST);  // 종료 버튼을 상단 오른쪽에 배치

        //add(topPanel, BorderLayout.NORTH);  // 상단 패널을 다이얼로그의 북쪽에 배치
        contentPanel.add(topPanel,BorderLayout.NORTH);

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

        label3 = new JLabel("Top1ProductName :");
        label3.setFont(new Font("맑은 고딕", Font.BOLD, 15));

        field3 = new JLabel(log.getTop1ProductName());
        field3.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
        field3.setBorder(BorderFactory.createEmptyBorder());

        label4 = new JLabel("Top1ProductOrderCount :");
        label4.setFont(new Font("맑은 고딕", Font.BOLD, 15));

        field4 = new JLabel(String.valueOf(log.getLogTop1ProductOrderCount()));
        field4.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
        field4.setBorder(BorderFactory.createEmptyBorder());

        label5 = new JLabel("Top1ProductExpense :");
        label5.setFont(new Font("맑은 고딕", Font.BOLD, 15));

        field5 = new JLabel(String.valueOf(log.getLogTop1ProductExpense()));
        field5.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
        field5.setBorder(BorderFactory.createEmptyBorder());

        label6 = new JLabel("Top1CouponUsage :");
        label6.setFont(new Font("맑은 고딕", Font.BOLD, 15));

        field5 = new JLabel(String.valueOf(log.getLogTop1ProductExpense()));
        field5.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
        field5.setBorder(BorderFactory.createEmptyBorder());





        // 입력 필드 패널
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputPanel.add(label1);
        inputPanel.add(field1);
        inputPanel.add(label2);
        inputPanel.add(field2);
        inputPanel.add(label3);
        inputPanel.add(field3);
        inputPanel.add(label4);
        inputPanel.add(field4);


        // 전체 레이아웃
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(inputPanel, BorderLayout.CENTER);

        //add(mainPanel, BorderLayout.CENTER);
        contentPanel.add(mainPanel,BorderLayout.CENTER);
        add(contentPanel);
        setSize(460, 450);
        setLocation(730, 230);
        
        setLocationRelativeTo(parent);
    }
}
