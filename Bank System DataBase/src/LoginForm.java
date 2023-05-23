import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginForm extends JDialog {
    private JTextField tfEmail;
    private JPasswordField pfPassword;
    private JButton loginButton;
    private JButton cancelButton;
    private JPanel LoginPanel;
    private final String type;
    private static String phoneNumberRegex = "^01\\d{8}$";
    private static String numbersRegex = "^[0-9]+$";
    private static String doubleRegex = "^[-+]?\\d*\\.?\\d+$";
    private static String nameRegex = "^[A-Za-z\\s]+$";
    private static String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
    //__________________________________________________________________________________________________________________

    public LoginForm() {
        type = null;
    }

    public LoginForm(JFrame parent, String Type)     // constructor that sets the suitable attributes for the form.
    {
        super(parent);
        setTitle("Login");
        setContentPane(LoginPanel);
        setMinimumSize(new Dimension(820, 440));
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        setModal(true);
        type = Type;

        //__________________________________________________________________________________________________________________

        loginButton.addActionListener(e -> {
            String email = tfEmail.getText();
            char[] passwordArr = pfPassword.getPassword();
            String password = new String(passwordArr);
            if (type.equals("Admin")) {
                checkLogin_Admin(email, password);
            } else if (type.equals("Employee")) {
                checkLogin(email, password,"Employee");
            }

            else {
                checkLogin(email, password,"Customer");
            }

        });

        // __________________________________________________________________________________________________________________

        cancelButton.addActionListener(e -> {
            new Home(null);
            dispose();
        });
    }

    //__________________________________________________________________________________________________________________

    void checkLogin_Admin(String email, String Password) // method to check the login when the admin is logged in.
    {
        if (email.equals("Admin") && Password.equals("Admin")) {
            new AdminForm(null);
            dispose();
        } else {
            JOptionPane.showMessageDialog(LoginForm.this,
                    "Invalid Email or Password!!!",
                    "Please,Try Again",
                    JOptionPane.ERROR_MESSAGE);
        }

    }

    //__________________________________________________________________________________________________________________

    void checkLogin(String email, String Password,String Table)  // method to check the login when the employee is logged in.
    {
        try {
            DataBase dataBase = new DataBase();
            if (dataBase.Login(email, Password, Table)) {
                dispose();
            } else {
                JOptionPane.showMessageDialog(LoginForm.this,
                        "Invalid Credentials",
                        "Login Failed!",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    public static boolean checkDouble(String Amount) {
        if (!Amount.matches(doubleRegex)) {
            JOptionPane.showMessageDialog(null,
                    "Please,Enter valid Amount",
                    "Invalid input for Amount",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    //__________________________________________________________________________________________________________________


    public static boolean checkNumber(String number) {
        if (!number.matches(numbersRegex)) {
            JOptionPane.showMessageDialog(null,
                    "Please,Enter valid Data",
                    "Invalid Data",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public static boolean checkWords(String word) {
        if (!word.matches(nameRegex)) {
            JOptionPane.showMessageDialog(null,
                    "Please,Enter valid Data",
                    "Invalid Data",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public static boolean checkEmail(String email) {
        if (!email.matches(emailRegex)) {
            JOptionPane.showMessageDialog(null,
                    "Please,Enter valid e-mail address",
                    "Invalid Email",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

}