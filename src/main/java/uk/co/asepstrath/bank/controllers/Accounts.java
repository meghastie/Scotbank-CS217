package uk.co.asepstrath.bank.controllers;

import io.jooby.ModelAndView;
import io.jooby.StatusCode;
import io.jooby.annotation.*;
import io.jooby.exception.StatusCodeException;

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
        ArrayList<Account> accounts = HelperMethods.getAccountList();

        return accounts.toString();
//
    }

    @GET("/details/{id}")
    public String printAccountDetails(@PathParam String id) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM `AccountList` WHERE `AccountId` = ?");
            statement.setString(1, id);
            ResultSet set = statement.executeQuery();
            statement.close();

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
        accounts = HelperMethods.getAccountList();
        model.put("name", username);

        for (Account account : accounts) {
            if (username.equals(account.getName())) {
                bal = HelperMethods.getCurrentBalance(account.getId(),dataSource);
            }
        }
        model.put("bal", bal);

        return new ModelAndView("home.hbs", model);
    }

    @POST("/handleButtonClick")
    public String handleButtonClick(@FormParam("username") String data) throws SQLException {
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

        PreparedStatement stmt = null;
        if (username != null && !username.isEmpty()) {
            try(Connection connection = dataSource.getConnection()){
                stmt = connection.prepareStatement("SELECT `RoundUpEnabled` FROM `AccountList` WHERE `customerName` = ?");
                stmt.setString(1,username);

                ResultSet result = stmt.executeQuery();
                if(!result.next()) return "account not found";

                stmt = connection.prepareStatement("UPDATE `AccountList` SET `RoundUpEnabled` = ? WHERE `customerName` = ?");
                stmt.setString(2,username);

                //flips roundUp value
                stmt.setBoolean(1, !result.getBoolean(1));
                stmt.executeUpdate();
                stmt.close();
                if(!result.getBoolean(1))
                    return "roundUp on!!!";
                else
                    return "roundUp off";

            }catch (Exception e){
                return "dataSource fail";
            }
        }
        return  "account not found";
    }


}
