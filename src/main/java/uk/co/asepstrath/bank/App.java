package uk.co.asepstrath.bank;

import kong.unirest.core.Unirest;
import uk.co.asepstrath.bank.controllers.TransactionController;
import uk.co.asepstrath.bank.models.Account;
import uk.co.asepstrath.bank.controllers.Accounts;
import io.jooby.Jooby;
import io.jooby.handlebars.HandlebarsModule;
import io.jooby.helper.UniRestExtension;
import io.jooby.hikari.HikariModule;
import org.slf4j.Logger;
import uk.co.asepstrath.bank.models.Transactions;
import uk.co.asepstrath.bank.services.HelperMethods;
import uk.co.asepstrath.bank.services.XmlParser;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class App extends Jooby {

    {
        /*
        This section is used for setting up the Jooby Framework modules
         */
        install(new UniRestExtension());
        install(new HandlebarsModule());
        install(new HikariModule("mem"));


        /*
        This will host any files in src/main/resources/assets on <host>/assets
        For example in the dice template (dice.hbs) it references "assets/dice.png" which is in resources/assets folder
         */
        assets("/assets/*", "/assets");
        assets("/service_worker.js","/service_worker.js");

        /*
        Now we set up our controllers and their dependencies
         */
        DataSource ds = require(DataSource.class);
        Logger log = getLog();


        mvc(new Accounts(ds,log));
        mvc(new TransactionController(ds,log));
        /*
        Finally we register our application lifecycle methods
         */
        onStarted(() -> onStart());
        onStop(() -> onStop());
    }

    public static void main(final String[] args) {runApp(args, App::new);}

    /*
    This function will be called when the application starts up,
    it should be used to ensure that the DB is properly setup
     */
    public void onStart() {
        Logger log = getLog();
        log.info("Starting Up...");

        // Fetch DB Source
        DataSource ds = require(DataSource.class);
        // Open Connection to DB
        try (Connection connection = ds.getConnection()) {
            //
            java.util.Date currentDate = new java.util.Date();
            Date sqlDate = new Date(currentDate.getTime());
            Statement stmt = connection.createStatement();


            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `AccountList` "
                    + "(`AccountId` varchar(50) NOT NULL,"
                    + "`customerName` varchar(100),"
                    + "`AccountType` varchar(45),"
                    + "`startingbalance` double,"
                    + "`RoundUpEnabled` integer,"
                    + "`Pot` double,"
                    + "PRIMARY KEY (`AccountId`)" //users may have the same account no OR sort code, but never 2 customers with the same acc no AND sort code
                    //+ "FOREIGN KEY (`customerName`) REFERENCES `Customer`(`customerName`)"
                    + ")");

            /*
                    //not being used right now

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `Account` ("
                    + "`sortCode` integer NOT NULL,"
                    + "`accNum` integer NOT NULL,"
                    + "`AccountType` varchar(45) NOT NULL,"
                    + "`balance` integer NOT NULL,"
                    + "`openDate` date NOT NULL,"
                    + "`cardNumber` integer NOT NULL,"
                    + "`username` varchar(255) NOT NULL,"
                    + "PRIMARY KEY (`accNum`, `sortCode`)," //users may have the same account no OR sort code, but never 2 customers with the same acc no AND sort code
                    + "FOREIGN KEY (`username`) REFERENCES `Customer`(`username`)"
                    + ")");
             */


            //populates Account database
            ArrayList<Account> accounts = HelperMethods.getAccountList();
            for(Account account: accounts){
                String insertAccount = ("INSERT INTO AccountList(AccountId,customerName,startingbalance,RoundUpEnabled,Pot)" + "VALUES (?,?,?,?,?)");
                PreparedStatement statement = connection.prepareStatement(insertAccount);


                statement.setString(1,account.getId());
                statement.setString(2,account.getName());
                statement.setDouble(3,account.getStartingBalance());
                statement.setBoolean(4,account.getRoundUp());
                statement.setDouble(5,account.getPot());
                statement.executeUpdate();
            }



            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `Transaction`"
                    + "(`transactionID` varchar(50) NOT NUll,"
                    + "`Type` varchar(15) NOT NULL,"
                    + "`amount` double NOT NULL,"
                    + "`to` varchar(50),"
                    + "`from` varchar(50),"
                    + "`time` varchar(50),"
                    + "PRIMARY KEY (`transactionID`)" //needed to uniquley identify the transaction as users can have many
                    + ")");


            //populates Transactions database
            List<Transactions> transactions = XmlParser.Parser();
            PreparedStatement statement = null;
            for(Transactions transaction: transactions) {
                String insertAccount = ("INSERT INTO Transaction(transactionID,Type,amount,`to`,`from`,`time`)" + "VALUES (?,?,?,?,?,?)");
                statement = connection.prepareStatement(insertAccount);


                statement.setString(1, transaction.getID());
                statement.setString(2, transaction.getType());
                statement.setDouble(3, transaction.getAmount());
                statement.setString(4, transaction.getTo());
                statement.setString(5, transaction.getFrom());
                statement.setString(6,transaction.getTime());
                statement.executeUpdate();
            }
            statement.close();

            /*
            //continue with next part - inserting data in 'Agile_Lab_Doc'
            String insert = ("INSERT INTO Customer(name, username, password, dob)"
                    + "VALUES (?,?,?,?)");
            String sql = "SELECT * FROM Customer";
            ResultSet rs = stmt.executeQuery(sql);
             */
            /*
            ResultSet result = stmt.executeQuery("SELECT * FROM Transaction");
            while(result.next()){
                System.out.println(result.getString("transactionID") + " " + result.getString("Type") + " " + result.getDouble("amount") + " " + result.getString("to") + " " + result.getString("from"));
            }
             */
            //NEED CUSTOMER CLASS FOR BELOW - USING EXAMPLE
            //while (rs.next()) {
            //  int id = rs.getInt("id");
            //String name = rs.getString("name");
            //int age = rs.getInt("phone");
            //String address = rs.getString("address");
            //double salary = rs.getDouble("salary");
            //java.sql.Date date = rs.getDate("dob");
            //Employee employee = new Employee(id, name, age, address, salary);
            //}
            //rs.close();``

            //completeTransactions();

            connection.close(); //close to free up resources
        } catch (SQLException e) {
            log.error("Database Creation Error",e);
        }

    }
    /*
    This function will be called when the application shuts down
     */
    public void onStop() {
        System.out.println("Shutting Down...");

    }

    /*
    private void completeTransactions(){
        ArrayList<Account> accountList = HelperMethods.getAccountList();
        List<Transactions> transactionList = XmlParser.Parser();
        System.out.println(transactionList.size());
        Boolean done = false;

        for(Transactions transaction: transactionList){
            done = false;
            for(Account acc: accountList){
                Boolean test = acc.myTranaction(transaction);
                done = done || test;
            }
            System.out.println(done);
        }
    }
     */
}
