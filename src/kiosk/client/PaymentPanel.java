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
    private JButton payWithCardButton, payWithKakaoButton, payWithAppleButton; // 결제 수단 버튼
    private int totalPrice; // 원래 총 금액
    private int finalPrice; // 할인 적용 후 금액
    private int discountPrice;
    private List<CouponVO> couponList; // 사용 가능한 쿠폰 목록
    private SqlSessionFactory factory;
    private OrderPanel orderPanel;
    private MainFrame mainFrame;

    public PaymentPanel(MainFrame mainFrame, SqlSessionFactory factory, OrderPanel orderPanel) {

        this.factory = factory;
        this.mainFrame = mainFrame;
        this.totalPrice = orderPanel.totalPrice;
        this.finalPrice = orderPanel.totalPrice;
        this.orderPanel = orderPanel;

        setLayout(new BorderLayout());

        // 상단: 제목
        JLabel titleLabel = new JLabel("결제하기", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        // 중앙: 결제 옵션 및 쿠폰
        JPanel centerPanel = new JPanel(new GridLayout(3, 1, 10, 10));

        // 쿠폰 선택
        JPanel couponPanel = new JPanel();
        couponPanel.add(new JLabel("쿠폰 적용: "));
        couponBox = new JComboBox<>();
        couponBox.addItem("쿠폰을 선택하세요");

        // PaymentPanel에서 사용
        if (mainFrame.userVO != null) {

            List<Integer> productIdxList = new ArrayList<>();

            // 주문 항목의 productIdx를 가져와서 해당 productIdx에 맞는 쿠폰을 가져옵니다.
            for (CartItem cartItem : orderPanel.cartItems) {
                // 가져온 쿠폰을 콤보박스에 추가합니다.
                if (cartItem.getOrderCount()==1)
                    productIdxList.add(cartItem.getProductIdx());
            }
            // getCouponData 메서드를 호출하여 해당 productIdx에 맞는 쿠폰을 가져옵니다.
            getCouponData(mainFrame.userVO.getUserIdx(), productIdxList);

            for (CouponVO coupon : couponList) {
                couponBox.addItem(coupon.getCouponName());
            }
        }


        couponBox.addActionListener(e -> applyCoupon());
        couponPanel.add(couponBox);

        centerPanel.add(couponPanel);


        // 결제 수단 버튼
        JPanel paymentPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        payWithCardButton = new JButton("신용카드/삼성페이");
        payWithKakaoButton = new JButton("카카오페이");
        payWithAppleButton = new JButton("애플페이");

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
        totalPriceLabel = new JLabel("총 결제 금액: ₩" + finalPrice, JLabel.RIGHT);
        totalPriceLabel.setFont(new Font("Arial", Font.BOLD, 18));

        discountPriceLabel = new JLabel("총 할인 금액: ₩" + discountPrice, JLabel.RIGHT);
        discountPriceLabel.setFont(new Font("Arial", Font.BOLD, 18));

        finalPriceLabel = new JLabel("최종결제금액: ₩" + finalPrice, JLabel.RIGHT);
        finalPriceLabel.setFont(new Font("Arial", Font.BOLD, 18));

        bottomPanel.add(totalPriceLabel);
        bottomPanel.add(discountPriceLabel);
        bottomPanel.add(finalPriceLabel);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    // 쿠폰 적용 로직
    private void applyCoupon() {
        int selectedIndex = couponBox.getSelectedIndex() - 1; // 쿠폰 리스트의 첫 번째 항목은 "쿠폰을 선택하세요"와 같은 안내 문구이기 때문에, 유효한 쿠폰 리스트 인덱스를 맞추기 위해 -1
        int totalDiscountPrice = 0;

        // 쿠폰이 선택되지 않은 경우
        if (selectedIndex < 0) {
            finalPrice = totalPrice;
        } else {
            System.out.println(couponList.size());
            System.out.println(selectedIndex);
            CouponVO selectedCoupon = couponList.get(selectedIndex); // 선택된 쿠폰의 정보 couponList에서 가져오기

            for (CartItem cartItem : orderPanel.cartItems) {
                if (selectedCoupon.getCouponRate() > 0 && cartItem.getOrderCount() == 1 && cartItem.getProductIdx() == selectedCoupon.getProductIdx()) {
                    discountPrice = cartItem.getOrderPrice() * (selectedCoupon.getCouponRate()/100);
                    finalPrice = totalPrice - discountPrice;
                } else if (selectedCoupon.getCouponFixed() > 0) {
                    discountPrice = selectedCoupon.getCouponFixed();
                    finalPrice = totalPrice - discountPrice;
                }
            }
        }
        totalPriceLabel.setText("총 결제 금액: ₩" + finalPrice);
        discountPriceLabel.setText("총 할인 금액: ₩" + discountPrice);
        finalPriceLabel.setText("최종결제금액: ₩" + finalPrice);
    }

    // 결제 처리
    private void processPayment(String method) {
        JOptionPane.showMessageDialog(this, method + "으로 결제가 완료되었습니다.\n결제 금액: ₩" + finalPrice);
    }

    // MyBatis를 통해 상품 데이터를 가져옴
    private void getCouponData(int userIdx, List<Integer> productIdxList) {
        SqlSession ss;
        try {
            System.out.println("userIdx" + userIdx);
            System.out.println("productIdxList size" +productIdxList.size());
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
}