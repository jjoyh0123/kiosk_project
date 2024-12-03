package kiosk.client;

import kiosk.clientVO.CartItem;
import kiosk.clientVO.CouponVO;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class PaymentPanel extends JPanel {
  private JLabel totalPriceLabel; // 총 주문 금액 표시
  private JLabel finalPriceLabel; // 할인 적용 후 주문 금액 표시
  private JLabel discountPriceLabel; // 할인 적용 금액 표시
  private JComboBox<String> couponBox; // 쿠폰 선택
  private JButton payWithCardButton, payWithKakaoButton, payWithAppleButton, paymentCancelButton; // 결제 수단 버튼
  private int totalPrice; // 원래 총 금액
  private int finalPrice; // 할인 적용 후 금액
  private int discountPrice;
  private List<CouponVO> couponList; // 사용 가능한 쿠폰 목록
  private SqlSessionFactory factory;
  private OrderPanel orderPanel;
  private MainFrame mainFrame;
  public int selectedCouponIdx;

  public PaymentPanel(MainFrame mainFrame, SqlSessionFactory factory, OrderPanel orderPanel) {

    this.factory = factory;
    this.mainFrame = mainFrame;
    this.totalPrice = orderPanel.totalPrice;
    this.finalPrice = orderPanel.totalPrice;
    this.orderPanel = orderPanel;
    this.setBackground(Color.WHITE);

    setLayout(new BorderLayout());

    // 상단: 제목
    JPanel titlePanel = new JPanel(new BorderLayout());
    titlePanel.setBackground(Color.WHITE);
    titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10)); // 상단 여백
    JLabel titleLabel = new JLabel("결제하기", JLabel.CENTER);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
    titlePanel.setBorder(BorderFactory.createEmptyBorder(80, 0, 0, 0));
    titlePanel.add(titleLabel, BorderLayout.CENTER);
    add(titlePanel, BorderLayout.NORTH);

    // 중앙: 결제 옵션 및 쿠폰
    JPanel centerPanel = new JPanel(new GridLayout(3, 1, 10, 10));
    centerPanel.setBackground(Color.WHITE);

    // 쿠폰 선택
    JPanel couponPanel = new JPanel(new BorderLayout());
    couponPanel.setBackground(Color.WHITE);
    JLabel couponLabel = new JLabel("쿠폰 적용: ");
    couponLabel.setFont(new Font("Arial", Font.BOLD, 27));
    couponLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
    couponPanel.add(couponLabel, BorderLayout.WEST);
    couponBox = new JComboBox<>();
    Dimension size = new Dimension(300, 500);
    couponBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
    couponBox.setPreferredSize(size);
    couponBox.setMaximumSize(size);
    couponBox.setMinimumSize(size);
    couponBox.setFont(new Font("Arial", Font.PLAIN, 20)); // 폰트 크기 조정
    couponBox.setBackground(Color.WHITE);
    couponBox.addItem("쿠폰을 선택하세요");

    // PaymentPanel에서 사용
    if (mainFrame.userVO != null) {

      List<Integer> productIdxList = new ArrayList<>();

      // 주문 항목의 productIdx를 가져와서 해당 productIdx에 맞는 쿠폰을 가져옵니다.
      for (CartItem cartItem : orderPanel.cartItems) {
        // 가져온 쿠폰을 콤보박스에 추가합니다.
        if (cartItem.getOrderCount() == 1)
          productIdxList.add(cartItem.getProductIdx());
      }
      // getCouponData 메서드를 호출하여 해당 productIdx에 맞는 쿠폰을 가져옵니다.
      getCouponData(mainFrame.userVO.getUserIdx(), productIdxList);

      List<CouponVO> filteredCouponList = new ArrayList<>();

      for (CouponVO coupon : couponList) {
        boolean chk = false;

        if (!coupon.isCouponStatus()) { // status가 false일 경우만
          for (CouponVO filterCoupon : filteredCouponList) {
            if (filterCoupon.getCouponName().equals(coupon.getCouponName())) {
              chk = true;
              // 동일한 이름의 쿠폰 중 만료일 비교
              if (coupon.getCouponExpDate().isBefore(filterCoupon.getCouponExpDate())) {
                // 새로운 쿠폰이 더 빠른 만료일을 가질 경우 교체
                filteredCouponList.remove(filterCoupon);
                filteredCouponList.add(coupon);
              }
              break; // 이미 처리한 쿠폰은 더 이상 비교하지 않음
            }
          }

          if (!chk) {
            // 중복이 아닌 경우 필터링된 리스트에 추가
            filteredCouponList.add(coupon);
          }
        }

      }
      // 콤보박스에 추가
      for (CouponVO coupon : filteredCouponList) {
        couponBox.addItem(coupon.getCouponName());
      }
    }

    couponBox.addActionListener(e -> applyCoupon());
    couponPanel.add(couponBox, BorderLayout.CENTER);
    centerPanel.add(couponPanel);


    // 결제 수단 버튼
    JPanel paymentPanel = new JPanel(new GridLayout(1, 3, 10, 10));
    paymentPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
    paymentPanel.setBackground(Color.WHITE);
    payWithCardButton = new JButton("신용카드/삼성페이");
    payWithKakaoButton = new JButton("카카오페이");
    payWithAppleButton = new JButton("애플페이");
    paymentCancelButton = new JButton("주문취소");

    payWithCardButton.addActionListener(e -> processPayment("신용카드/삼성페이"));
    payWithKakaoButton.addActionListener(e -> processPayment("카카오페이"));
    payWithAppleButton.addActionListener(e -> processPayment("애플페이"));

    paymentPanel.add(payWithCardButton);
    paymentPanel.add(payWithKakaoButton);
    paymentPanel.add(payWithAppleButton);

    centerPanel.add(paymentPanel);

    add(centerPanel, BorderLayout.CENTER);

    // 하단: 최종 금액
    JPanel bottomPanel = new JPanel();
    bottomPanel.setBackground(Color.WHITE);
    bottomPanel.setLayout(new GridLayout(3, 1));

    JPanel totalAndDiscountPanel = new JPanel(new BorderLayout(10, 10));
    totalAndDiscountPanel.setBackground(Color.WHITE);
    totalPriceLabel = new JLabel("총 결제 금액: ₩" + finalPrice, JLabel.LEFT);
    totalPriceLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
    totalPriceLabel.setFont(new Font("Arial", Font.BOLD, 20));
    discountPriceLabel = new JLabel("총 할인 금액: ₩" + discountPrice, JLabel.RIGHT);
    discountPriceLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
    discountPriceLabel.setFont(new Font("Arial", Font.BOLD, 20));
    totalAndDiscountPanel.add(totalPriceLabel, BorderLayout.WEST);
    totalAndDiscountPanel.add(discountPriceLabel, BorderLayout.EAST);
    bottomPanel.add(totalAndDiscountPanel);

    JPanel finalPricePanel = new JPanel(new BorderLayout(10, 10));
    finalPricePanel.setBackground(Color.WHITE);
    finalPriceLabel = new JLabel("최종 결제 금액: ₩" + finalPrice, JLabel.CENTER);
    finalPricePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
    finalPriceLabel.setFont(new Font("Arial", Font.BOLD, 20));
    finalPricePanel.add(finalPriceLabel, BorderLayout.EAST);
    bottomPanel.add(finalPricePanel);

    JPanel cancelButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
    cancelButtonPanel.setBackground(Color.WHITE);
    paymentCancelButton = new JButton("주문취소");
    paymentCancelButton.setPreferredSize(new Dimension(500, 50));
    paymentCancelButton.setFont(new Font("Arial", Font.BOLD, 18));
    paymentCancelButton.addActionListener(e -> {
      // 커스텀 JDialog 생성
      JDialog dialog = new JDialog((Frame) null, "전체 취소 확인", true); // 모달 Dialog
      dialog.setUndecorated(true); // 창 테두리 제거
      dialog.setBackground(Color.WHITE);
      dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE); // 창 닫기 버튼 비활성화
      dialog.setSize(400, 400);
      dialog.setLocationRelativeTo(null); // 화면 중앙에 표시

      // 메시지 패널
      JPanel messagePanel = new JPanel(new BorderLayout());
      messagePanel.setBackground(Color.WHITE);
      JLabel messageLabel = new JLabel("<html>입력하신 모든 내용이 취소됩니다.<br>전체 취소하시겠습니까?</html>", JLabel.CENTER);
      messageLabel.setFont(new Font("Arial", Font.PLAIN, 30));
      messagePanel.add(messageLabel, BorderLayout.CENTER);

      // 버튼 패널
      JPanel buttonPanel = new JPanel(new FlowLayout());
      buttonPanel.setBackground(Color.WHITE);
      JButton yesButton = new JButton("예");
      JButton noButton = new JButton("아니요");

      yesButton.setPreferredSize(new Dimension(190, 40)); // 버튼 크기 조정
      noButton.setPreferredSize(new Dimension(190, 40)); // 버튼 크기 조정

      // "예" 버튼 동작
      yesButton.addActionListener(event -> {
        setNullCoupon(orderPanel.cartItems, couponList); // 쿠폰 초기화
        mainFrame.switchToOrderDetailsScreen(orderPanel); // 주문 화면으로 전환
        dialog.dispose(); // Dialog 닫기
      });

      // "아니요" 버튼 동작
      noButton.addActionListener(event -> dialog.dispose()); // Dialog 닫기

      buttonPanel.add(yesButton);
      buttonPanel.add(noButton);

      // Dialog에 컴포넌트 추가
      dialog.add(messagePanel, BorderLayout.CENTER);
      dialog.add(buttonPanel, BorderLayout.SOUTH);

      // Dialog 표시
      dialog.setVisible(true);
    });
    cancelButtonPanel.add(paymentCancelButton);
    bottomPanel.add(cancelButtonPanel);
    add(bottomPanel, BorderLayout.SOUTH);
  }

  // 쿠폰 적용 로직
  private void applyCoupon() {
    setNullCoupon(orderPanel.cartItems, couponList);
    int selectedIndex = couponBox.getSelectedIndex() - 1; // 쿠폰 리스트의 첫 번째 항목은 "쿠폰을 선택하세요"와 같은 안내 문구이기 때문에, 유효한 쿠폰 리스트 인덱스를 맞추기 위해 -1

    // 쿠폰이 선택되지 않은 경우
    if (selectedIndex < 0 || couponBox.getSelectedIndex() == 0) {
      finalPrice = totalPrice;
      discountPrice = 0; // 할인 금액 초기화

      // CartItem의 orderCouponApplyPrice를 finalPrice로 설정
      for (CartItem cartItem : orderPanel.cartItems) {
        cartItem.setOrderCouponApplyPrice(cartItem.getOrderPrice()); // 원래 가격 그대로 유지
        System.out.println("쿠폰선택안됨");
      }

    } else {
      System.out.println(couponList.size());
      System.out.println(selectedIndex);

      CouponVO selectedCoupon = couponList.get(selectedIndex); // 선택된 쿠폰의 정보 couponList에서 가져오기
      selectedCouponIdx = selectedCoupon.getCouponIdx();
      selectedCoupon.setCouponStatus(true);

      for (CartItem cartItem : orderPanel.cartItems) {
        if (cartItem.getProductIdx() == selectedCoupon.getProductIdx()) {
          if (selectedCoupon.getCouponRate() > 0 && cartItem.getOrderCount() == 1) {
            // 할인율 계산
            discountPrice = (int) (cartItem.getOrderPrice() * (selectedCoupon.getCouponRate() / 100.0));
            cartItem.setOrderCouponApplyPrice(cartItem.getOrderPrice() - discountPrice); // carItem에 적용
            //System.out.println("할인적용된가격:" + (totalPrice - discountPrice));
          } else if (selectedCoupon.getCouponFixed() > 0) {
            // 고정 금액 할인
            discountPrice = selectedCoupon.getCouponFixed();
            cartItem.setOrderCouponApplyPrice(cartItem.getOrderPrice() - discountPrice); // carItem에 적용
            //System.out.println("할인적용된가격:" + (totalPrice - discountPrice));
          }
          // 쿠폰 idx cartItem 에 적용
          cartItem.setCouponIdx(selectedCoupon.getCouponIdx());

          // 쿠폰 상태를 true로 업데이트

        }
      }
      // 최종 금액 계산
      finalPrice = totalPrice - discountPrice;
    }

    // 화면에 업데이트
    totalPriceLabel.setText("총 결제 금액: ₩" + finalPrice);
    discountPriceLabel.setText("총 할인 금액: ₩" + discountPrice);
    finalPriceLabel.setText("최종결제금액: ₩" + finalPrice);
  }


  // 결제 처리
  private void processPayment(String MethodOfPayment) {
    // PaymentDialog 호출

    if (selectedCouponIdx == 0) { // 쿠폰 선택이 안된 경우 원래 가격 유지 (콤보박스를 아예 안클릭한 경우를 위해 여기서 선언)
      for (CartItem cartItem : orderPanel.cartItems) {
        cartItem.setOrderCouponApplyPrice(cartItem.getOrderPrice()); // 원래 가격 그대로 유지
        System.out.println("쿠폰선택안됨");
      }
    }

    PaymentDialog dialog = new PaymentDialog(mainFrame, orderPanel, MethodOfPayment, orderPanel.cartItems, selectedCouponIdx, factory);
  }

  // MyBatis를 통해 상품 데이터를 가져옴
  private void getCouponData(int userIdx, List<Integer> productIdxList) {
    SqlSession ss;
    try {
      System.out.println("userIdx" + userIdx);
      System.out.println("productIdxList size" + productIdxList.size());
      ss = factory.openSession();
      // Map에 파라미터 추가
      Map<String, Object> map = new HashMap<>();
      map.put("userIdx", userIdx);
      map.put("productIdxList", productIdxList);

      System.out.println(userIdx);
      System.out.println(productIdxList);
      System.out.println(map);
      // list에 map을 기반으로 저장
      couponList = ss.selectList("client.getCouponsByUserIdxAndProductIdx", map);
      System.out.println(couponList);
      ss.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void setNullCoupon(List<CartItem> cartItems, List<CouponVO> couponList) {
    if (cartItems != null) { // null인지 체크
      for (CartItem cartItem : cartItems) {
        cartItem.setCouponIdx(null);
        cartItem.setOrderCouponApplyPrice(cartItem.getOrderPrice());
      }
    }
    if (couponList != null) { // null인지 체크
      for (CouponVO couponVO : couponList) {
        couponVO.setCouponStatus(false);
      }
    }
  }
}