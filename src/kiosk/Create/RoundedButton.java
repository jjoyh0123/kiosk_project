package kiosk.Create;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RoundedButton extends JButton {
    private int cornerRadius = 15; // 모서리 반경

    public RoundedButton() {
        super();
        decorate();
        addListeners();
    }

    public RoundedButton(String text) {
        super(text);
        decorate();
        addListeners();
    }

    public RoundedButton(Action action) {
        super(action);
        decorate();
        addListeners();
    }

    public RoundedButton(Icon icon) {
        super(icon);
        decorate();
        addListeners();
    }

    public RoundedButton(String text, Icon icon) {
        super(text, icon);
        decorate();
        addListeners();
    }

    protected void decorate() {
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR)); // 마우스 커서 변경
    }

    private void addListeners() {
        // 마우스 입력 및 종료 이벤트 처리
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // 마우스가 버튼 위로 올라갔을 때 동작
                setForeground(new Color(50, 50, 250)); // 글자색 변경 예제
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // 마우스가 버튼에서 벗어났을 때 동작
                setForeground(Color.BLACK); // 기본 글자색으로 복구
            }
        });

        // 클릭 이벤트 처리
        addActionListener(e -> {
            // 클릭 시 실행할 동작
            System.out.println("RoundedButton clicked: " + getText());
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // 버튼 상태에 따라 색상 변경
        Color gradientStart;
        Color gradientEnd;
        Color borderColor;

        if (getModel().isPressed()) {
            gradientStart = new Color(180, 180, 180); // 클릭 시
            gradientEnd = new Color(150, 150, 150);
            borderColor = new Color(120, 120, 120);
        } else if (getModel().isRollover()) {
            gradientStart = new Color(220, 220, 220); // Hover 시
            gradientEnd = new Color(200, 200, 200);
            borderColor = new Color(160, 160, 160);
        } else {
            gradientStart = new Color(240, 240, 240); // 기본 상태
            gradientEnd = new Color(200, 200, 200);
            borderColor = new Color(180, 180, 180);
        }

        // 그라데이션 배경 그리기
        GradientPaint gradient = new GradientPaint(0, 0, gradientStart, 0, height, gradientEnd);
        graphics.setPaint(gradient);
        graphics.fillRoundRect(0, 0, width, height, cornerRadius, cornerRadius);

        // 테두리 그리기
        graphics.setColor(borderColor);
        graphics.drawRoundRect(0, 0, width - 1, height - 1, cornerRadius, cornerRadius);

        // 글자 그리기
        graphics.setColor(getForeground());
        graphics.setFont(getFont());
        FontMetrics fontMetrics = graphics.getFontMetrics();
        Rectangle stringBounds = fontMetrics.getStringBounds(this.getText(), graphics).getBounds();
        int textX = (width - stringBounds.width) / 2;
        int textY = (height - stringBounds.height) / 2 + fontMetrics.getAscent();
        graphics.drawString(getText(), textX, textY);

        graphics.dispose();
    }

    @Override
    public void setContentAreaFilled(boolean b) {
        // 기본 JButton의 배경 그리기 방지
    }

    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
    }
}
