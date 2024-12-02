package kiosk.adminCouponManagement;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import org.apache.ibatis.session.SqlSession;

import kiosk.client.MainFrame; 

public class AdminCouponSetting extends JPanel {
    JPanel titlePanel, couponPanel; 
    JLabel titleLabel;
    JTable couponTable;
    DefaultTableModel couponModel;
    JScrollPane scrollpane;
    TableRowSorter<DefaultTableModel> sorter;
    List<kiosk.adminVO.CouponSettingVO> couponList;
    List<String> gradeOrder = Arrays.asList("silver", "gold", "platinum", "diamond");

    // 컬럼명 정의
    String[] columnNames = { "이름", "품명", "등급", "할인율", "고정할인금액", "사용상태" };

    // MainFrame 주입을 위해 생성자에서 받음
    private MainFrame mainFrame;

    public AdminCouponSetting(MainFrame mainFrame) {
        this.mainFrame = mainFrame; // MainFrame 주입
        //this.setLayout(new BorderLayout()); // AdminCouponSetting의 레이아웃 설정
        
        titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // FlowLayout으로 변경
        titleLabel = new JLabel("쿠폰관리");
        titleLabel.setFont(new Font("맑은고딕", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        titleLabel.setPreferredSize(new Dimension(180, 40));
        titlePanel.add(titleLabel);
        this.add(titlePanel, BorderLayout.NORTH);
        
        
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
        
        couponTable = new JTable(couponModel);

        // TableRowSorter 초기화
        sorter = new TableRowSorter<>(couponModel);
        sorter.setComparator(2, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return Integer.compare(gradeOrder.indexOf(o1), gradeOrder.indexOf(o2));
            }
        });
        couponTable.setRowSorter(sorter);

        // 테이블 헤더 클릭 이벤트
        couponTable.getTableHeader().addMouseListener(new MouseAdapter() {
           

            @Override
            public void mouseClicked(MouseEvent e) {
                int column = couponTable.columnAtPoint(e.getPoint());
                if (column == 2) { // "등급" 열
                    // 우선순위 순환 (silver -> gold -> platinum -> diamond)
                    Collections.rotate(gradeOrder, -1);
                    sorter.sort(); // 강제 정렬 갱신
                }
            }
        });

        

        // 스크롤 추가
        scrollpane = new JScrollPane(couponTable);
        couponPanel.add(scrollpane, BorderLayout.CENTER);

        // 이 JPanel(AdminCouponSetting)에 couponPanel 추가
        this.add(couponPanel, BorderLayout.CENTER);

        // DB에서 데이터 불러오기
        loadCouponData();
        
     // 정렬을 위한 이벤트 리스너 추가 (예: 등급 컬럼 기준 정렬)
        couponTable.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int column = couponTable.columnAtPoint(e.getPoint());
                if (column == 2) { // "등급" 컬럼
                    sorter.toggleSortOrder(column);
                }
            }
        });
        
        
        
        couponTable.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) { // 클릭감지
                    int selectedRow = couponTable.getSelectedRow();
                    if (selectedRow != -1) {
                    	// 선택된 행의 데이터 가져오기
                        String name = (String) couponTable.getValueAt(selectedRow, 0).toString();
                        String productName = (String) couponTable.getValueAt(selectedRow, 1).toString();
                        String grade = (String) couponTable.getValueAt(selectedRow, 2).toString();
                        String rate = (String) couponTable.getValueAt(selectedRow, 3).toString();
                        String fixed = (String) couponTable.getValueAt(selectedRow, 4).toString();
                        String status = (String) couponTable.getValueAt(selectedRow, 5).toString();
                        
                        int modelRow = couponTable.convertRowIndexToModel(selectedRow);
                        kiosk.adminVO.CouponSettingVO coupon = couponList.get(modelRow);
          
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

    
     //DB에서 쿠폰 데이터를 불러와 테이블에 표시
     
    public void loadCouponData() {
    	
    	 // 테이블 초기화
        couponModel.setRowCount(0);
    	
    	
    	
        try {
            // MyBatis SqlSession 생성
            SqlSession ss = mainFrame.factory.openSession(); // MainFrame에서 제공된 factory 사용
            couponList = ss.selectList("couponSetting.couponSet"); // Mapper 호출
            ss.close();

           

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