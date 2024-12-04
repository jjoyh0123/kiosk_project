package kiosk.adminMenuManagement;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.apache.ibatis.session.SqlSession;

import Create.RoundedButton;
import kiosk.adminVO.productVO;
import kiosk.client.MainFrame;

public class AdminMenuManagementPanel extends JPanel {
  JPanel centerPanel; // centerPanel을 클래스 레벨에서 선언하여 참조
  MainFrame mainFrame;
  Dimension buttonSize;

  public AdminMenuManagementPanel(MainFrame mainFrame) {
    this.mainFrame = mainFrame;
    // 기본 레이아웃 설정
    setLayout(new BorderLayout());

    // 상단 NORTH Panel과 버튼들
    JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // 레이아웃 설정 추가
    JButton singleButton = new RoundedButton("단품");
    JButton setButton = new RoundedButton("세트");
    JButton snackButton = new RoundedButton("스낵");
    JButton drinkButton = new RoundedButton("음료");
    JPanel menuNorthPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    //menuNorthPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0)); // 위쪽과 아래쪽 여백 10픽셀 추가
    
    Font buttonFont = new Font("aria", Font.BOLD, 20); // 폰트 크기 20, 굵게 설정
    singleButton.setFont(buttonFont);
    setButton.setFont(buttonFont);
    snackButton.setFont(buttonFont);
    drinkButton.setFont(buttonFont);

    Dimension buttonSize = new Dimension(100, 50);
    singleButton.setPreferredSize(buttonSize);
    setButton.setPreferredSize(buttonSize);
    snackButton.setPreferredSize(buttonSize);
    drinkButton.setPreferredSize(buttonSize);

    JLabel menulable = new JLabel("메뉴관리");
    
    //menulable.setBorder(BorderFactory.createEmptyBorder(15, 5, 15, 0)); // 위쪽과 아래쪽 여백 10픽셀 추가

    menulable.setFont(new Font("맑은고딕", Font.BOLD, 18));

    //menulable.setPreferredSize(new Dimension(300, 15));
    menuNorthPanel.add(menulable);
    menuNorthPanel.setBackground(new Color(190 ,190, 190)); // 상단 패널 배경색 설정 (밝은 회색)

    // 버튼들을 패널에 추가
    northPanel.add(singleButton);
    northPanel.add(setButton);
    northPanel.add(snackButton);
    northPanel.add(drinkButton);
    northPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
    buttonSize = new Dimension(100, 50); // 버튼 크기 설정

    singleButton.setPreferredSize(buttonSize);
    setButton.setPreferredSize(buttonSize);
    snackButton.setPreferredSize(buttonSize);
    drinkButton.setPreferredSize(buttonSize);

    // 큰 중앙 패널 (카드 레이아웃)
    centerPanel = new JPanel(new CardLayout());

    // 각 카테고리 패널 생성
    JPanel singlePanel = createCategoryPanel("단품", 1, 7);
    JPanel setPanel = createCategoryPanel("세트", 8, 9);
    JPanel snackPanel = createCategoryPanel("스낵", 12, 12);
    JPanel drinkPanel = createCategoryPanel("음료", 13, 17);

    // 카드 레이아웃에 각 패널 추가
    centerPanel.add(singlePanel, "단품");
    centerPanel.add(setPanel, "세트");
    centerPanel.add(snackPanel, "스낵");
    centerPanel.add(drinkPanel, "음료");

    // 버튼 클릭 시 해당 카테고리로 전환
    singleButton.addActionListener(e -> showCategory("단품"));
    setButton.addActionListener(e -> showCategory("세트"));
    snackButton.addActionListener(e -> showCategory("스낵"));
    drinkButton.addActionListener(e -> showCategory("음료"));

    // 전체 패널에 NORTH 와 CENTER 패널 추가
    JPanel combinedNorthPanel = new JPanel(new BorderLayout());

    combinedNorthPanel.add(menuNorthPanel, BorderLayout.NORTH);
    combinedNorthPanel.add(northPanel, BorderLayout.SOUTH);
    add(combinedNorthPanel, BorderLayout.NORTH);
    add(centerPanel, BorderLayout.CENTER);
  }

  private JPanel createCategoryPanel(String category, int startImage, int endImage) {
    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(0, 3, 5, 5));

    String[] itemNames = {"시추안 크리스프 버거", "쉑마이스터 버거", "쉑버거", "스모크쉑", "슈룸 버거", "쉑 스택", "햄버거", "시추안 크리스프 프라이", "쉑버거",
        "시추안 크리스프 프라이", "프라이", "치즈 프라이", "유자 바질 레모네이드", "레몬에이드", "아이스티", "피프티/피프티", "탄산음료"};

    for (int i = startImage - 1; i < endImage; i++) {
      String imagePath = "/kiosk/static/product" + (i + 1) + ".jpg";
      ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
      JPanel imagePanel = new JPanel();
      imagePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 5));

      JLabel label = new JLabel(icon);
      JLabel textLabel = new JLabel(itemNames[i]);
      textLabel.setHorizontalAlignment(SwingConstants.CENTER);
      textLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 16)); // 한글을 지원하는 폰트 설정

      imagePanel.add(label);
      imagePanel.add(textLabel);

      int index = i + 1;
      label.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
          showProductDetails(index);
        }
      });

      panel.add(imagePanel);
    }

    // 빈 패널 추가하여 3x3 그리드 유지
    int totalItems = endImage - startImage + 1;
    int emptyCells = 9 - totalItems;
    for (int i = 0; i < emptyCells; i++) {
      panel.add(new JPanel());
    }

    return panel;
  }

  private void showCategory(String category) {
    CardLayout layout = (CardLayout) centerPanel.getLayout();
    layout.show(centerPanel, category); // 카테고리를 전환
  }

  private void showProductDetails(int productIdx) {
    SqlSession session = null;
    try {
      session = mainFrame.factory.openSession();
      productVO product = session.selectOne("adminMenuManagement.getProductById", productIdx);
      if (product != null) {
        String productRegDate = product.getProductRegDate(); // 등록일
        int productCategory = product.getProductCategory();
        String categoryName;

        switch (productCategory) {
          case 1:
            categoryName = "단품";
            break;
          case 2:
            categoryName = "세트";
            break;
          case 3:
            categoryName = "스낵";
            break;
          case 4:
            categoryName = "음료";
            break;
          default:
            categoryName = "기타";
            break;
        }

        String categoryTest = categoryName + "(" + productCategory + ")";

        adminMenuDetailJDialog detailsFrame = new adminMenuDetailJDialog(mainFrame, productIdx,
            product.getProductName(), "/kiosk/static/product" + productIdx + ".jpg",
            SwingUtilities.getWindowAncestor(this), product.getProductPrice(), product.getProductCategory(),
            product.isProductRecommendStatus(), product.isProductSaleStatus(), productRegDate // 등록일 추가
        );
        detailsFrame.setVisible(true);
        detailsFrame.setLocationRelativeTo(null);
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