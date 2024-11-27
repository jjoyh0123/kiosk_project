package kiosk.adminMenu;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import kiosk.config.productVO;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Reader;

public class AdminDialog extends JPanel {
    JPanel centerPanel; // centerPanel을 클래스 레벨에서 선언하여 참조
    SqlSessionFactory factory;

    public AdminDialog() {
        dbConnect();
        // 기본 레이아웃 설정
        setLayout(new BorderLayout());

     // 상단 NORTH Panel과 버튼들
        JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // 레이아웃 설정 추가
        JButton singleButton = new JButton("단품");
        JButton setButton = new JButton("세트");
        JButton snackButton = new JButton("스낵");
        JButton drinkButton = new JButton("음료");

        // 버튼들을 패널에 추가
        northPanel.add(singleButton);
        northPanel.add(setButton);
        northPanel.add(snackButton);
        northPanel.add(drinkButton);


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

        // 전체 패널에 NORTH와 CENTER 패널 추가
 

        add(northPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
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

    private JPanel createCategoryPanel(String category, int startImage, int endImage) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 3, 5, 5));

        String[] itemNames = {
            "시추안 크리스프 버거", "쉑마이스터 버거", "쉑버거", "스모크쉑", "슈룸 버거",
            "쉑 스택", "햄버거", "시추안 크리스프 프라이", "쉑버거", "시추안 크리스프 프라이",
            "프라이", "치즈 프라이", "유자 바질 레모네이드", "레몬에이드", "아이스티",
            "피프티/피프티", "탄산음료"
        };

        for (int i = startImage - 1; i < endImage; i++) {
            String imagePath = "/kiosk/static/product" + (i + 1) + ".jpg";
            ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
            JPanel imagePanel = new JPanel();
            imagePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 5));

            JLabel label = new JLabel(icon);
            JLabel textLabel = new JLabel(itemNames[i]);
            textLabel.setHorizontalAlignment(SwingConstants.CENTER);
            textLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 14)); // 한글을 지원하는 폰트 설정

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

        return panel;
    }

    private void showCategory(String category) {
        CardLayout layout = (CardLayout) centerPanel.getLayout();
        layout.show(centerPanel, category); // 카테고리를 전환
    }

    private void showProductDetails(int productIdx) {
        SqlSession session = null;
        try {
            session = factory.openSession();
            productVO product = session.selectOne("ProductMapper.getProductById", productIdx);
            if (product != null) {
                adminMenuJdialog detailsFrame = new adminMenuJdialog(
                    factory,
                    productIdx, // 수정된 부분: productIdx 전달
                    product.getProductName(),
                    "/kiosk/static/product" + productIdx + ".jpg",
                    SwingUtilities.getWindowAncestor(this),
                    product.getProductPrice(),
                    product.getProductCategory(),
                    product.isProductRecommendStatus(),
                    product.isProductSaleStatus()
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

    public static void main(String[] args) {
        JFrame frame = new JFrame("Admin Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.add(new AdminDialog());
        frame.setVisible(true);
    }
}
