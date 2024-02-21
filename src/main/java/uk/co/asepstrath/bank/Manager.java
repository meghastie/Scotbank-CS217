package uk.co.asepstrath.bank;

import io.jooby.Jooby;
import kong.unirest.core.*;
import uk.co.asepstrath.bank.models.Account;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class Manager extends Jooby {
    ArrayList<Account> fetchAccountData() {
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

    public static void main(String[] args) {
        Manager man = new Manager();
        ArrayList<Account> accounts = man.fetchAccountData();

        System.out.println(accounts.toString());
        System.out.println(accounts.size());
    }
}
