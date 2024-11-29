
package kiosk.adminVO;

public class productVO {
  int idx;
  String productName;
  String productPrice;
  String productCategory;
  boolean productRecommendStatus;
  boolean productSaleStatus;
  String productImagePath;
  String productRegDate;

  public String getProductRegDate() {
    return productRegDate;
  }

  public void setProductRegDate(String productRegDate) {
    this.productRegDate = productRegDate;
  }

  // Getters and Setters
  public int getIdx() {
    return idx;
  }

  public void setIdx(int idx) {
    this.idx = idx;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public String getProductPrice() {
    return productPrice;
  }

  public void setProductPrice(String productPrice) {
    this.productPrice = productPrice;
  }

  public String getProductCategory() {
    return productCategory;
  }

  public void setProductCategory(String productCategory) {
    this.productCategory = productCategory;
  }

  public boolean isProductRecommendStatus() {
    return productRecommendStatus;
  }

  public void setProductRecommendStatus(boolean productRecommendStatus) {
    this.productRecommendStatus = productRecommendStatus;
  }

  public boolean isProductSaleStatus() {
    return productSaleStatus;
  }

  public void setProductSaleStatus(boolean productSaleStatus) {
    this.productSaleStatus = productSaleStatus;
  }

  public String getProductImagePath() {
    return productImagePath;
  }

  public void setProductImagePath(String productImagePath) {
    this.productImagePath = productImagePath;
  }
}
