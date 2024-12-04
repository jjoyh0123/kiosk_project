package Create;

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

    private void decorate() {
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR)); // 마우스 커서 변경
        setOpaque(false); // 배경 투명 설정
    }

    private void addListeners() {
        // 마우스 입력 및 종료 이벤트 처리
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setForeground(new Color(50, 50, 250)); // Hover 상태 글자색
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setForeground(Color.BLACK); // 기본 글자색 복구
            }
        });

        // 클릭 이벤트 처리
        addActionListener(e -> {
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
            gradientStart = new Color(180, 180, 180); // 클릭 상태
            gradientEnd = new Color(150, 150, 150);
            borderColor = new Color(120, 120, 120);
        } else if (getModel().isRollover()) {
            gradientStart = new Color(220, 220, 220); // Hover 상태
            gradientEnd = new Color(200, 200, 200);
            borderColor = new Color(160, 160, 160);
        } else {
            gradientStart = new Color(240, 240, 240); // 기본 상태
            gradientEnd = new Color(200, 200, 200);
            borderColor = new Color(180, 180, 180);
        }

        // 배경 그리기
        GradientPaint gradient = new GradientPaint(0, 0, gradientStart, 0, height, gradientEnd);
        graphics.setPaint(gradient);
        graphics.fillRoundRect(1, 1, width - 2, height - 2, cornerRadius, cornerRadius);

        // 테두리 그리기
        graphics.setColor(borderColor);
        graphics.drawRoundRect(1, 1, width - 3, height - 3, cornerRadius, cornerRadius);

        // 텍스트 그리기
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
