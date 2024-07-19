package ZohoProject;

public class Customer {

    private String customerName;
    private String customerPhoneNo;
    public double membershipPoints;
    public double customerbalance;

    // Constructor
    public Customer() {}

    public Customer(String customerPhoneNo,String customerName,double membershipPoints,double customerbalance) {
        this.customerName = customerName;
        this.customerPhoneNo = customerPhoneNo;
        this.membershipPoints=membershipPoints;
        this.customerbalance=customerbalance;
    }

    // Getters and Setters

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhoneNo() {
        return customerPhoneNo;
    }

    public void setCustomerPhoneNo(String customerPhoneNo) {
        this.customerPhoneNo = customerPhoneNo;
    }

    public double getMembershipPoints() {
        return membershipPoints;
    }

    public void setMembershipPoints(double membershipPoints) {
        this.membershipPoints = membershipPoints;
    }

    public double getCustomerbalance(){
        return customerbalance;
    }

    public void setCustomerbalance(double customerbalance){ 
        this.customerbalance=customerbalance;
    }
}
