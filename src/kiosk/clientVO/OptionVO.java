package kiosk.clientVO;

public class OptionVO {

    private int optionIdx; // 옵션 고유 ID
    private String optionName; // 옵션 이름
    private int optionPrice; // 옵션 가격
    private int optionCategory; // 옵션 카테고리 (1: 프라이, 2: 음료 등)
    private int optionCalorie; // 옵션 칼로리
    private boolean optionSalesStatus; // 판매 상태 (true: 판매 중, false: 판매 중지)

    // Getter and Setter
    public int getOptionIdx() {
        return optionIdx;
    }

    public void setOptionIdx(int optionIdx) {
        this.optionIdx = optionIdx;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public int getOptionPrice() {
        return optionPrice;
    }

    public void setOptionPrice(int optionPrice) {
        this.optionPrice = optionPrice;
    }

    public int getOptionCategory() {
        return optionCategory;
    }

    public void setOptionCategory(int optionCategory) {
        this.optionCategory = optionCategory;
    }

    public int getOptionCalorie() {
        return optionCalorie;
    }

    public void setOptionCalorie(int optionCalorie) {
        this.optionCalorie = optionCalorie;
    }

    public boolean isOptionSalesStatus() {
        return optionSalesStatus;
    }

    public void setOptionSalesStatus(boolean optionSalesStatus) {
        this.optionSalesStatus = optionSalesStatus;
    }

    // toString() 메서드 추가
    @Override
    public String toString() {
        return optionName + " (" + optionPrice + "원)";
    }
}
