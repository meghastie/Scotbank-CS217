package uk.co.asepstrath.bank.controllers;

import io.jooby.ModelAndView;
import io.jooby.StatusCode;
import io.jooby.annotation.*;
import io.jooby.exception.StatusCodeException;
import kong.unirest.core.Unirest;

import org.slf4j.Logger;
import uk.co.asepstrath.bank.models.Account;
import uk.co.asepstrath.bank.services.HelperMethods;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@Path("/bank")
public class Accounts {
    private final DataSource dataSource;
    private final Logger logger;
    public Accounts(DataSource ds, Logger log){
        dataSource = ds;
        logger = log;
    }
    @GET
    public ModelAndView login() {
        Map<String, Object> model = new HashMap<>();
        model.put("name", "luke");

        return new ModelAndView("login.hbs",model);
    }

    /*@GET("/home")
    public ModelAndView home() {
        Map<String, Object> model = new HashMap<>();
        model.put("name", "luke");

        return new ModelAndView("home.hbs",model);
    }*/

    @GET("/accounts")
    public String sayHi() {
        String response = Unirest.get("https://api.asep-strath.co.uk/api/accounts").asString().getBody();
        StringTokenizer tokens = new StringTokenizer(response,"[]{},:\"");

        ArrayList<Account> accounts = new ArrayList<>();

        while(tokens.hasMoreTokens()){
            tokens.nextToken();     //id
            String id = tokens.nextToken();
            tokens.nextToken();     //name
            String name = tokens.nextToken();
            tokens.nextToken();     //starting bal
            String bal = tokens.nextToken();
            tokens.nextToken();     //roundup
            String roundup = tokens.nextToken();

            accounts.add(new Account(id,name,Double.parseDouble(bal),Boolean.parseBoolean(roundup)));
        }


        return accounts.toString();
//
    }

    public ArrayList<Account> allAccounts() {
        String response = Unirest.get("https://api.asep-strath.co.uk/api/accounts").asString().getBody();
        StringTokenizer tokens = new StringTokenizer(response,"[]{},:\"");

        ArrayList<Account> accounts = new ArrayList<>();

        while(tokens.hasMoreTokens()){
            tokens.nextToken();     //id
            String id = tokens.nextToken();
            tokens.nextToken();     //name
            String name = tokens.nextToken();
            tokens.nextToken();     //starting bal
            String bal = tokens.nextToken();
            tokens.nextToken();     //roundup
            String roundup = tokens.nextToken();

            accounts.add(new Account(id,name,Double.parseDouble(bal),Boolean.parseBoolean(roundup)));
        }


        return accounts;
//
    }

    @GET("/details/{id}")
    public String printAccountDetails(@PathParam String id){
        try(Connection connection = dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM `AccountList` WHERE `AccountId` = ?");
            statement.setString(1,id);
            ResultSet set = statement.executeQuery();

            if (!set.next()) throw new StatusCodeException(StatusCode.NOT_FOUND, "Account Not Found");

            return set.getString("customerName");
        } catch (SQLException e) {
            // If something does go wrong this will log the stack trace
            logger.error("Database Error Occurred", e);
            // And return a HTTP 500 error to the requester
            throw new StatusCodeException(StatusCode.SERVER_ERROR, "Database Error Occurred");
        }
    }

    @POST("/home")
    public ModelAndView transferToHome(String username, String password) {
        Map<String, Object> model = new HashMap<>();
        double bal = 0;
        try(Connection connection = dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement("SELECT AccountId FROM `AccountList` WHERE `customerName` = ?");
            statement.setString(1,username);
            ResultSet set = statement.executeQuery();
            if (!set.next()) throw new StatusCodeException(StatusCode.NOT_FOUND, "Account Not Found");
            String id = set.getString("AccountId");
            //bal = set.getDouble("startingbalance");
            bal = getCurrentBalance(id, dataSource);

        } catch (SQLException e) {
            // If something does go wrong this will log the stack trace
            logger.error("Database Error Occurred", e);
            // And return a HTTP 500 error to the requester
            throw new StatusCodeException(StatusCode.SERVER_ERROR, "Database Error Occurred");
        }

        model.put("name", username);
        model.put("bal", bal);

        return new ModelAndView("home.hbs",model);
    }


    public static double getCurrentBalance(String id,DataSource ds){
        double balance = -1.0;
        try(Connection connection = ds.getConnection()){
            PreparedStatement stmt = connection.prepareStatement("SELECT SUM(`amount`) AS `total` FROM `Transaction` WHERE `to` = ?");
            stmt.setString(1,id);


            ResultSet totalTo = stmt.executeQuery();

            stmt = connection.prepareStatement("SELECT SUM(amount) FROM `Transaction` WHERE `from` = ?");
            stmt.setString(1,id);
            ResultSet totalFrom = stmt.executeQuery();

            stmt = connection.prepareStatement("SELECT `startingbalance` FROM `AccountList` WHERE `AccountId` = ?");
            stmt.setString(1,id);
            ResultSet startingBal = stmt.executeQuery();

            if (!totalTo.next() || !totalFrom.next() || !startingBal.next()) throw new StatusCodeException(StatusCode.NOT_FOUND, "Something doesn't work");

            balance =  startingBal.getDouble(1) + (totalTo.getDouble(1) - totalFrom.getDouble(1));
            return balance;
        }catch(Exception e){
            throw new StatusCodeException(StatusCode.SERVER_ERROR, "Database Error Occurred");
        }
    }
}

