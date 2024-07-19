package ZohoProject;

import java.awt.*;
import java.awt.event.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class UpdateProductFrame extends Frame {
    BillingDAO billingDAO=new BillingDAO();
    TextField productIdField, productNameField, productQuantityField,productPriceField;
    Button updateButton;

    public UpdateProductFrame() {
        setTitle("Update exisiting Product Quantity or Price");

        // Product ID
        Label productIdLabel = new Label("Product ID:");
        productIdLabel.setBounds(50, 50, 100, 25);
        productIdField = new TextField();
        productIdField.setBounds(150, 50, 200, 25);

        // Product Name
        Label productNameLabel = new Label("Product Name:");
        productNameLabel.setBounds(50, 100, 100, 25);
        productNameField = new TextField();
        productNameField.setBounds(150, 100, 200, 25);
        productNameField.setEditable(false);

        // Product Quantity
        Label productQuantityLabel = new Label("Quantity:");
        productQuantityLabel.setBounds(50, 150, 100, 25);
        productQuantityField = new TextField();
        productQuantityField.setBounds(150, 150, 200, 25);

        Label productPriceLabel = new Label("Price:");
        productPriceLabel.setBounds(50,200,100,25);
        productPriceField=new TextField();
        productPriceField.setBounds(150,200,200,25);


        // Update Button
        updateButton = new Button("Update");
        updateButton.setBounds(150, 250, 100, 25);


        // Adding components to the frame
        add(productIdLabel);add(productIdField);
        add(productNameLabel);add(productNameField);
        add(productQuantityLabel);add(productQuantityField);
        add(productPriceLabel);add(productPriceField);
        add(updateButton);
        
        // Add key listener to productIdField to fetch product name when the product ID changes
        productIdField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                fillProductName();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String productId = productIdField.getText();
                String quantityStr = productQuantityField.getText();
                String pricestr=productPriceField.getText();

                if (productId.isEmpty() || quantityStr.isEmpty()) {
                    System.out.println("Please fill all fields.");
                    return;
                }
                int ExistingQuantity=billingDAO.getProductQuantityById(productId);
                int productPrice;
                int  newQuantity;
                try {
                    newQuantity = Integer.parseInt(quantityStr);
                    productPrice=Integer.parseInt(pricestr);
                } catch (NumberFormatException f) {
                    System.out.println("Quantity or price must be a number.");
                    return;
                }
                billingDAO.updateExistingQuantity(newQuantity+ExistingQuantity,productId,productPrice);
                productIdField.setText("");
                productQuantityField.setText("");
                productNameField.setText("");

                setVisible(false);
            }
        });

        // Setting frame properties
        setLayout(null);
        setSize(400, 350);
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                dispose();
            }
        });
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
}
