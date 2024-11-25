package kiosk.client;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.Reader;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class MainFrame extends JFrame {
  JPanel mainPanel;
  JButton userLoginBtn, nonMemberOrderBtn;
  JButton adminBtn;

  SqlSessionFactory factory;

  public MainFrame() {

    try {
      Reader reader = Resources.getResourceAsReader("kiosk/config/config.xml");
      factory = new SqlSessionFactoryBuilder().build(reader);
      reader.close();
      System.out.println("SqlSessionFactory 초기화 성공!");
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("SqlSessionFactory 초기화 실패");
    }

    /* 메인 패널에 배경이미지 추가 */
    mainPanel = new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        Image background1 = new ImageIcon(getClass().getResource("/kiosk/static/main1.jpg")).getImage();
        g.drawImage(background1, 0, 0, getWidth(), getHeight(), this);
      }
    };
    mainPanel.setLayout(null);

    // 오큐파이드

    userLoginBtn = new JButton("회원주문");
    userLoginBtn.setBounds(80, 850, 140, 50);
    mainPanel.add(userLoginBtn);

    nonMemberOrderBtn = new JButton("회원주문");
    nonMemberOrderBtn.setBounds(320, 850, 140, 50);
    mainPanel.add(nonMemberOrderBtn);

    this.setContentPane(mainPanel);
    this.setUndecorated(true);
    this.setSize(540, 960);
    this.setLocationRelativeTo(null);
    this.setVisible(true);

    JPanel contentPane = (JPanel) getContentPane();

    InputMap inputMap = contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
    ActionMap actionMap = contentPane.getActionMap();

    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "spaceAction");
    actionMap.put("spaceAction", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        System.out.println("관리자모드 진입");
        new Admin(MainFrame.this);
      }
    });

    userLoginBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
         new OrderDialog(MainFrame.this, factory);
      }
    });
  }

  public static void main(String[] args) {
    new MainFrame();
  }
}