package kiosk.client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Reader;
import java.util.List;
import javax.swing.*;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import kiosk.vo.OrderVO;

public class AdminOrderList extends JPanel {
    MainFrame parent;
    JPanel cookingOrderPanel;
    JPanel completedOrderPanel;
    JPanel topPanel;
    JButton refreshBtn;
    List<OrderVO> orderList;

    public AdminOrderList(MainFrame parent) {
        //super(parent, "AdminOrderList", true);
        this.parent = parent;
        this.setLayout(new BorderLayout());

        
        
        // 상단 새로고침 버튼
        topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        refreshBtn = new JButton("새로고침");
        topPanel.add(refreshBtn);
        this.add(topPanel, BorderLayout.NORTH);

        // 주문 패널 초기화
        cookingOrderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        completedOrderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        add(cookingOrderPanel, BorderLayout.CENTER);
        add(completedOrderPanel, BorderLayout.SOUTH);

        try (SqlSession session = parent.factory.openSession()) {
            orderList = session.selectList("adminOrderList.orderList");
            System.out.println(orderList);
            updateOrderPanels();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // 새로고침 버튼 액션
        refreshBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("새로고침 버튼 클릭!");
                try (SqlSession session = parent.factory.openSession()) {
                    orderList = session.selectList("adminOrderList.orderList");
                    System.out.println(orderList);
                    updateOrderPanels();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // 다이얼로그 기본 설정
        setBounds(300, 300, 400, 600);
        //setUndecorated(true);
        //setLocationRelativeTo(parent);
        setVisible(true);
    }

    // 주문 패널 업데이트 메서드
    private void updateOrderPanels() {
        cookingOrderPanel.removeAll();
        completedOrderPanel.removeAll();
        System.out.println(orderList.size());
        if(orderList != null) {
            for (OrderVO order : orderList) {
                JPanel orderCard = createOrderCard(order); // 카드 형식으로 주문 표시
                System.out.println(order.getOrderStatus());
                if ("0".equals(order.getOrderStatus())) {
                    JButton completeBtn = new JButton("조리중");
                    completeBtn.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try (SqlSession session = parent.factory.openSession()) {
                                order.setOrderStatus("조리완료");
                                session.update("kiosk.mapper.updateOrderStatus", order); // 상태 업데이트
                                session.commit();
                                updateOrderPanels(); // 패널 새로 고침
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                    orderCard.add(completeBtn, BorderLayout.SOUTH); // 버튼을 카드 하단에 추가
                    cookingOrderPanel.add(orderCard);
                } else if ("1".equals(order.getOrderStatus())) {
                    completedOrderPanel.add(orderCard);
                }
            }
        }

        cookingOrderPanel.revalidate();
        cookingOrderPanel.repaint();
        completedOrderPanel.revalidate();
        completedOrderPanel.repaint();
    }

    // 카드 형식으로 주문을 표시하는 메서드
    private JPanel createOrderCard(OrderVO order) {
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BorderLayout());
        cardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        // 카드 상단: 주문 번호와 총 가격
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("주문번호: " + order.getOrderNumber()));
        topPanel.add(new JLabel("총 가격: " + order.getOrderPrice()));
        cardPanel.add(topPanel, BorderLayout.NORTH);

        // 카드 중앙: 품목 및 사용 쿠폰명 (길이가 길면 ...으로 처리)
        JPanel middlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        middlePanel.add(new JLabel("품목: " + truncateText(order.getProductName(), 20)));
        middlePanel.add(new JLabel("쿠폰명: " + truncateText(order.getCouponName(), 15)));
        cardPanel.add(middlePanel, BorderLayout.CENTER);

        // 카드 하단: 조리 상태
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.add(new JLabel("상태: " + order.getOrderStatus()));
        cardPanel.add(bottomPanel, BorderLayout.SOUTH);

        return cardPanel;
    }

    // 길이가 긴 텍스트는 ...으로 표시
    private String truncateText(String text, int maxLength) {
        if (text.length() > maxLength) {
            return text.substring(0, maxLength) + "...";
        }
        return text;
    }
}
