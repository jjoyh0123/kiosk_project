package kiosk.adminMenuManagement;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.apache.ibatis.session.SqlSession;

import kiosk.adminVO.productVO;
import kiosk.client.MainFrame;

public class adminMenuDetailJDialog extends JDialog {
  boolean productRecommendStatus;
  boolean productSaleStatus;
  JCheckBox recommendCheckBox;
  JCheckBox saleCheckBox;
  int productIdx;

  MainFrame mainFrame;
  Dimension buttonSize;

  public adminMenuDetailJDialog(MainFrame mainFrame, int productIdx, String productName, String imagePath,
                                Window parent, int productPrice, int productCategory, boolean productRecommendStatus, boolean productSaleStatus,
                                String productRegDate) {
    super(parent, "상품 상세", ModalityType.APPLICATION_MODAL);
    this.mainFrame = mainFrame;
    this.productIdx = productIdx;
    this.productRecommendStatus = productRecommendStatus;
    this.productSaleStatus = productSaleStatus;
    setUndecorated(true);
    // 창 초기화
    setLayout(new BorderLayout());
    setLocation(945, 230);

    // 상품 이미지 로드
    ImageIcon imageIcon = new ImageIcon(getClass().getResource(imagePath));
    JLabel imageLabel = new JLabel(imageIcon);
    imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

    // 상품 이름
    JLabel nameLabel = new JLabel("이름: ");
    JTextField nameTextField = new JTextField(productName);
    nameTextField.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
    nameTextField.setBorder(BorderFactory.createEmptyBorder());
    nameTextField.setEditable(false);

    // 상품 가격
    JLabel priceLabel = new JLabel("가격: ");
    JTextField priceTextField = new JTextField(String.valueOf(productPrice));
    priceTextField.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
    priceTextField.setBorder(BorderFactory.createEmptyBorder());
    priceTextField.setEditable(false);

    // 카테고리
    JLabel categoryLabel = new JLabel("카테고리: ");
    int categoryWithNumber = productCategory;
    JTextField categoryTextField = new JTextField(String.valueOf(productCategory));
    categoryTextField.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
    categoryTextField.setBorder(BorderFactory.createEmptyBorder());
    categoryTextField.setEditable(false);

    // 등록일
    JLabel regDateLabel = new JLabel("등록일: ");
    JTextField regDateTextField = new JTextField(productRegDate);
    regDateTextField.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
    regDateTextField.setBorder(BorderFactory.createEmptyBorder());
    regDateTextField.setEditable(false);

    // 추천 체크박스
    recommendCheckBox = new JCheckBox("추천 상품", productRecommendStatus);

    // 판매 상태 체크박스
    saleCheckBox = new JCheckBox("판매 상태", productSaleStatus);

    // 저장 및 취소 버튼
    JButton saveButton = new JButton("저장");
    JButton cancelButton = new JButton("취소");

    buttonSize = new Dimension(100, 60); // 버튼 크기 설정

    saveButton.setPreferredSize(buttonSize);
    cancelButton.setPreferredSize(buttonSize);

    saveButton.addActionListener(e -> {
      updateProductStatus();
      JOptionPane.showMessageDialog(adminMenuDetailJDialog.this, "저장되었습니다!");
      dispose();
    });

    cancelButton.addActionListener(e -> dispose());

    // 상단 패널
    JPanel topPanel = new JPanel(new BorderLayout());
    topPanel.add(imageLabel, BorderLayout.CENTER);

    // 입력 필드 패널
    JPanel inputPanel = new JPanel(new GridLayout(7, 2, 10, 10));
    inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    inputPanel.add(nameLabel);
    inputPanel.add(nameTextField);
    inputPanel.add(priceLabel);
    inputPanel.add(priceTextField);
    inputPanel.add(categoryLabel);
    inputPanel.add(categoryTextField);
    inputPanel.add(regDateLabel);
    inputPanel.add(regDateTextField);
    inputPanel.add(recommendCheckBox);
    inputPanel.add(saleCheckBox);

    // 버튼 패널
    JPanel buttonPanel = new JPanel();
    buttonPanel.add(saveButton);
    buttonPanel.add(cancelButton);

    // 전체 레이아웃
    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.add(topPanel, BorderLayout.NORTH);
    mainPanel.add(inputPanel, BorderLayout.CENTER);
    mainPanel.add(buttonPanel, BorderLayout.SOUTH);

    add(mainPanel, BorderLayout.CENTER);

    setSize(460, 450);
    setLocation(730, 230);
    setLocationRelativeTo(parent);
  }

  private void updateProductStatus() {
    SqlSession session = null;
    try {
      session = mainFrame.factory.openSession();
      productVO product = new productVO();
      product.setIdx(productIdx);

      product.setProductRecommendStatus(recommendCheckBox.isSelected());
      product.setProductSaleStatus(saleCheckBox.isSelected());

      int affectedRows = session.update("adminMenuManagement.updateProductStatus", product);
      session.commit();

      if (affectedRows > 0) {
        System.out.println("업데이트 성공");
      } else {
        System.out.println("업데이트 실패: 변경된 행 없음");
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (session != null) {
        session.close();
      }
    }
  }
}