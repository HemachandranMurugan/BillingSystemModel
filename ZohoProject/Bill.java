package ZohoProject;

public class Bill {
    private String billNo;
    private String billDate;
    private String customerid;
    private double billamt;
    private double discount;
    private double totalAmount;

    public Bill() {
    }

    // Getters and Setters
    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo){
        this.billNo=billNo;
    }

    public double getDiscount(){
        return discount;
    }

    public void setDiscount(double discount){
        this.discount=discount;
    }
    public String getDate() {
        return
                billDate;
    }


    public void setDate(String billDate){

        this.billDate=billDate;
    }
    public String getCustomerid() {

        return customerid;
    }
    public void setCustomerid(String customerid){

        this.customerid=customerid;
    }


    public double getbillamt() {

        return billamt;
    }


    public void setbillamt(double billamt) {

        this.billamt = billamt;
    }

    public void setTotalAmt(double totalAmount) {

        this.totalAmount=totalAmount;
    }

    public double getTotalAmt() {

        return totalAmount;
    }
}

