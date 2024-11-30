package kiosk.client;

import kiosk.clientVO.CartItem;
import kiosk.clientVO.OptionVO;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class OptionDialog extends JDialog {
    private int quantity = 1; // 기본 수량
    private int basePrice;
    private int totalPrice;
    private JLabel quantityLabel;
    private JLabel totalPriceLabel;
    private ButtonGroup friesGroup;
    private ButtonGroup drinkGroup;
    private int selectedFriesIdx; // 선택된 프라이 옵션
    private int selectedDrinkIdx; // 선택된 음료 옵션
    private int friesPrice = 0; // 프라이 옵션 가격
    private int drinkPrice = 0; // 음료 옵션 가격
    private int friesCalorie = 0;
    private int drinkCalorie = 0;

    public OptionDialog(Frame parent, String productName, int productIdx, int productPrice, List<OptionVO> friesOptions, List<OptionVO> drinkOptions) {
        super(parent, "추가 옵션을 선택해주세요!", true);

        basePrice = productPrice;
        totalPrice = basePrice;

        setLayout(new BorderLayout());
        setSize(500, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        // 상단: 상품 정보
        JPanel productInfoPanel = new JPanel(new BorderLayout());
        productInfoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 동적으로 이미지 설정
        JLabel productImage = new JLabel();
        try {
            String imagePath = "/kiosk/static/product" + productIdx + ".jpg";
            ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
            Image scaledImage = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            productImage.setIcon(new ImageIcon(scaledImage));
        } catch (NullPointerException e) {
            // 기본 이미지 제공
            productImage.setIcon(new ImageIcon(new ImageIcon("default_image_path.jpg").getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
            System.err.println("이미지 로드 실패: /kiosk/static/product" + productIdx + ".jpg");
        }
        productImage.setPreferredSize(new Dimension(150, 150));
        productInfoPanel.add(productImage, BorderLayout.WEST);

        JPanel productDetails = new JPanel(new GridLayout(3, 1));
        productDetails.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel nameLabel = new JLabel(productName);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        JLabel priceLabel = new JLabel("₩" + productPrice);
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        productDetails.add(nameLabel);
        productDetails.add(priceLabel);
        productInfoPanel.add(productDetails, BorderLayout.CENTER);

        add(productInfoPanel, BorderLayout.NORTH);

        // 중앙: 옵션 선택
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS)); // BoxLayout을 사용하여 동적 레이아웃 적용
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 프라이 옵션
        JPanel friesContainer = new JPanel();
        friesContainer.setLayout(new BoxLayout(friesContainer, BoxLayout.Y_AXIS)); // 세로 정렬
        friesContainer.setBorder(BorderFactory.createTitledBorder("프라이 선택"));
        friesGroup = new ButtonGroup();

        for (OptionVO option : friesOptions) {
            JRadioButton radioButton = new JRadioButton(option.getOptionName() + " - ₩" + option.getOptionPrice());
            radioButton.addActionListener(e -> {
                selectedFriesIdx = option.getOptionIdx();
                friesPrice = option.getOptionPrice();
                friesCalorie = option.getOptionCalorie();
                updateTotalPrice();
            });
            friesGroup.add(radioButton);
            friesContainer.add(radioButton);
        }

        JScrollPane friesScrollPane = new JScrollPane(friesContainer);
        friesScrollPane.setPreferredSize(new Dimension(400, 100)); // 스크롤 높이 제한
        friesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        friesScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        optionsPanel.add(friesScrollPane);

        // 음료 옵션
        JPanel drinkContainer = new JPanel();
        drinkContainer.setLayout(new BoxLayout(drinkContainer, BoxLayout.Y_AXIS)); // 세로 정렬
        drinkContainer.setBorder(BorderFactory.createTitledBorder("음료 선택"));
        drinkGroup = new ButtonGroup();

        for (OptionVO option : drinkOptions) {
            JRadioButton radioButton = new JRadioButton(option.getOptionName() + " - ₩" + option.getOptionPrice());
            radioButton.addActionListener(e -> {
                selectedDrinkIdx = option.getOptionIdx();
                drinkPrice = option.getOptionPrice();
                drinkCalorie = option.getOptionCalorie();
                updateTotalPrice();
            });
            drinkGroup.add(radioButton);
            drinkContainer.add(radioButton);
        }

        JScrollPane drinkScrollPane = new JScrollPane(drinkContainer);
        drinkScrollPane.setPreferredSize(new Dimension(400, 100)); // 스크롤 높이 제한
        drinkScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        drinkScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        optionsPanel.add(drinkScrollPane);

        add(optionsPanel, BorderLayout.CENTER);

        // 하단: 수량 선택 및 확인/취소 버튼
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel quantityPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton minusButton = new JButton("-");
        minusButton.addActionListener(e -> {
            if (quantity > 1) {
                quantity--;
                updateTotalPrice();
            }
        });
        quantityLabel = new JLabel(String.valueOf(quantity));
        quantityLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JButton plusButton = new JButton("+");
        plusButton.addActionListener(e -> {
            quantity++;
            updateTotalPrice();
        });
        quantityPanel.add(minusButton);
        quantityPanel.add(quantityLabel);
        quantityPanel.add(plusButton);

        totalPriceLabel = new JLabel("총 가격: ₩" + totalPrice);
        totalPriceLabel.setFont(new Font("Arial", Font.BOLD, 18));
        totalPriceLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        bottomPanel.add(quantityPanel, BorderLayout.WEST);
        bottomPanel.add(totalPriceLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton confirmButton = new JButton("확인");
        confirmButton.addActionListener(e -> {
            if (validateSelection()) {

                dispose();

            } else {
                JOptionPane.showMessageDialog(null, "모든 옵션을 선택해주세요!", "경고", JOptionPane.WARNING_MESSAGE);
            }
        });
        JButton cancelButton = new JButton("취소");
        cancelButton.addActionListener(e -> {
            selectedFriesIdx = 0;
            selectedDrinkIdx = 0;
            dispose();
        });
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);

        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void updateTotalPrice() {
        totalPrice = (basePrice + friesPrice + drinkPrice) * quantity;
        totalPriceLabel.setText("총 가격: ₩" + totalPrice);
        quantityLabel.setText(String.valueOf(quantity));
    }

    private boolean validateSelection() {
        return friesGroup.getSelection() != null && drinkGroup.getSelection() != null;
    }

    public int getSelectedFriesIdx() {
        return selectedFriesIdx;
    }

    public int getSelectedDrinkIdx() {
        return selectedDrinkIdx;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getFriesPrice() {
        return friesPrice;
    }

    public int getDrinkPrice() {
        return drinkPrice;
    }

    public int getFriesCalorie() {
        return friesCalorie;
    }

    public int getDrinkCalorie() {
        return drinkCalorie;
    }
}
