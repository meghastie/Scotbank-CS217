package uk.co.asepstrath.bank.controllers;


import io.jooby.annotation.GET;
import io.jooby.annotation.Path;
import kong.unirest.core.Unirest;

import org.slf4j.Logger;
import uk.co.asepstrath.bank.Transaction;

import javax.sql.DataSource;
import java.util.*;
@Path("/bank")
public class Transactions {
    private final DataSource dataSource;
    private final Logger logger;
    public Transactions(DataSource ds, Logger log){
        dataSource = ds;
        logger = log;
    }
    @GET("/transactions")
    public String getTransactions(){
        String response = Unirest.get("https://api.asep-strath.co.uk/api/transactions").asString().getBody();
        StringTokenizer tokens = new StringTokenizer(response,"[]{},:\"");
        ArrayList<Transaction> transactions = new ArrayList<>();

        while(tokens.hasMoreTokens()){
            tokens.nextToken();     //time
            String time = tokens.nextToken();
            tokens.nextToken();     //amount
            String amount = tokens.nextToken();
            tokens.nextToken();     //from who
            String from = tokens.nextToken();
            tokens.nextToken();     //id
            String id = tokens.nextToken();
            tokens.nextToken();     //to
            String to = tokens.nextToken();
            tokens.nextToken();     //type
            String type = tokens.nextToken();

            transactions.add(new Transaction(time, Double.parseDouble(amount), from, id, to, type));
            return "123456";
    }
        return "13456";
}}
