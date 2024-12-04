package kiosk.userMenuManagement;

import org.apache.ibatis.session.SqlSession;

import Create.RoundedButton;
import kiosk.admin.Admin;
import kiosk.adminVO.UserVO;
import kiosk.client.MainFrame;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class AdminUserManagementPanel extends JPanel {

  private DefaultTableModel tableModel;
  private JTable table;
  private MainFrame mainFrame;

  public AdminUserManagementPanel(MainFrame mainFrame) {
    this.mainFrame = mainFrame;
    setLayout(new BorderLayout());

    // 상단: 회원관리 레이블 및 버튼 패널
    JPanel headerPanel = new JPanel(new BorderLayout());

    // 회원관리 레이블
    JPanel titlepanel = new JPanel(new BorderLayout());
    JLabel titleLabel = new JLabel("회원관리");
    titleLabel.setFont(new Font("aria", Font.BOLD, 18));
    titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 0)); //여백

    //headerPanel.add(titleLabel, BorderLayout.WEST);
    titlepanel.add(titleLabel, BorderLayout.WEST);
    titlepanel.setBackground(new Color(190 ,190, 190)); // 상단 패널 배경색 설정 (밝은 회색)

    // 버튼 패널
    JPanel buttonPanel = new JPanel();
    JButton viewAllButton = new RoundedButton("전체보기");
    viewAllButton.setFont(new Font("맑은고딕", Font.BOLD, 18));
    viewAllButton.setPreferredSize(new Dimension(90,40));
    JButton searchByNumberButton = new RoundedButton("번호검색");
    searchByNumberButton.setFont(new Font("맑은고딕", Font.BOLD, 18));
    searchByNumberButton.setPreferredSize(new Dimension(90,40));
    buttonPanel.add(viewAllButton);
    buttonPanel.add(searchByNumberButton);
    headerPanel.add(buttonPanel, BorderLayout.SOUTH);
    titlepanel.setPreferredSize(new Dimension(500,35)); 
     
    headerPanel.add(titlepanel, BorderLayout.NORTH);
    add(headerPanel, BorderLayout.NORTH);

    // 테이블 모델 생성
    String[] columnNames = {"고객번호", "가입일", "고객등급", "주문내역", "쿠폰보기", "쿠폰지급", "userIdx(숨김)"};
    tableModel = new DefaultTableModel(columnNames, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false; // 모든 셀 편집 불가능
      }
    };

    // 테이블 생성 및 스크롤 추가
    table = new JTable(tableModel);
    JTableHeader header = table.getTableHeader();
    Font currentFont = header.getFont();

    // 현재 폰트의 스타일과 크기를 유지하면서 굵게 만듭니다.
    Font boldFont = new Font(currentFont.getName(), Font.BOLD, currentFont.getSize());

    // 새로운 폰트를 테이블 헤더에 적용합니다.
    header.setFont(boldFont);
    centerTableContent();
    JScrollPane tableScrollPane = new JScrollPane(table);
    add(tableScrollPane, BorderLayout.CENTER);

    // idx는 비교만 하고 정보는 안 보이게 하기 때문에 idx 열 숨기기
    table.getColumnModel().getColumn(6).setMinWidth(0);
    table.getColumnModel().getColumn(6).setMaxWidth(0);
    table.getColumnModel().getColumn(6).setWidth(0);

    // 버튼 이벤트 추가
    viewAllButton.addActionListener(e -> {
      tableModel.setRowCount(0); // 기존 데이터를 초기화
      loadUserData(); // 모든 사용자 데이터 로드
    });

    searchByNumberButton.addActionListener(e -> {
      Admin owner = (Admin) SwingUtilities.getWindowAncestor(this); // Admin 창 참조
      new AdminUserContactSearchJdialog(owner, input -> searchByNumber(input)).setVisible(true);
    });

    // 테이블 클릭 이벤트 추가
    table.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        int row = table.getSelectedRow();
        int column = table.getSelectedColumn();

        if (column == 3) { // "주문내역 버튼" 클릭
          handleOrderList(row);
        } else if (column == 4) { // "쿠폰보기 버튼" 클릭
          handleCouponView(row);
        } else if (column == 5) { // "쿠폰지급 버튼" 클릭
          handleCouponPayment(row);
        }
      }
    });

    // 초기 데이터 로드
    loadUserData();
  }

  private void handleOrderList(int row) {
    Object userIdxObj = tableModel.getValueAt(row, 6);
    if (userIdxObj instanceof Integer) {
      int userIdx = (Integer) userIdxObj;
      Admin owner = (Admin) SwingUtilities.getWindowAncestor(table);
      new AdminUserOrderListDialog(owner, mainFrame, userIdx).setVisible(true);
    } else {
      System.err.println("Invalid userIdx type: " + userIdxObj);
    }
  }

  private void handleCouponView(int row) {
    Object userIdxObj = tableModel.getValueAt(row, 6);
    String userContact = (String) tableModel.getValueAt(row, 0);
    String userGrade = (String) tableModel.getValueAt(row, 2);

    if (userIdxObj instanceof Integer) {
      int userIdx = (Integer) userIdxObj;
      Admin owner = (Admin) SwingUtilities.getWindowAncestor(table);
      new AdminUserCouponViewDialog(owner, userIdx, userGrade, userContact, mainFrame);
    } else {
      System.err.println("Invalid userIdx type: " + userIdxObj);
    }
  }

  private void handleCouponPayment(int row) {
    Object userIdxObj = tableModel.getValueAt(row, 6);
    String userContact = (String) tableModel.getValueAt(row, 0);
    String userGrade = (String) tableModel.getValueAt(row, 2);

    if (userIdxObj instanceof Integer) {
      int userIdx = (Integer) userIdxObj;
      Admin owner = (Admin) SwingUtilities.getWindowAncestor(table);
      new AdminUserCouponPayment(owner, userIdx, userGrade, userContact, mainFrame);
    } else {
      System.err.println("Invalid userIdx type: " + userIdxObj);
    }
  }

  private void loadUserData() {
    try (SqlSession session = mainFrame.factory.openSession()) {
      List<UserVO> userList = session.selectList("adminUserManagement.selectAllUsers");
      tableModel.setRowCount(0); // 기존 데이터 초기화
      for (UserVO user : userList) {
        String formattedContact = user.getUserIdx() == 1 ? "비회원" : formatPhoneNumber(user.getUserContact());

        tableModel.addRow(new Object[]{
            formattedContact,
            user.getUserJoinDate(),
            user.getUserGrade(),
            "주문내역 보기",
            "쿠폰보기 보기",
            "쿠폰지급 보기",
            user.getUserIdx()
        });
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void searchByNumber(String number) {
    try (SqlSession session = mainFrame.factory.openSession()) {
      List<UserVO> userList = session.selectList("adminUserManagement.searchByNumber", "%" + number + "%");
      tableModel.setRowCount(0); // 기존 데이터 초기화
      for (UserVO user : userList) {
        tableModel.addRow(new Object[]{
            user.getUserContact(),
            user.getUserJoinDate(),
            user.getUserGrade(),
            "주문내역 버튼",
            "쿠폰보기 버튼",
            "쿠폰지급 버튼"
        });
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void centerTableContent() {
    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
    centerRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER); // 가운데 정렬

    for (int i = 0; i < table.getColumnCount(); i++) {
      table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
    }
  }

  private String formatPhoneNumber(String phoneNumber) {
    if (phoneNumber == null || phoneNumber.isEmpty()) {
      return phoneNumber;
    }

    if (phoneNumber.length() == 11) {
      return phoneNumber.substring(0, 3) + "-" + phoneNumber.substring(3, 7) + "-" + phoneNumber.substring(7);
    } else if (phoneNumber.length() == 10) {
      return phoneNumber.substring(0, 3) + "-" + phoneNumber.substring(3, 6) + "-" + phoneNumber.substring(6);
    } else {
      return phoneNumber;
    }
  }
}
