package kiosk.client;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class AdminOrder extends JFrame {

	JPanel northPanel, listPanel;
	JButton refreshBtn;

	public AdminOrder() {
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

}