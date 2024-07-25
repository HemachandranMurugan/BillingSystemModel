package ZohoProject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.text.SimpleDateFormat;

public class BillingDAO {
    private final String jdbcURL = "jdbc:mysql://localhost:3306/billingSystemModel";
    private final String jdbcUsername = "root";
    private final String jdbcPassword = "gmhemanth2003";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
    }

    public boolean insertProduct(Product product) {
        String productId = getNextProductId(product.getProductType());
        String sql = "INSERT INTO Product (product_id, product_name, product_type, product_price, product_quantity,Unit ) VALUES (?, ?, ?, ?, ?,?)";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, productId);
            ps.setString(2, product.getProductName());
            ps.setString(3, product.getProductType());
            ps.setInt(4, product.getProductQuantity());
            ps.setDouble(5, product.getProductPrice());
            ps.setString(6,product.getUnit());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean insertCustomer(Customer customer) {
        String sql = "INSERT INTO Customer (customer_phoneNo, customer_name, Membership_points, Customer_balance) VALUES (?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, customer.getCustomerPhoneNo()); // Setting customer_phoneNo
            ps.setString(2, customer.getCustomerName()); // Setting customer_name
            ps.setDouble(3, customer.getMembershipPoints()); // Setting Membership_points
            ps.setDouble(4, customer.getCustomerbalance()); // Setting Customer_balance

            int rows_affected = ps.executeUpdate();
            return rows_affected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void insertBill(Bill bill) {
        String sql = "INSERT INTO Bill (Bill_No,Bill_Date,Customer_id,Bill_amt,Bill_discount,paidInvoice) VALUES (?, ?, ?, ?,?,?)";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1,bill.getBillNo());
            ps.setString(2, bill.getDate());
            ps.setString(3, bill.getCustomerid());
            ps.setDouble(4, bill.getbillamt());
            ps.setDouble(5,bill.getDiscount());
            ps.setBoolean(6,bill.getpaidStatus());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertItemSales(String productId,String BillNo, int BillQuantity){
        String sql = "INSERT INTO ItemSales (product_id,Bill_No,Bill_Quantity) VALUES (?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1,productId);
            ps.setString(2, BillNo);
            ps.setInt(3, BillQuantity);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateCustomerBalance(String customerId, double newBalance) {
        String sql="UPDATE Customer SET customer_balance = customer_balance + ? WHERE customer_phoneNo = ?";
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, newBalance);
            stmt.setString(2, customerId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getNextProductId(String productType) {
        char firstChar = productType.charAt(0);
        int asciiValue = (int) firstChar;
        String prefix = String.format("%03d", asciiValue); // e.g., "071" for 'G'

        String sql = "SELECT MAX(product_id) FROM Product WHERE product_id LIKE ?";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, prefix + "%");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String maxId = rs.getString(1);
                if (maxId != null) {
                    int nextId = Integer.parseInt(maxId.substring(4)) + 1;
                    return String.format("%s-%03d", prefix, nextId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return String.format("%s-001", prefix);
    }

    public String getNextBillNumber() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMM");
        String formattedDate = dateFormat.format(new Date());

        String sql = "SELECT MAX(Bill_No) FROM Bill WHERE Bill_No LIKE ?";
        String prefix = formattedDate + "-";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, prefix + "%");
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String maxId = rs.getString(1);
                if (maxId != null) {
                    int nextId = Integer.parseInt(maxId.substring(7)) + 1;
                    return String.format("%s%03d", prefix, nextId);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return prefix + "001";
    }

    public boolean customerExists(String customerPhoneNo) {
        String sql = "SELECT COUNT(*) FROM Customer WHERE customer_phoneNo = ?";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, customerPhoneNo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void UpdateMembershipPoints(double MembershipPoints,String cutomerPhoneNo){
        String sql="UPDATE customer set Membership_points = ?  where customer_phoneNo= ?";
        try(Connection connection=getConnection();
            PreparedStatement ps=connection.prepareStatement(sql)){
            ps.setDouble(1,MembershipPoints);
            ps.setString(2,cutomerPhoneNo);
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void updateProductQuantity(String productId, int Quantity) {
        String sql = "UPDATE Product SET product_quantity = ? WHERE product_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, Quantity);
            ps.setString(2, productId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getProductNameById(String productId) {
        String sql = "SELECT product_name FROM Product WHERE product_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("product_name");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getProductQuantityById(String productId) {
        String sql = "SELECT product_quantity FROM Product WHERE product_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("product_quantity");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // Return 0 if product not found or in case of any error
    }

    public Product getProductById(String productId) {
        Product product = null;
        String query = "SELECT * FROM Product WHERE product_id = ?";
        try (Connection connection=getConnection();
             PreparedStatement ps = connection.prepareStatement(query)){
            ps.setString(1, productId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                product = new Product();
                product.setProductId(rs.getString("product_id"));
                product.setProductName(rs.getString("product_name"));
                product.setPrice(rs.getDouble("product_price"));
                product.setProductQuantity(rs.getInt("product_quantity"));
                product.setUnit(rs.getString("unit"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    public Map<Customer, Integer> getTotalPurchaseByCustomer() {
        Map<Customer, Integer> customerPurchases = new LinkedHashMap<>();
        String sql = "SELECT c.customer_phoneNo, c.customer_name, SUM(b.Bill_amt) AS total_purchase " +
                "FROM Customer c " +
                "JOIN Bill b ON c.customer_phoneNo = b.Customer_id " +
                "GROUP BY c.customer_phoneNo, c.customer_name " +
                "ORDER BY c.customer_phoneNo";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setCustomerPhoneNo(rs.getString("customer_PhoneNo"));
                customer.setCustomerName(rs.getString("customer_name"));
                int totalPurchase = rs.getInt("total_purchase");
                customerPurchases.put(customer, totalPurchase);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customerPurchases;
    }

    public Customer getCustomerById(String customerId) {
        Customer customer = null;
        String query = "SELECT * FROM customer WHERE customer_phoneNo = ?";

        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setString(1, customerId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                customer = new Customer();
                customer.setCustomerPhoneNo(rs.getString("customer_phoneNo"));
                customer.setCustomerName(rs.getString("customer_name"));
                customer.setMembershipPoints(rs.getInt("Membership_points"));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception properly
        }

        return customer;
    }

    public void displayTotalPurchaseByCustomer() {
        Map<Customer, Integer> customerPurchases = getTotalPurchaseByCustomer();

        System.out.println("Total Purchases by Customer:");
        System.out.printf("%-20s %-30s %s\n", "Customer ID", "Customer Name", "Total Purchase");
        System.out.println("-------------------------------------------------------------------");
        for (Map.Entry<Customer, Integer> entry : customerPurchases.entrySet()) {
            Customer customer = entry.getKey();
            int totalPurchase = entry.getValue();
            System.out.printf("%-20s %-30s Rs.%d\n", customer.getCustomerPhoneNo(), customer.getCustomerName(), totalPurchase);
        }
        System.out.println();
    }

    public void updateExistingQuantity(int newQuantity, String productId,int productPrice){

        String sql = "UPDATE Product SET product_quantity = ?,product_price = ? WHERE product_id = ?";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, newQuantity);
            ps.setInt(2,productPrice);
            ps.setString(3, productId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<String, List<Bill>> getSalesDetailsByDate() {
        Map<String, List<Bill>> salesByDate = new HashMap<>();

        String sql = "SELECT Bill_Date, Bill_No, Bill_amt,Bill_discount FROM Bill";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String date = rs.getString("Bill_Date");
                String billNo = rs.getString("Bill_No");
                double billAmt = rs.getDouble("Bill_amt");
                double billdiscount=rs.getDouble("Bill_discount");

                Bill bill = new Bill();
                bill.setBillNo(billNo);
                bill.setbillamt(billAmt);
                bill.setDiscount(billdiscount);

                // If the date key is not present, add an empty list
                salesByDate.putIfAbsent(date, new ArrayList<>());
                // Add the current bill to the list for the corresponding date
                salesByDate.get(date).add(bill);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return salesByDate;
    }

    public void displaySalesDetailsByDate(String dateToDisplay) {
        BillingDAO billingDAO = new BillingDAO();
        Map<String, List<Bill>> salesByDate = billingDAO.getSalesDetailsByDate();

        System.out.println(salesByDate);

        System.out.println("Sales Details for Date: " + dateToDisplay);
        System.out.println("BillNo             Bill Amount           Bill Discount");
        System.out.println("------------------------------------------------------");

        // Retrieve bills for the specified date
        if (salesByDate.containsKey(dateToDisplay)) {
            List<Bill> bills = salesByDate.get(dateToDisplay);

            for (Bill bill : bills) {
                System.out.printf("%-15s      Rs.%.2f           Rs.%.2f\n", bill.getBillNo(), bill.getbillamt(), bill.getDiscount());
            }

            // Print total amount for the date
            double totalAmount = getTotalAmountForDate(bills);
            System.out.printf("Total Amt Rs.%.2f\n", totalAmount);
        } else {
            System.out.println("No sales found for date: " + dateToDisplay);
        }
    }

    private double getTotalAmountForDate(List<Bill> bills) {
        double totalAmount = 0.0;
        for (Bill bill : bills) {
            totalAmount += bill.getbillamt();
        }
        return totalAmount;
    }

    public List<ItemSales> getItemSalesByProductId(String productId) {
        List<ItemSales> itemSalesList = new ArrayList<>();
        String sql = "SELECT Bill_No, Bill_Quantity FROM ItemSales WHERE product_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ItemSales itemSales = new ItemSales();
                    itemSales.setProductId(productId);
                    itemSales.setBillNo(rs.getString("Bill_No"));
                    itemSales.setQuantity(rs.getInt("Bill_Quantity"));
                    itemSalesList.add(itemSales);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return itemSalesList;
    }

    public void displayItemSalesDetailsByProductId(String productId) {
        List<ItemSales> itemSalesList = getItemSalesByProductId(productId);
        int totalQuantity = 0;
        Product product = getProductById(productId);
        String unit=product.getUnit();

        System.out.println("Product ID: " + productId);
        if (product != null) {
            System.out.println("Product Name: " + product.getProductName());
        } else {
            System.out.println("Product Name: Not Found");
        }
        System.out.println(String.format("%-15s%-15s%-15s", "Bill No", "Quantity","Unit"));

        for (ItemSales itemSales : itemSalesList) {
            String billNo = itemSales.getBillNo();
            int quantity = itemSales.getQuantity();
            totalQuantity += quantity;

            System.out.println(String.format("%-15s%-15d%-15s", billNo, quantity,unit));
        }

        System.out.println("\nTotal Quantity Sold: " + totalQuantity + " "+ unit);
    }
    
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM Product";
        try (Connection connection=getConnection();
            PreparedStatement ps = connection.prepareStatement(query)){
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getString("product_id"));
                product.setProductName(rs.getString("product_name"));
                product.setPrice(rs.getDouble("product_price"));
                product.setProductQuantity(rs.getInt("product_quantity"));
                product.setUnit(rs.getString("unit"));
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public Map<String, Double> fetchCustomerBalance() {
        Map<String, Double> customerBalances = new HashMap<>();
        String sql = "SELECT Customer_phoneNo, Customer_balance as Total_Balance FROM customer";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String customerphoneNo = rs.getString("Customer_phoneNo");
                double totalBalance = rs.getDouble("Total_Balance");
                customerBalances.put(customerphoneNo, totalBalance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerBalances;
    }

    public List<String> fetchInvoicesByStatus(boolean isPaid) {
        List<String> invoices = new ArrayList<>();
        String sql = "SELECT Bill_No, Bill_Date, Bill_amt, PaidInvoice FROM Bill WHERE PaidInvoice = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setBoolean(1, isPaid);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String billNo = resultSet.getString("Bill_No");
                String billDate = resultSet.getString("Bill_Date");
                double billAmt = resultSet.getDouble("Bill_amt");
                boolean paidStatus = resultSet.getBoolean("PaidInvoice");

                invoices.add(billNo + "\t" + billDate + "\t" + billAmt + "\t" + paidStatus);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoices;
    }

    public List<String> fetchBillDetails(String billNo) {
        List<String> billDetails = new ArrayList<>();
        String query = "SELECT b.Bill_No, b.Bill_Date, b.Customer_id, b.Bill_amt, b.Bill_discount, " +
                "p.product_name, p.product_price,p.unit,i.Bill_Quantity, (p.product_price * i.Bill_Quantity) AS TotalPrice " +
                "FROM Bill b " +
                "JOIN ItemSales i ON b.Bill_No = i.Bill_No " +
                "JOIN Product p ON i.product_id = p.product_id " +
                "WHERE b.Bill_No = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, billNo);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String billDetail = rs.getString("Bill_No") + "\t" +
                        rs.getString("Bill_Date") + "\t" +
                        rs.getString("Customer_id") + "\t" +
                        rs.getDouble("Bill_amt") + "\t" +
                        rs.getDouble("Bill_discount") + "\t" +
                        rs.getString("product_name") + "\t" +
                        rs.getDouble("product_price") + "\t" +
                        rs.getString("unit")+"\t"+
                        rs.getInt("Bill_Quantity") + "\t" +
                        rs.getDouble("TotalPrice");
                billDetails.add(billDetail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return billDetails;
    }
}
