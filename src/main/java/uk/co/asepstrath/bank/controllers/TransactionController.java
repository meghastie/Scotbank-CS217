package uk.co.asepstrath.bank.controllers;

import io.jooby.annotation.GET;
import io.jooby.annotation.Path;
import kong.unirest.core.Unirest;
import org.slf4j.Logger;
import uk.co.asepstrath.bank.models.Account;
import uk.co.asepstrath.bank.models.Transactions;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.StringTokenizer;


@Path("/bank")
public class TransactionController {


        private final DataSource dataSource;
        private final Logger logger;


    public TransactionController(DataSource dataSource, Logger logger, Transactions transactions) {
        this.dataSource = dataSource;
        this.logger = logger;
    }

    @GET("/accounts")
        public String showAccount() {

            return null;

        }


}
