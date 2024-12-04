package kiosk.client;

import java.text.DecimalFormat;

public class CurrencyFormatter {
  private static final DecimalFormat formatter = new DecimalFormat("#,###");

  public static String format(int amount) {
    return formatter.format(amount);
  }
}
