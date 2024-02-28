package uk.co.asepstrath.bank.controllers;

import io.jooby.annotation.GET;
import io.jooby.annotation.Path;
import org.slf4j.Logger;
import uk.co.asepstrath.bank.models.Transactions;
import uk.co.asepstrath.bank.services.XmlParser;

import javax.sql.DataSource;


@Path("/bank")
public class TransactionController {


        private final DataSource dataSource;
        private final Logger logger;
    private final XmlParser xmlParser;
    private Transactions transactions;


    public TransactionController(DataSource dataSource, Logger logger, XmlParser  xmlParser ) {
        this.dataSource = dataSource;
        this.logger = logger;
        this.xmlParser = xmlParser;


    }

    @GET("/transactions")
        public String showAccount() {
        xmlParser.Parser();
        return transactions.getID();

        }


}
