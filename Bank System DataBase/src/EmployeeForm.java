import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EmployeeForm extends JDialog {
    private JPanel EmployeePanel;
    private JButton addCustomerButton;
    private JButton LogOutButton;
    private JButton updateCustomerButton;
    private JButton showCustomersButton;
    private JButton LoansButton;

    public EmployeeForm(JFrame parent)  // Constructor.
    {
        // Setting the attributes of the panel.
        super(parent);
        setTitle("Admin");
        setContentPane(EmployeePanel);
        setMinimumSize(new Dimension(820, 420));
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        setModal(true);

        addCustomerButton.addActionListener(e -> {

            new AddCustomerForm(null);
            dispose();

        });

        updateCustomerButton.addActionListener(e -> {

//            new UpdateCustomerForm(null);
            dispose();

        });
        showCustomersButton.addActionListener(e -> {

            new CustomersListForm(null);
            dispose();

        });

        LoansButton.addActionListener(e -> {

//            new LoansForm(null);
            dispose();

        });
        LogOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Home(null);
                dispose();
            }
        });
    }
}