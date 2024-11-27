package kiosk.adminOrderManagement;

import java.awt.CardLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class AdminOrder extends JFrame {

	JPanel topPanel, listPanel;
	JButton refresh;

	public AdminOrder() {
		JPanel topPanel = new JPanel();
		refresh = new JButton("refresh");
		topPanel.add(refresh);

		JPanel listPanel = new JPanel();
		listPanel.setLayout(new CardLayout());

		this.setBounds(300, 350, 280, 500);
		this.setVisible(true);

	}

}