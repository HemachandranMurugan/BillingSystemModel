package ZohoProject;

import java.awt.*;
import java.awt.event.*;

public class AdministratorFrame extends Frame {
    BillingDAO billingDAO=new BillingDAO();
    AdministratorFrame(Frame frame){
        setTitle("Administrator DashBoard");

        Button Addproduct=new Button("AddProduct");
        Addproduct.setBounds(150,75,200,50);

        Button Updateproduct=new Button("UpdateProduct");
        Updateproduct.setBounds(150,150,200,50);

        Button ItemSalesAnalysis = new Button("ItemSalesAnalysis");
        ItemSalesAnalysis.setBounds(150,225,200,50);

        Button getBSummary=new Button("GetBillSummary");
        getBSummary.setBounds(150,300,200,50);

        Button getCReport=new Button("GetCustomerReport");
        getCReport.setBounds(150,370,200,50);


        add(Addproduct);
        add(Updateproduct);
        add(getCReport);
        add(getBSummary);
        add(ItemSalesAnalysis);

        Addproduct.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new AddProductFrame();
            }
        });
        Updateproduct.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new UpdateProductFrame();
            }
        });

        getBSummary.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new dateframe();
            }
        });

        getCReport.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    billingDAO.getTotalPurchaseByCustomer();
                    billingDAO.displayTotalPurchaseByCustomer();
            }
        });

        ItemSalesAnalysis.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ProductSalesframe();
            }
        });


        setLayout(null);
        setSize(500,500);
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                Main frame=new Main();
                frame.setEnabled(true);
                dispose();
            }
        });

        frame.dispose();
    }




    public class dateframe extends Frame{
        dateframe() {
            Label enterlabel = new Label("Enter Date: ");
            TextField date = new TextField();
            Button next = new Button("Next");
            enterlabel.setBounds(50, 100, 100, 25);
            date.setBounds(150, 100, 100, 25);
            next.setBounds(200, 150, 50, 25);

            add(enterlabel);add(date);add(next);

            next.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    billingDAO.displaySalesDetailsByDate(date.getText());
                }
            });
            setLayout(null);
            setSize(300,300);
            setVisible(true);

            addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent we) {
                    dispose();
                }
            });
        }
    }

    public class ProductSalesframe extends Frame{
        ProductSalesframe(){
            Label productId=new Label("Product id: ");
            productId.setBounds(50,100,75,25);
            TextField id = new TextField();
            id.setBounds(125, 100, 125, 25);
            Button next = new Button("Next");
            next.setBounds(200, 150, 50, 25);

            add(productId);add(id);add(next);
            next.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    billingDAO.displayItemSalesDetailsByProductId(id.getText());
                }
            });

            setLayout(null);
            setSize(300,300);
            setVisible(true);

            addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent we) {
                    dispose();
                }
            });


        }
    }
}
