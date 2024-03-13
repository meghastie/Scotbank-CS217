package uk.co.asepstrath.bank;

import kong.unirest.core.Unirest;
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
import java.util.StringTokenizer;

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
        ArrayList<Account> accounts = completeTransactions();
        XmlParser p = new XmlParser();
        ArrayList<Transactions> transactionList = p.ParserList();
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
                    + "`RoundUpPot` double,"
                    + "PRIMARY KEY (`AccountId`)" //users may have the same account no OR sort code, but never 2 customers with the same acc no AND sort code
                    //+ "FOREIGN KEY (`customerName`) REFERENCES `Customer`(`customerName`)"
                    + ")");


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


            //populates Account database


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
                    + "(`time` varchar(50) NOT NULL,)"
                    + "(`amount` double NOT NUll,"
                    + "`from` varchar(50) NOT NULL,"
                    + "`transactionID` varchar(50) NOT NULL,"
                    + "`to` varchar(50),"
                    + "`type` varchar(50),"
                    + "PRIMARY KEY (`transactionID`)" //needed to uniquley identify the transaction as users can have many
                    + ")");

            for(Transactions transaction: transactionList){
                String insertTransaction = ("INSERT INTO Transaction(time,amount,from,transactionID,to,type)" + "VALUES (?,?,?,?,?,?)");
                PreparedStatement statement = connection.prepareStatement(insertTransaction);


                statement.setString(1,transaction.getTime());
                statement.setDouble(2,transaction.getAmount());
                statement.setString(3,transaction.getFrom());
                statement.setString(4,transaction.getID());
                statement.setString(5,transaction.getTo());
                statement.setString(6,transaction.getType());
                statement.executeUpdate();
            }


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

    private ArrayList<Account> completeTransactions(){
        XmlParser p = new XmlParser();
        ArrayList<Account> accountList = HelperMethods.getAccountList();
        ArrayList<Transactions> transactionList = p.ParserList();
        System.out.println(transactionList.get(1).getID());
        System.out.println(transactionList.get(2).getID());
        System.out.println(transactionList.get(3).getID());
        for(Transactions transaction: transactionList){
            boolean finish = false;
            for(Account acc: accountList){
                    Boolean work = acc.myTranaction(transaction);
                    finish = work || finish;
            }
        }
        return accountList;
    }
}
