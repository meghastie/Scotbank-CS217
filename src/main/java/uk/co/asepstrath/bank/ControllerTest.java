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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
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
        return Unirest.get("https://api.asep-strath.co.uk/api/accounts").asString().getBody();
//
    }

}

//    HttpResponse<accounts> bookResponse =
//            Unirest.get("https://api.asep-strath.co.uk/api/accounts").asObject(accounts.name);
//    Book bookObject = bookResponse.getBody();
