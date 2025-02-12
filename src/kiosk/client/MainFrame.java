package kiosk.client;

import kiosk.admin.Admin;
import kiosk.adminLogin.AdminLoginJDialog;
import kiosk.clientVO.UserVO;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.Reader;
import java.util.List;

public class MainFrame extends JFrame {
  public JPanel layoutPanel;
  public MainPanel mainPanel;
  public OrderPanel orderPanel;
  public OrderDetailsPanel orderDetailsPanel;
  public PaymentPanel paymentPanel;

  private final CardLayout cardLayout;

  public SqlSessionFactory factory;

  public RoundedButton roundedButton;
  List<kiosk.adminVO.productVO> list;

  public UserVO userVO;

  public int orderNumber = 0;

  public MainFrame() {
    dbConnect();
    getTodayLastOrderNumber(); // order 테이블에서 날짜가 가장 높은 오더넘버 + 1

    layoutPanel = new JPanel(cardLayout = new CardLayout());

    mainPanel = new MainPanel(MainFrame.this);
    orderPanel = new OrderPanel(MainFrame.this, factory);
    orderDetailsPanel = new OrderDetailsPanel(this, orderPanel);
    paymentPanel = new PaymentPanel(this, factory, orderPanel);

    layoutPanel.add(mainPanel, "mainPanel");
    layoutPanel.add(orderPanel, "OrderScreen");

    setContentPane(layoutPanel);
    setSize(540, 960);
    setLocationRelativeTo(null);
    setUndecorated(true);
    setVisible(true);

    setAdminAuthKeyEvent();
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

  private void getTodayLastOrderNumber() {
    Integer num = 0;
    try (SqlSession ss = factory.openSession()) {
      num = ss.selectOne("client.getMaxOrderNumber");
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (num == null) orderNumber = 1;
    else orderNumber = num;
  }

  private void setAdminAuthKeyEvent() {
    /* Set Admin Authorize Key Event */
    JPanel contentPane = (JPanel) getContentPane();

    InputMap inputMap = contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
    ActionMap actionMap = contentPane.getActionMap();

    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "upAction");
    actionMap.put("upAction", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        System.out.println("관리자 로그인 진입");
        new AdminLoginJDialog(MainFrame.this);
      }
    });

    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "downAction");
    actionMap.put("downAction", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        System.out.println("관리자 모드 진입");
        new Admin(MainFrame.this, null);
      }
    });
  }

  public void switchToMainMenu() {
    cardLayout.show(layoutPanel, "MainPanel");
  }

  public void switchToOrderScreen() {
    cardLayout.show(layoutPanel, "OrderScreen");
  }

  public void switchToOrderDetailsScreen(OrderPanel orderPanel) {
    OrderDetailsPanel orderDetailsPanel = new OrderDetailsPanel(this, orderPanel);
    layoutPanel.add(orderDetailsPanel, "OrderDetailsScreen");
    cardLayout.show(layoutPanel, "OrderDetailsScreen");
  }

  public void switchToPaymentScreen(OrderPanel orderPanel) {
    PaymentPanel paymentPanel = new PaymentPanel(this, factory, orderPanel);
    layoutPanel.add(paymentPanel, "PaymentScreen");
    cardLayout.show(layoutPanel, "PaymentScreen");
  }

  public static void main(String[] args) {
    new MainFrame();
  }
}