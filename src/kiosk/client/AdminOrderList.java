package kiosk.client;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.ibatis.session.SqlSession;

import kiosk.vo.OrderVO;

public class AdminOrderList extends JDialog {
    MainFrame parent;
    CardLayout cardLayout;
    JPanel listPanel, cardPanel, refreshPanel;
    JButton refreshBtn;
    List<OrderVO> orderList;

    public AdminOrderList(MainFrame parent) {
        super(parent, "Admin Order", true); // 모달 다이얼로그 설정
        this.parent = parent;
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout());

        // 카드 레이아웃 생성
        cardLayout = new CardLayout();
        listPanel = new JPanel(cardLayout);

        // 버튼 초기화
        refreshBtn = new JButton("새로고침");

        // 주문 리스트 가져오기
        orderList = getOrderList();

        // 주문 리스트를 카드 레이아웃으로 추가
        int cardIndex = 1;
        for (OrderVO order : orderList) {
            cardPanel = new JPanel();
            cardPanel.setBackground(Color.WHITE);

            // 새로고침 버튼 추가
            refreshPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            refreshPanel.add(refreshBtn);

            // 주문 정보 레이블 추가
            cardPanel.add(new JLabel("주문번호: " + order.getOrderNumber())); // 주문번호
            cardPanel.add(new JLabel("총 가격: " + order.getOrderPrice() + "원")); // 총 가격
            cardPanel.add(new JLabel("품목: " + order.getOrderProductCategory())); // 해당 주문 품목
            cardPanel.add(new JLabel("쿠폰: " + order.getCouponName())); // 사용한 쿠폰명
            cardPanel.add(new JLabel("조리 상태: " + (order.isOrderStatus() ? "조리중" : "완료"))); // 조리 상태

            // "조리중" 버튼 생성
            JButton cookingBtn = new JButton(order.isOrderStatus() ? "조리중" : "조리완료");
            cardPanel.add(cookingBtn);

            // 버튼에 'order' 객체를 연결
            cookingBtn.putClientProperty("order", order);

            // 버튼 클릭 시 처리하는 리스너 추가
            cookingBtn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // 클릭된 버튼에서 'order' 객체 가져오기
                    OrderVO currentOrder = (OrderVO) cookingBtn.getClientProperty("order");

                    // 버튼 텍스트가 "조리중"일 경우 -> "조리완료"로 변경
                    if (cookingBtn.getText().equals("조리중")) {
                        cookingBtn.setText("조리완료");
                        currentOrder.setOrderStatus(false); // 상태를 '완료'로 변경
                        updateOrderStatus(currentOrder); // DB 업데이트
                        moveCardToBottom(cardPanel); // 카드를 아래로 이동
                        
                    } else {
                        cookingBtn.setText("조리중");
                    
                    }
                }
            });

            // 각 카드에 이름을 부여하여 추가
            listPanel.add(cardPanel, "Card " + cardIndex++);
        }

        // 기본 카드 표시 (첫 번째 카드)
        if (orderList.size() > 0) {
            cardLayout.show(listPanel, "Card 1");
        }

        // 패널을 다이얼로그에 추가
        add(listPanel, BorderLayout.CENTER);

        // 다이얼로그 크기 설정
        setSize(460, 600);
        setUndecorated(true);
        setLocationRelativeTo(parent); // 부모창 기준으로 중앙 정렬
        setVisible(true);
    }

    private List<OrderVO> getOrderList() {
        SqlSession ss = parent.factory.openSession();
        List<OrderVO> orderList = ss.selectList("adminOrderList.orderList");
        ss.close();
        return orderList;
    }

    // 주문 상태 업데이트하는 메서드 (DB 갱신)
    private void updateOrderStatus(OrderVO order) {
        SqlSession ss = parent.factory.openSession();
        ss.update("adminOrderList.updateOrderStatus", order);
        ss.commit();
        ss.close();
    }

    // 카드 레이아웃에서 해당 카드를 아래로 이동시키는 메서드
    private void moveCardToBottom(JPanel card) {
        cardLayout.last(listPanel);  // 카드 레이아웃에서 마지막 카드로 이동
    }
}