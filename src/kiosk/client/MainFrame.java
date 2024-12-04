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

  private CardLayout cardLayout;
  public JPanel mainPanel;

  public int orderNumber = 0;

  public UserVO userVO;
  public OrderPanel orderPanel;
  public OrderDetailsPanel orderDetailsPanel;
  public PaymentPanel paymentPanel;
  public RoundedButton roundedButton;
  public SqlSessionFactory factory;
  List<kiosk.adminVO.productVO> list;

  public MainFrame() {
    dbConnect();
    getMaxDateOrderNumber(); // order테이블에서 날짜가 가장 높은 오더넘버 + 1

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
    RoundedButton userLoginBtn = new RoundedButton("회원 주문");
    userLoginBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 24));
    userLoginBtn.setBounds(50, 850, 200, 70);
    userLoginBtn.addActionListener(e -> new MemberDialog(this, factory)); // OrderDialog 호출

    RoundedButton nonMemberOrderBtn = new RoundedButton("비회원 주문");
    nonMemberOrderBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 24));
    nonMemberOrderBtn.setBounds(290, 850, 200, 70);
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

  private void getMaxDateOrderNumber() {
    Integer num = 0;
    try (SqlSession ss = factory.openSession()) {
      num = ss.selectOne("client.getMaxOrderNumber");
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (num == null) {
      orderNumber = 1;
    }
    orderNumber = 1;
  }

  public static void main(String[] args) {
    new MainFrame();
  }
}