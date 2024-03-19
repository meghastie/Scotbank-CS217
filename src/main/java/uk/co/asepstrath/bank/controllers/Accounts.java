package uk.co.asepstrath.bank.controllers;

import io.jooby.ModelAndView;
import io.jooby.StatusCode;
import io.jooby.annotation.*;
import io.jooby.exception.StatusCodeException;

import org.slf4j.Logger;
import uk.co.asepstrath.bank.models.Account;
import uk.co.asepstrath.bank.models.Transactions;
import uk.co.asepstrath.bank.services.HelperMethods;

import javax.sql.DataSource;
import java.awt.*;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.*;
import java.util.*;


@Path("/bank")
public class Accounts {
    private final DataSource dataSource;
    private final Logger logger;

    public Accounts(DataSource ds, Logger log) {
        dataSource = ds;
        logger = log;
    }

    @GET
    public ModelAndView login() {
        Map<String, Object> model = new HashMap<>();
        model.put("name", "luke");

        return new ModelAndView("login.hbs", model);


    }


    @GET("/accounts")
    public String sayHi() {
        ArrayList<Account> accounts = HelperMethods.getAccountList();

        return accounts.toString();
//
    }

    @GET("/details/{id}")
    public String printAccountDetails(@PathParam String id) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM `AccountList` WHERE `AccountId` = ?");
            statement.setString(1, id);
            ResultSet set = statement.executeQuery();
            statement.close();

            if (!set.next()) throw new StatusCodeException(StatusCode.NOT_FOUND, "Account Not Found");

            return set.getString("customerName");
        } catch (SQLException e) {
            // If something does go wrong this will log the stack trace
            logger.error("Database Error Occurred", e);
            // And return a HTTP 500 error to the requester
            throw new StatusCodeException(StatusCode.SERVER_ERROR, "Database Error Occurred");
        }
    }

    @POST("/home")
    public ModelAndView transferToHome(String username, String password) {
        Map<String, Object> model = new HashMap<>();
        double bal = 0;

        if (username.equals("Manager")) {
            return new ModelAndView("managerView.hbs", model);
        }

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT AccountId FROM `AccountList` WHERE `customerName` = ?");
            statement.setString(1, username);
            ResultSet set = statement.executeQuery();
            if (!set.next()) throw new StatusCodeException(StatusCode.NOT_FOUND, "Account Not Found");
            String id = set.getString("AccountId");

            bal = HelperMethods.getCurrentBalance(id, dataSource);

            PreparedStatement recentTransactionsStatement = connection.prepareStatement("SELECT `amount`, Type FROM `Transaction` WHERE `to` = ? OR `from` = ?  LIMIT 3");
            recentTransactionsStatement.setString(1, id);
            recentTransactionsStatement.setString(2, id);
            ResultSet recentTransactionsResultSet = recentTransactionsStatement.executeQuery();
            ArrayList<Double> amounts = new ArrayList<>();
            ArrayList<String> types = new ArrayList<>();

            while (recentTransactionsResultSet.next()) {
                double amount = recentTransactionsResultSet.getDouble(1);
                String type = recentTransactionsResultSet.getString(2);
                amounts.add(amount);
                types.add(type);
            }

            double amount1 = 0.0;
            double amount2 = 0.0;
            double amount3 = 0.0;
            String type1 = "N/A";
            String type2 = "N/A";
            String type3 = "N/A";

            switch (amounts.size()) {
                case 1:
                    amount1 = amounts.get(0);
                    type1 = types.get(0);
                    break;
                case 2:
                    amount1 = amounts.get(0);
                    amount2 = amounts.get(1);
                    type1 = types.get(0);
                    type2 = types.get(1);
                    break;
                case 3:
                    amount1 = amounts.get(0);
                    amount2 = amounts.get(1);
                    amount3 = amounts.get(2);
                    type1 = types.get(0);
                    type2 = types.get(1);
                    type3 = types.get(2);
                    break;
            }

            model.put("amount1", amount1);
            model.put("amount2", amount2);
            model.put("amount3", amount3);
            model.put("type1", type1);
            model.put("type2", type2);
            model.put("type3", type3);

        } catch (SQLException e) {
            // If something does go wrong this will log the stack trace
            logger.error("Database Error Occurred", e);
            // And return a HTTP 500 error to the requester
            throw new StatusCodeException(StatusCode.SERVER_ERROR, "Database Error Occurred");
        }


        ArrayList<Account> accounts;
        accounts = HelperMethods.getAccountList();
        model.put("name", username);

        for (Account account : accounts) {
            if (username.equals(account.getName())) {
                bal = HelperMethods.getCurrentBalance(account.getId(), dataSource);
            }
        }
        model.put("bal", bal);


        return new ModelAndView("home.hbs", model);

    }
    @POST("/testAddMoney")
    public String addMoney(@FormParam("amount") String amount, @FormParam("name") String name) {
        String username;
        try {
            // Convert the amount to a numerical value (assuming it's a string)
            double moneyToAdd = Double.parseDouble(amount);

            // Decode the name parameter
            username = URLDecoder.decode(name, "UTF-8");

            // Perform any necessary operations with the username

            // Return a success message
            return amount;
        } catch (NumberFormatException e) {
            // If the amount is not a valid number
            e.printStackTrace(); // Log the error
            return "Invalid amount format";
        } catch (UnsupportedEncodingException e) {
            // If decoding the name parameter fails
            e.printStackTrace(); // Log the error
            return "Error decoding name parameter";
        } catch (Exception e) {
            // If any other exception occurs
            e.printStackTrace(); // Log the error
            return "Error adding money";
        }
    }


    @POST("/handleButtonClick")
    public String handleButtonClick(@FormParam("username") String data) throws SQLException {
        String username;
        try {
            username = URLDecoder.decode(data, "UTF-8");
        }

        // This exception should never occur.
        catch (UnsupportedEncodingException e) {
            username = data;
        }

        PreparedStatement stmt = null;
        if (username != null && !username.isEmpty()) {
            try (Connection connection = dataSource.getConnection()) {
                stmt = connection.prepareStatement("SELECT `RoundUpEnabled` FROM `AccountList` WHERE `customerName` = ?");
                stmt.setString(1, username);

                ResultSet result = stmt.executeQuery();
                if (!result.next()) return "account not found";

                stmt = connection.prepareStatement("UPDATE `AccountList` SET `RoundUpEnabled` = ? WHERE `customerName` = ?");
                stmt.setString(2, username);

                //flips roundUp value
                stmt.setBoolean(1, !result.getBoolean(1));
                stmt.executeUpdate();
                stmt.close();
                if (!result.getBoolean(1))
                    return "roundUp on!!!";
                else
                    return "roundUp off";

            } catch (Exception e) {
                return "dataSource fail";
            }
        }
        return "account not found";
    }


    @GET("/allAccounts")
    public ArrayList<Account> getAllAccounts() {
        ArrayList<Account> accounts = HelperMethods.getAccountList();
        return accounts;
    }

    @GET("/displayAccounts")
    public String displayAccounts() {
        ArrayList<Account> accounts = HelperMethods.getAccountList();

        StringBuilder accountsString = new StringBuilder();
        for (Account account : accounts) {
            accountsString.append(account.getId()).append(",")
                    .append(account.getName()).append(",")
                    .append(account.getBalance()).append(",")
                    .append(account.roundUpEnabled()).append(";");
        }

        return accountsString.toString();
    }


    private double[] getIncome() {
        double income[] = new double[12];


        return income;
    }

    private double[] getSpending() {
        double spend[] = new double[12];


        return spend;
    }

    @GET("/withdrawals/{username}")
    public double getWithdrawals(@PathParam String username) {
        double withdrawData = 0.0;

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement IDstatement = connection.prepareStatement("SELECT AccountId FROM `AccountList` WHERE `customerName` = ?");
            IDstatement.setString(1, username);
            ResultSet set = IDstatement.executeQuery();
            if (!set.next()) throw new StatusCodeException(StatusCode.NOT_FOUND, "Account Not Found");
            String id = set.getString("AccountId");

            PreparedStatement statement = connection.prepareStatement("SELECT SUM(amount) AS total_withdrawals FROM `Transaction` WHERE `type` = 'WITHDRAWAL' AND `from` = ?");
            statement.setString(1,id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                withdrawData = resultSet.getDouble("total_withdrawals");
            }
        } catch (SQLException e) {
            logger.error("Error occurred while fetching income data", e);
            throw new StatusCodeException(StatusCode.SERVER_ERROR, "Error occurred while fetching income data");
        }
        System.out.println(withdrawData);
        return withdrawData;
    }

    @GET("/payments/{username}")
    public double getPayments(@PathParam String username) {
        double totalPayments = 0.0;

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement IDstatement = connection.prepareStatement("SELECT AccountId FROM `AccountList` WHERE `customerName` = ?");
            IDstatement.setString(1, username);
            ResultSet set = IDstatement.executeQuery();
            if (!set.next()) throw new StatusCodeException(StatusCode.NOT_FOUND, "Account Not Found");
            String id = set.getString("AccountId");

            PreparedStatement statement = connection.prepareStatement("SELECT SUM(amount) AS total_payments FROM `Transaction` WHERE `type` = 'PAYMENT' AND `from` = ?");
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                totalPayments = resultSet.getDouble("total_payments");
            }
        } catch (SQLException e) {
            logger.error("Error occurred while fetching outgoing data", e);
            throw new StatusCodeException(StatusCode.SERVER_ERROR, "Error occurred while fetching outgoing data");
        }
        System.out.println(totalPayments);
        return totalPayments;
    }

    @GET("/deposits/{username}")
    public double getDeposits(@PathParam String username) {
        double totalDepos = 0.0;

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement IDstatement = connection.prepareStatement("SELECT AccountId FROM `AccountList` WHERE `customerName` = ?");
            IDstatement.setString(1, username);
            ResultSet set = IDstatement.executeQuery();
            if (!set.next()) throw new StatusCodeException(StatusCode.NOT_FOUND, "Account Not Found");
            String id = set.getString("AccountId");

            PreparedStatement statement = connection.prepareStatement("SELECT SUM(amount) AS total_deposits FROM `Transaction` WHERE `type` = 'DEPOSIT' AND `to` = ?");
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                totalDepos = resultSet.getDouble("total_deposits");
            }
        } catch (SQLException e) {
            logger.error("Error occurred while fetching outgoing data", e);
            throw new StatusCodeException(StatusCode.SERVER_ERROR, "Error occurred while fetching outgoing data");
        }
        System.out.println(totalDepos);
        return totalDepos;
    }

    @GET("/transfers/{username}")
    public double getTransfersTo(@PathParam String username) {
        double totalTransfers = 0.0;

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement IDstatement = connection.prepareStatement("SELECT AccountId FROM `AccountList` WHERE `customerName` = ?");
            IDstatement.setString(1, username);
            ResultSet set = IDstatement.executeQuery();
            if (!set.next()) throw new StatusCodeException(StatusCode.NOT_FOUND, "Account Not Found");
            String id = set.getString("AccountId");

            PreparedStatement statement = connection.prepareStatement("SELECT SUM(amount) AS total_transfers FROM `Transaction` WHERE `type` = 'TRANSFER' AND `to` = ? OR `from` = ?");
            statement.setString(1, id);
            statement.setString(2, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                totalTransfers = resultSet.getDouble("total_transfers");
            }
        } catch (SQLException e) {
            logger.error("Error occurred while fetching outgoing data", e);
            throw new StatusCodeException(StatusCode.SERVER_ERROR, "Error occurred while fetching outgoing data");
        }
        System.out.println(totalTransfers);
        return totalTransfers;
    }


    @GET("/collectRoundUps/{username}")
    public double collectRound(@PathParam String username) {
        double totalRoundsCollected = 0.0;
        ArrayList<Account> accounts = HelperMethods.getAccountList();

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement IDstatement = connection.prepareStatement("SELECT AccountId FROM `AccountList` WHERE `customerName` = ?");
            IDstatement.setString(1, username);
            ResultSet set = IDstatement.executeQuery();
            if (!set.next()) throw new StatusCodeException(StatusCode.NOT_FOUND, "Account Not Found");
            String id = set.getString("AccountId");

            PreparedStatement statement = connection.prepareStatement("SELECT SUM(amount) AS total_rounds FROM `Transaction` WHERE `to` = ? AND `from` = ?");
            statement.setString(1, id);
            statement.setString(2, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                totalRoundsCollected = resultSet.getDouble("total_rounds");
            }
        } catch (SQLException e) {
            logger.error("Error occurred while fetching outgoing data", e);
            throw new StatusCodeException(StatusCode.SERVER_ERROR, "Error occurred while fetching outgoing data");
        }
        System.out.println(totalRoundsCollected);
        return totalRoundsCollected;
    }



}
