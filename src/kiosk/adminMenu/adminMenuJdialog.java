package kiosk.adminMenu;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import kiosk.config.productVO;

public class adminMenuJdialog extends JDialog {
    boolean productRecommendStatus;
    boolean productSaleStatus;
    JCheckBox recommendCheckBox;
    JCheckBox saleCheckBox;
    SqlSessionFactory factory;
    int productIdx; // Product ID를 저장

    public adminMenuJdialog(SqlSessionFactory factory, int productIdx, String productName, String imagePath, Window parent,
                            String productPrice, String productCategory, 
                            boolean productRecommendStatus, boolean productSaleStatus) {
        super(parent, "상품 상세", ModalityType.APPLICATION_MODAL);
        this.factory = factory;
        this.productIdx = productIdx; // productIdx 초기화
        this.productRecommendStatus = productRecommendStatus;
        this.productSaleStatus = productSaleStatus;

        // 디버깅 코드로 productIdx 확인
        System.out.println("Constructor - Product ID: " + this.productIdx);

        // 창 초기화
        setLayout(new BorderLayout());

        // 상품 이미지 로드
        ImageIcon imageIcon = new ImageIcon(getClass().getResource(imagePath));
        JLabel imageLabel = new JLabel(imageIcon);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // 상품 이름 레이블과 읽기 전용 텍스트 필드
        JLabel nameLabel = new JLabel("이름: ");
        JTextField nameTextField = new JTextField(productName);
        nameTextField.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        nameTextField.setBorder(BorderFactory.createEmptyBorder());
        nameTextField.setEditable(false); // 읽기 전용 설정

        // 상품 가격
        JLabel priceLabel = new JLabel("가격: ");
        JTextField priceTextField = new JTextField(productPrice);
        priceTextField.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        priceTextField.setBorder(BorderFactory.createEmptyBorder());
        priceTextField.setEditable(false); // 읽기 전용 설정

        // 카테고리
        JLabel categoryLabel = new JLabel("카테고리: ");
        JTextField categoryTextField = new JTextField(productCategory);
        categoryTextField.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        categoryTextField.setBorder(BorderFactory.createEmptyBorder());
        categoryTextField.setEditable(false); // 읽기 전용 설정

        // 추천 상품 체크박스
        recommendCheckBox = new JCheckBox("추천 상품", productRecommendStatus);

        // 판매 상태 체크박스
        saleCheckBox = new JCheckBox("판매 상태", productSaleStatus);

        // 저장 버튼과 취소 버튼
        JButton saveButton = new JButton("저장");
        JButton cancelButton = new JButton("취소");

        // 저장 버튼 액션 리스너
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateProductStatus();
                JOptionPane.showMessageDialog(adminMenuJdialog.this, "저장되었습니다!");
                dispose();
            }
        });

        // 취소 버튼 액션 리스너
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        // 상품 이미지와 상품 이름을 상단에 배치
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(imageLabel, BorderLayout.CENTER);

        // 하단의 입력 항목들 (가격, 카테고리, 추천 상품, 판매 상태)
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 이름, 가격, 카테고리 항목을 그리드에 배치
        inputPanel.add(nameLabel);
        inputPanel.add(nameTextField);
        inputPanel.add(priceLabel);
        inputPanel.add(priceTextField);
        inputPanel.add(categoryLabel);
        inputPanel.add(categoryTextField);
        inputPanel.add(recommendCheckBox);
        inputPanel.add(saleCheckBox);

        // 버튼 패널 (저장, 취소 버튼)
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // 전체 레이아웃
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(inputPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        setSize(500, 400);
        setLocationRelativeTo(parent);
    }

    private void updateProductStatus() {
        SqlSession session = null;
        try {
            session = factory.openSession();
            productVO product = new productVO();
            product.setIdx(productIdx); // productIdx가 제대로 설정되었는지 확인
            product.setProductRecommendStatus(recommendCheckBox.isSelected());
            product.setProductSaleStatus(saleCheckBox.isSelected());

            // 디버깅 출력
            System.out.println("Updating Product ID: " + productIdx);
            System.out.println("Recommend Status: " + recommendCheckBox.isSelected());
            System.out.println("Sale Status: " + saleCheckBox.isSelected());

            // 업데이트 실행
            int affectedRows = session.update("ProductMapper.updateProductStatus", product);
            session.commit(); // 커밋하여 변경 사항 반영

            // 결과 확인
            if (affectedRows > 0) {
                System.out.println("업데이트 성공");
            } else {
                System.out.println("업데이트 실패: 변경된 행 없음");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close(); // 세션 닫기
            }
        }
    }
}
