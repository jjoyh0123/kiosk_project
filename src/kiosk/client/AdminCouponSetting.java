package kiosk.client;

import javax.swing.JPanel;
import javax.swing.JTable;

public class AdminCouponSetting extends JPanel {
	Admin parent;
	MainFrame mainFrame;
	JPanel couponPanel;
	JTable couponTable;

	public AdminCouponSetting(Admin parent) {
		this.parent = parent;
		this.mainFrame = parent.mainFrame;
		this.parent.add(this);
		
		couponPanel = new JPanel();
		couponTable = new JTable();
		
		
		
		this.add(couponPanel);
		this.add(couponTable);
		
	}

}
