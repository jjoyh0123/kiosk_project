package kiosk.adminUserLogManagement;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import kiosk.client.MainFrame;

public class AdminUserLog extends JPanel {

	private JButton allSelectBtn; // 전체선택 버튼
	private JButton numberSearchBtn; // 번호검색 버튼
	private JPanel topPanel; // 상단 버튼 패널
	private JPanel contentPanel; // 중앙 데이터 패널
	private JLabel dateLabel; // 현재 검색 날짜를 표시할 라벨
	private JButton prevMonthBtn; // 이전 달 버튼
	private JButton nextMonthBtn; // 다음 달 버튼
	private LocalDate currentDate; // 현재 선택된 날짜 (기본: 오늘)
	private final LocalDate today; // 오늘 날짜
	private final LocalDate minDate; // 검색 가능한 최소 날짜 (3개월 전)
	private final LocalDate maxDate; // 검색 가능한 최대 날짜 (12월)

	public AdminUserLog(MainFrame mainFrame) {
		// 오늘 날짜 및 검색 범위 설정
		today = LocalDate.now(); // 2024년 12월을 기준으로 시작
		minDate = today.minusMonths(2); // 3개월 전 (2024년 9월)
		maxDate = today.withMonth(12).withDayOfMonth(today.lengthOfMonth());
		currentDate = today; // 기본 날짜: 오늘

		// 패널 레이아웃 설정
		setLayout(new BorderLayout());

		// 상단 버튼 패널 초기화
		topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // 버튼 가운데 정렬
		allSelectBtn = new JButton("전체선택");
		numberSearchBtn = new JButton("번호검색");

		// 상단 패널에 버튼 추가
		topPanel.add(allSelectBtn);
		topPanel.add(numberSearchBtn);

		// 메인 패널에 상단 패널 추가
		add(topPanel, BorderLayout.NORTH);

		// 중앙 데이터 패널 초기화
		contentPanel = new JPanel();
		contentPanel.setBackground(Color.WHITE); // 데이터 영역 배경색 설정
		add(contentPanel, BorderLayout.CENTER);

		// 날짜 표시 라벨 초기화
		currentDate = LocalDate.now(); // 기본적으로 오늘 날짜로 설정
		dateLabel = new JLabel(getFormattedDate(currentDate), JLabel.CENTER);
		dateLabel.setFont(dateLabel.getFont().deriveFont(18.0f)); // 라벨 텍스트 크기 설정

		// 이전/다음 달 버튼 추가
		prevMonthBtn = new JButton("◀");
		nextMonthBtn = new JButton("▶");

		prevMonthBtn.addActionListener(e -> updateDate(-1)); // 이전 달
		nextMonthBtn.addActionListener(e -> updateDate(1)); // 다음 달

		// 버튼 상태 업데이트
		updateButtonState();

		contentPanel.add(prevMonthBtn, BorderLayout.WEST);
		contentPanel.add(dateLabel, BorderLayout.CENTER);
		contentPanel.add(nextMonthBtn, BorderLayout.EAST);

		// 메인 패널에 중앙 패널 추가
		add(contentPanel, BorderLayout.CENTER);

		// 전체선택 버튼 이벤트
		allSelectBtn.addActionListener(e -> loadAllData());

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
	}

	// 날짜 업데이트 메서드
	private void updateDate(int monthOffset) {
		if (currentDate.getMonthValue() == 12 && monthOffset > 0) {
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
	}

	// 전체 데이터를 불러오는 메서드
	private void loadAllData() {
		// 여기에 데이터를 로드하는 로직 추가
		System.out.println("전체 데이터 로드 로직 구현");
	}

	// 번호로 데이터를 검색하는 메서드
	private void searchByNumber(String number) {
		System.out.println("검색한 번호: " + number);
	}

}
