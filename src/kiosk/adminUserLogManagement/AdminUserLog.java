package kiosk.adminUserLogManagement;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import Create.RoundedButton;
import org.apache.ibatis.session.SqlSession;

import kiosk.adminVO.UserLogVO;
import kiosk.client.MainFrame;

public class AdminUserLog extends JPanel {

  MainFrame mainFrame;

  private JButton allSelectBtn; // 전체선택 버튼
  private JButton numberSearchBtn; // 번호검색 버튼
  private JPanel topPanel, titlePanel; // 상단 버튼 패널
  private JPanel contentPanel; // 중앙 데이터 패널
  private JLabel dateLabel; // 현재 검색 날짜를 표시할 라벨
  private JButton prevMonthBtn; // 이전 달 버튼
  private JButton nextMonthBtn; // 다음 달 버튼
  private LocalDate currentDate; // 현재 선택된 날짜
  private LocalDate today; // 오늘 날짜
  private LocalDate minDate; // 검색 가능한 최소 날짜
  private LocalDate maxDate; // 검색 가능한 최대 날짜
  private JTable logTable;
  List<UserLogVO> userLogList;
  DefaultTableModel userLogModel;
  JScrollPane scrollPane;

  String[] columnNames = {"고객연락처", "등급"};

  public AdminUserLog(MainFrame mainFrame) {
    this.mainFrame = mainFrame;

    // 오늘 날짜 및 검색 범위 설정
    today = LocalDate.now();
    minDate = today.minusMonths(3);
    maxDate = today.minusMonths(1).withDayOfMonth(today.minusMonths(1).lengthOfMonth());
    currentDate = today;

    // 패널 레이아웃 설정
    setLayout(new BorderLayout());

    // 상단 버튼 패널 초기화
    JPanel mainpanel = new JPanel(new BorderLayout());
    JPanel northJPanel = new JPanel(new BorderLayout());
    JLabel northlable = new JLabel("성향분석");
    northlable.setFont(new Font("맑은고딕", Font.BOLD, 18));
    northlable.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 0)); // 여백 추가
    northJPanel.add(northlable,BorderLayout.WEST);
    northJPanel.setPreferredSize(new Dimension(500,35));
    topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // 버튼 가운데 정렬
    northJPanel.setBackground(new Color(190 ,190, 190)); // 상단 패널 배경색 설정 (밝은 회색)
    allSelectBtn = new RoundedButton("전체선택");
    allSelectBtn.setFont(new Font("맑은고딕", Font.BOLD, 18));
    allSelectBtn.setPreferredSize(new Dimension(90,40));
    numberSearchBtn = new RoundedButton("번호검색");
    numberSearchBtn.setFont(new Font("맑은고딕", Font.BOLD, 18));
    numberSearchBtn.setPreferredSize(new Dimension(90,40));

    // 상단 패널에 버튼 추가
    topPanel.add(allSelectBtn);
    topPanel.add(numberSearchBtn);

    JLabel title = new JLabel("성향분석");
    JPanel titlePanel = new JPanel();
    titlePanel.add(title);
    title.setFont(new Font("맑은고딕", Font.BOLD, 25));
    // 메인 패널에 상단 패널 추가
    mainpanel.add(northJPanel,BorderLayout.NORTH);
    mainpanel.add(topPanel,BorderLayout.CENTER);

    // 중앙 데이터 패널 초기화
    contentPanel = new JPanel();

    //contentPanel.setBackground(Color.WHITE); // 데이터 영역 배경색 설정
    //add(contentPanel, BorderLayout.CENTER);

    // 날짜 표시 라벨 초기화
    currentDate = LocalDate.now(); // 기본적으로 오늘 날짜로 설정
    dateLabel = new JLabel(getFormattedDate(currentDate), JLabel.CENTER);
    dateLabel.setFont(new Font("맑은고딕", Font.BOLD, 20));

    // 이전/다음 달 버튼 추가
    prevMonthBtn = new RoundedButton("◀");
    prevMonthBtn.setPreferredSize(new Dimension(70,25));
    nextMonthBtn = new RoundedButton("▶");
    nextMonthBtn.setPreferredSize(new Dimension(70,25));
    prevMonthBtn.addActionListener(e -> updateDate(-1)); // 이전 달
    nextMonthBtn.addActionListener(e -> updateDate(1)); // 다음 달

    // 버튼 상태 업데이트
    updateButtonState();

    contentPanel.add(prevMonthBtn, BorderLayout.WEST);
    contentPanel.add(dateLabel, BorderLayout.CENTER);
    contentPanel.add(nextMonthBtn, BorderLayout.EAST);



    // 테이블 초기화
    userLogModel = new DefaultTableModel(columnNames, 0); // 컬럼명 설정
    logTable = new JTable(userLogModel) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };
 // 테이블 내용 가운데 정렬 설정
    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
    centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

    // 각 열에 가운데 정렬 렌더러 적용
    for (int i = 0; i < logTable.getColumnModel().getColumnCount(); i++) {
        logTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
    }

    // 스크롤 추가
    JTableHeader header = logTable.getTableHeader();
    Font currentFont = header.getFont();

    // 현재 폰트의 스타일과 크기를 유지하면서 굵게 만듭니다.
    Font boldFont = new Font(currentFont.getName(), Font.BOLD, currentFont.getSize());

    // 새로운 폰트를 테이블 헤더에 적용합니다.
    header.setFont(boldFont);
    scrollPane = new JScrollPane(logTable);
    contentPanel.add(scrollPane, BorderLayout.CENTER);
    contentPanel.setPreferredSize(new Dimension(500,690));

    mainpanel.add(contentPanel,BorderLayout.SOUTH);
    add(mainpanel);

    // 버튼 이벤트 예제
    allSelectBtn.addActionListener(e -> {
      JOptionPane.showMessageDialog(this, "전체 데이터를 불러옵니다.");
      loadAllData();
    });

    // 번호검색 버튼 이벤트
    numberSearchBtn.addActionListener(e -> {
      AdminUserSeachDialog dialog = new AdminUserSeachDialog(JOptionPane.getFrameForComponent(this), // 다이얼로그의 부모

              this::searchByNumber);
      dialog.setLocationRelativeTo(this); // 현재 화면 중앙에 다이얼로그 표시
      dialog.setVisible(true); // 다이얼로그 표시
    });

    // DB에서 데이터 불러오기
    searchByDate(currentDate);

    logTable.addMouseListener(new MouseAdapter() {

      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 1) { // 클릭 감지
          int selectedRow = logTable.getSelectedRow();
          if (selectedRow != -1) {
            // 선택된 행의 데이터 가져오기
            String userContact = logTable.getValueAt(selectedRow, 0).toString();
            String usergrade = (String) logTable.getValueAt(selectedRow, 1).toString();

            kiosk.adminVO.UserLogVO log = userLogList.get(selectedRow);

            showDialog(log);
          }
        }
      }
    });

  }

  // 조회된 테이블을 선택했을때 뜨는 다이얼로그
  private void showDialog(UserLogVO log) {
    AdminUserTableLogDialog dialog = new AdminUserTableLogDialog(mainFrame, SwingUtilities.getWindowAncestor(this), this, log);
    dialog.setVisible(true);
    dialog.setLocationRelativeTo(null);
  }


  // 날짜 업데이트 메서드
  private void updateDate(int monthOffset) {
    if (currentDate.getMonthValue() == 11 && monthOffset > 0) {
      // 12월을 넘어가는 경우, 아무것도 하지 않고 종료
      nextMonthBtn.setEnabled(false); // 12월이면 버튼 비활성화
      return;
    }

    // 새로운 날짜 계산
    LocalDate newDate = currentDate.plusMonths(monthOffset); // newDate는 한 번만 선언

    if (currentDate.getMonthValue() == 12) {
      nextMonthBtn.setEnabled(false); // 12월이면 비활성화
    } else {
      nextMonthBtn.setEnabled(true); // 그 외에는 활성화
    }

    currentDate = newDate; // 날짜 업데이트
    dateLabel.setText(getFormattedDate(currentDate)); // 라벨 업데이트
    searchByDate(currentDate); // 해당 월의 데이터를 검색하는 메서드 호출
    updateButtonState(); // 버튼 상태 업데이트
  }

  // 버튼 상태 업데이트 (3개월 범위에 따라 버튼 비활성화)
  private void updateButtonState() {
    // 이전 달 버튼 비활성화 조건
    prevMonthBtn.setEnabled(!currentDate.isEqual(minDate));

    // 다음 달 버튼 비활성화 조건
    nextMonthBtn.setEnabled(currentDate.isBefore(maxDate));
  }

  // 날짜 형식 변환 메서드
  private String getFormattedDate(LocalDate date) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
    return date.format(formatter);
  }

  // 특정 월의 데이터를 검색하는 메서드
  private void searchByDate(LocalDate date) {
    System.out.println("검색된 날짜의 데이터 검색: " + getFormattedDate(date));
    // TODO: DB 연동 및 데이터 로드 로직 추가
    try {
      String yearMonth = date.format(DateTimeFormatter.ofPattern("yyyy-MM"));

      // MyBatis SqlSession 생성
      SqlSession ss = mainFrame.factory.openSession(); // MainFrame에서 제공된 factory 사용
      userLogList = ss.selectList("searchUserData.getUserData2", yearMonth); // Mapper 호출
      ss.close();

      // 테이블 초기화
      userLogModel.setRowCount(0);

      // 쿠폰 데이터를 테이블에 추가
      for (UserLogVO log : userLogList) {
        userLogModel.addRow(new Object[]{
                log.getUserContact(),
                log.getLogGrade(),
                log.getTop1ProductName(),
                log.getTop2ProductName(),
                log.getTop3ProductName(),
        });
      }

      contentPanel.revalidate();
      contentPanel.repaint();
      logTable.revalidate();
      logTable.repaint();
    } catch (Exception e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(this, "데이터를 불러오는 중 오류가 발생했습니다.");
    }
  }

  // 전체 데이터를 불러오는 메서드
  private void loadAllData() {
    // 여기에 데이터를 로드하는 로직 추가
    System.out.println("전체 데이터 로드 로직 구현");
    try {
      // MyBatis SqlSession 생성
      SqlSession ss = mainFrame.factory.openSession(); // MainFrame에서 제공된 factory 사용
      userLogList = ss.selectList("searchUserData.getUserData"); // Mapper 호출
      ss.close();

      // 테이블 초기화
      userLogModel.setRowCount(0);

      // 로그 데이터를 테이블에 추가
      for (UserLogVO log : userLogList) {
        userLogModel.addRow(new Object[]{
                log.getUserContact(),
                log.getLogGrade(),
                log.getTop1ProductName(),
                log.getTop2ProductName(),
                log.getTop3ProductName(),
        });
      }

      contentPanel.revalidate();
      contentPanel.repaint();
      logTable.revalidate();
      logTable.repaint();
    } catch (Exception e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(this, "데이터를 불러오는 중 오류가 발생했습니다.");
    }
  }

  // 번호로 데이터를 검색하는 메서드
  public void searchByNumber(String number) {
    try {
      System.out.println("검색한 번호: " + number);
      SqlSession ss = mainFrame.factory.openSession(); // MainFrame에서 제공된 factory 사용
      userLogList = ss.selectList("searchUserData.searchByNumber", number); // Mapper 호출
      ss.close();

      // 테이블 초기화
      userLogModel.setRowCount(0);

      // 조회된 데이터로 테이블에 추가
      for (UserLogVO log : userLogList) {
        userLogModel.addRow(new Object[]{
                log.getUserContact(),
                log.getLogGrade(),
        });
      }

      contentPanel.revalidate();
      contentPanel.repaint();
      logTable.revalidate();
      logTable.repaint();
    } catch (Exception e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(this, "번호로 데이터를 불러오는 중 오류가 발생했습니다.");
    }
  }
}