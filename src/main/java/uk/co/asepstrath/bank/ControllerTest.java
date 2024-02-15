package uk.co.asepstrath.bank;

import io.jooby.annotation.GET;
import io.jooby.annotation.Path;
import kong.unirest.core.Unirest;

import io.jooby.ModelAndView;
import io.jooby.StatusCode;
import io.jooby.annotation.*;
import io.jooby.exception.StatusCodeException;
import kong.unirest.core.Unirest;
import org.slf4j.Logger;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.Date;

@Path("/tests")
public class ControllerTest {
    private final DataSource dataSource;
    private final Logger logger;
    public ControllerTest(DataSource ds, Logger log){
        dataSource = ds;
        logger = log;
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

}
