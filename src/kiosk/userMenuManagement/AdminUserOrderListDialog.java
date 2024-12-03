package kiosk.userMenuManagement;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import org.apache.ibatis.session.SqlSession;

import kiosk.admin.Admin;
import kiosk.adminVO.OrderVO;
import kiosk.client.MainFrame;

public class AdminUserOrderListDialog extends JDialog {
	MainFrame mainFrame;
	JPanel topPanel, orderPanel;
	JButton refreshBtn;
	List<OrderVO> orderList;
	JScrollPane scrollPane;
	LocalDate currentDate; // 현재 선택된 날짜
	JButton prevDayButton, nextDayButton; // 날짜 이동 버튼
	JLabel dateLabel; // 현재 날짜 표시
	int useridx;

	public AdminUserOrderListDialog(Admin owner, MainFrame mainFrame, int useridx) {
		super(owner, "사용자 주문 목록", true);
		this.mainFrame = mainFrame;
		this.useridx = useridx;
		setLayout(new BorderLayout());
		setSize(460, 450);
		setLocationRelativeTo(owner);

		setLocation(730, 230);

		setUndecorated(true);// 위에 윈도우창없애기

		// 현재 날짜 초기화
		currentDate = LocalDate.now();

		topPanel = new JPanel(new BorderLayout());
		JLabel titleLabel = new JLabel("주문내역", JLabel.CENTER);
		titleLabel.setFont(new Font("맑은고딕", Font.PLAIN, 35));
		titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 여백 추가
		topPanel.add(titleLabel, BorderLayout.NORTH);
		JPanel navigationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		prevDayButton = new JButton("<");
		dateLabel = new JLabel(currentDate.toString());
		nextDayButton = new JButton(">");
		refreshBtn = new JButton("나가기");

		navigationPanel.add(prevDayButton);
		navigationPanel.add(dateLabel);
		navigationPanel.add(nextDayButton);

		refreshBtn.addActionListener(e -> dispose());

		topPanel.add(navigationPanel, BorderLayout.CENTER); // 가운데 날짜 이동 버튼들
		topPanel.add(refreshBtn, BorderLayout.EAST); // 오른쪽에 "나가기" 버튼
		add(topPanel, BorderLayout.NORTH); // 최상단에 상단 패널 추가

		prevDayButton.addActionListener(e -> changeDate(-1));
		nextDayButton.addActionListener(e -> changeDate(1));
		refreshBtn.addActionListener(e -> dispose());

		orderPanel = new JPanel();
		orderPanel.setLayout(new BoxLayout(orderPanel, BoxLayout.Y_AXIS));
		orderPanel.setPreferredSize(new java.awt.Dimension(780, 500)); // 패널 크기 고정

		scrollPane = new JScrollPane(orderPanel);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPane, BorderLayout.CENTER);

		loadOrderData();
	}

	private void changeDate(int days) {
		LocalDate newDate = currentDate.plusDays(days);

		if (newDate.isAfter(LocalDate.now())) {
			JOptionPane.showMessageDialog(this, "미래 날짜는 선택할 수 없습니다.");
			return;
		}

		if (newDate.isBefore(LocalDate.now().minusDays(3))) {
			JOptionPane.showMessageDialog(this, "3일 전의 주문은 볼 수 없습니다.");
			return;
		}

		currentDate = newDate;
		dateLabel.setText(currentDate.toString());
		loadOrderData();
	}

	// 주문 데이터 로드
	private void loadOrderData() {
		orderPanel.removeAll();
		Map<String, Object> params = new HashMap<>();
		params.put("orderDate", currentDate.toString());
		params.put("userIdx", useridx);
		try (SqlSession session = mainFrame.factory.openSession()) {
			orderList = session.selectList("adminUserManagement.orderListByDate", params);
			if (orderList != null && !orderList.isEmpty()) {
				for (OrderVO order : orderList) {
					orderPanel.add(createOrderCard(order));
				}
			} else {
				JLabel noDataLabel = new JLabel("해당 날짜에 주문 내역이 없습니다.");
				noDataLabel.setHorizontalAlignment(SwingConstants.CENTER);
				orderPanel.add(noDataLabel);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		orderPanel.revalidate();
		orderPanel.repaint();
	}

	// 주문 카드 생성
	private JPanel createOrderCard(OrderVO order) {
		JPanel cardPanel = new JPanel(new BorderLayout());
		cardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		cardPanel.setPreferredSize(new java.awt.Dimension(760, 100)); // 카드 크기 고정

		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		topPanel.add(new JLabel("주문번호: " + order.getOrderNumber()));
		topPanel.add(new JLabel("주문날짜: " + order.getOrderDate()));
		cardPanel.add(topPanel, BorderLayout.NORTH);

		JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		centerPanel.add(new JLabel("품목: " + truncateText(order.getProducts(), 30)));
		cardPanel.add(centerPanel, BorderLayout.CENTER);

		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		bottomPanel.add(new JLabel("총 가격: " + order.getTotalOrderPrice() + "원"));
		cardPanel.add(bottomPanel, BorderLayout.SOUTH);

		return cardPanel;
	}

	// 텍스트 길이 제한
	private String truncateText(String text, int maxLength) {
		if (text != null && text.length() > maxLength) {
			return text.substring(0, maxLength) + "...";
		}
		return text;
	}
}