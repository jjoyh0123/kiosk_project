package kiosk.client;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.Reader;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import kiosk.adminLogin.AdminLoginJDialog;

public class MainFrame extends JFrame {
  JPanel mainPanel;
  JButton adminBtn;
  public SqlSessionFactory factory;
  List<kiosk.adminVO.productVO> list;

  public MainFrame() {
    dbConnect();

    /* 메인 패널에 배경이미지 추가 */
    mainPanel = new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        Image background1 = new ImageIcon(getClass().getResource("/kiosk/static/main1.jpg")).getImage();
        g.drawImage(background1, 0, 0, getWidth(), getHeight(), this);
      }
    };
    mainPanel.setLayout(new BorderLayout());

    this.setContentPane(mainPanel);
    this.setUndecorated(true);
    this.setSize(540, 960);
    this.setLocationRelativeTo(null);
    this.setVisible(true);

    JPanel contentPane = (JPanel) getContentPane();

    InputMap inputMap = contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
    ActionMap actionMap = contentPane.getActionMap();

    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "spaceAction");
    actionMap.put("spaceAction", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        System.out.println("관리자모드 진입");
        // new Admin(MainFrame.this);
        new AdminLoginJDialog(MainFrame.this);
      }
    });
  }

  private void dbConnect() {
    try {
      Reader r = Resources.getResourceAsReader("kiosk/config/config.xml");
      SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
      factory = builder.build(r);
      r.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    new MainFrame();
  }
}