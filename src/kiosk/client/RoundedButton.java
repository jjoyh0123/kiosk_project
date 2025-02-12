package kiosk.client;

import javax.swing.*;
import java.awt.*;

public class RoundedButton extends JButton {

  private Color defaultColor = Color.WHITE;   // 기본 색상
  private Color hoverColor = Color.LIGHT_GRAY;  // 마우스 오버 색상
  private Color pressedColor = Color.DARK_GRAY; // 눌림 색상
  private Color borderColor = Color.GREEN; // 테두리 색상
  private int borderThickness = 4; // 테두리 두께

  public RoundedButton(String text) {
    super(text);

    setContentAreaFilled(false);
    setFocusPainted(false);
    setBorderPainted(false);
    setBackground(defaultColor); // 초기 배경색 설정
  }

  @Override
  protected void paintComponent(Graphics g) {
    Graphics2D g2 = (Graphics2D) g.create();
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    // 눌림 상태 확인
    if (getModel().isRollover()) {
      g2.setColor(hoverColor); // 마우스 오버 상태일 때
    } else if (getModel().isPressed()) {
      g2.setColor(pressedColor); // 눌린 상태일 때
    } else {
      g2.setColor(defaultColor); // 기본 상태
    }
    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20); // 둥근 버튼

    // 버튼 테두리
    g2.setColor(borderColor);
    g2.setStroke(new BasicStroke(borderThickness));
    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

    // 텍스트 조정
    g2.setColor(getForeground());
    FontMetrics fm = g2.getFontMetrics();
    int x = (getWidth() - fm.stringWidth(getText())) / 2;
    int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
    System.out.println(fm.getDescent());
    g2.drawString(getText(), x, y);

    g2.dispose();
  }

  // 색상을 변경할 수 있는 메서드 추가
  public void setDefaultColor(Color defaultColor) {
    this.defaultColor = defaultColor;
    repaint(); // 변경된 색상을 적용
  }

  public void setHoverColor(Color hoverColor) {
    this.hoverColor = hoverColor;
    repaint();
  }

  public void setPressedColor(Color pressedColor) {
    this.pressedColor = pressedColor;
    repaint();
  }
}
