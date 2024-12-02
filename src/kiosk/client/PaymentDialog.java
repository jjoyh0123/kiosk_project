package kiosk.client;

import kiosk.clientVO.CartItem;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class PaymentDialog extends JDialog {
    private JLabel titleLabel;  // 다이얼로그 제목
    private JButton confirmButton, cancelButton;  // 확인/취소 버튼
    private List<CartItem> cartItems;  // CartItem 리스트
    private SqlSessionFactory factory;
    private OrderPanel orderPanel;
    private MainFrame mainFrame;
    private int selectedCouponIdx = 0;

    public PaymentDialog(MainFrame mainFrame, OrderPanel orderPanel, String paymentMethod, List<CartItem> cartItems, int selectedCouponIdx, SqlSessionFactory factory) {

        super(mainFrame, "결제", true);
        this.mainFrame = mainFrame;
        this.cartItems = cartItems;
        this.orderPanel = orderPanel;
        this.selectedCouponIdx = selectedCouponIdx;
        this.factory = factory;
        this.setBackground(Color.WHITE);
        this.setUndecorated(true); // 창 테두리 제거

        setLayout(new BorderLayout());
        setSize(500, 580);
        setLocationRelativeTo(null);

        // 상단: 결제 방식
        JPanel northPanel = new JPanel();
        northPanel.setBackground(Color.WHITE);
        titleLabel = new JLabel(paymentMethod, SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20,0,0,0));
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBackground(Color.WHITE);
        northPanel.add(titleLabel);
        this.add(northPanel, BorderLayout.NORTH);

        // 중앙: 안내 메시지
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        JLabel messageLabel = new JLabel("바코드를 리더기에 인식시켜주세요", SwingConstants.CENTER);
        messageLabel.setBorder(BorderFactory.createEmptyBorder(20,0,0,0));
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        centerPanel.add(messageLabel, BorderLayout.NORTH);

        // 상품 이미지
        JLabel barcodeScannerImage = new JLabel();
        try {
            // 이미지 경로
            String imagePath = "/kiosk/static/barcode.png";

            // 원본 이미지 로드
            ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));

            // 새로운 크기로 이미지 스케일링
            int newWidth = 300; // 원하는 폭 (가로)
            int newHeight = 300; // 원하는 높이 (세로)
            Image scaledImage = icon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

            // 조정된 이미지를 JLabel에 추가
            barcodeScannerImage.setIcon(new ImageIcon(scaledImage));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        barcodeScannerImage.setHorizontalAlignment(SwingConstants.CENTER);
        centerPanel.add(barcodeScannerImage, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // 하단: 확인/취소 버튼
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        confirmButton = new JButton("확인");
        confirmButton.setPreferredSize(new Dimension(200, 40));
        confirmButton.addActionListener(e -> handleConfirm());

        cancelButton = new JButton("취소");
        cancelButton.setPreferredSize(new Dimension(200, 40));
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // 확인 버튼 처리
    private void handleConfirm() {
        try (SqlSession session = factory.openSession()) {
            for (CartItem cartItem : cartItems) {
                session.insert("client.insertCartItemToOrderTable", cartItem);  // order 테이블에 주문 정보 저장
            }
            session.commit();
            JOptionPane.showMessageDialog(this, "결제가 완료되었습니다!", "성공", JOptionPane.INFORMATION_MESSAGE);
            mainFrame.userVO = null;
            orderPanel.cartItems = null;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "결제 처리 중 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        if (selectedCouponIdx > 0) {
            System.out.printf("쿠폰인덱스번호 : %d", selectedCouponIdx);
            try {
                SqlSession ss = factory.openSession();
                ss.update("client.updateCouponVO", selectedCouponIdx);
                ss.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mainFrame.orderNumber++; // 주문번호 증가
        orderPanel.clearCart();
        mainFrame.switchToMainMenu();
        dispose();

    }
}

