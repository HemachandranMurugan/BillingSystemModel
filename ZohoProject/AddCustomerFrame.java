package ZohoProject;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AddCustomerFrame extends Frame {
    BillingDAO billingDAO = new BillingDAO();
    public AddCustomerFrame(String a,Frame EmployeeFrame) {
        
        setTitle("Add New Customer");

        Label IDLabel = new Label("Customer ID Mobileno.:");
        IDLabel.setBounds(100, 150, 150, 25);
        TextField IDField = new TextField(a);
        IDField.setBounds(250, 150, 150, 25);
        IDField.setEditable(false);

        Label nameLabel = new Label("Customer Name:");
        nameLabel.setBounds(100, 200, 150, 25);
        TextField nameField = new TextField();
        nameField.setBounds(250, 200, 150, 25);
        
        Button submit = new Button("Submit");
        submit.setBounds(300, 350, 100, 25);

        Button proceedToBillingButton = new Button("Proceed to Billing");
        proceedToBillingButton.setBounds(300,400 , 100, 25);
        proceedToBillingButton.setEnabled(false);

        add(IDLabel);add(IDField);
        add(nameLabel);add(nameField);
        add(submit);
        add(proceedToBillingButton);

        // add the new customer when submit button is clicked
        submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String customerName = nameField.getText();
                String customerId = IDField.getText();
                double MemberShipPoints = 0;
                double customerbalance=0;
                Customer customer = new Customer(customerId, customerName, MemberShipPoints,customerbalance);
                if(billingDAO.insertCustomer(customer)){
                    nameField.setText("");
                    IDField.setText("");
                    showDialog("Success", "Customer added successfully!");
                    proceedToBillingButton.setEnabled(true);
                    submit.setEnabled(false);
                    proceedToBillingButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            new BillingFrame(customerId,AddCustomerFrame.this);
                        }
                    });
                }
                else{
                    showDialog("Error", "Failed to add customer. Please try again.");
                }
            }
        });
        setLayout(null);
        setSize(500, 500);
        setVisible(true);
        
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                dispose();
            }
        });
        
        EmployeeFrame.dispose();
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
}
