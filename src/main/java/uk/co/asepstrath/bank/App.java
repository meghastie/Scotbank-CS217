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


        mvc(new ExampleController(ds,log));

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
            stmt.executeUpdate("CREATE TABLE IF NOT EXSITS `Customer` (\n"
                    + "`name` varchar(255) NOT NULL \n"
                    + "`username` varchar(255) PRIMARY KEY \n" //two users cannot have same username, however they could possibly have same accNo - only unique identifier when paired with sort code
                    + "`passcode` varchar(255) NOT NULL \n"
                    + "`dob` date NOT NULL,\n"
                    + ")");


            stmt.executeUpdate("CREATE TABLE IF NOT EXSITS `Account` (\n"
                    + "`sortCode` integer NOT NULL, \n"
                    + "`accNum` integer NOT NULL, \n"
                    + "`AccountType` varchar(45) NOT NULL \n"
                    + "`balance` integer NOT NULL \n"
                    + "`openDate` date NOT NULL, \n"
                    + "`cardNumber` integer NOT NULL"
                    + "`username` varchar(255) NOT NULL \n"
                    + "PRIMARY KEY (`accountNo`, `sortCode`)," //users may have the same account no OR sort code, but never 2 customers with the same acc no AND sort code
                    + "FOREIGN KEY (`username`) REFERENCES `Customer`(`username`)"
                    + ")");

            stmt.executeUpdate("CREATE TABLE IF NOT EXSITS `Transaction` (\n"
                    + "`accNum` integer NOT NULL, \n"
                    + "`sortCode` integer NOT NULL, \n"
                    + "`transactionType` varchar(45) \n"
                    + "`amount` integer NOT NULL \n"
                    + "`transactionDate` date NOT NULL,\n"
                    + "`username` varchar(255) NOT NULL \n"
                    + "`transactionID` integer PRIMARY KEY \n" //needed to uniquley identify the transaction as users can have many
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
