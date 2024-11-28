package kiosk.clientVO;

public class ProductVO {

    int productIdx;
    String productName, productDescription;
    int productPrice, productCategory, productCalories;
    boolean productRecommendStatus, productSaleStatus;
    String productRegDate;

    public int getProductIdx() {
        return productIdx;
    }

    public void setProductIdx(int productIdx) {
        this.productIdx = productIdx;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(int productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public int getProductCalories() {
        return productCalories;
    }

    public void setProductCalories(int productCalories) {
        this.productCalories = productCalories;
    }

    public boolean isProductSaleStatus() {
        return productSaleStatus;
    }

    public void setProductSaleStatus(boolean productSaleStatus) {
        this.productSaleStatus = productSaleStatus;
    }

    public boolean isProductRecommendStatus() {
        return productRecommendStatus;
    }

    public void setProductRecommendStatus(boolean productRecommendStatus) {
        this.productRecommendStatus = productRecommendStatus;
    }

    public String getProductRegDate() {
        return productRegDate;
    }

    public void setProductRegDate(String productRegDate) {
        this.productRegDate = productRegDate;
    }
}
