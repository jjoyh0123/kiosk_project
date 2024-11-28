package kiosk.client;

import kiosk.clientVO.CartItem;
import kiosk.clientVO.UserVO;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class MainFrame extends JFrame {

  private CardLayout cardLayout;
  private JPanel mainPanel;
  private SqlSessionFactory factory;

  public int orderNumber = 1;

  public UserVO userVO;
  public OrderPanel orderPanel;
  public OrderDetailsPanel orderDetailsPanel;
  public PaymentPanel paymentPanel;

  public MainFrame() {
    dbConnect();

    // CardLayout 및 메인 패널 설정
    cardLayout = new CardLayout();
    mainPanel = new JPanel(cardLayout);

    // 각 화면(카드) 추가
    mainPanel.add(createMainMenuPanel(), "MainMenu");
    orderPanel = new OrderPanel(MainFrame.this, factory);
    mainPanel.add(orderPanel, "OrderScreen");

    orderDetailsPanel = new OrderDetailsPanel(this, orderPanel);

    paymentPanel = new PaymentPanel(this, factory, orderPanel);

    // 프레임 설정
    this.setContentPane(mainPanel);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setSize(540, 960);
    this.setLocationRelativeTo(null);
    this.setUndecorated(true); // 창닫기 바 제거
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
  }

  private JPanel createMainMenuPanel() {
    JPanel mainMenuPanel = new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        // 배경 이미지 설정
        Image background1 = new ImageIcon(getClass().getResource("/kiosk/static/main1.jpg")).getImage();
        g.drawImage(background1, 0, 0, getWidth(), getHeight(), this);
      }
    };
    mainMenuPanel.setLayout(null);

    // 버튼 추가
    JButton userLoginBtn = new JButton("회원 주문");
    userLoginBtn.setBounds(80, 850, 140, 50);
    userLoginBtn.addActionListener(e -> new MemberDialog(this, factory)); // OrderDialog 호출

    JButton nonMemberOrderBtn = new JButton("비회원 주문");
    nonMemberOrderBtn.setBounds(320, 850, 140, 50);
    nonMemberOrderBtn.addActionListener(e -> switchToOrderScreen());

    mainMenuPanel.add(userLoginBtn);
    mainMenuPanel.add(nonMemberOrderBtn);

    return mainMenuPanel;

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

  // 화면 전환 메서드: 주문 화면으로 이동
  public void switchToOrderScreen() {
    cardLayout.show(mainPanel, "OrderScreen");
  }

  // 화면 전환 메서드: 주문내역 화면으로 이동
  public void switchToOrderDetailsScreen(OrderPanel orderPanel) {
    OrderDetailsPanel orderDetailsPanel = new OrderDetailsPanel(this, orderPanel);
    mainPanel.add(orderDetailsPanel, "OrderDetailsScreen");
    cardLayout.show(mainPanel, "OrderDetailsScreen");
  }

  public void switchToPaymentScreen(OrderPanel orderPanel) {
    PaymentPanel paymentPanel = new PaymentPanel(this, factory, orderPanel);
    mainPanel.add(paymentPanel, "PaymentScreen");
    cardLayout.show(mainPanel, "PaymentScreen");
  }

  // 화면 전환 메서드: 메인 메뉴로 돌아가기
  public void switchToMainMenu() {
    cardLayout.show(mainPanel, "MainMenu");
  }

  public static void main(String[] args) {
    new MainFrame();
  }
}
