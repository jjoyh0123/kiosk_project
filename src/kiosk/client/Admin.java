package kiosk.client;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import kiosk.adminMenu.AdminDialog;

public class Admin extends JDialog {
    JMenuBar menuBar;
    JMenu storeMenu, userMenu, systemMenu;
    JMenuItem menuManageMenuItem, orderManageMenuItem, settlementMenuItem,
        userManageMenuItem, couponManageMenuItem, behavioralAnalysisMenuItem,
        closeAdminMenuItem, exitMenuItem;

    JPanel contentPanel;
    JPanel p1, p2;
    CardLayout cardLayout; // 중앙패널변경하기위한 카드레이아웃입니다

    //public Admin(MainFrame mainFrame) {
      public Admin(AdminLogin adminLogin, MainFrame mainFrame) {
    //super(mainFrame, "관리자", true);
    super(adminLogin, "관리자", true);

        // 메뉴 초기화
        storeMenu = new JMenu("매장");
        menuManageMenuItem = new JMenuItem("메뉴 관리");
        orderManageMenuItem = new JMenuItem("주문 관리");
        settlementMenuItem = new JMenuItem("정산");
        storeMenu.add(menuManageMenuItem);
        storeMenu.add(orderManageMenuItem);
        storeMenu.add(settlementMenuItem);

        userMenu = new JMenu("회원");
        userManageMenuItem = new JMenuItem("회원 관리");
        couponManageMenuItem = new JMenuItem("쿠폰 관리");
        behavioralAnalysisMenuItem = new JMenuItem("성향 분석");
        userMenu.add(userManageMenuItem);
        userMenu.add(couponManageMenuItem);
        userMenu.add(behavioralAnalysisMenuItem);

        systemMenu = new JMenu("시스템");
        closeAdminMenuItem = new JMenuItem("닫기");
        exitMenuItem = new JMenuItem("종료");
        systemMenu.add(closeAdminMenuItem);
        systemMenu.add(exitMenuItem);

        menuBar = new JMenuBar();
        menuBar.add(storeMenu);
        menuBar.add(userMenu);
        menuBar.add(systemMenu);

    setJMenuBar(menuBar);

        contentPanel = new JPanel();
        cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);

        /* 이벤트 리스너 ※주의: 모든 이벤트는 컴포넌트 호출 위에 작성해야 합니다. */
        // 메뉴 관리 메뉴
        // "메뉴 관리" 클릭 시 AdminDialog 내용을 표시하도록 설정
        menuManageMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Admin.this.getContentPane().removeAll();
                p2 = new AdminDialog(mainFrame);
                Admin.this.getContentPane().add(p2);
                Admin.this.revalidate();
                Admin.this.repaint();
              Admin.this.getContentPane().removeAll();
        Admin.this.getContentPane().add(p1);
        Admin.this.revalidate();
        Admin.this.repaint();
      }
        });

        // 나머지 메뉴들에 대한 ActionListener 작성
        orderManageMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 필요한 동작 구현
            }
        });

    // 정산 메뉴
        settlementMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Admin.this.add(p2);
              Admin.this.getContentPane().removeAll();
        Admin.this.getContentPane().add(p2);
        Admin.this.revalidate();
        Admin.this.repaint();
      }
        });

    // 회원 관리 메뉴
        userManageMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 필요한 동작 구현
            }
        });

    // 쿠폰 관리 메뉴
        couponManageMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 필요한 동작 구현
            }
        });

    // 성향 분석 메뉴
        behavioralAnalysisMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 필요한 동작 구현
            }
        });


    // 닫기 메뉴
        closeAdminMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

    // 종료 메뉴
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        /* 컴포넌트 호출 */
        this.setUndecorated(true);
        this.setSize(500, 800);
        this.setLocationRelativeTo(mainFrame);
        this.add(contentPanel);  // contentPanel을 JDialog에 추가
        this.setVisible(true);
    }
}
