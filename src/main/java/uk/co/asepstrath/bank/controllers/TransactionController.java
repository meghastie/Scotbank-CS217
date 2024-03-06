package uk.co.asepstrath.bank.controllers;

import io.jooby.annotation.GET;
import io.jooby.annotation.Path;
import org.slf4j.Logger;
import uk.co.asepstrath.bank.models.Transactions;
import uk.co.asepstrath.bank.services.XmlParser;

import javax.sql.DataSource;


@Path("/bank")
public class TransactionController {

//    private final DataSource dataSource;
//    private final Logger logger;
//    private final XmlParser parser;
//    private List<Transactions> transactions;


//    public TransactionController(DataSource dataSource, Logger logger, XmlParser parser ) {
//        this.dataSource = dataSource;
//        this.logger = logger;
//        this.parser = parser;
//
//
//    }
//
//    @GET("/transactions")
//        public String showAccount() {
//        String test = parser.Parser();
//        String test1 = transactions.getFrom();
//        return  test1;
//
//        }


    private final DataSource dataSource;
    private final Logger logger;
    private final XmlParser parser;

    public TransactionController(DataSource dataSource, Logger logger, XmlParser parser) {
        this.dataSource = dataSource;
        this.logger = logger;
        this.parser = parser;


    }

    @GET("/transactions")
        public String showAccount() {
            return xmlParser.Parser();
        }


}
