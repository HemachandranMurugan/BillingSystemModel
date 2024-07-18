package ZohoProject;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AddProductFrame extends Frame {
    BillingDAO billingDAO=new BillingDAO();
    AddProductFrame(){
        setTitle("Add New Product");

        Label ProdcutName=new Label("Product name: ");
        ProdcutName.setBounds(100, 150, 150, 25);
        TextField nameField=new TextField();
        nameField.setBounds(250, 150, 150, 25);

        Label ProductType = new Label("Product Type");
        ProductType.setBounds(100, 200, 150, 25);
        Choice TypeField= new Choice();
        TypeField.add("Grocery");//G-71
        TypeField.add("Cosmetics");//C-67
        TypeField.add("HouseHold");//C-72
        TypeField.add("Vegetables");//-86
        TypeField.add("Snacks");//83
        TypeField.setBounds(250,200,150,25);

        Label ProductQuantity=new Label("Product Quantity: ");
        ProductQuantity.setBounds(100, 250, 150, 25);
        TextField QuantityField=new TextField();
        QuantityField.setBounds(250, 250, 150, 25);

        Label ProdcutPrice=new Label("Product Price: ");
        ProdcutPrice.setBounds(100, 300, 150, 25);
        TextField PriceField=new TextField();
        PriceField.setBounds(250, 300, 150, 25);


        Button Submit=new Button("Submit");
        Submit.setBounds(300, 400, 100, 25);

        add(ProdcutName);add(nameField);
        add(ProductType);add(TypeField);
        add(ProductQuantity);add(QuantityField);
        add(ProdcutPrice);add(PriceField);
        add(Submit);

        Submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String productName = nameField.getText();
                String productType = TypeField.getSelectedItem();
                int productQuantity;
                int productPrice;
                int productTax;
                try {
                    productQuantity = Integer.parseInt(QuantityField.getText());
                    productPrice = Integer.parseInt(PriceField.getText());
                } catch (NumberFormatException ex) {
                    showDialog("Error", "Please enter valid numeric values for quantity, price, and tax.");
                    return;
                }
                Product product = new Product(productName, productType,productPrice, productQuantity);
                if (billingDAO.insertProduct(product)) {
                    showDialog("Success", "Product added successfully!");
                } else {
                    showDialog("Error", "Failed to add product.");
                }
                nameField.setText("");
                QuantityField.setText("");
                PriceField.setText("");
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