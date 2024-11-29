package kiosk.adminOrderManagement;

import javax.swing.*;
import java.awt.*;

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