package ZohoProject;

public class Product {
    private String productId;
    private String productName;
    private String productType;
    private int productQuantity;
    private double price;

    public Product(){

    }
    public Product(String productName, String productType,double price, int productQuantity) {
        this.productName = productName;
        this.productType = productType;
        this.productQuantity = productQuantity;
        this.price = price;
    }
    // Getters
    public String getProductId() {
        return productId;
    }
    public void setProductId(String productId) {
        this.productId = productId;
    }
    public void setProductName(String productName){
        this.productName=productName;
    }
    public String getProductName() {
        return productName;
    }
    public String getProductType() {
        return productType;
    }
    public int getProductQuantity() {
        return productQuantity;
    }
    public void setProductQuantity(int productQuantity){
        this.productQuantity=productQuantity;
    }
    public double getProductPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
}
