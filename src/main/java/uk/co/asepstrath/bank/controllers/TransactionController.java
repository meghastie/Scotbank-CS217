package uk.co.asepstrath.bank.controllers;
import io.jooby.ModelAndView;
import io.jooby.StatusCode;
import io.jooby.annotation.GET;
import io.jooby.annotation.Path;
import io.jooby.exception.StatusCodeException;
import io.netty.util.internal.shaded.org.jctools.queues.MpscArrayQueue;
import org.slf4j.Logger;
import uk.co.asepstrath.bank.models.Transactions;
import uk.co.asepstrath.bank.services.XmlParser;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Path("/bank2")
public class TransactionController {


    private final DataSource dataSource;
    private final Logger logger;
    private Map<String, Transactions> transactionMap;


    public TransactionController(DataSource dataSource, Logger logger) {
        this.dataSource = dataSource;
        this.logger = logger;

    }

//     Method to fetch data from the database and populate the HashMa


    // Optional: Method to interact with the HashMap

    private void fetchTransactionsFromDatabase() {
        try (Connection connection = dataSource.getConnection()) {
            String query = "SELECT * FROM `Transaction`";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String id = resultSet.getString("transactionID");
                String type = resultSet.getString("Type");
                double amount = resultSet.getDouble("amount");
                String to = resultSet.getString("to");
                String from = resultSet.getString("from");
                String time = resultSet.getString("time");

                // Create a Transaction object
                Transactions transaction = new Transactions(time, amount, from, id, to, type);
//                System.out.println(transaction);
                // Put the transaction into the HashMap
                transactionMap.put(id, transaction);
            }
        } catch (SQLException e) {
            logger.error("Error fetching transactions from database", e);
        }
    }
    @GET("/transaction")
//    public ModelAndView test (){
//        Map<String, Object> model = new HashMap<>();
//        List<Transactions> transactions = XmlParser.Parser();
//       for(Transactions transaction : transactions){
//           model.put("id", transaction.getID());
//           model.put("from", transaction.getFrom());
//           model.put("to", transaction.getTo());
//           model.put("type", transaction.getType());
//           model.put("amt", transaction.getAmount());
//           model.put("time", transaction.getTime());
//
//       }
//        return new ModelAndView("transactions.hbs", model);
//    }
    public ModelAndView test (){
        Map<String, Object> model = new HashMap<>();
        List<Transactions> transactions = XmlParser.Parser();
        System.out.println(transactions);
        model.put("transactions", transactions);
        return new ModelAndView("transactions.hbs", model);
    }
}
