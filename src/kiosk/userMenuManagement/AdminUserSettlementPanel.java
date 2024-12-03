package kiosk.userMenuManagement;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import org.apache.ibatis.session.SqlSession;
import kiosk.adminVO.OrderVO;
import kiosk.client.MainFrame;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminUserSettlementPanel extends JPanel {
    JLabel dateLabel;
    JLabel totalAmountLabel;
    JButton dailyViewButton, monthlyViewButton, yearlyViewButton, inputDateButton;
    JButton prevPeriodButton, nextPeriodButton;
    JTable table;
    DefaultTableModel tableModel;
    String viewMode = "daily"; // Default mode
    LocalDate currentDate = LocalDate.now();
    MainFrame mainFrame;

    public AdminUserSettlementPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());

        // 상단: 합계 및 날짜 이동 패널
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        JLabel settlementLabel = new JLabel("정산", JLabel.CENTER);
        settlementLabel.setFont(new Font("맑은고딕", Font.BOLD, 35));
        settlementLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 여백 추가
        topPanel.add(settlementLabel, BorderLayout.NORTH);


        // 합계 표시 패널
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        
        JLabel titleLabel = new JLabel("쿠폰 보기", JLabel.CENTER);
        titleLabel.setFont(new Font("맑은고딕", Font.PLAIN, 35));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 여백 추가
        topPanel.add(titleLabel, BorderLayout.NORTH);
        totalAmountLabel = new JLabel("총매출: 0원");
        totalPanel.add(totalAmountLabel);

        // 날짜 이동 및 보기 모드 버튼 패널
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BorderLayout());

        // 날짜 이동 버튼 패널
        JPanel dateNavigationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        prevPeriodButton = new JButton("<");
        dateLabel = new JLabel(currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        nextPeriodButton = new JButton(">");
        dateNavigationPanel.add(prevPeriodButton);
        dateNavigationPanel.add(dateLabel);
        dateNavigationPanel.add(nextPeriodButton);

        // 보기 모드 버튼 패널
        JPanel viewButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        dailyViewButton = new JButton("일별보기");
        monthlyViewButton = new JButton("월별보기");
        yearlyViewButton = new JButton("연별보기");
        inputDateButton = new JButton("기간 입력");
        viewButtonPanel.add(dailyViewButton);
        viewButtonPanel.add(monthlyViewButton);
        viewButtonPanel.add(yearlyViewButton);
        viewButtonPanel.add(inputDateButton);

        // 패널 구조 조립
        controlPanel.add(viewButtonPanel, BorderLayout.NORTH);
        controlPanel.add(dateNavigationPanel, BorderLayout.SOUTH);
        topPanel.add(totalPanel, BorderLayout.NORTH);
        topPanel.add(controlPanel, BorderLayout.SOUTH);

        // 상단 패널 추가
        add(topPanel, BorderLayout.NORTH);

        // 테이블 섹션
        String[] columnNames = {"", "주문번호", "회원번호", "주문 일자", "주문금액"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 테이블 데이터 수정 불가
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(20); // 행 높이를 줄임
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // 테이블 스크롤 패널
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(tableScrollPane, BorderLayout.CENTER);

        // 이벤트 리스너 등록
        registerEventHandlers();
        loadTableData();
    }

    private void registerEventHandlers() {
        // "일별보기" 버튼
        dailyViewButton.addActionListener(e -> changeViewMode("daily"));

        // "월별보기" 버튼
        monthlyViewButton.addActionListener(e -> changeViewMode("monthly"));

        // "연별보기" 버튼
        yearlyViewButton.addActionListener(e -> changeViewMode("yearly"));

        // "기간 입력" 버튼
        inputDateButton.addActionListener(e -> showDateInputDialog());

        // 이전 기간 버튼
        prevPeriodButton.addActionListener(e -> changePeriod(-1));

        // 다음 기간 버튼
        nextPeriodButton.addActionListener(e -> changePeriod(1));
    }

    private void changeViewMode(String mode) {
        viewMode = mode;   // 뷰 모드 변경
        updateDateLabel(); // 레이블 업데이트
        loadTableData();   // 데이터를 다시 로드
    }

    private void changePeriod(int direction) {
        switch (viewMode) {
            case "daily":
                currentDate = currentDate.plusDays(direction);
                break;
            case "monthly":
                currentDate = currentDate.plusMonths(direction);
                break;
            case "yearly":
                currentDate = currentDate.plusYears(direction);
                break;
        }
        updateDateLabel(); // 날짜 레이블 업데이트
        loadTableData();   // 데이터 로드
    }

    private void updateDateLabel() {
        switch (viewMode) {
            case "daily":
                dateLabel.setText(currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                break;
            case "monthly":
                dateLabel.setText(currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM")));
                break;
            case "yearly":
                dateLabel.setText(currentDate.format(DateTimeFormatter.ofPattern("yyyy")));
                break;
        }
    }

    private void loadTableData() {
        tableModel.setRowCount(0); // 기존 데이터 초기화
        try (SqlSession session = mainFrame.factory.openSession()) {
            String queryId = null;
            String dateParam = null;

            // 뷰 모드에 따라 쿼리 및 날짜 파라미터 설정
            switch (viewMode) {
                case "daily":
                    queryId = "adminUserManagement.getDailyOrders";
                    dateParam = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); // 일별
                    break;
                case "monthly":
                    queryId = "adminUserManagement.getMonthlyOrders";
                    dateParam = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM")); // 월별
                    break;
                case "yearly":
                    queryId = "adminUserManagement.getYearlyOrders";
                    dateParam = currentDate.format(DateTimeFormatter.ofPattern("yyyy")); // 연별
                    break;
            }

            // 데이터 조회
            if (queryId != null && dateParam != null) {
                List<OrderVO> results = session.selectList(queryId, dateParam);
                int rowNum = 1;
                int total = 0;
                for (OrderVO order : results) {
                    tableModel.addRow(new Object[]{
                        rowNum++, 
                        order.getOrderNumber(), 
                        order.getUserContact(), 
                        order.getOrderDate(), 
                        order.getTotalOrderPrice()
                    });
                    total += order.getTotalOrderPrice();
                }

                // 합계 계산 및 상단 레이블 업데이트
                totalAmountLabel.setText("총 매출: " + total + "원");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "데이터를 불러오는 데 실패했습니다.");
        }
    }

    private void showDateInputDialog() {
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        JFrame parentFrame = parentWindow instanceof JFrame ? (JFrame) parentWindow : null;
        AdminUserSettlementDayDialog dialog = new AdminUserSettlementDayDialog();
        dialog.setVisible(true);

        if (dialog.isApplied()) {
            String startDate = dialog.getStartDate();
            String endDate = dialog.getEndDate();
            dateLabel.setText(startDate + " ~ " + endDate);
            loadTableDataForPeriod(startDate, endDate);
        }
    }

    private void loadTableDataForPeriod(String startDate, String endDate) {
        tableModel.setRowCount(0); // 기존 데이터 초기화
        try (SqlSession session = mainFrame.factory.openSession()) {
            // 매개변수 설정
            Map<String, Object> params = new HashMap<>();
            params.put("startDate", startDate);
            params.put("endDate", endDate);

            // 쿼리 실행
            List<OrderVO> results = session.selectList("adminUserManagement.getOrdersForPeriod", params);

            int total = 0;
            for (int i = 0; i < results.size(); i++) {
                OrderVO order = results.get(i);
                tableModel.addRow(new Object[]{
                    i + 1,
                    order.getOrderNumber(),
                    order.getUserContact(),
                    order.getOrderDate(),
                    order.getTotalOrderPrice()
                });
                total += order.getTotalOrderPrice();
            }

            // 총 매출 업데이트
            totalAmountLabel.setText("총 매출: " + total + "원");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "데이터를 불러오는 데 실패했습니다.");
        }
    }

    private String maskContact(String contact) {
        if (contact != null && contact.length() == 11) {
            return contact.substring(0, 3);

        }
        return contact;
    }
}
