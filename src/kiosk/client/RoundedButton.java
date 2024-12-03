package kiosk.client;

import javax.swing.*;
import java.awt.*;

public class RoundedButton extends JButton {

  public RoundedButton(String text) {
    super((text));

    setContentAreaFilled(false);
    setFocusPainted(false);
    setBorderPainted(false);
  }

  @Override
  protected void paintComponent(Graphics g) {
    Graphics2D g2 = (Graphics2D) g.create();

    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    g2.setColor(getBackground());
    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20); // 모서리의 곡률 20px

    g2.setColor(getForeground());
    FontMetrics fm = g2.getFontMetrics();
    int x = (getWidth() - fm.stringWidth(getText())) / 2;
    int y = (getHeight() + fm.getAscent()) / 2 - 3;
    g2.drawString(getText(), x, y);

    g2.dispose();
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(100, 40);
  }
}
