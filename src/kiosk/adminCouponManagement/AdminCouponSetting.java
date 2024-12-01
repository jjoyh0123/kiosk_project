package kiosk.adminCouponManagement;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import org.apache.ibatis.session.SqlSession;

import kiosk.adminMenuManagement.adminMenuDetailJDialog;
import kiosk.client.MainFrame;
import kiosk.adminVO.CouponSettingVO; 

public class AdminCouponSetting extends JPanel {
    JPanel couponPanel; 
    JTable couponTable;
    DefaultTableModel couponModel;
    JScrollPane scrollpane;
    
    List<kiosk.adminVO.CouponSettingVO> couponList;

    // 컬럼명 정의
    String[] columnNames = { "이름", "품명", "등급", "할인율", "고정할인금액", "사용상태" };

    // MainFrame 주입을 위해 생성자에서 받음
    private MainFrame mainFrame;

    public AdminCouponSetting(MainFrame mainFrame) {
        this.mainFrame = mainFrame; // MainFrame 주입
        this.setLayout(new BorderLayout()); // AdminCouponSetting의 레이아웃 설정

        // 패널 초기화
        couponPanel = new JPanel();
        couponPanel.setLayout(new BorderLayout());

        // 테이블 초기화
        couponModel = new DefaultTableModel(columnNames, 0); // 컬럼명 설정
        couponTable = new JTable(couponModel) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
        };

        // 스크롤 추가
        scrollpane = new JScrollPane(couponTable);
        couponPanel.add(scrollpane, BorderLayout.CENTER);

        // 이 JPanel(AdminCouponSetting)에 couponPanel 추가
        this.add(couponPanel);

        // DB에서 데이터 불러오기
        loadCouponData();
        
        couponTable.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) { // 더블 클릭 감지
                    int selectedRow = couponTable.getSelectedRow();
                    if (selectedRow != -1) {
                    	// 선택된 행의 데이터 가져오기
                        String name = (String) couponTable.getValueAt(selectedRow, 0).toString();
                        String productName = (String) couponTable.getValueAt(selectedRow, 1).toString();
                        String grade = (String) couponTable.getValueAt(selectedRow, 2).toString();
                        String rate = (String) couponTable.getValueAt(selectedRow, 3).toString();
                        String fixed = (String) couponTable.getValueAt(selectedRow, 4).toString();
                        String status = (String) couponTable.getValueAt(selectedRow, 5).toString();
                        
                        
                        kiosk.adminVO.CouponSettingVO coupon = couponList.get(selectedRow);
          
                        showDialog(coupon);
                    }
				}
			}
		});
    }
    
    private void showDialog(kiosk.adminVO.CouponSettingVO coupon) {
    	AdminCouponDialog dialog = new AdminCouponDialog(mainFrame,
    			SwingUtilities.getWindowAncestor(this), AdminCouponSetting.this, coupon);
        dialog.setVisible(true);
        dialog.setLocationRelativeTo(null);
    }

    /**
     * DB에서 쿠폰 데이터를 불러와 테이블에 표시
     */
    public void loadCouponData() {
        try {
            // MyBatis SqlSession 생성
            SqlSession ss = mainFrame.factory.openSession(); // MainFrame에서 제공된 factory 사용
            couponList = ss.selectList("couponSetting.couponSet"); // Mapper 호출
            ss.close();

            // 테이블 초기화
            couponModel.setRowCount(0);

            // 쿠폰 데이터를 테이블에 추가
            for (kiosk.adminVO.CouponSettingVO coupon : couponList) {
                couponModel.addRow(new Object[] {
                    coupon.getCouponSettingName(), 
                    coupon.getProductName(),      
                    coupon.getCouponSettingGrade(), 
                    coupon.getCouponSettingRate(),
                    coupon.getCouponSettingFixed(),
                    coupon.isCouponSettingStatus()
                });
            }
            
            couponPanel.revalidate();
            couponPanel.repaint();
            couponTable.revalidate();
            couponTable.repaint();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "쿠폰 데이터를 불러오는 중 오류가 발생했습니다.");
        }
    }
}