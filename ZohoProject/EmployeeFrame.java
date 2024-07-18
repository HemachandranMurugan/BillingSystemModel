package ZohoProject;

import java.awt.*;
import java.awt.event.*;

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

                //new AddProductFrame();
            }
        });
        paidInvoices.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //new AddProductFrame();
            }
        });
        CustomerBalance.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //new AddProductFrame();
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
}
