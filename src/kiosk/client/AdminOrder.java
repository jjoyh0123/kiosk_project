package kiosk.client;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.ibatis.session.SqlSession;

import kiosk.vo.OrderVO;

public class AdminOrder extends JDialog {
	MainFrame parent;
	JPanel northPanel, listPanel;
	JButton refreshBtn;

	public AdminOrder(MainFrame parent) {
		super(parent, "Admin Login", true); // 모달 다이얼로그 설정
        this.parent = parent;
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout());
		
		JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		refreshBtn = new JButton("새로고침");
		northPanel.add(refreshBtn);
		this.add(northPanel,BorderLayout.NORTH);

		JPanel listPanel = new JPanel();
		listPanel.setLayout(new CardLayout());
		this.add(listPanel,BorderLayout.CENTER);

		this.setBounds(300, 350, 280, 500);
		this.setVisible(true);

	}
	
	private List<OrderVO> getOrderList() {
		SqlSession ss = parent.factory.openSession();
		List<OrderVO> orderList = ss.selectList("order.orderList");
		return orderList;
	}

}