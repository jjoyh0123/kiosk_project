
package kiosk.config;

public class productVO {
    private int idx;
    private String productName;
    private String productPrice;
    private String productCategory;
    private boolean productRecommendStatus;
    private boolean productSaleStatus;
    private String productImagePath;

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
