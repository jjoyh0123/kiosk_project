package kiosk.client;

import kiosk.clientVO.CartItem;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class OrderDetailsPanel extends JPanel {
    private List<CartItem> cartItems;
    private JLabel totalPriceLabel;
    private OrderPanel orderPanel;
    private MainFrame mainFrame;
    JPanel itemListPanel, bottomPanel, itemPanel;
    private JButton payButton; // 결제하기 버튼

    public OrderDetailsPanel(MainFrame mainFrame, OrderPanel orderPanel) {

        this.mainFrame = mainFrame;
        this.orderPanel = orderPanel;

        setLayout(new BorderLayout());

        // 상단 제목
        JLabel titleLabel = new JLabel("주문내역을 확인해 주세요.", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 25));
        titleLabel.setPreferredSize(new Dimension(400, 100)); // 높이를 설정
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0)); // 상하 여백 설정
        add(titleLabel, BorderLayout.NORTH);

        // 중앙: 주문 내역 리스트
        itemListPanel = new JPanel();
        itemListPanel.setLayout(new BoxLayout(itemListPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(itemListPanel);
        add(scrollPane, BorderLayout.CENTER);

        // 하단: 총 금액 및 버튼들
        bottomPanel = new JPanel(new BorderLayout());
        totalPriceLabel = new JLabel("", JLabel.RIGHT);
        totalPriceLabel.setFont(new Font("Arial", Font.BOLD, 25));
        bottomPanel.add(totalPriceLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton backButton = new JButton("이전으로");
        backButton.setPreferredSize(new Dimension(150, 50));
        payButton = new JButton("결제하기");
        payButton.setPreferredSize(new Dimension(150, 50));

        backButton.addActionListener(e -> {
            updateTotalPrice();
            refreshItemList();
            orderPanel.updateCartLabel(); // 장바구니레이블 갱신
            orderPanel.updateCartDisplay();
            mainFrame.switchToOrderScreen();
        });
        payButton.addActionListener(e ->
                mainFrame.switchToPaymentScreen(orderPanel));

        buttonPanel.add(backButton);
        buttonPanel.add(payButton);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        refreshItemList();
        updateTotalPrice();
        updatePayButtonState();
    }

    public void updateTotalPrice() {
        int total = orderPanel.cartItems.stream()
                .mapToInt(item -> item.getOrderPrice() * item.getOrderCount())
                .sum();

        orderPanel.totalPrice = total;
        totalPriceLabel.setText("총 주문 금액: ₩" + total);
    }

    public void refreshItemList() {
        itemListPanel.removeAll(); // 기존 항목 제거
        itemListPanel.setLayout(new GridBagLayout()); // GridBagLayout 사용

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // 항목 간 간격 설정
        gbc.fill = GridBagConstraints.HORIZONTAL; // 수평으로만 확장
        gbc.weightx = 1.0; // 수평 비율
        gbc.weighty = 0.0; // 수직 비율 (항목 고정 크기)
        gbc.anchor = GridBagConstraints.NORTH; // 항목이 상단에 붙도록 설정
        gbc.gridwidth = GridBagConstraints.REMAINDER; // 한 줄에 하나씩 배치

        int fixedHeight = 100; // 고정 높이
        int itemCount = 0;

        // 주문 항목 추가
        for (CartItem item : orderPanel.cartItems) {
            if (item.getOrderCount() <= 0) continue;

            JPanel itemPanel = new JPanel(new GridBagLayout());
            itemPanel.setPreferredSize(new Dimension(400, fixedHeight)); // 수평 길이 설정
            itemPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

            GridBagConstraints innerGbc = new GridBagConstraints();
            innerGbc.insets = new Insets(5, 5, 5, 5);
            innerGbc.fill = GridBagConstraints.BOTH; // 채우기
            innerGbc.weightx = 0.0;

            // 이미지
            innerGbc.gridx = 0;
            innerGbc.gridy = 0;
            innerGbc.weightx = 0.2;
            JLabel imageLabel = new JLabel();
            try {
                Image productImage = new ImageIcon(getClass().getResource("/kiosk/static/product" + item.getProductIdx() + ".jpg")).getImage();
                Image scaledImage = productImage.getScaledInstance(70, 50, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImage));
            } catch (Exception e) {
                System.out.println("이미지를 불러오는 중 오류 발생: " + e.getMessage());
            }
            itemPanel.add(imageLabel, innerGbc);

            // 이름
            innerGbc.gridx = 1;
            innerGbc.weightx = 0.3;
            innerGbc.anchor = GridBagConstraints.WEST;
            JTextArea nameLabel = new JTextArea(item.getProductName());
            nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
            nameLabel.setLineWrap(true); // 줄 바꿈 허용
            nameLabel.setWrapStyleWord(true); // 단어 단위로 줄 바꿈
            nameLabel.setOpaque(false); // 배경 투명
            nameLabel.setEditable(false); // 편집 불가
            nameLabel.setFocusable(false); // 포커스 제거
            itemPanel.add(nameLabel, innerGbc);

            // 수량 조절
            innerGbc.gridx = 2;
            innerGbc.weightx = 0.2;
            innerGbc.anchor = GridBagConstraints.CENTER;
            JPanel quantityPanel = new JPanel(new FlowLayout());
            JButton decreaseButton = new JButton("-");
            decreaseButton.setPreferredSize(new Dimension(30, 30));
            JLabel quantityLabel = new JLabel(String.valueOf(item.getOrderCount()), JLabel.CENTER);
            JButton increaseButton = new JButton("+");
            increaseButton.setPreferredSize(new Dimension(30, 30));

            decreaseButton.addActionListener(e -> {
                item.setOrderCount(item.getOrderCount() - 1);
                if (item.getOrderCount() <= 0) {
                    orderPanel.cartItems.remove(item);
                }
                refreshItemList();
            });

            increaseButton.addActionListener(e -> {
                item.setOrderCount(item.getOrderCount() + 1);
                refreshItemList();
            });

            quantityPanel.add(decreaseButton);
            quantityPanel.add(quantityLabel);
            quantityPanel.add(increaseButton);
            itemPanel.add(quantityPanel, innerGbc);

            // 금액 표시
            innerGbc.gridx = 3;
            innerGbc.weightx = 0.2;
            innerGbc.anchor = GridBagConstraints.EAST;
            JLabel priceLabel = new JLabel("₩" + (item.getOrderPrice() * item.getOrderCount()), JLabel.CENTER);
            priceLabel.setFont(new Font("Arial", Font.BOLD, 14));
            itemPanel.add(priceLabel, innerGbc);

            // 삭제 버튼
            innerGbc.gridx = 4;
            innerGbc.weightx = 0.1;
            innerGbc.anchor = GridBagConstraints.CENTER;
            JButton deleteButton = new JButton("X");
            deleteButton.setPreferredSize(new Dimension(50, 50));
            deleteButton.addActionListener(e -> {
                orderPanel.cartItems.remove(item);
                refreshItemList();
            });
            itemPanel.add(deleteButton, innerGbc);

            // GridBagConstraints로 추가
            gbc.gridy = itemCount++;
            itemListPanel.add(itemPanel, gbc);
        }

        // 남은 공간을 채우는 빈 패널 추가
        gbc.weighty = 1.0; // 남은 공간을 차지하도록 설정
        gbc.fill = GridBagConstraints.BOTH;
        JPanel spacerPanel = new JPanel();
        itemListPanel.add(spacerPanel, gbc);

        // 업데이트
        updateTotalPrice();
        updatePayButtonState();
        itemListPanel.revalidate();
        itemListPanel.repaint();
    }



    // 버튼 상태를 업데이트하는 메서드
    public void updatePayButtonState() {
        if (orderPanel.cartItems.size() == 0) {
            payButton.setEnabled(false);
        } else {
            payButton.setEnabled(true);
        }
    }
}
