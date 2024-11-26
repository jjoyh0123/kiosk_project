package kiosk.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Admin extends JDialog {
  // MainFrame mainFrame;
  JButton exitBtn;
  JMenuBar menuBar;
  JMenu storeMenu, userMenu, systemMenu;
  JMenuItem menuManageMenuItem, orderManageMenuItem, settlementMenuItem,
      userManageMenuItem, couponManageMenuItem, behavioralAnalysisMenuItem,
      closeAdminMenuItem, exitMenuItem;

  //public Admin(MainFrame mainFrame) {
  public Admin(AdminLogin adminLogin, MainFrame mainFrame) {
    //super(mainFrame, "관리자", true);
    super(adminLogin, "관리자", true);
    // this.mainFrame = mainFrame;

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
    
    if("Junior".equals(adminLogin.loggedInAdmin.getAdminName())) {
    	// 주니어가 볼 수 없는 메뉴는 비활성화
    	menuManageMenuItem.setEnabled(false);
    	settlementMenuItem.setEnabled(false);
    	behavioralAnalysisMenuItem.setEnabled(false);
    }

    /* 이벤트 리스너 ※주의: 모든 이벤트는 컴포넌트 호출 위에 작성해야 합니다. */
    // 메뉴 관리 메뉴
    menuManageMenuItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
      }
    });

    // 주문 관리 메뉴
    orderManageMenuItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
      }
    });

    // 정산 메뉴
    settlementMenuItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
      }
    });

    // 회원 관리 메뉴
    userManageMenuItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
      }
    });

    // 쿠폰 관리 메뉴
    couponManageMenuItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
      }
    });

    // 성향 분석 메뉴
    behavioralAnalysisMenuItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
      }
    });

    // 닫기 메뉴
    closeAdminMenuItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // 종료
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
    this.setVisible(true);
  }
}
