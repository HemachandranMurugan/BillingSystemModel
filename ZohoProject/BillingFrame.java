package ZohoProject;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class EmployeeFrame extends Frame {
    BillingDAO billingDAO = new BillingDAO();

     EmployeeFrame(Frame frame) {
        setTitle("Employee DashBoard");

        Button allInvoices = new Button("Fetch Invoices");
        allInvoices.setBounds(150, 75, 200, 50);

        Button paidInvoices = new Button("PaidInvoices");
        paidInvoices.setBounds(150, 150, 200, 50);

        Button CustomerBalance = new Button("Customer Balance");
        CustomerBalance.setBounds(150, 225, 200, 50);

        Button printBill = new Button("printBill");
        printBill.setBounds(150, 300, 200, 50);

        add(allInvoices);
        add(paidInvoices);
        add(CustomerBalance);
        add(printBill);


        allInvoices.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //fetchAndPrintBill();
                 new billno();
            }
        });
        paidInvoices.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //new AddProductFrame();
            }
        });
        CustomerBalance.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                billingDAO.fetchCustomerBalance();
                fetchAndPrintCustomerBalances();
            }
        });
        printBill.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                new Check();
            }
        });

        setLayout(null);
        setSize(500, 500);
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                Main frame = new Main();
                setEnabled(true);
                dispose();
            }
        });
        frame.dispose();
    }

    private void showDialog(String title, String message) {
        Dialog dialog = new Dialog(this, title, true);
        dialog.setLayout(new FlowLayout());
        dialog.add(new Label(message));
        Button okButton = new Button("OK");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
                dialog.dispose();
            }
        });
        dialog.add(okButton);
        dialog.setSize(300, 100);
        dialog.setVisible(true);
    }

    private boolean isValidPhoneNumber(String phoneNo) {
        // Check if the phone number has at least 10 digits and contains only digits
        if (phoneNo.length() < 10) {
            return false;
        }
        for (char c : phoneNo.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    private void fetchAndPrintCustomerBalances() {
        Map<String, Double> balances = billingDAO.fetchCustomerBalance();
        System.out.println("Customer Balances:");
        for (Map.Entry<String, Double> entry : balances.entrySet()) {
            System.out.println("Customer ID: " + entry.getKey() + ", Balance: " + entry.getValue());
        }
    }


    class billno extends Frame{
         billno(){
             Label bill = new Label("Enter Bill No:");
             bill.setBounds(150, 150, 100, 25);
             TextField BIllNO = new TextField(); // Set the customer ID
             BIllNO.setBounds(250, 150, 150, 25);
             BIllNO.setEditable(true);

             Button checkStatus = new Button("Fetch Invoice details");
             checkStatus.setBounds(200, 200, 150, 25);

             checkStatus.addActionListener(new ActionListener() {
                 public void actionPerformed(ActionEvent e) {
                     String BillNO = BIllNO.getText();
                     fetchAndPrintBill(BillNO);
                 }
             });

             add(bill);add(BIllNO);add(checkStatus);

             setLayout(null);
             setSize(500, 500);
             setVisible(true);

             addWindowListener(new WindowAdapter() {
                 public void windowClosing(WindowEvent we) {
                     Main frame = new Main();
                     setEnabled(true);
                     dispose();
                 }
             });
         }
    }

    class Check extends Frame {
        Check() {

            Label CustomerLabel = new Label("Customer ID:");
            CustomerLabel.setBounds(150, 150, 100, 25);
            TextField customerField = new TextField(); // Set the customer ID
            customerField.setBounds(250, 150, 150, 25);
            customerField.setEditable(true);

            Button checkStatus = new Button("CheckStatus");
            checkStatus.setBounds(200, 200, 100, 25);

            checkStatus.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String customerId = customerField.getText();// Fetch customer ID here
                    // Validate the phone number length and content
                    if (!isValidPhoneNumber(customerId)) {
                        showDialog("Error", "Please enter a valid phone number");
                        return;
                    }
                    if (!billingDAO.customerExists(customerId)) {
                        new AddCustomerFrame(customerId, Check.this);

                    } else {
                        new BillingFrame(customerId, Check.this); // Pass customerId to BillingFrame constructor
                    }
                }
            });

            add(CustomerLabel);
            add(customerField);
            add(checkStatus);


            setLayout(null);
            setSize(500, 500);
            setVisible(true);

            addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent we) {
                    Main frame = new Main();
                    setEnabled(true);
                    dispose();
                }
            });
        }

    }

    private void fetchAndPrintBill(String billNo) {
        List<String> billDetails = billingDAO.fetchBillDetails(billNo);

        // Print header
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = formatter.format(new Date());

        System.out.println("G.M.H Stores");
        System.out.println("Date: " + currentDate + "\t\t\tBill NO: " + billNo);
        System.out.println("--------------------------------------------------------------");
        System.out.println(String.format("%-6s%-15s%-10s%-10s%-15s", "S.No", "Name", "Price", "Quantity", "Total Price"));

        int serialNumber = 1;
        double totalAmount = 0.0;

        for (String detail : billDetails) {
            String[] parts = detail.split("\t");
            String productName = parts[5];
            double price = Double.parseDouble(parts[6]);
            int quantity = Integer.parseInt(parts[7]);
            double totalPrice = Double.parseDouble(parts[8]);

            System.out.println(String.format("%-6d%-15s%-10.2f%-10d%-15.2f", serialNumber, productName, price, quantity, totalPrice));

            totalAmount += totalPrice;
            serialNumber++;
        }

        double discount = Double.parseDouble(billDetails.get(0).split("\t")[4]);
        double finalAmount = totalAmount - discount;

        // Print totals and footer
        System.out.println("--------------------------------------------------------------");
        System.out.println("Total Bill Amount: " + totalAmount);
        System.out.println("Discount: " + discount);
        System.out.println("Final Bill Amount: " + finalAmount);
        System.out.println("--------------------------------------------------------------");
        System.out.println("***Thank you**\n**Visit again***\n");
    }

}
