package kiosk.adminUserLogManagement;

import kiosk.adminVO.UserLogVO;
import kiosk.client.MainFrame;

import javax.swing.*;
import java.awt.*;

public class AdminUserTableLogDialog extends JDialog {

    MainFrame mainFrame;
    JLabel titleLabel, label1, label2, label3;
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

        JLabel label1 = new JLabel("<html>" +
               "로그 기준날짜 : " + log.getLogDate() + "<br>" +
               "고객 연락처 : " + log.getUserContact() + "<br>" +
               "전월 기준 고객 등급 : " + log.getLogGrade() + "<br>" +
               "Top1 구매한 상품명 : " + log.getTop1ProductName() + "<br>" +
               "Top1 구매 갯수 : " + log.getLogTop1ProductOrderCount() + "<br>" +
               "Top1 구매한 비용 : " + log.getLogTop1ProductExpense() + "<br>" +
               "Top1 사용한 쿠폰 : " + log.getLogTop1CouponUsage() + "<br>" +
               "</html>");
        label1.setFont(new Font("맑은 고딕", Font.BOLD, 11));

        JLabel label2 = new JLabel("<html>" +
                "Top2 구매한 상품명 : " + log.getTop2ProductName() + "<br>" +
                "Top2 구매 갯수 : " + log.getLogTop2ProductOrderCount() + "<br>" +
                "Top2 구매한 비용 : " + log.getLogTop2ProductExpense() + "<br>" +
                "Top2 사용한 쿠폰 : " + log.getLogTop2CouponUsage() + "<br>" +
                "</html>");
        label2.setFont(new Font("맑은 고딕", Font.BOLD, 11));

        JLabel label3 = new JLabel("<html>" +
                "Top3 구매한 상품명 : " + log.getTop3ProductName() + "<br>" +
                "Top3 구매 갯수 : " + log.getLogTop3ProductOrderCount() + "<br>" +
                "Top3 구매한 비용 : " + log.getLogTop3ProductExpense() + "<br>" +
                "Top3 사용한 쿠폰 : " + log.getLogTop3CouponUsage() + "<br>" +
                "</html>");
        label3.setFont(new Font("맑은 고딕", Font.BOLD, 11));



        // 입력 필드 패널
        JPanel inputPanel = new JPanel(new GridLayout(0, 1, 10, 10));

        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputPanel.add(label1);
        inputPanel.add(label2);
        inputPanel.add(label3);



        // 전체 레이아웃
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(inputPanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        setSize(460, 400);
        setLocationRelativeTo(parent);
    }
}
