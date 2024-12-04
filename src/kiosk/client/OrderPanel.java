package kiosk.client;

import kiosk.clientVO.CartItem;
import kiosk.clientVO.OptionVO;
import kiosk.clientVO.ProductVO;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class OrderPanel extends JPanel {

  private MainFrame mainFrame;
  private SqlSessionFactory factory;
  public List<ProductVO> productList;
  public List<CartItem> cartItems; // 장바구니 항목 리스트
  private JPanel cartPanel; // 장바구니 표시 패널
  private JLabel totalPriceLabel; // 총 가격 라벨
  public int totalPrice = 0; // 총 가격 변수
  private JButton payButton; // 결제 버튼

  private JLabel cartLabel; // 장바구니 수량 표시 레이블


  public OrderPanel(MainFrame mainFrame, SqlSessionFactory factory) {
    this.mainFrame = mainFrame;
    this.factory = factory;
    this.cartItems = new ArrayList<>(); // 장바구니 초기화
    setLayout(new BorderLayout());

    // 상단 카테고리 영역
    JPanel categoryPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
    categoryPanel.setBackground(Color.WHITE);
    String[] categories = {"세트 메뉴", "버거", "사이드", "음료"};
    int[] categoryIndices = {2, 1, 3, 4}; // 카테고리 매핑

    for (int i = 0; i < categories.length; i++) {
      JButton categoryButton = new RoundedButton(categories[i]);
      categoryButton.setFont(new Font("맑은 고딕", Font.BOLD, 20));
      categoryButton.setPreferredSize(new Dimension(110, 50));

      int categoryIdx = categoryIndices[i];
      categoryButton.addActionListener(e -> {
        getProductData(categoryIdx);
        updateProductGrid();
      });

      categoryPanel.add(categoryButton);
    }
    add(categoryPanel, BorderLayout.NORTH);

    // 중앙 스크롤 가능한 그리드 영역
    JPanel gridPanel = new JPanel(new GridLayout(3, 3, 10, 10));
    gridPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

    JScrollPane scrollPane = new JScrollPane(gridPanel);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    add(scrollPane, BorderLayout.CENTER);

    this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    this.setBackground(Color.WHITE);

    // 하단 패널
    JPanel bottomPanel = new JPanel(new BorderLayout());

    // 버튼 패널
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
    buttonPanel.setBackground(Color.WHITE);
    cartLabel = new JLabel("장바구니 수량:" + cartItems.size());
    payButton = new RoundedButton("결제");
    cartLabel.setFont(new Font("맑은 고딕", Font.BOLD, 15));
    cartLabel.setPreferredSize(new Dimension(150, 50));
    payButton.setFont(new Font("맑은 고딕", Font.BOLD, 18));
    payButton.setPreferredSize(new Dimension(120, 50));

    JButton cancelButton = new RoundedButton("취소"); // 홈화면으로 돌아가기
    cancelButton.setFont(new Font("맑은 고딕", Font.BOLD, 18));
    cancelButton.setPreferredSize(new Dimension(120, 50));

    buttonPanel.add(cartLabel);
    buttonPanel.add(payButton);
    buttonPanel.add(cancelButton);

    // 장바구니 표시 영역
    cartPanel = new JPanel();
    cartPanel.setLayout(new BoxLayout(cartPanel, BoxLayout.Y_AXIS));
    cartPanel.setBackground(Color.WHITE);
    JScrollPane cartScrollPane = new JScrollPane(cartPanel);
    cartScrollPane.setPreferredSize(new Dimension(400, 200));

    // 총 가격 및 초기화 버튼
    JPanel totalPanel = new JPanel(new BorderLayout());
    totalPanel.setBackground(Color.WHITE);
    JButton clearCartButton = new RoundedButton("초기화");
    clearCartButton.setFont(new Font("맑은 고딕", Font.BOLD, 16));
    clearCartButton.setPreferredSize(new Dimension(100, 40));
    clearCartButton.addActionListener(e -> clearCart());

    totalPriceLabel = new JLabel("총 가격: ₩0", JLabel.RIGHT);
    totalPriceLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));

    totalPanel.add(clearCartButton, BorderLayout.WEST);
    totalPanel.add(totalPriceLabel, BorderLayout.CENTER);

    bottomPanel.add(buttonPanel, BorderLayout.NORTH);
    bottomPanel.add(cartScrollPane, BorderLayout.CENTER);
    bottomPanel.add(totalPanel, BorderLayout.SOUTH);
    add(bottomPanel, BorderLayout.SOUTH);


    payButton.addActionListener(e -> {
      if (cartItems.size() > 0) {
        mainFrame.orderDetailsPanel.updateTotalPrice();
        mainFrame.switchToOrderDetailsScreen(this);
        //mainFrame.orderDetailsPanel.revalidate();
        //mainFrame.orderDetailsPanel.repaint();

        // test
        // userVO가 널이 아니면 여기 매개변수에 userVO의 정보를 전달
        if (mainFrame.userVO != null) {
          System.out.println(mainFrame.userVO.getUserIdx());
        }
      }
    });

    cancelButton.addActionListener(e -> {
      if (mainFrame.userVO != null) {
        mainFrame.userVO = null;
      }
      // 장바구니 초기화
      clearCart();

      // 메인화면으로 이동
      mainFrame.switchToMainMenu();
    });

    // 기본 카테고리 데이터 로드 및 화면 초기화
    getProductData(1);
    updateProductGrid();

    updatePayButtonState(); // 처음 버튼 상태 비활성화
  }

  // MyBatis를 통해 상품 데이터를 가져옴
  public void getProductData(int categoryIdx) {
    try (SqlSession ss = factory.openSession()) {
      productList = ss.selectList("client.getProductData", categoryIdx);
    }
  }

  // 상품화면 UI 보여주기
  private void updateProductGrid() {
    JPanel gridPanel = new JPanel(new GridBagLayout());
    gridPanel.setBackground(Color.WHITE);
    JScrollPane scrollPane = (JScrollPane) getComponent(1); // 기존 스크롤패인 가져오기
    scrollPane.setViewportView(gridPanel); // 새로운 gridPanel 설정

    gridPanel.removeAll(); // 기존 항목 제거

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(0, 0, 0, 0); // 여백 설정
    gbc.fill = GridBagConstraints.BOTH;
    gbc.weightx = 1.0;
    gbc.weighty = 1.0;

    Dimension panelSize = new Dimension(140, 180); // 각 상품 패널 크기
    int row = 0;
    int col = 0;
    int maxCols = 3; // 한 행의 최대 열 수
    int totalSlots = 9; // 그리드 총 슬롯 수 (3x3 그리드)

    for (int i = 0; i < totalSlots; i++) {
      JPanel itemPanel = new JPanel();
      itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
      itemPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
      itemPanel.setBackground(Color.WHITE);
      itemPanel.setPreferredSize(panelSize);

      if (i < productList.size()) {
        ProductVO product = productList.get(i);

        JLabel itemImage = new JLabel();
        try {
          Image productImage = new ImageIcon(getClass().getResource("/kiosk/static/product" + product.getProductIdx() + ".jpg")).getImage();
          Image scaledImage = productImage.getScaledInstance(140, 90, Image.SCALE_SMOOTH);
          itemImage.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
          e.printStackTrace();
        }
        itemImage.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel itemName = new JLabel(product.getProductName());
        itemName.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        itemName.setAlignmentX(Component.CENTER_ALIGNMENT);
        itemName.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel itemPrice = new JLabel("₩" + CurrencyFormatter.format(product.getProductPrice()));
        itemPrice.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        itemPrice.setAlignmentX(Component.CENTER_ALIGNMENT);
        itemPrice.setHorizontalAlignment(SwingConstants.CENTER);

        itemPanel.addMouseListener(new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
            // 좌클릭하면
            if (SwingUtilities.isLeftMouseButton(e)) {
              if (product.getProductCategory() == 2) {
                addSetMenuToCart(product);
              } else {
                addToCart(product, 1, 0, 0, 0, 0, 0, 0);
              }
            }
          }
        });

        itemPanel.add(Box.createVerticalGlue());
        itemPanel.add(itemImage);
        itemPanel.add(Box.createVerticalStrut(20));
        itemPanel.add(itemName);
        itemPanel.add(Box.createVerticalStrut(10));
        itemPanel.add(itemPrice);
        itemPanel.add(Box.createVerticalGlue());
      } else {
        itemPanel.setBackground(Color.WHITE);
      }

      gbc.gridx = col;
      gbc.gridy = row;
      gridPanel.add(itemPanel, gbc);

      col++;
      if (col >= maxCols) {
        col = 0;
        row++;
      }
    }

    gridPanel.revalidate();
    gridPanel.repaint();
  }

  // 세트메뉴 추가 버튼 누르면 OptionDialog 값전달 후 값 다시 받아와서 저장
  private void addSetMenuToCart(ProductVO product) {
    // "fries"는 프라이 옵션, "drink"는 음료 옵션을 의미
    List<OptionVO> friesOptions = getOptionList(1);  // 1: 프라이
    List<OptionVO> drinkOptions = getOptionList(2);  // 2: 음료

    // OptionDialog로 데이터 전달
    OptionDialog optionDialog = new OptionDialog(mainFrame, product.getProductName(), product.getProductIdx(), product.getProductPrice(), friesOptions, drinkOptions);

    int selectedFriesIdx = optionDialog.getSelectedFriesIdx();
    int selectedDrinkIdx = optionDialog.getSelectedDrinkIdx();
    int friesPrice = optionDialog.getFriesPrice();
    int drinkPrice = optionDialog.getDrinkPrice();
    int quantity = optionDialog.getQuantity();
    int friesCalorie = optionDialog.getFriesCalorie();
    int drinkCalorie = optionDialog.getDrinkCalorie();

    if (selectedFriesIdx != 0 && selectedDrinkIdx != 0) {
      addToCart(product, quantity, selectedFriesIdx, friesPrice, selectedDrinkIdx, drinkPrice, friesCalorie, drinkCalorie);
    }
  }

  // 데이터 소스에서 OptionVO 리스트를 받아오는 메서드 (프라이 또는 음료 옵션)
  private List<OptionVO> getOptionList(int category) {
    List<OptionVO> options = new ArrayList<>();
    try (SqlSession ss = factory.openSession()) {
      // category 값에 따라 옵션 데이터를 받아옴 (1: 프라이, 2: 음료)
      options = ss.selectList("client.getOptionData", category);
    }
    return options;
  }

  private void addToCart(ProductVO product, int quantity, Integer friesOptionIdx, int friesPrice, Integer drinkOptionIdx, int drinkPrice, int friesCalorie, int drinkCalorie) {

    if (cartItems == null) {
      cartItems = new ArrayList<>(); // null인 경우 초기화
    }

    boolean itemExists = false;

    for (CartItem item : cartItems) {
      if (item.getProductIdx() == product.getProductIdx()
          && item.getOption1Idx() == friesOptionIdx
          && item.getOption2Idx() == drinkOptionIdx) {
        item.setOrderCount(item.getOrderCount() + quantity); // 수량 증가
        itemExists = true;
        break;
      }
    }

    if (!itemExists) {
      //CartItem cartItem = new CartItem(product, quantity, friesOption, friesPrice, drinkOption, drinkPrice);
      CartItem cartItem = new CartItem();
      cartItem.setProductName(product.getProductName());
      if (mainFrame.userVO != null) {
        cartItem.setUserIdx(mainFrame.userVO.getUserIdx());
        // System.out.println(mainFrame.userVO.getUserIdx()); userIdx 값이 들어가는지 확인
      }
      cartItem.setProductIdx(product.getProductIdx());
      cartItem.setOption1Idx(friesOptionIdx);
      cartItem.setOption2Idx(drinkOptionIdx);
      cartItem.setOrderNumber(mainFrame.orderNumber);
      cartItem.setOrderProductCategory(product.getProductCategory());
      cartItem.setOrderCount(quantity);
      cartItem.setOrderStatus(true);
      cartItem.setOrderCalorie((product.getProductCalorie() + friesCalorie + drinkCalorie) * quantity);
      System.out.println((product.getProductCalorie() + friesCalorie + drinkCalorie) * quantity);
      cartItem.setOrderPrice(product.getProductPrice() + friesPrice + drinkPrice);
      cartItems.add(cartItem);
    }

    totalPrice += (product.getProductPrice() + friesPrice + drinkPrice) * quantity;
    updateCartLabel(); // 장바구니레이블 갱신
    updateCartDisplay(); // 장바구니 UI 갱신
    updatePayButtonState(); // 버튼 상태 갱신
  }

  public void updateCartDisplay() {
    cartPanel.removeAll(); // 기존 내용 제거

    // 수평 정렬을 위한 패널 생성
    JPanel horizontalCartPanel = new JPanel();
    horizontalCartPanel.setBackground(Color.WHITE);
    horizontalCartPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0)); // 수평 정렬
    horizontalCartPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // 장바구니 여백 설정

    // 장바구니 항목 추가
    for (CartItem item : cartItems) {
      // 이미지와 X버튼을 겹치게 할 레이어드 패널 생성
      JLayeredPane itemPanel = new JLayeredPane();

      itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS)); // 수직 정렬
      itemPanel.setPreferredSize(new Dimension(130, 180)); // 고정 크기
      itemPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // 테두리 설정
      itemPanel.setBackground(Color.WHITE);

      // 상품 이미지
      JLabel itemImage = new JLabel();
      try {
        String imagePath = "/kiosk/static/product" + item.getProductIdx() + ".jpg";
        ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
        Image scaledImage = icon.getImage().getScaledInstance(100, 80, Image.SCALE_SMOOTH);
        itemImage.setIcon(new ImageIcon(scaledImage));
      } catch (NullPointerException e) {
        itemImage.setText("이미지 없음");
      }
      itemImage.setAlignmentX(Component.CENTER_ALIGNMENT); // 중앙 정렬

      JPanel topPanel = new JPanel();
      topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
      topPanel.setOpaque(false);
      topPanel.setPreferredSize(new Dimension(125,40));

      // 수량을 표시하는 동그라미
      JLabel quantityCircle = new JLabel(String.valueOf(item.getOrderCount()), JLabel.CENTER) {
        @Override
        protected void paintComponent(Graphics g) {
          Graphics2D g2d = (Graphics2D) g;
          g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
          g2d.setColor(Color.GREEN); // 동그라미 색상
          g2d.fillOval(0, 0, 30, 30); // 동그라미 그리기
          g2d.setColor(Color.WHITE); // 숫자 색상
          g2d.drawString(getText(), 30 / 2 - g.getFontMetrics().stringWidth(getText()) / 2, 30 / 2 + g.getFontMetrics().getAscent() / 3);
        }
      };
      quantityCircle.setPreferredSize(new Dimension(30, 30));
      quantityCircle.setFont(new Font("맑은 고딕", Font.BOLD, 14)); // 텍스트 폰트

      // 상품 이름
      JLabel itemNameLabel = new JLabel(item.getProductName());
      itemNameLabel.setFont(new Font("맑은 고딕", Font.BOLD, 12));
      itemNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // 중앙 정렬

      // 수량
      //JLabel itemQuantityLabel = new JLabel("수량: " + item.getOrderCount());
      //itemQuantityLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
      //itemQuantityLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // 중앙 정렬

      // 상품 가격
      JLabel itemPriceLabel = new JLabel("₩" + CurrencyFormatter.format(item.getOrderPrice() * item.getOrderCount()));
      itemPriceLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
      itemPriceLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // 중앙 정렬

      // 상품 이미지 에서 x버튼
      JButton removeButton = new JButton();
      try {
        // 이미지 경로
        String imagePath = "/kiosk/static/xbtn.png";

        // 원본 이미지 로드
        ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));

        // 새로운 크기로 이미지 스케일링
        int newWidth = 30; // 원하는 폭 (가로)
        int newHeight = 30; // 원하는 높이 (세로)
        Image scaledImage = icon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

        // 조정된 이미지를 JLabel에 추가
        removeButton.setIcon(new ImageIcon(scaledImage));

        // 버튼 스타일 설정
        removeButton.setBorderPainted(false);
        removeButton.setContentAreaFilled(false);
        removeButton.setFocusPainted(false);
      } catch (NullPointerException e) {
        e.printStackTrace();
      }

      // 삭제 버튼
      //removeButton.setAlignmentX(Component.CENTER_ALIGNMENT); // 중앙 정렬
      //removeButton.setBounds(120, 5, 20, 20); // 위치를 오른쪽 위로 설정

      topPanel.add(quantityCircle);
      topPanel.add(Box.createHorizontalStrut(35)); // 가운데 유동적 공간
      topPanel.add(removeButton);

      removeButton.addActionListener(e -> {
        cartItems.remove(item); // 장바구니 항목 삭제
        totalPrice -= item.getOrderPrice() * item.getOrderCount(); // 총 가격 업데이트
        updateCartLabel(); // 장바구니레이블 갱신
        updateCartDisplay(); // 화면 갱신
        updatePayButtonState(); // 버튼 상태 갱신
      });

      //itemPanel.add(removeButton, JLayeredPane.PALETTE_LAYER);
      itemPanel.add(topPanel, BorderLayout.NORTH);

      // 아이템 패널에 요소 추가
      //itemPanel.add(Box.createVerticalGlue()); // 위 여백
      itemPanel.add(itemImage);
      itemPanel.add(Box.createVerticalStrut(6)); // 간격 추가
      itemPanel.add(itemNameLabel);
      itemPanel.add(Box.createVerticalStrut(6)); // 간격 추가
      //itemPanel.add(itemQuantityLabel); // 수량 표시
      //itemPanel.add(Box.createVerticalStrut(0)); // 간격 추가
      itemPanel.add(itemPriceLabel);
      //itemPanel.add(Box.createVerticalStrut(0)); // 간격 추가
      itemPanel.add(Box.createVerticalGlue()); // 아래 여백

      horizontalCartPanel.add(itemPanel);
    }

    // 스크롤 패널 설정
    JScrollPane scrollPane = new JScrollPane(horizontalCartPanel);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS); // 수평 스크롤 활성화
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER); // 수직 스크롤 비활성화
    scrollPane.setBorder(BorderFactory.createEmptyBorder()); // 스크롤 패널 테두리 제거

    cartPanel.add(scrollPane);

    totalPriceLabel.setText("총 가격: ₩" + CurrencyFormatter.format(totalPrice)); // 총 가격 레이블 초기화

    cartPanel.revalidate();
    cartPanel.repaint();

    updatePayButtonState(); // 버튼 상태 갱신
  }

  public void clearCart() {

    if (cartItems == null) {
      cartItems = new ArrayList<>(); // cartItems가 null이면 새로 초기화
    } else {
      cartItems.clear(); // 기존 리스트 초기화
    }

    totalPrice = 0;
    updateCartLabel();
    updateCartDisplay();
    updatePayButtonState();
  }

  public void updateCartLabel() {
    int totalCartCount = 0;
    for (CartItem item : cartItems) {
      totalCartCount += item.getOrderCount();
    }
    cartLabel.setText("장바구니 수량: " + totalCartCount);
  }

  // 버튼 상태를 업데이트하는 메서드
  public void updatePayButtonState() {
    if (cartItems.size() == 0) {
      payButton.setEnabled(false);
    } else {
      payButton.setEnabled(true);
    }
  }
}

