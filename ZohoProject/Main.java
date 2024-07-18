package ZohoProject;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main extends Frame {
        public Main() {
            setTitle("GMH's Store Manager");
            Button Administrator = new Button("Administrator");
            Administrator.setBounds(150,175,200,50);
            Button Employee = new Button("Employee");
            Employee.setBounds(150,275,200,50);
            add(Administrator);
            add(Employee);
            //Administrator button
            Administrator.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    new AdministratorFrame(Main.this);
                }
            });
            //Empolyee button
            Employee.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    new EmployeeFrame(Main.this);
                }
            });
            setLayout(null);
            setSize(500,500);
            setVisible(true);

            addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent we) {
                    System.exit(0);
                }
            });
        }
    public static void main(String[] args) {
            new Main();
    }

}