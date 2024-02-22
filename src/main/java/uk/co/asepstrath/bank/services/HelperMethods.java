package uk.co.asepstrath.bank.services;

import kong.unirest.core.Unirest;
import uk.co.asepstrath.bank.models.Account;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class HelperMethods {

    public static ArrayList<Account> getAccountList(){
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
    }
}
