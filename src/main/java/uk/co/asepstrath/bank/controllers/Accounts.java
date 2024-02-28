package uk.co.asepstrath.bank.controllers;

import io.jooby.ModelAndView;
import io.jooby.StatusCode;
import io.jooby.annotation.GET;
import io.jooby.annotation.Path;
import io.jooby.annotation.PathParam;
import io.jooby.annotation.QueryParam;
import io.jooby.exception.StatusCodeException;
import kong.unirest.core.Unirest;

import org.slf4j.Logger;
import uk.co.asepstrath.bank.models.Account;

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

}
