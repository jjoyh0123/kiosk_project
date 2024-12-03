package Create;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RoundedButton extends JButton {
    private Color normalBackground = new Color(0x3498db); // 기본 배경색
    private Color hoverBackground = new Color(0x2980b9);  // 호버 배경색
    private Color clickBackground = new Color(0x1abc9c);  // 클릭 배경색
    private Color disabledBackground = new Color(0xbdc3c7); // 비활성화 배경색

    public RoundedButton(String text) {
        super(text); // 버튼 텍스트 설정
        setFocusPainted(false); // 포커스 테두리 제거
        //setContentAreaFilled(false); // 버튼 배경 제거
       
         setForeground(Color.black); // 텍스트 색상 설정

    }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // 안티앨리어싱 활성화
        g2.setColor(isEnabled() ? getBackground() : disabledBackground); // 비활성화 상태 색상 처리
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30); // 둥근 사각형 그리기 (30px 모서리 반경)
        super.paintComponent(g); // 기본 텍스트 및 아이콘 그리기
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // 안티앨리어싱 활성화
        g2.setColor(getForeground()); // 버튼 테두리 색상 설정
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30); // 둥근 사각형 테두리 그리기
    }
}
