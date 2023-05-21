import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerInfoForm extends JDialog{
    private JPanel CustomerInfoPanel;
    private javax.swing.JPanel JPanel;
    private JTextField tfFirstName;
    private JTextField tfEmail;
    private JPasswordField pfPassword;
    private JTextField tfLastName;
    private JTextField tfPhone;
    private JTextField tfCountry;
    private JTextField tfCity;
    private JTextField tfStreet;
    private JTextField tfBuildingNumber;
    private JTextField tfAccountType;
    private JTextField tfAccountNumber;
    private JTextField tfAccountBalance;
    private JButton addCustomerButton;
    private JButton cancelButton;

    private int CustomerSSN;
    private int EmployeeSSN;
    public CustomerInfoForm(JFrame parent,int employeeSSN,int customerSSN)  // Constructor.
    {
        // Setting the attributes of the panel.
        super(parent);
        setTitle("Customer Info");
        setContentPane(CustomerInfoPanel);
        setMinimumSize(new Dimension(820, 820));
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        setModal(true);

        EmployeeSSN = employeeSSN;
        CustomerSSN = customerSSN;

        addCustomerButton.addActionListener(e -> {

            new EmployeeForm(null,EmployeeSSN);
            dispose();

        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new EmployeeForm(null,EmployeeSSN);
                dispose();
            }
        });
    }
}
