package kiosk.client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;
import org.apache.ibatis.session.SqlSession;
import kiosk.vo.OrderVO;

public class AdminOrderList extends JDialog {
    MainFrame parent;
    JPanel cardPanel;  // 주문내역을 카드형식으로 담을 패널
    JPanel CompletedPanel;  // 조리완료 주문을 담을 패널 (아래쪽으로 이동)
    JPanel topPanel; // 새로고침 버튼을 넣을 패널
    JButton refreshBtn; // 새로고침 버튼
    List<OrderVO> orderList;
   

    public AdminOrderList(MainFrame parent) {
        super(parent, "AdminOrderList", true);
        this.parent = parent;
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout());
        
       

        // 새로고침 버튼 만들기
        topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        refreshBtn = new JButton("새로고침");
        topPanel.add(refreshBtn);
        this.add(topPanel, BorderLayout.NORTH); 
        

        refreshBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("새로고침 버튼 클릭!");
                loadOrderData();
            }
        });

        // 레이아웃 설정
        cardPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));  // 왼쪽 정렬로 카드 나열
        CompletedPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));  // 조리완료 주문은 맨 아래에 배치

        // 다이얼로그에 주문 카드 패널, 조리완료 주문 패널 추가
        add(cardPanel, BorderLayout.CENTER);
        add(CompletedPanel, BorderLayout.SOUTH);

        // 다이얼로그 사이즈와 위치설정
        setSize(800, 600);
        setLocationRelativeTo(parent);  // 부모 창의 위치를 기준으로 다이얼로그 위치 설정
        setVisible(true);
        
        // 초기 주문내역 보이게하기
        loadOrderData();

    
    }

    // 주문 내역을 DB에서 가져오는 메서드
    private void loadOrderData() {
        // DB에서 오늘 날짜의 주문 내역을 가져옵니다.
        orderList = getTodayOrderList();  // MyBatis를 통해 주문 데이터 가져오기

        // 기존 카드들 제거 (새로고침 시 기존 카드들 지우기)
        //cardPanel.removeAll();
        CompletedPanel.removeAll();

        // 주문 내역에 맞는 카드 생성 및 추가
        for (OrderVO order : orderList) {
            JPanel card = createOrderCard(order);  // 각 주문에 대한 카드 생성
            if (order.isOrderStatus()) {  // 상태가 '조리완료'인 주문은 맨 아래로 내림
            	CompletedPanel.add(card);
            } else {  // 조리중인 주문은 상단에 배치
                cardPanel.add(card);
            }
        }

        // 패널 리프레시
        cardPanel.revalidate();
        cardPanel.repaint();
        CompletedPanel.revalidate();
        CompletedPanel.repaint();
    }

    // DB에서 오늘 날짜의 주문 내역을 가져오는 메서드 
    private List<OrderVO> getTodayOrderList() {
        SqlSession ss = parent.factory.openSession();
        List<OrderVO> orderList = ss.selectList("adminOrderList.orderList");
        
        ss.close();
       return orderList;  // 오늘 날짜의 주문 내역 가져오기
        }
    

    // 주문에 해당하는 카드 패널 생성
    private JPanel createOrderCard(OrderVO order) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // 카드 크기 고정 (예: 200px x 150px)
        panel.setPreferredSize(new Dimension(200, 150));

        // 주문 번호
        JLabel orderNumberLabel = new JLabel("주문번호: " + order.getOrderNumber(), SwingConstants.CENTER);
        // 주문 품목 및 사용 쿠폰명
        String productCategory = truncateText(String.valueOf(order.getOrderProductCategory()), 10);  // 품목 이름 길이 제한
        String couponName = truncateText(order.getCouponName(), 10);  // 쿠폰명 길이 제한
        JTextArea detailsArea = new JTextArea(
            "품목: " + productCategory + "\n" +
            "쿠폰: " + couponName + "\n" +
            "가격: " + order.getOrderPrice() + "원"
        );
        detailsArea.setEditable(false);

        // '조리중' 버튼 생성
        JButton cookingButton = new JButton(order.isOrderStatus() ? "조리완료" : "조리중");
        cookingButton.addActionListener(e -> updateOrderStatusToCompleted(order, panel));

        // 카드 디자인을 위한 간단한 스타일 설정
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));  // 카드 테두리 설정
        panel.setPreferredSize(new Dimension(300, 150)); 
        panel.setBackground(new Color(240, 240, 240)); // 카드 배경 색

        orderNumberLabel.setFont(new Font("Arial", Font.BOLD, 14));  // 제목 스타일

        panel.add(orderNumberLabel, BorderLayout.NORTH);
        panel.add(new JScrollPane(detailsArea), BorderLayout.CENTER);
        panel.add(cookingButton, BorderLayout.SOUTH);

        return panel;
    }

    private void updateOrderStatusToCompleted(OrderVO order, JPanel cardPanel) {
        order.setOrderStatus(true);  // 주문 상태를 '조리완료'로 변경

        // 상태 변경 후, 카드 위치를 맨 아래로 이동
        cardPanel.getParent().remove(cardPanel);  // 기존 카드 패널에서 카드 제거
        CompletedPanel.add(cardPanel);  // '조리완료' 패널에 카드를 추가

        // 패널 리프레시
        CompletedPanel.revalidate();  // CompletedPanel을 리프레시
        CompletedPanel.repaint();  // 화면 다시 그리기
    }

    // 텍스트가 일정 길이를 초과하면 '...' 으로 자르는 메서드
    private String truncateText(String text, int maxLength) {
        if (text != null && text.length() > maxLength) {
            return text.substring(0, maxLength) + "...";
        }
        return text;
    } 

 
}
