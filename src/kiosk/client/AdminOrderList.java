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
	Admin parent;
	MainFrame mainFrame;
	JPanel topPanel, orderPanel;
	JButton refreshBtn;
	List<OrderVO> orderList;
	JScrollPane scrollPane;

	public AdminOrderList(Admin parent) {
		
		this.setLayout(new BorderLayout());

		this.parent = parent;
		this.mainFrame = parent.mainFrame;
		this.parent.add(this);

		// 상단 새로고침 버튼

		topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		refreshBtn = new JButton("새로고침");
		topPanel.add(refreshBtn);
		this.add(topPanel, BorderLayout.NORTH);

		orderPanel = new JPanel(new GridLayout(0, 1));
		scrollPane = new JScrollPane(orderPanel);
		this.add(scrollPane, BorderLayout.CENTER);
		
		

		try (SqlSession session = mainFrame.factory.openSession()) {
			orderList = session.selectList("adminOrderList.orderList");
			System.out.println(orderList);
			updateOrderPanels();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		updateUI();
		
		// 새로고침 버튼 액션
		refreshBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("새로고침 버튼 클릭!");
				try (SqlSession session = mainFrame.factory.openSession()) {
					orderList = session.selectList("adminOrderList.orderList");
					System.out.println(orderList);
					updateOrderPanels();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});

	}

	// 주문 패널 업데이트 메서드
	private void updateOrderPanels() {
		orderPanel.removeAll();

		System.out.println(orderList.size());
		if (orderList != null) {
			for (OrderVO order : orderList) {
				JPanel orderCard = createOrderCard(order); // 카드 형식으로 주문 표시
				System.out.println(order.isOrderStatus());

				JButton completeBtn = new JButton(getOrderStatusText(order.isOrderStatus()));
				completeBtn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						try (SqlSession session = mainFrame.factory.openSession()) {
							order.setOrderStatus(!order.isOrderStatus());
							session.update("adminOrderList.updateOrderStatus", order); // 상태 업데이트
							session.commit();

							completeBtn.setEnabled(false);

							orderList = session.selectList("adminOrderList.orderList");
							updateOrderPanels(); // 패널 새로 고침
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				});

				if (order.isOrderStatus()) {
					completeBtn.setEnabled(false); // 이미 조리완료된 주문은 버튼 비활성화
				}

				orderCard.add(completeBtn, BorderLayout.EAST); // 버튼을 카드 하단에 추가
				orderPanel.add(orderCard, BorderLayout.EAST);
			}
		}

		orderPanel.revalidate();
		orderPanel.repaint();
	}

	private String getOrderStatusText(boolean orderStatus) {
		return (orderStatus) ? "조리완료" : "조리중";
	}

	// 카드 형식으로 주문을 표시하는 메서드
	private JPanel createOrderCard(OrderVO order) {
		JPanel cardPanel = new JPanel();
		cardPanel.setLayout(new BorderLayout());
		cardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

		// 카드 상단: 주문 번호와 총 가격
		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		topPanel.add(new JLabel("주문번호. " + order.getOrderNumber()));
		topPanel.add(new JLabel("주문날짜: " + order.getOrderDate()));
		topPanel.add(new JLabel("총 가격: " + order.getTotalOrderPrice()));
		cardPanel.add(topPanel, BorderLayout.NORTH);

		// 카드 중앙: 품목 및 사용 쿠폰명 (길이가 길면 ...으로 처리)
		JPanel middlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		middlePanel.add(new JLabel("품목: " + truncateText(order.getProducts(), 20)));
		
		cardPanel.add(middlePanel, BorderLayout.CENTER);

		// 카드 하단: 조리 상태
		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		bottomPanel.add(new JLabel("쿠폰: " + truncateText(order.getAppliedCoupon(), 15)));
		//bottomPanel.add(new JLabel("조리상태: " + order.isOrderStatus()));
		cardPanel.add(bottomPanel, BorderLayout.SOUTH);
		return cardPanel;

	}

	// 길이가 긴 텍스트는 ...으로 표시
	private String truncateText(String text, int maxLength) {
		if (text != null && text.length() > maxLength) {
			return text.substring(0, maxLength) + "...";
		}
		return text;
	}
}
