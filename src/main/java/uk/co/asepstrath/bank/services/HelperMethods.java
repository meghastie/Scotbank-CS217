package uk.co.asepstrath.bank.services;

import io.jooby.StatusCode;
import io.jooby.exception.StatusCodeException;
import kong.unirest.core.Unirest;
import uk.co.asepstrath.bank.models.Account;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        double balance = getAccountFromList(id).getStartingBalance();
        try(Connection connection = ds.getConnection()){
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Transaction WHERE `to` = ? OR `from` = ?");
            stmt.setString(1,id);
            stmt.setString(2,id);

            ResultSet result = stmt.executeQuery();
            while(result.next()){
                if(result.getString("Type").equals("PAYMENT")){
                    balance -= result.getDouble("amount");
                } else if (result.getString("Type").equals("TRANSFER")) {
                    if(result.getString("to").equals("635e583f-0af2-47cb-9625-5b66ba30e188"))
                        balance += result.getDouble("amount");
                    else
                        balance -= result.getDouble("amount");
                } else if (result.getString("Type").equals("DEPOSIT")) {
                    balance += result.getDouble("amount");
                } else if (result.getString("Type").equals("WITHDRAWAL")) {
                    if (balance - result.getDouble("amount") >= 0)
                        balance -= result.getDouble("amount");
                } else {    //collect round up
                    balance += result.getDouble("amount");
                }
            }

            stmt.close();
            return (BigDecimal.valueOf(balance).setScale(2, RoundingMode.HALF_EVEN).doubleValue());
        }catch(Exception e){
            throw new StatusCodeException(StatusCode.SERVER_ERROR, "Database Error Occurred");
        }
    }

    public static String replacePlaceholders(String content, Map<String, String> placeholders) {
        for (Map.Entry<String, String> row : placeholders.entrySet()) {
            String placeholder = Pattern.quote(row.getKey());
            String replacement = Matcher.quoteReplacement(row.getValue());
            content = content.replaceAll(placeholder, replacement);
        }
        return content;
    }

    public static Account getAccountFromList(String nameOrId){
        ArrayList<Account> accounts = getAccountList();

        for(Account a:accounts){
            if(a.getId().equals(nameOrId) || a.getName().equals(nameOrId))
                return a;
        }

        return null;
    }
}
