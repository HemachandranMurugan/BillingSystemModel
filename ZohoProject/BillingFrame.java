package ZohoProject;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.swing.;

public class BillingFrame extends Frame {
    BillingDAO billingDAO = new BillingDAO();

    TextField dateField, productIdField, productNameField, productQuantityField, CustomerField;
    TextArea billTextArea;
    int serialNumber;
    double totalPrice;
    Label totalBillLabel;
    List<String> billItems; // To keep track of added items
    private Map<String, Integer> productQuantityMap;

    public BillingFrame(String customerId,Frame AddcustomerFrame) {
        setTitle("Billing System");

        productQuantityMap = new HashMap<>();

        Label dateLabel = new Label("Date:");
        dateLabel.setBounds(50, 50, 50, 25);
        dateField = new TextField();
        dateField.setBounds(100, 50, 100, 25);
        dateField.setText(getCurrentDate());
        dateField.setEditable(false);

        Label CustomerLabel = new Label("Customer ID:");
        CustomerLabel.setBounds(300, 50, 100, 25);
        CustomerField = new TextField(customerId);
        CustomerField.setBounds(400, 50, 125, 25);
        CustomerField.setEditable(false);

        Label productLabel = new Label("Product:");
        productLabel.setBounds(50, 100, 75, 25);

        Label productIdLabel = new Label("ID:");
        productIdLabel.setBounds(50, 125, 25, 25);
        productIdField = new TextField();
        productIdField.setBounds(75, 125, 75, 25);

        productIdField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                fillProductName();
            }
        });

        Label productNameLabel = new Label("Name:");
        productNameLabel.setBounds(175, 125, 50, 25);
        productNameField = new TextField();
        productNameField.setBounds(200, 125, 100, 25);
        productNameField.setEditable(false);

        Label productQuantityLabel = new Label("Quantity:");
        productQuantityLabel.setBounds(350, 125, 75, 25);
        productQuantityField = new TextField();
        productQuantityField.setBounds(400, 125, 125, 25);

        Button Stock=new Button("Stock available");
        Stock.setBounds(100,175,100,25);
        Stock.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                 new StockFrame();
            }
        });

        Button addButton = new Button("Add to Bill");
        addButton.setBounds(250, 175, 100, 25);
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //System.out.println("Add to Bill button clicked");
                addItemToBill();
            }
        });

        Button clearLastItemButton = new Button("Clear Last Item");
        clearLastItemButton.setBounds(400, 175, 100, 25);
        clearLastItemButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearLastItem();
            }
        });

        billTextArea = new TextArea();
        billTextArea.setBounds(100, 225, 400, 200);
        billTextArea.setEditable(false);
        billTextArea.setFont(new Font("Courier", Font.PLAIN, 12));
        billTextArea.setText(String.format("%-6s%-15s%-10s%-10s%-15s\n", "S.No", "Name", "Price", "Quantity", "Total Price"));

        Button printButton = new Button("Print Bill");
        printButton.setBounds(350, 450, 100, 25);
        printButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //String customerId = CustomerField.getText();
                Customer customer=billingDAO.getCustomerById(customerId);
                //System.out.println("Print Bill button clicked");
                printBill(customer);
                billTextArea.setText("");
                productIdField.setText("");
                productNameField.setText("");
                productQuantityField.setText("");
                totalBillLabel.setText("");

            }
        });

        totalBillLabel = new Label("Total Bill Amount: 0.0");
        totalBillLabel.setBounds(100, 450, 200, 25);

        add(dateLabel);add(dateField);add(productLabel);
        add(CustomerLabel);add(CustomerField);
        add(productIdLabel);add(productIdField);
        add(productNameLabel);add(productNameField);
        add(productQuantityLabel);add(productQuantityField);
        add(Stock);add(addButton);add(clearLastItemButton);
        add(billTextArea);add(printButton);add(totalBillLabel);


        setLayout(null);
        setSize(600, 550);
        setVisible(true);



        serialNumber = 1;
        totalPrice = 0.0;
        billItems = new ArrayList<>(); // Initialize the list


        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                dispose();
            }
        });

        AddcustomerFrame.dispose();
    }



    private void fillProductName() {
        String productId = productIdField.getText();
        String productName = billingDAO.getProductNameById(productId);
        if (productName != null) {
            productNameField.setText(productName);
        } else {
            productNameField.setText("");
        }
    }


    private void addItemToBill() {
        String productId = productIdField.getText();
        String productName = productNameField.getText();
        String quantityStr = productQuantityField.getText();


        if (productId.isEmpty() || productName.isEmpty() || quantityStr.isEmpty()) {
            System.out.println("All fields must be filled.");
            return;
        }
        int quantity;

        try {
            quantity = Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            System.out.println("Quantity must be a number.");
            return;
        }
        billingDAO.updateProductQuantity(productId,quantity);

        double price = billingDAO.getProductPriceById(productId);
        double itemTotalPrice = price * quantity;
        totalPrice += itemTotalPrice;

        String itemDetails = String.format("%-6d%-15s%-10.2f%-10d%-15.2f\n", serialNumber, productName, price, quantity, itemTotalPrice);

        billTextArea.append(itemDetails);

        billItems.add(itemDetails); // Add the item to the list

        totalBillLabel.setText("Total Bill Amount: " + totalPrice);


        if (productQuantityMap.containsKey(productId)) {
            int currentQuantity = productQuantityMap.get(productId);
            productQuantityMap.put(productId, currentQuantity + quantity);
        } else {
            productQuantityMap.put(productId, quantity);
        }


        serialNumber++;
    }

    private void clearLastItem() {
        if (!billItems.isEmpty()) {
            String lastItem = billItems.remove(billItems.size() - 1);
            String[] details = lastItem.trim().split("\\s+");

            if (details.length == 5) {
                double itemTotalPrice = Double.parseDouble(details[4]);
                totalPrice -= itemTotalPrice;
            }
            // Update the TextArea
            billTextArea.setText(String.format("%-6s%-15s%-10s%-10s%-15s\n", "S.No", "Name", "Price", "Quantity", "Total Price"));
            for (int i = 0; i < billItems.size(); i++) {
                billTextArea.append(billItems.get(i));
            }
            totalBillLabel.setText("Total Bill Amount: " + totalPrice);
            serialNumber--;
        }
    }


    private String getCurrentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        return formatter.format(date);
    }



    public void printBill(Customer customer) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String currentDate = formatter.format(date);
        String Billno=billingDAO.getNextBillNumber();

        System.out.println("G.M.H Stores");
        System.out.println("Date: " + currentDate+"\t\t\tBill NO: "+Billno);
        System.out.println("Customer ID: " + customer.getCustomerPhoneNo()+"\t \t CustomerName: "+customer.getCustomerName());
        System.out.println("--------------------------------------------------------------");
        System.out.println(String.format("%-6s%-15s%-10s%-10s%-15s", "S.No", "Name", "Price", "Quantity", "Total Price"));

        int serialNumber = 1;
        double totalAmount = 0.0;
        double totalBillAmount;
        double MembershipPoint=customer.getMembershipPoints();
        double Discount = 0;

        for (String line : billItems) {
            String[] parts = line.trim().split("\\s{2,}"); // Split by 2 or more spaces

            if (parts.length >= 5) {
                String productName = parts[1].trim();
                double price = Double.parseDouble(parts[2].trim());
                int quantity = Integer.parseInt(parts[3].trim());
                double itemTotalPrice = Double.parseDouble(parts[4].trim());

                System.out.println(String.format("%-6d%-15s%-10.2f%-10d%-15.2f", serialNumber, productName, price, quantity, itemTotalPrice));

                totalAmount += itemTotalPrice;
                serialNumber++;
            }
        }
        System.out.println("--------------------------------------------------------------");


        if(totalAmount>1000 && totalAmount<2000){
            System.out.println("Discount 10% ");
            totalBillAmount=totalAmount*0.9;
            Discount=totalAmount*0.1;

        }
        else if(totalAmount>2000){
            System.out.println("Discount 20%");
            totalBillAmount=totalAmount*0.8;
            Discount=totalAmount*0.2;
        }
        else{
            totalBillAmount=totalAmount;

        }

        MembershipPoint=MembershipPoint+totalBillAmount*0.1;

        if(MembershipPoint>=1000){
            MembershipPoint=MembershipPoint-1000;
            totalBillAmount=totalBillAmount*0.9;
            System.out.println("The Redeemed MemberShip Points = 10%");
        }
        customer.setMembershipPoints(MembershipPoint);


        billingDAO.UpdateMembershipPoints(MembershipPoint,customer.getCustomerPhoneNo());


        System.out.printf("\nThe earned Membership points: %.2f\n",MembershipPoint);

        // Display total bill amount
        System.out.println("Total Bill Amount: "+totalBillAmount);
        System.out.println("Savings: " +(totalAmount-totalBillAmount));

        System.out.println("--------------------------------------------------------------");
        System.out.println("***Thank you**\n**Visit again***");
        System.out.println();

        Bill bill = new Bill();
        bill.setBillNo(Billno);
        bill.setbillamt(totalBillAmount);
        bill.setDate(currentDate);
        bill.setCustomerid(customer.getCustomerPhoneNo());
        bill.setDiscount(Discount);

        billingDAO.insertBill(bill);



        for (Map.Entry<String, Integer> entry : productQuantityMap.entrySet()) {
            String productId = entry.getKey();
            int quantity = entry.getValue();
            billingDAO.insertItemSales(productId,Billno,quantity);
        }
    }



}



class StockFrame extends Frame {
    BillingDAO billingDAO = new BillingDAO();
    TextArea stockTextArea;

    public StockFrame() {
        setTitle("Stock Availability");

        stockTextArea = new TextArea();
        stockTextArea.setBounds(50, 50, 400, 300);
        stockTextArea.setEditable(false);
        stockTextArea.setFont(new Font("Courier", Font.PLAIN, 12));

        fillStockDetails();

        add(stockTextArea);

        setLayout(null);
        setSize(500, 300);
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                dispose();
            }
        });
    }

    private void fillStockDetails() {
        List<Product> products = billingDAO.getAllProducts();
        stockTextArea.setText(String.format("%-15s%-20s%-10s%-10s\n", "Product ID", "Product Name", "Price", "Quantity"));

        for (Product product : products) {
            stockTextArea.append(String.format("%-15s%-20s%-10.2f%-10d\n",
                    product.getProductId(),
                    product.getProductName(),
                    product.getProductPrice(),
                    product.getProductQuantity()));
        }
    }
}



