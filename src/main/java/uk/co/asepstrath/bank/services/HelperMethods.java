package uk.co.asepstrath.bank.services;

import io.jooby.StatusCode;
import io.jooby.exception.StatusCodeException;
import kong.unirest.core.Unirest;
import org.slf4j.Logger;
import uk.co.asepstrath.bank.models.Account;

import javax.sql.DataSource;
import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class HelperMethods {

    public static String getStringFromApi(String url){
        return Unirest.get(url).asString().getBody();
    }
    public static ArrayList<Account> getAccountList(){
        String response = getStringFromApi("https://api.asep-strath.co.uk/api/accounts");
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
    }

    public static double getCurrentBalance(String id,DataSource ds){
        double balance = -9999.0;
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
