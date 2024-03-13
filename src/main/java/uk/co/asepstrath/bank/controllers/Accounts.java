package uk.co.asepstrath.bank.controllers;

import io.jooby.ModelAndView;
import io.jooby.StatusCode;
import io.jooby.annotation.*;
import io.jooby.exception.StatusCodeException;
import kong.unirest.core.Unirest;

import org.slf4j.Logger;
import uk.co.asepstrath.bank.models.Account;
import uk.co.asepstrath.bank.models.Transactions;
import uk.co.asepstrath.bank.services.HelperMethods;

import javax.sql.DataSource;
import java.awt.*;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.*;
import java.util.*;


@Path("/bank")
public class Accounts {
    private final DataSource dataSource;
    private final Logger logger;

    public Accounts(DataSource ds, Logger log) {
        dataSource = ds;
        logger = log;
    }

    @GET
    public ModelAndView login() {
        Map<String, Object> model = new HashMap<>();
        model.put("name", "luke");

        return new ModelAndView("login.hbs", model);


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
        StringTokenizer tokens = new StringTokenizer(response, "[]{},:\"");

        ArrayList<Account> accounts = new ArrayList<>();

        while (tokens.hasMoreTokens()) {
            tokens.nextToken();     //id
            String id = tokens.nextToken();
            tokens.nextToken();     //name
            String name = tokens.nextToken();
            tokens.nextToken();     //starting bal
            String bal = tokens.nextToken();
            tokens.nextToken();     //roundup
            String roundup = tokens.nextToken();

            accounts.add(new Account(id, name, Double.parseDouble(bal), Boolean.parseBoolean(roundup)));
        }


        return accounts.toString();
//
    }

    public ArrayList<Account> allAccounts() {
        String response = Unirest.get("https://api.asep-strath.co.uk/api/accounts").asString().getBody();
        StringTokenizer tokens = new StringTokenizer(response, "[]{},:\"");

        ArrayList<Account> accounts = new ArrayList<>();

        while (tokens.hasMoreTokens()) {
            tokens.nextToken();     //id
            String id = tokens.nextToken();
            tokens.nextToken();     //name
            String name = tokens.nextToken();
            tokens.nextToken();     //starting bal
            String bal = tokens.nextToken();
            tokens.nextToken();     //roundup
            String roundup = tokens.nextToken();

            accounts.add(new Account(id, name, Double.parseDouble(bal), Boolean.parseBoolean(roundup)));
        }


        return accounts;
//
    }

    @GET("/details/{id}")
    public String printAccountDetails(@PathParam String id) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM `AccountList` WHERE `AccountId` = ?");
            statement.setString(1, id);
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
        /*try(Connection connection = dataSource.getConnection()){
            PreparedStatement statement = connection.prepareStatement("SELECT balance FROM `AccountList`, `Account` WHERE `customerName` = username AND `AccountList.username` = `Account.username`;?");
            statement.setString(1,username);
            ResultSet set = statement.executeQuery();

        } catch (SQLException e) {
            // If something does go wrong this will log the stack trace
            logger.error("Database Error Occurred", e);
            // And return a HTTP 500 error to the requester
            throw new StatusCodeException(StatusCode.SERVER_ERROR, "Database Error Occurred");
        }*/


        ArrayList<Account> accounts;
        accounts = allAccounts();
        model.put("name", username);

        for (Account account : accounts) {
            if (username.equals(account.getUsername())) {
                bal += account.getBalance();
            }
        }
        model.put("bal", bal);

        if (username.equals("Manager")) {
            return new ModelAndView("managerView.hbs", model);
        }

        return new ModelAndView("home.hbs", model);

    }

    @POST("/handleButtonClick")
    public String handleButtonClick(@FormParam("username") String data) {
//        HelperMethods accounts = new HelperMethods();
        String username;
        try
        {
             username = URLDecoder.decode(data, "UTF-8");
        }

        // This exception should never occur.
        catch (UnsupportedEncodingException e)
        {
            username = data;
        }
                if (username != null && !username.isEmpty()) {
            ArrayList<HelperMethods> accounts =  new ArrayList<>() ;
            for (int i =0; i< accounts.size(); i++) {
                System.out.println("This is accounts test");
                System.out.println(accounts.get(i).toString());

//                if (username.equals(acc.getUsername())){
//                    System.out.println("WE FOUND ITTT");
//                    acc.roundUpSwitch();
//                    if(acc.isRoundUpEnabled()) {
//                        return "Roundup is now ON";
//                    } else {
//                        return "Roundup is now OFF";
//                    }
//                }

            }
        }
        return "account not found";
    }


    @GET("/allAccounts")
    public ArrayList<Account> getAllAccounts() {
        ArrayList<Account> accounts = allAccounts();
        return accounts;
    }

    @GET("/displayAccounts")
    public String displayAccounts() {
        ArrayList<Account> accounts = allAccounts();

        StringBuilder accountsString = new StringBuilder();
        for (Account account : accounts) {
            accountsString.append(account.getId()).append(",")
                    .append(account.getName()).append(",")
                    .append(account.getBalance()).append(",")
                    .append(account.roundUpEnabled()).append(";");
        }

        return accountsString.toString();
    }


}
