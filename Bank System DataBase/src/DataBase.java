import javax.swing.*;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class DataBase {
    Connection connection;

    public DataBase() throws SQLException {
        String currentDir = java.lang.System.getProperty("user.dir");
        String url = "jdbc:sqlite:" + currentDir + "\\identifier.sqlite";
        connection = DriverManager.getConnection(url);
    }

    public List<List<String>> showList() throws SQLException {
        String sql = "Select * from CUSTOMER;";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        List<List<String>> list = new LinkedList<>();

        while (resultSet.next()) {
            List<String> customer = new LinkedList<>();

            customer.add(Integer.toString(resultSet.getInt("SSN")));
            customer.add(resultSet.getString("FName"));
            customer.add(resultSet.getString("LName"));
            customer.add(resultSet.getString("Phone"));
            customer.add(resultSet.getString("Country"));
            customer.add(resultSet.getString("City"));
            customer.add(resultSet.getString("Street"));
            customer.add(Integer.toString(resultSet.getInt("BuildingNumber")));
            customer.add(resultSet.getString("Email"));
            customer.add(resultSet.getString("Password"));

            this.getBranchesInfo(resultSet.getInt("BankCode"),
                    resultSet.getInt("BranchNumber"),
                    customer);

            list.add(customer);
        }

        return list;
    }

    private void getBanksInfo(int bankId, List<String> customer) throws SQLException {
        String sql = "Select * from Bank where Code =" + bankId + ";";
        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery(sql);
        customer.add(resultSet.getString("Name"));
    }

    private void getBranchesInfo(int bankCode, int Id, List<String> customer) throws SQLException {
        String sql = "Select * from Branch where BranchNumber =" + Id + " and BankCode = " + bankCode + ";";
        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery(sql);
        int bankId = 0;
        bankId = resultSet.getInt("BankCode");
        customer.add(resultSet.getString("Name"));

        getBanksInfo(bankId, customer);
    }

    public Connection getConnection() {
        return connection;
    }

    public void addAccount(int accountNumber, int ssn,
                           String accountType, double accountBalance,
                           int branchNumber, int bankCode) throws SQLException {

        String sql = "insert into Account values (" + accountNumber + "," + branchNumber + "," + bankCode + "," +
                " " + ssn + ", " + accountBalance + ", '" + accountType + "' );";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);


    }

    public void addCustomer(int ssn, String firstName, String lastName, int phone,
                            String street, String city, String country, int buildingNumber,
                            String email, String password, int branchNumber,
                            int bankCode) throws SQLException {

        String sql = "insert into Customer values(" + ssn + ",'" + firstName + "','" + lastName + "',"
                + phone + ",'" + street + "','" + city + "','" + country + "'," + buildingNumber + ",'" + email + "','"
                + password + "'," + branchNumber + "," + bankCode + ");";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);


    }

    public boolean addBranch(int branchNumber, int bankCode, String name, String street, String city, String country) throws SQLException {
        String selectQuery = "SELECT * FROM Branch WHERE BranchNumber = " + branchNumber + " AND BankCode = " + bankCode;
        Statement selectStatement = connection.createStatement();
        ResultSet resultSet = selectStatement.executeQuery(selectQuery);

        if (resultSet.next()) {
            // Branch with the same branch number and bank code already exists
            resultSet.close();
            selectStatement.close();
            return false;
        } else {
            String insertQuery = "INSERT INTO Branch (BranchNumber, BankCode, Name, Street, City, Country) VALUES ("
                    + branchNumber + ", "
                    + bankCode + ", '"
                    + name + "', '"
                    + street + "', '"
                    + city + "', '"
                    + country + "')";
            Statement insertStatement = connection.createStatement();
            int rowsAffected = insertStatement.executeUpdate(insertQuery);
            insertStatement.close();

            resultSet.close();
            selectStatement.close();

            return rowsAffected > 0;
        }
    }

    public boolean Login(String email, String password, String Table)
            throws SQLException {
        DataBase database = new DataBase();
        Connection connection = database.getConnection();
        Statement statement = connection.createStatement();
        String query = "SELECT * FROM " + Table + " WHERE Email = '" + email + "' AND password = '" + password + "'";

        ResultSet resultSet = statement.executeQuery(query);


        if (resultSet.next()) {
            int SSN = resultSet.getInt("SSN");
            String Name = resultSet.getString("FName") + " " + resultSet.getString("LName");
            JOptionPane.showMessageDialog(null,
                    "Welcome, " + Name,
                    "Successful Login!",
                    JOptionPane.INFORMATION_MESSAGE);
            new EmployeeForm(null, SSN);
            return true;
        }
        return false;
    }

    public boolean UpdateCustomer(int employeeSSN, int CustomerSSN)
            throws SQLException {
        DataBase database = new DataBase();
        Connection connection = database.getConnection();
        Statement statement = connection.createStatement();
        String query = "SELECT * FROM Customer , Account  WHERE Customer.SSN = Account.CustomerSSN and SSN = '" + CustomerSSN + "'";

        String FirstName, Email, Password, LastName, Country, City, Street, AccountType;
        int Phone, BuildingNumber, BranchNumber, BankCode;
        double AccountBalance;

        ResultSet resultSet = statement.executeQuery(query);
        if (resultSet.next()) {
            Phone = resultSet.getInt("Phone");
            BuildingNumber = resultSet.getInt("BuildingNumber");
            BranchNumber = resultSet.getInt("BranchNumber");
            FirstName = resultSet.getString("FName");
            LastName = resultSet.getString("LName");
            Street = resultSet.getString("Street");
            City = resultSet.getString("City");
            Country = resultSet.getString("Country");
            Email = resultSet.getString("Email");
            Password = resultSet.getString("Password");
            BankCode = resultSet.getInt("BankCode");
            AccountBalance = resultSet.getDouble("Balance");
            AccountType = resultSet.getString("Type");
            new CustomerInfoForm(null, employeeSSN, CustomerSSN,
                    FirstName, LastName, Email, Password, Country, City, Street
                    , AccountType, BankCode, BranchNumber, BuildingNumber, Phone, AccountBalance);
            return true;
        }

        JOptionPane.showMessageDialog(null,
                "Couldn't find Customer",
                "Invalid SSN!!!",
                JOptionPane.ERROR_MESSAGE);
        return false;

    }

    public void setCustomer(
            int ssn, String firstName, String lastName, int phone,
            String street, String city, String country, int buildingNumber,
            String email, String password, int branchNumber,
            int bankCode) throws SQLException {

        String sql = "UPDATE Customer SET FName = '" + firstName + "', LName = '" + lastName + "', Phone = " + phone + "," +
                " Street = '" + street + "', City = '" + city + "', Country = '" + country + "', BuildingNumber = " + buildingNumber + ", " +
                "Email = '" + email + "', Password = '" + password + "', BranchNumber = " + branchNumber + ", BankCode = " + bankCode + " WHERE SSN = " + ssn;
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
    }

    public void setACCount(int ssn,
                           String accountType, double accountBalance,
                           int branchNumber, int bankCode) throws SQLException {

        String sql = "UPDATE Account SET Type = '" + accountType + "', Balance = " + accountBalance + "" +
                ", BranchNumber = " + branchNumber + ", BankCode = " + bankCode + "" +
                " WHERE CustomerSSN = " + ssn;
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);


    }

    public Boolean addBank(int code, String name, String Street, String City, String Country) throws SQLException {
        String selectQuery = "SELECT * FROM Bank WHERE Code = " + code;
        Statement selectStatement = connection.createStatement();
        ResultSet resultSet = selectStatement.executeQuery(selectQuery);
        if (resultSet.next()) {
            // Bank with the same bank code already exists
            resultSet.close();
            selectStatement.close();
            return false;
        } else {
            String sql = "INSERT INTO Bank(Code,Name,Street,City,Country) " +
                    "VALUES(" + code + ",'" + name + "','" + Street + "','" + City + "','" + Country + "');";
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);


            statement.close();
            resultSet.close();
            selectStatement.close();
            return true;
        }
    }




    public List<List<String>> getLoans(int ss) throws SQLException {
        String selectQuery = "select Loan.LoanNumber,Loan.BranchNumber ,Loan.BankCode\n" +
                "     , Loan.LoanAmount, Loan.LoanType, Employee.SSN\n" +
                "     , Employee.FName, Employee.LName\n" +
                "from Loan , Employee\n" +
                "where Loan.BranchNumber = Employee.BranchNumber\n" +
                "and Loan.BankCode = Employee.BankCode and Employee.SSN =" +ss+ " ;";

        Statement selectStatement = connection.createStatement();
        ResultSet resultSet = selectStatement.executeQuery(selectQuery);
        List<List<String>> list = new LinkedList<>();

        while (resultSet.next()){
            List<String> loan = new LinkedList<>();
            loan.add(resultSet.getString("LoanNumber"));
            loan.add(resultSet.getString("BranchNumber"));
            loan.add(resultSet.getString("BankCode"));
            loan.add(resultSet.getString("LoanAmount"));
            loan.add(resultSet.getString("LoanType"));
            loan.add(resultSet.getString("SSN"));
            loan.add(resultSet.getString("FName"));
            loan.add(resultSet.getString("LName"));


            list.add(loan);

        }
        resultSet.close();
        selectStatement.close();
        return list;

    }

    public static void main(String[] args) throws SQLException {
        DataBase database = new DataBase();
        Connection connection = database.getConnection();
        Statement statement = connection.createStatement();
        database.getLoans(112);


    }


}

