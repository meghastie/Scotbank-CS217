package uk.co.asepstrath.bank;

import uk.co.asepstrath.bank.example.ExampleController;
import io.jooby.Jooby;
import io.jooby.handlebars.HandlebarsModule;
import io.jooby.helper.UniRestExtension;
import io.jooby.hikari.HikariModule;
import org.slf4j.Logger;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

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


        mvc(new ControllerTest(ds,log));

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

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `Customer`"
                    + "(`name` varchar(255) NOT NULL,"
                    + "`username` varchar(255) PRIMARY KEY," //two users cannot have same username, however they could possibly have same accNo - only unique identifier when paired with sort code
                    + "`password` varchar(255) NOT NULL,"
                    + "`dob` date"
                    + ")");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `Account` "
                    + "(`AccountId` varchar(50) NOT NULL,"
                    + "`customerName` varchar(100),"
                    + "`AccountType` varchar(45),"
                    + "`startingbalance` double,"
                    + "`RoundUpEnabled` integer,"
                    + "PRIMARY KEY (`AccountId`)" //users may have the same account no OR sort code, but never 2 customers with the same acc no AND sort code
                    //+ "FOREIGN KEY (`customerName`) REFERENCES `Customer`(`customerName`)"
                    + ")");


            //populates Account database
            Manager man = new Manager();
            ArrayList<Account> accounts = man.fetchAccountData();
            for(Account account: accounts){
                String insertAccount = ("INSERT INTO Account(AccountId,customerName,startingbalance,RoundUpEnabled)" + "VALUES (?,?,?,?)");
                PreparedStatement statement = connection.prepareStatement(insertAccount);


                statement.setString(1,account.getId());
                statement.setString(2,account.getName());
                statement.setDouble(3,account.getStartingBalance());
                statement.setBoolean(4,account.getRoundUp());
                statement.executeUpdate();
            }


            //prints accounts in Account database
            ResultSet result = stmt.executeQuery("SELECT * FROM Account");
            while(result.next()){
                System.out.println(result.getString("AccountId") + " " + result.getString("customerName") + " " + result.getDouble("startingbalance") + " " + result.getBoolean("RoundUpEnabled"));
            }


            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS `Transaction`"
                    + "(`transactionID` integer NOT NUll,"
                    + "`Type` varchar(15) NOT NULL,"
                    + "`amount` double NOT NULL,"
                    + "`to` varchar(50),"
                    + "`from` varchar(50),"
                    + "PRIMARY KEY (`transactionID`)" //needed to uniquley identify the transaction as users can have many
                    + ")");



            //continue with next part - inserting data in 'Agile_Lab_Doc'
            String insert = ("INSERT INTO Customer(name, username, password, dob)"
                    + "VALUES (?,?,?,?)");
            PreparedStatement prep = connection.prepareStatement(insert);
            prep.setString(1, "Bob");
            prep.setString(2,"bOB2024");
            prep.setString(3, "OnlineBanking1234*");
            prep.setDate(4, sqlDate); //dob is current date - fix
            prep.executeUpdate();

            Statement stmt2 = connection.createStatement();
            String sql = "SELECT * FROM Customer";
            ResultSet rs = stmt.executeQuery(sql);


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

}

