package uk.co.asepstrath.bank.controllers;

import io.jooby.annotation.GET;
import io.jooby.annotation.Path;
import org.slf4j.Logger;
import uk.co.asepstrath.bank.models.Transactions;
import uk.co.asepstrath.bank.services.XmlParser;

import javax.sql.DataSource;


@Path("/bank2")
public class TransactionController {


        private final DataSource dataSource;
        private final Logger logger;
    private Transactions transactions;


    public TransactionController(DataSource dataSource, Logger logger ) {
        this.dataSource = dataSource;
        this.logger = logger;
        //this.xmlParser = xmlParser;


    }

    @GET("/transaction")
        public String showAccount() {
            return "hi9";
        }


}
